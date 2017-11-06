package by.delaidelo.olmicro.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * vladimir
 * 04.11.17
 * 2013-2017, ЗАО "Делай Дело"
 */
@RequestScoped
@Path("hosts")
public class InventoryResource {

    @Inject
    private InventoryManager manager;

    @GET
    @Path("{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getPropertiesForHost(@PathParam("hostname") String hostname) {
        return manager.get(hostname);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject listContents() {
        return manager.list();
    }
}
