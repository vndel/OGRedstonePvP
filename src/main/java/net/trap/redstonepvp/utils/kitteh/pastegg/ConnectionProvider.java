package me.drman.redstonepvp.utils.kitteh.pastegg;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConnectionProvider {
    static String processPasteRequest(String string) {
        URL uRL = null;
        try {
            uRL = new URL("https://api.paste.gg/v1/pastes");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) uRL.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        {
            try {
                httpURLConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept", "application/json");

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(string.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (InputStream inputStream = httpURLConnection.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return string;
    }
}