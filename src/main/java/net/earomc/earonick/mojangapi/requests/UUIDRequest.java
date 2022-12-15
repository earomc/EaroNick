package net.earomc.earonick.mojangapi.requests;

public class UUIDRequest {
    public static final String URL_STRING = "https://api.mojang.com/users/profiles/minecraft/%s";

    public static String getFormattedRequestURL(String name) {
        return String.format(URL_STRING, name);
    }


}
