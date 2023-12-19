package com.AlbaninanDeity.Commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.AlbaninanDeity.Commands.Bet.*;

public class Stand implements ICommand{

    @Override
    public String getName() {
        return "stand";
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
        TextChannel channel = event.getMessage().getChannel().asTextChannel();
        String playerId = event.getAuthor().getId();
        // Use the playerId to keep track of cards specific to the player
        ArrayList<String> playerDealtCards = playerDealtCardsMap.computeIfAbsent(playerId, k -> new ArrayList<>());
        if (playerDealtCards.isEmpty()){
            return;
        }
        String dealer = "pngs/" + playerId +  "dealer.png";
        String player = "pngs/" + playerId +  "player.png";
        int dealerSum =0;
        int playerSum = 0;

        //Calculate sum of the dealer cards
        for (int i = 0; i < 2; i++) {
            char temp = playerDealtCards.get(i).charAt(1);
            if (temp == 'A'){
                dealerSum += 11;
            }else if(temp == '1'){
                dealerSum += 10;
            } else if (temp > '9' || temp < 1) {
                dealerSum += 10;
            }else {
                dealerSum += Integer.parseInt(String.valueOf(temp));
            }
        }

        for (int i = 2; i < playerDealtCards.size(); i++) {
            char temp = playerDealtCards.get(i).charAt(1);
            if (temp == 'A') {
                playerSum += 1; // Initially count Ace as 1, and later check if adding 10 makes a better hand
            } else if (temp >= '2' && temp <= '9') {
                playerSum += Character.getNumericValue(temp); // Convert char to int for numeric cards
            } else {
                playerSum += 10; // For '10', 'J', 'Q', 'K'
            }
        }


        //Add all the new cards towards dealers sum
        while (dealerSum < 16){
            generateRandomCard(playerDealtCards);
            char temp = playerDealtCards.get(playerDealtCards.size()-1).charAt(1);
            if (temp == 'A') {
                dealerSum += 1; // Initially count Ace as 1, and later check if adding 10 makes a better hand
            } else if (temp >= '2' && temp <= '9') {
                dealerSum += Character.getNumericValue(temp); // Convert char to int for numeric cards
            } else {
                dealerSum += 10; // For '10', 'J', 'Q', 'K'
            }
            mergeFiles(dealer,"pngs/" + playerDealtCards.get(playerDealtCards.size()-1),dealer);
        }
        //Check if aces should be 1s or 11s
        if (playerDealtCards.contains("A") && playerSum + 10 <= 21) {
            playerSum += 10; // Add 10 for Ace if it doesn't bust the hand
        }
        if (playerDealtCards.contains("A") && dealerSum + 10 <= 21) {
            dealerSum += 10; // Add 10 for Ace if it doesn't bust the hand
        }

        channel.sendMessage("My cards:").queue();
        channel.sendFiles(FileUpload.fromData(new File(dealer))).queue();
        channel.sendMessage("Your cards:").queue();
        channel.sendFiles(FileUpload.fromData(new File(player))).queue();

        if (dealerSum > 21){
            channel.sendMessage("Dealer got more than 21. You win!").queue();

        }else if(dealerSum == playerSum){
            channel.sendMessage("Draw!").queue();

        }else if(dealerSum < playerSum){
            channel.sendMessage("You win!").queue();

        }else {
            channel.sendMessage("Dealer wins!").queue();

        }
        playerDealtCardsMap.remove(playerId);
        new File(dealer).delete();
        new File(player).delete();
    }
}
