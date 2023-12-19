package com.AlbaninanDeity.Commands;

import com.AlbaninanDeity.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Queue implements ICommand{
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Returns the songs queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(MessageReceivedEvent event, String str) {
        BlockingQueue<AudioTrack> queue = PlayerManager.getInstance().getMusicManager(event.getGuild()).getTrackScheduler().getQueue();
        TextChannel channel = event.getChannel().asTextChannel();
        if (!queue.isEmpty()) {
            channel.sendTyping().queue();
            for (AudioTrack track : queue
            ) {
                channel.sendMessage(track.getInfo().title).queue();
            }
        }else {
            channel.sendMessage("There are no songs in the queue.").queue();
        }
    }
}
