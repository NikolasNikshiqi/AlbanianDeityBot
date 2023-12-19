package com.AlbaninanDeity;

import com.AlbaninanDeity.Commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


import javax.security.auth.login.LoginException;

public class Main {
    public static JDABuilder builder;
    public static final String prefix = "$";

    public static void main(String[] args) throws LoginException {
        CommandManager manager = new CommandManager();
        manager.add(new Play());
        manager.add(new Skip());
        manager.add(new Queue());
        manager.add(new Bet());
        manager.add(new Hit());
        manager.add(new Stand());
        manager.add(new Footy());
        builder = JDABuilder.createDefault(Config.get("TOKEN"));
        //builder.enableIntents(GatewayIntent.);
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setChunkingFilter(ChunkingFilter.ALL); // enable member chunking for all guilds
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ACTIVITY);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("The Çifteli"));
        builder.addEventListeners(manager);
        builder.addEventListeners(new Commander());
        builder.build();
    }
}
