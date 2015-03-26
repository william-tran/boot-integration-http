package demo;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EchoIntegrationTest extends BaseWebIntegrationTest {
    
    @Test
    public void testEcho() throws Exception {
        String response = restTemplate.postForObject("/echo", "foo", String.class);
        assertEquals("foo from the other side", response);
    }

}
