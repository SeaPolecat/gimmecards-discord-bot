package com.wixsite.seapolecat.Helpers;
import com.wixsite.seapolecat.Interfaces.*;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.EmbedBuilder;

public class Rest implements Emotes {

    public static void sendMessage(GuildMessageReceivedEvent event, String message) {
        try {
            event.getChannel().sendMessage(message).queue();
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendEmbed(GuildJoinEvent event, EmbedBuilder embed) {
        try {
            event.getGuild().getDefaultChannel().sendMessageEmbeds(embed.build()).queue(message -> {}, failure -> {});
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendEmbed(GuildMessageReceivedEvent event, EmbedBuilder embed) {
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue(message -> {}, failure -> {});
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendDynamicEmbed(GuildMessageReceivedEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        if(!disp.getMessageId().equals("")) {
            Rest.removeReaction(event, disp, "◀");
            Rest.removeReaction(event, disp, "▶");
        }
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue(message -> {
                disp.setChannelId(event.getChannel().getId());
                disp.setMessageId(message.getId());
                disp.setPage(page);

                if(page != -1) {
                    message.addReaction("◀").queue(message2 -> {}, failure -> {});
                    message.addReaction("▶").queue(message2 -> {}, failure -> {});
                }
            }, failure -> {});
        } catch(InsufficientPermissionException e) {}
        embed.clear();
    }

    public static void editEmbed(GuildMessageReceivedEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                try {
                    message.editMessageEmbeds(embed.build()).queue(message2 -> {}, failure -> {});
                    embed.clear();
                } catch(InsufficientPermissionException e) {}
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void editEmbed(GuildMessageReactionAddEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                try {
                    message.editMessageEmbeds(embed.build()).queue(message2 -> {}, failure -> {});
                    embed.clear();
                } catch(InsufficientPermissionException e) {}
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void deleteMessage(GuildMessageReceivedEvent event, Display disp) {
        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                message.delete().queue();
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void removeReaction(GuildMessageReceivedEvent event, Display disp, String reaction) {
        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                message.removeReaction(reaction).queue(message2 -> {}, failure -> {});
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void removeReaction(GuildMessageReactionAddEvent event) {
        try {
            event.getReaction().removeReaction(event.getUser()).queue();
        } catch(InsufficientPermissionException e) {}
    }
}
