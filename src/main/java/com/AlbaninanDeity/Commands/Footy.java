package com.AlbaninanDeity.Commands;

import com.AlbaninanDeity.Entities.Football.Match;
import com.AlbaninanDeity.Entities.Football.Team;
import com.AlbaninanDeity.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Footy implements ICommand{
    @Override
    public String getName() {
        return "footy";
    }

    @Override
    public String getDescription() {
        return "Displays a list of football matches that will be played today";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(MessageReceivedEvent event, String str) {
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

            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());

            // Extract and process match data
            List<Match> matches = new ArrayList<>();

            JsonNode matchesNode = rootNode.get("matches");

            if (matchesNode.isArray()){
                for (JsonNode node: matchesNode) {
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                    Match match = new Match(LocalDateTime.parse(node.get("utcDate").asText(),formatter),LocalDateTime.parse(node.get("lastUpdated").asText(),formatter),node.get("status").asText());
                    Team homeTeam = objectMapper.treeToValue(node.get("homeTeam"), Team.class);
                    match.setHomeTeam(homeTeam);

                    // Set away team
                    Team awayTeam = objectMapper.treeToValue(node.get("awayTeam"), Team.class);
                    match.setAwayTeam(awayTeam);

                    if (!node.get("score").get("winner").isNull() && node.get("score").get("winner") != null) {
                        match.setHomeScore(node.get("score").get("fullTime").get("home").asInt());
                        match.setAwayScore(node.get("score").get("fullTime").get("away").asInt());
                    }
                    matches.add(match);
                }
            }
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Matches");

            for (Match match : matches) {
                embedBuilder.addField(
                        String.format(""),
                        String.format(
                                "%s %s %s %s\n",
                                match.getFormattedDate(),
                                match.getStatus(),
                                match.getOpponents(),
                                match.getScore()
                        ),false
                );

            }
            event.getChannel().sendMessage("").setEmbeds(embedBuilder.build()).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
