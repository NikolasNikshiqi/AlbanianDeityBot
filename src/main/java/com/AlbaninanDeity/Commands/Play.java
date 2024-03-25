package com.AlbaninanDeity.Commands;

import com.AlbaninanDeity.lavaplayer.PlayerManager;


import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Play implements ICommand{

    public String getName() {
        return "play";
    }


    public String getDescription() {
        return "Will play a song";
    }

    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "name", "Name of the song to play", true));
        return options;
    }

    @Override
    public void execute(MessageReceivedEvent event ,String link) {
        final TextChannel channel = event.getChannel().asTextChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final AudioManager audioManager = event.getGuild().getAudioManager();
        final AudioChannel memberChannel = memberVoiceState.getChannel();
        //Join audio channel
        if (!selfVoiceState.inAudioChannel()) {
            audioManager.openAudioConnection(memberChannel);
        }
        
        //channel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`", memberChannel.getName()).queue();

        //Play

        //String link = "https://soundcloud.com/theoneworldensemble/himni-i-flamurit-hymn-to-the";
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        //event.getMessage().getChannel().sendTyping().queue();
        PlayerManager.getInstance().loadAndPlay(channel, link);
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
