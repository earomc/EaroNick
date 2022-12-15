package net.earomc.earonick.mojangapi;

import net.earomc.earonick.mojangapi.requests.ProfileRequest;
import net.earomc.earonick.mojangapi.requests.UUIDRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class MojangAPI {

    /**
     * Sends out a http request to the given url expecting a json object.
     * Takes some time to execute, so it is advised for this method to be executed asynchronously.
     * @param url Http URL where the json object is requested from.
     * @return Returns a {@link JSONObject} that can contain all sorts of useful info.
     * @throws MojangAPIException If there's any error during the process it is summarized as {@link MojangAPIException}
     * and rethrown. If there is an "errorMessage" attribute in the json file as usually seen with faulty mojang api requests,
     * a new {@link MojangAPIException} is thrown containing the actual error message from the json object.
     */
    public static JSONObject requestHTTPJson(String url) throws MojangAPIException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
            Object errorMessageObj = jsonObject.get("errorMessage");
            if (errorMessageObj != null) {
                throw new MojangAPIException((String) errorMessageObj);
            }
            return jsonObject;
        } catch (IOException | ParseException e) {
            throw new MojangAPIException(e);
        }
    }

    public static JSONObject requestProfileJson(UUID uuid) throws MojangAPIException {
        return requestHTTPJson(ProfileRequest.getFormattedRequestURL(uuid));
    }

    public static ProfileRequest.Result requestProfile(UUID uuid) throws MojangAPIException {
        JSONObject jsonObject = requestProfileJson(uuid);
        return ProfileRequest.Result.fromJSON(jsonObject);
    }

    public static Skin requestSkin(UUID uuid) throws MojangAPIException {
        return Skin.fromProfileRequest(requestProfile(uuid));
    }

    public static UUID requestUUID(String playerName) throws MojangAPIException {
        JSONObject jsonObject = requestHTTPJson(UUIDRequest.getFormattedRequestURL(playerName));
        return uuidFromString((String) jsonObject.get("id"));
    }

    public static String requestName(UUID uuid) throws MojangAPIException {
        return requestProfile(uuid).name;
    }

    /**
     * Method to turn a UUID object to a UUID String.
     * Differs from the Standard {@link UUID}#toString(s) method in that it returns the String without dashes needed for the Mojang API http request.
     * Taken from {@link com.mojang.util.UUIDTypeAdapter}
     *
     * @param uuid The UUID to turn into a String.
     * @return The UUID as string usable for Mojang API requests.
     */
    public static String getUUIDString(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    /**
     * Method to turn a Mojang-formatted UUID String back into a {@link UUID} object.
     * Taken from {@link com.mojang.util.UUIDTypeAdapter}
     *
     * @param uuidString The String to parse
     * @return The UUID.
     */
    public static UUID uuidFromString(String uuidString) {
        return UUID.fromString(uuidString.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

}
