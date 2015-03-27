package demo;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@Data
public class Rest2SoapProperties {
    private String jaxbContext;
    private String requestType;
    private Class<?> requestClass;
    private String soapAction;
    private String endpoint;
}
