package demo;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.handler.MessageHandlerChain;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.integration.transformer.support.StaticHeaderValueMessageProcessor;
import org.springframework.integration.ws.MarshallingWebServiceOutboundGateway;
import org.springframework.integration.ws.WebServiceHeaders;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableConfigurationProperties
public class Rest2SoapConfig {

    @Bean
    HttpRequestHandlingMessagingGateway httpRequestHandlingMessagingGateway() {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway();
        gateway.setRequestChannel(httpRequestChannel());
        gateway.setReplyChannel(httpResponseChannel());
        gateway.getRequestMapping().setMethods(HttpMethod.POST);
        gateway.getRequestMapping().setPathPatterns("rest2soap");
        return gateway;
    }

    @Bean
    DirectChannel httpRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    DirectChannel httpResponseChannel() {
        return new DirectChannel();
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix="rest2soap")
    Rest2SoapProperties rest2SoapProperties() {
        return new Rest2SoapProperties();
    }

    @Bean
    Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath(rest2SoapProperties().getJaxbContext());
        return jaxb2Marshaller;
    }
    
    @Bean
    EventDrivenConsumer eventDrivenConsumer() {
        return new EventDrivenConsumer(httpRequestChannel(), chain());
    }

    @Bean
    MessageHandlerChain chain() {
        MessageHandlerChain chain = new MessageHandlerChain();
        chain.setHandlers(Arrays.asList(
                jsonToObjectMessageHandler(),
                headerEnricherMessageHandler(),
                soapGateway(),
                objectToJsonMessageHandler()));
        chain.setOutputChannel(httpResponseChannel());
        return chain;
    }

    @Bean
    MessageTransformingHandler jsonToObjectMessageHandler() {
        return new MessageTransformingHandler(jsonToObjectTransformer());
    }

    @Bean
    @RefreshScope
    JsonToObjectTransformer jsonToObjectTransformer() {
        return new JsonToObjectTransformer(rest2SoapProperties().getRequestClass());
    }
    
    @Bean
    MessageTransformingHandler headerEnricherMessageHandler() {
        return new MessageTransformingHandler(headerEnricher());
    }
    
    @Bean
    HeaderEnricher headerEnricher() {
        HeaderEnricher headerEnricher = new HeaderEnricher(
                Collections.singletonMap(WebServiceHeaders.SOAP_ACTION,  
                        soapActionHeaderValue()));
        return headerEnricher;
    }

    @Bean
    @RefreshScope
    StaticHeaderValueMessageProcessor<String> soapActionHeaderValue() {
        return new StaticHeaderValueMessageProcessor<String>(
                rest2SoapProperties().getSoapAction());
    }
    
    @Bean
    MarshallingWebServiceOutboundGateway soapGateway() {
        return new MarshallingWebServiceOutboundGateway(rest2SoapProperties().getEndpoint(),marshaller());
    }
    
    @Bean
    MessageTransformingHandler objectToJsonMessageHandler() {
        return new MessageTransformingHandler(new ObjectToJsonTransformer());
    }

}
