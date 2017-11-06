package by.delaidelo.olmicro.it;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * vladimir
 * 02.11.2017
 * (C) ЗАО "Делай Дело", 2013-2017
 */
public class EndpointIT {
    private static String URL;

    @BeforeClass
    public static void init() {
        String port = System.getProperty("liberty.test.port");
//        String war = System.getProperty("war.name");
        URL = "http://localhost:" + port + "/" + "servlet";
    }

    @Test
    public void testServlet() throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(URL);
        try {
            int statusCode = client.executeMethod(method);
            assertEquals("HTTP GET failed", HttpStatus.SC_OK, statusCode);
            String response = method.getResponseBodyAsString(1000);

            assertTrue("Unexpected response body", response.contains("Hello! How are you today?"));
        } finally {
            method.releaseConnection();
        }
    }
}
