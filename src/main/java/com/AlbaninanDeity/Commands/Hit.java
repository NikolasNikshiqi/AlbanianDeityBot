package com.AlbaninanDeity.Commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.AlbaninanDeity.Commands.Bet.*;

public class Hit implements ICommand{
    @Override
    public String getName() {
        return "hit";
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
        ArrayList<String> playerDealtCards = playerDealtCardsMap.get(playerId);
        if (playerDealtCards.isEmpty()){
            return;
        }
        int sum = 0;
        String dealer = "pngs/" + playerId +  "dealer.png";
        String player = "pngs/" + playerId +  "player.png";
        // Deal an additional card
        generateRandomCard(playerDealtCards);
        for (int i = playerDealtCards.size() - 1; i < playerDealtCards.size(); i++) {
            mergeFiles(player,"pngs/" + playerDealtCards.get(i),player);

        }

        channel.sendMessage("My cards:").queue();
        channel.sendFiles(FileUpload.fromData(new File(dealer))).queue();
        channel.sendMessage("Your cards:").queue();
        channel.sendFiles(FileUpload.fromData(new File(player))).queue();

        for (int i = 2; i < playerDealtCards.size(); i++) {
            char temp = playerDealtCards.get(i).charAt(1);
            if (temp == 'A') {
                sum += 1; // Initially count Ace as 1, and later check if adding 10 makes a better hand
            } else if (temp >= '2' && temp <= '9') {
                sum += Character.getNumericValue(temp); // Convert char to int for numeric cards
            } else {
                sum += 10; // For '10', 'J', 'Q', 'K'
            }
        }

        if (playerDealtCards.contains("A") && sum + 10 <= 21) {
            sum += 10; // Add 10 for Ace if it doesn't bust the hand
        }

        if (sum > 21){
            channel.sendTyping().queue();
            channel.sendMessage("Busted!").queue();
            new File(dealer).delete();
            new File(player).delete();
            playerDealtCardsMap.remove(playerId);
        }
    }

}
