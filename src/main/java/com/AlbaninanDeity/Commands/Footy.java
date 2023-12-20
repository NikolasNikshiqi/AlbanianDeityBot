package com.AlbaninanDeity.Commands;

import com.AlbaninanDeity.Entities.Football.Match;
import com.AlbaninanDeity.Entities.Football.Team;
import com.AlbaninanDeity.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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
        return "Displays lists of matches to be played today or matches of selected team";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "", "", false));
        options.add(new OptionData(OptionType.STRING, "name", "Name of team", true));
        return options;
    }

    @Override
    public void execute(MessageReceivedEvent event, String str) {
        String apiKey = Config.get("FOOTY");

        // Build the request URI
        TextChannel channel = event.getChannel().asTextChannel();
        if (!str.equals(" ")){
            int id = getTeamId("https://api.football-data.org/v4/teams?limit=500",apiKey,channel,str);
            //Have to do 2 calls since the api does not support 2 filters initialized in one call
            String temp = "https://api.football-data.org/v4/teams/" + id + "/matches?status=SCHEDULED";
            getMatchesOfTeam(temp,apiKey,channel);
            temp = "https://api.football-data.org/v4/teams/" + id + "/matches?status=TIMED";
            getMatchesOfTeam(temp,apiKey,channel);
        }else {
            getTodaysMatches("https://api.football-data.org/v4/matches", apiKey, channel);
        }
    }

    public static void getTodaysMatches(String apiUrl,String apiKey, TextChannel channel){
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
            channel.sendMessage("").setEmbeds(embedBuilder.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get upcoming matches of a team
    public static void getMatchesOfTeam(String apiUrl,String apiKey, TextChannel channel){
        URI uri = URI.create(apiUrl);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("X-Auth-Token",apiKey)
                .build();

        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());

            JsonNode matchesNode = rootNode.get("matches");

            List<Match> matches = new ArrayList<>();

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
                channel.sendMessage(String.format(
                        "%s %s %s %s\n",
                        match.getFormattedDate(),
                        match.getStatus(),
                        match.getOpponents(),
                        match.getScore()
                )).queue();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getTeamId(String apiUrl,String apiKey, TextChannel channel,String name){
        URI uri = URI.create(apiUrl);
        int id = 0;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("X-Auth-Token",apiKey)
                .build();


        try {
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode teamsNode = rootNode.get("teams");
            if (teamsNode.isArray()) {
                for (JsonNode node : teamsNode) {
                    if (node.get("name").asText().equalsIgnoreCase(name) || node.get("shortName").asText().equalsIgnoreCase(name) || node.get("tla").asText().equalsIgnoreCase(name)) {
                        System.out.println(node.get("id").asInt());
                        return node.get("id").asInt();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }
}
