package com.AlbaninanDeity.Commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bet implements ICommand{

    protected static final Map<String, ArrayList<String>> playerDealtCardsMap = new HashMap<>();
    @Override
    public String getName() {
        return "bet";
    }

    @Override
    public String getDescription() {
        return "Start playing a game of blackjack";
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

        String dealer = "pngs/" + playerId +  "dealer.png";
        String player = "pngs/" + playerId +  "player.png";

        // Play the game with the specific player
        for (int i = 0; i < 4; i++) {
            generateRandomCard(playerDealtCards);
        }
        //bot
        channel.sendMessage("My cards: ").queue();
        mergeFiles("pngs/" + playerDealtCards.get(0),"pngs/" + playerDealtCards.get(1),dealer);
        channel.sendFiles(FileUpload.fromData(new File(dealer))).queue();
        //player
        channel.sendMessage("Your cards: ").queue();
        mergeFiles("pngs/" + playerDealtCards.get(2),"pngs/" + playerDealtCards.get(3),player);
        channel.sendFiles(FileUpload.fromData(new File(player))).queue();
        scheduleCardClearTask(playerId,5,TimeUnit.MINUTES);
    }

    protected static void generateRandomCard(ArrayList<String> dealtCards) {
        final String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        final String[] SUITS = {"h", "d", "c", "s"};
        Random random = new Random();
        String rank, suit, card;
        do {
            rank = RANKS[random.nextInt(RANKS.length)];
            suit = SUITS[random.nextInt(SUITS.length)];
            card = suit + rank;
        } while (dealtCards.contains(card + ".png")); // Keep generating until a unique card is found
        dealtCards.add(card + ".png");
    }

    public static void mergeFiles(String firstImage, String secondImage,String output) {
        try {
            // Load images
            BufferedImage image1 = ImageIO.read(new File(firstImage));
            BufferedImage image2 = ImageIO.read(new File(secondImage));

            // Create a new BufferedImage with the combined dimensions
            int combinedWidth = image1.getWidth() + image2.getWidth();
            int combinedHeight = Math.max(image1.getHeight(), image2.getHeight());
            BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

            // Draw the first image onto the combined image
            Graphics2D g = combinedImage.createGraphics();
            g.drawImage(image1, 0, 0, null);

            // Draw the second image onto the combined image next to the first image
            g.drawImage(image2, image1.getWidth(), 0, null);
            g.dispose();
            // Save the result
            ImageIO.write(combinedImage, "png", new File(output));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void scheduleCardClearTask(String playerId, long delay, TimeUnit timeUnit) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            playerDealtCardsMap.remove(playerId);
            System.out.println("Player's dealt cards cleared after " + delay + " minutes.");
        };

        // Schedule the task to run after the specified delay
        executorService.schedule(task, delay, timeUnit);

        // Shutdown the executor service to release resources after the task completes
        executorService.shutdown();
    }
}
