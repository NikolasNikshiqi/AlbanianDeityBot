package com.AlbaninanDeity.Commands;

import com.AlbaninanDeity.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
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
        Map<Long, List<Activity>> map = new HashMap<>();
        for (Member member: members
             ) {
            map.put(member.getIdLong(),member.getActivities());
        }

        for (Map.Entry<Long, List<Activity>> entry : map.entrySet()) {
            long memberId = entry.getKey();
            List<Activity> activities = entry.getValue();

            for (Activity activity : activities) {
                if (activity.getType() == Activity.ActivityType.PLAYING && activity.getName().equalsIgnoreCase("League of Legends")) {
                    channel.get(0).sendMessage("Stop playing that! " + channel.get(0).getGuild().getMemberById(memberId).getAsMention()).queue();
                }
            }
        }
    }
}
