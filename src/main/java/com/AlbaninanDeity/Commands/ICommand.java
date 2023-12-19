package com.AlbaninanDeity.Commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface ICommand {
    String getName();

    String getDescription();

    List<OptionData> getOptions();

    //void execute();
    void execute(MessageReceivedEvent event,String str);
}
