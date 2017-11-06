package by.delaidelo.olmicro.inventory;

import by.delaidelo.olmicro.util.InventoryUtil;
import by.delaidelo.olmicro.util.ReadyJson;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * vladimir
 * 04.11.17
 * 2013-2017, ЗАО "Делай Дело"
 */
@ApplicationScoped
public class InventoryManager {
    private ConcurrentMap<String, JsonObject> inv = new ConcurrentHashMap<>();

    public void add(String hostname, JsonObject systemProps) {
        inv.putIfAbsent(hostname, systemProps);
    }

    public JsonObject get(String hostname) {
        JsonObject properties = inv.get(hostname);
        if (properties == null) {
            if (InventoryUtil.responseOk(hostname)) {
                properties = InventoryUtil.getProperties(hostname);
                this.add(hostname, properties);
            } else {
                return ReadyJson.SERVICE_UNREACHABLE.getJson();
            }
        }
        return properties;
    }

    public JsonObject list() {
        JsonObjectBuilder systems = Json.createObjectBuilder();
        inv.forEach((host, props) -> {
            JsonObject systemProps = Json.createObjectBuilder()
                    .add("os.name", props.getString("os.name"))
                    .add("user.name", props.getString("user.name"))
                    .build();
            systems.add(host, systemProps);
        });
        systems.add("hosts", systems);
        systems.add("total", inv.size());
        return systems.build();
    }
}
