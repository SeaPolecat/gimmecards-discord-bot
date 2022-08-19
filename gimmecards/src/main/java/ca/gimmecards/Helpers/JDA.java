package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.EmbedBuilder;

public class JDA implements Emotes {

    public static String findMentionId(MessageReceivedEvent event, String nick) {
        String mentionId;

        try {
            mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
        } catch(IndexOutOfBoundsException e) {
            try {
                mentionId = event.getGuild().getMembersByEffectiveName(nick, true).get(0).getId();
            } catch(IndexOutOfBoundsException e2) {
                return null;
            }
        }
        return mentionId;
    }

    public static void sendMessage(MessageReceivedEvent event, int color, String emote, String msg) {
        try {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setDescription(emote + " " + msg);
            embed.setColor(color);
            sendEmbed(event, embed);
            embed.clear();
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendEmbed(GuildJoinEvent event, EmbedBuilder embed) {
        try {
            event.getGuild().getDefaultChannel().sendMessageEmbeds(embed.build()).queue();
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendEmbed(MessageReceivedEvent event, EmbedBuilder embed) {
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendDynamicEmbed(MessageReceivedEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        if(!disp.getMessageId().equals("")) {
            JDA.removeReaction(event, disp, "◀");
            JDA.removeReaction(event, disp, "▶");
        }
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue(message -> {
                disp.setChannelId(event.getChannel().getId());
                disp.setMessageId(message.getId());
                disp.setPage(page);

                if(page != -1) {
                    message.addReaction(Emoji.fromFormatted("◀")).queue();
                    message.addReaction(Emoji.fromFormatted("▶")).queue();
                }
            });
        } catch(InsufficientPermissionException e) {}
    }

    public static void editEmbed(MessageReceivedEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(user, event); //so it shows the correct pfp for updating multiplayer disps
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                try {
                    message.editMessageEmbeds(embed.build()).queue();
                    embed.clear();
                } catch(InsufficientPermissionException e) {}
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void editEmbed(MessageReactionAddEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                try {
                    message.editMessageEmbeds(embed.build()).queue();
                    embed.clear();
                } catch(InsufficientPermissionException e) {}
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void deleteMessage(MessageReceivedEvent event, Display disp) {
        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                message.delete().queue();
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void removeReaction(MessageReceivedEvent event, Display disp, String reaction) {
        try {
            event.getJDA().getTextChannelById(disp.getChannelId()).retrieveMessageById(disp.getMessageId()).queue(message -> {
                message.removeReaction(Emoji.fromFormatted(reaction)).queue();
            }, (failure) -> {});
        } catch(NullPointerException | InsufficientPermissionException e) {}
    }

    public static void removeReaction(MessageReactionAddEvent event) {
        try {
            event.getReaction().removeReaction(event.getUser()).queue();
        } catch(InsufficientPermissionException e) {}
    }
}
