package net.earomc.earonick.mojangapi.requests;

import net.earomc.earonick.mojangapi.MojangAPI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.UUID;

public class ProfileRequest {

    public static final String URL_STRING = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    public static String getFormattedRequestURL(UUID uuid) {
        return String.format(URL_STRING, MojangAPI.getUUIDString(uuid));
    }

    public static class Result {
        // the player's UUID
        public final UUID id;
        // the player's name
        public final String name;
        //player's skin texture signature
        public final String signature;
        //the actual skin texture serialized as a string
        public final String value;
        /*
        the name of the property which in this case should always be "textures" as in skin textures.

         */

        public final String propName;

        public Result(UUID id, String name, String signature, String value, String propName) {
            this.id = id;
            this.name = name;
            this.signature = signature;
            this.value = value;
            this.propName = propName;
        }

        @Override
        public String toString() {
            return "Result{" + "id=" + id +
                    ", name='" + name + '\'' +
                    ", signature='" + signature + '\'' +
                    ", value='" + value + '\'' +
                    ", propName='" + propName + '\'' +
                    '}';
        }

        public static Result fromJSON(JSONObject jsonObject) {
            UUID uuid = MojangAPI.uuidFromString((String) jsonObject.get("id"));
            String name = (String) jsonObject.get("name");

            //properties
            JSONArray properties = (JSONArray) jsonObject.get("properties");

            //only get the first entry because the response from Mojang only has one entry in the properties array for this request.
            JSONObject propFirstEntry = (JSONObject) properties.get(0);
            String signature = (String) propFirstEntry.get("signature");
            String value = (String) propFirstEntry.get("value");
            String propName = (String) propFirstEntry.get("name");

            return new Result(uuid, name, signature, value, propName);
        }

        public static Result fromJSON(String json) {
            JSONParser jsonParser = new JSONParser();
            try {
                return fromJSON((JSONObject) jsonParser.parse(json));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
