package net.earomc.earonick;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mojang.util.UUIDTypeAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MojangAPI {

    public static final String HTTP_REQUEST = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    private static Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();

    private static ExecutorService pool = Executors.newCachedThreadPool();

    public static JSONObject requestHTTPJson(String url) throws ParseException {
        StringBuilder json = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            JSONParser jsonParser = new JSONParser();
            return (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject requestProfileJson(UUID uuid) throws ParseException {
        return requestHTTPJson(String.format(HTTP_REQUEST, UUIDTypeAdapter.fromUUID(uuid)));
    }
}
