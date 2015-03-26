package demo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.soap.FahrenheitToCelsius;
import demo.soap.FahrenheitToCelsiusResponse;

public class Rest2SoapIntegrationTest extends BaseWebIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testRest2Soap() throws Exception {
        FahrenheitToCelsius request = new FahrenheitToCelsius();
        request.setFahrenheit("90");
        ResponseEntity<FahrenheitToCelsiusResponse> responseEntity = restTemplate.postForEntity("/rest2soap", objectMapper.writeValueAsString(request), FahrenheitToCelsiusResponse.class);
        assertEquals("32.2222222222222", responseEntity.getBody().getFahrenheitToCelsiusResult());
        assertTrue(responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith("application/json"));
    }

}
