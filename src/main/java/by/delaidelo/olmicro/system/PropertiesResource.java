package by.delaidelo.olmicro.system;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * vladimir
 * 04.11.17
 * 2013-2017, ЗАО "Делай Дело"
 */
@RequestScoped
@Path("properties")
public class PropertiesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProperties() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        System.getProperties()
                .forEach((key, value) -> builder.add((String) key,
                        (String) value));

        return builder.build();
    }
}
