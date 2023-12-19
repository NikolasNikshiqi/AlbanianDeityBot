package com.AlbaninanDeity;

import com.AlbaninanDeity.Commands.ICommand;
import net.dv8tion.jda.api.entities.Guild;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter{

    private List<ICommand> commands = new ArrayList<>();


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String arg = event.getMessage().getContentRaw();
        String arr[] = arg.split(" ",2);
        String command = arr[0];
        String link = " ";
        if (arr.length >1) {
            link = arr[1];
        }
        //Get the raw command
        if (arr[0].startsWith(Main.prefix)){
            command = arr[0].substring(1);
        }
        for(ICommand cmd : commands) {
            if(cmd.getName().equals(command)) {
                cmd.execute(event,link);
                return;
            }
        }
    }

    public void add(ICommand command) {
        commands.add(command);
    }
}