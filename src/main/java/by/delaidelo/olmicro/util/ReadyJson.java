package by.delaidelo.olmicro.util;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * vladimir
 * 04.11.17
 * 2013-2017, ЗАО "Делай Дело"
 */
public enum ReadyJson {

    SERVICE_UNREACHABLE();

    private JsonObject json;

    public JsonObject getJson() {
        switch(this) {
            case SERVICE_UNREACHABLE:
                this.serviceUnreachable();
                break;
            default:
                break;
        }
        return json;
    }

    private void serviceUnreachable() {
        json = Json.createObjectBuilder()
                .add("ERROR", "Unknown hostname or the resource may not be running on the host machine")
                .build();
    }

}
