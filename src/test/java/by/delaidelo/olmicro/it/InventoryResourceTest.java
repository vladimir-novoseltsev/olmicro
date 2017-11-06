package by.delaidelo.olmicro.it;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * vladimir
 * 04.11.17
 * 2013-2017, ЗАО "Делай Дело"
 */
public class InventoryResourceTest {
    private String port;
    private String baseUrl;

    private Client client;

    private final String SYSTEM_PROPERTIES = "system/properties";
    private final String INVENTORY_HOSTS = "inventory/hosts";

    @Before
    public void setup() {
        port = System.getProperty("liberty.test.port");
        baseUrl = "http://localhost:" + port + "/";
        client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);
    }

    @After
    public void teardown() {
        client.close();
    }
    @Test
    public void testSuite() {
        this.testEmptyInventory();
        this.testHostRegistration();
        this.testSystemPropertiesMatch();
//        this.testUnknownHost();
    }

//    @Test
    public void testEmptyInventory() {
        Response response = this.getResponse(baseUrl+INVENTORY_HOSTS);
        assertResponse(baseUrl, response);

        JsonObject obj = response.readEntity(JsonObject.class);
        int expected = 0;
        int actual = obj.getInt("total");
        assertEquals("This system should be empty", expected, actual);
    }

//    @Test
    public void testHostRegistration() {
        this.visitLocalhost();

        Response invResponse = this.getResponse(baseUrl + INVENTORY_HOSTS);
        this.assertResponse(baseUrl, invResponse);

        JsonObject obj = invResponse.readEntity(JsonObject.class);

        int expected = 1;
        int actual = obj.getInt("total");
        assertEquals("The inventory must have one entry for localhost", expected, actual);

        boolean expectedLocalhost = true;
        boolean actualLocalhost = obj.getJsonObject("hosts").containsKey("localhost");
        assertEquals("A host was registered, but it was not localhost",
                expectedLocalhost,
                actualLocalhost);

        invResponse.close();
    }

//    @Test
    public void testSystemPropertiesMatch() {
        Response invResponse = this.getResponse(baseUrl + INVENTORY_HOSTS);
        Response sysResponse = this.getResponse(baseUrl + SYSTEM_PROPERTIES);
        this.assertResponse(baseUrl + INVENTORY_HOSTS, invResponse);
        this.assertResponse(baseUrl + SYSTEM_PROPERTIES, sysResponse);

        JsonObject jsonFromInventory = invResponse.readEntity(JsonObject.class)
                .getJsonObject("hosts")
                .getJsonObject("localhost");
        JsonObject jsonFromSystem = sysResponse.readEntity(JsonObject.class);

        String osNameFromInventory = jsonFromInventory.getString("os.name");
        String osNameFromSystem = jsonFromSystem.getString("os.name");

        String userNameFromInventory = jsonFromInventory.getString("user.name");
        String userNameFromSystem = jsonFromSystem.getString("user.name");

        this.assertProperty("os.name", osNameFromSystem, osNameFromInventory);
        this.assertProperty("user.name", userNameFromSystem, userNameFromInventory);

        invResponse.close();
        sysResponse.close();
    }

    @Test
    public void testUnknownHost() {
        Response response = this.getResponse(baseUrl + INVENTORY_HOSTS);
        this.assertResponse(baseUrl, response);

        Response badResponse = client.target(baseUrl + INVENTORY_HOSTS + "/" + "badhostname")
                .request(MediaType.APPLICATION_JSON)
                .get();

        JsonObject obj = badResponse.readEntity(JsonObject.class);

        boolean expected = true;
        boolean actual = obj.containsKey("ERROR");
        assertEquals("[badhostname] is invalid but didn't raise an error", expected, actual);

        response.close();
        badResponse.close();
    }

    private Response getResponse(String url) {
        return client.target(url).request().get();
    }

    private void assertResponse(String url, Response response) {
        assertEquals("Incorrect response code from " + url, 200, response.getStatus());;
    }

    private void assertProperty(String propertyName, String expected, String actual) {
        assertEquals("System property [" + propertyName + "] "
                        + "from the System service does not match the one in "
                        + "the Inventory service for localhost",
                expected, actual);
    }

    private void visitLocalhost() {
        Response response = this.getResponse(baseUrl + SYSTEM_PROPERTIES);
        this.assertResponse(baseUrl, response);
        response.close();

        Response targetResponse = client.target(baseUrl + INVENTORY_HOSTS + "/localhost")
                .request()
                .get();
        targetResponse.close();
    }
}
