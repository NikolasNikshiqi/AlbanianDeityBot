package com.AlbaninanDeity.Commands;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Commander extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

           }

    @Override
    public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event) {
        checkActivity(event.getGuild().getMembers(),event.getGuild().getTextChannelsByName("chat",true));    }

    @Override
    public void onUserActivityStart(UserActivityStartEvent event) {
        checkActivity(event.getGuild().getMembers(),event.getGuild().getTextChannelsByName("chat",true));
    }

    private void checkActivity(List<Member> members, List<TextChannel> channel) {

    }
}
