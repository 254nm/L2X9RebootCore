package me.l2x9.core.chat.translate;

import com.google.gson.*;
import lombok.Cleanup;
import me.l2x9.core.Localization;
import me.l2x9.core.chat.Translator;
import me.l2x9.core.util.Utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 254n_m
 * @since 2023/04/29 11:06 PM
 * This file was created as a part of L2X9RebootCore
 */

public class LibreTranslate implements Translator {
    private final String apiURL;
    private final String apiKey;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private HashSet<String> supportedLanguages;

    public LibreTranslate(String apiURL, String apiKey) {
        this.apiURL = apiURL;
        this.apiKey = apiKey;
        try {
            URL url = new URL(apiURL + "/languages");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("accept", "application/json");

            JsonArray resp = gson.fromJson(new InputStreamReader(conn.getInputStream()), JsonArray.class);
            HashSet<String> buf = new HashSet<>();
            for (JsonElement jsonElement : resp) {
                JsonObject object = jsonElement.getAsJsonObject();
                buf.add(object.get("code").getAsString());
            }
            supportedLanguages = buf;
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<String> translate(String toTranslate, String targetLang, boolean safeFail) {
        return translate(toTranslate, "auto", targetLang, safeFail);
    }

    @Override
    public CompletableFuture<String> translate(String toTranslate, String sourceLang, String targetLang, boolean safeFail) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                JsonObject args = new JsonObject();
                args.addProperty("q", toTranslate);
                args.addProperty("source", sourceLang);
                args.addProperty("target", targetLang);
                args.addProperty("format", "text");
                args.addProperty("api_key", apiKey);
                JsonObject response = makeRequest("translate", args).getAsJsonObject();

                if (response.has("translatedText")) return response.get("translatedText").getAsString();
                return String.format("Failed to translate %s. Error from api: %s", toTranslate, response.get("error").getAsString());
            } catch (Throwable t) {
                Utils.log("&cFailed to connect to the translator service. Please see stacktrace below for more info");
                t.printStackTrace();
                return safeFail ? toTranslate : String.format(Localization.getLocalization(targetLang).get("could_not_translate_message"), t.getClass().getName());
            }
        });
    }

    @Override
    public CompletableFuture<String> detectLanguage(String str) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("q", str);
                jsonObject.addProperty("api_key", apiKey);
                JsonObject response = makeRequest("detect", jsonObject).getAsJsonArray().get(0).getAsJsonObject();
                if (response.has("error")) {
                    Utils.log("&cFailed to detect language of string&r&a %s&r&3 got error message&r&a \"%s\"&r&3 back from the api", str, response.get("error").getAsString());
                    return "fail";
                }
                float confidence = Float.parseFloat(response.get("confidence").getAsString());
                if (confidence >= 65) return response.get("language").getAsString();
                return "fail";
            } catch (Throwable t) {
                t.printStackTrace();
                return "fail";
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> checkFunctionality() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                detectLanguage("hola").join();
                String translation = translate("hola", "es", "en", true).get();
                return translation.equals("hello");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    @Override
    public HashSet<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    private JsonElement makeRequest(String endpoint, JsonObject args) throws Throwable {
        URL url = new URL(apiURL + "/" + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        conn.getOutputStream().write(gson.toJson(args).getBytes());
        conn.getOutputStream().flush();
        conn.getOutputStream().close();

        @Cleanup InputStream responseStream = conn.getInputStream();
        if (responseStream.available() == 0) {
            Utils.log("&3The request to&r&a %s&r&3 failed retrying...", endpoint);
            responseStream.close();
            return makeRequest(endpoint, args);
        }
        return gson.fromJson(new InputStreamReader(conn.getInputStream()), JsonElement.class);
    }
}
