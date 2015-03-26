package demo;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SoapIntegrationTest extends BaseWebIntegrationTest {
    
    @Test
    public void testSoap() throws Exception {
        String requestXml = IOUtils.getResourceAsString("/soap-request.xml");
        String expectedResponse = IOUtils.getResourceAsString("/soap-response.xml");
        String response = restTemplate.postForObject("/soap", requestXml, String.class);
        assertEquals(expectedResponse, response);
    }

}
