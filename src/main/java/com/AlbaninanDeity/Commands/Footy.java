package com.AlbaninanDeity.Commands;

import com.AlbaninanDeity.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Footy implements ICommand{
    @Override
    public String getName() {
        return "footy";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(MessageReceivedEvent event, String str) {
        System.out.println("here");
        String apiKey = Config.get("FOOTY");

        // Build the request URI
        String apiUrl = "https://api.football-data.org/v4/matches";
        URI uri = URI.create(apiUrl);

        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("X-Auth-Token", apiKey)  // Add your API key to the request headers
                .build();

        try {
            // Send the request and retrieve the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response status code and body
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
