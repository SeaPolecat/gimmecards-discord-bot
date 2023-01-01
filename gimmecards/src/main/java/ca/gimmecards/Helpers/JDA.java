package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.EmbedBuilder;

public class JDA implements Emotes {

    public static void sendMessage(MessageReceivedEvent event, int color, String emote, String msg) {
        try {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setDescription(emote + " " + msg);
            embed.setColor(color);
            sendEmbed(event, embed);
            embed.clear();
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendMessage(SlashCommandInteractionEvent event, int color, String emote, String msg) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setDescription(emote + " " + msg);
        embed.setColor(color);
        sendEmbed(event, embed);
        embed.clear();
    }

    public static void sendEmbed(MessageReceivedEvent event, EmbedBuilder embed) {
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } catch(InsufficientPermissionException e) {}
    }

    public static void sendEmbed(SlashCommandInteractionEvent event, EmbedBuilder embed) {
        event.replyEmbeds(embed.build()).queue();
    }

    private static String createButtonId(String userId, String slashId, String choice) {
        return userId + ";" + slashId + "_" + choice;
    }

    public static void sendDynamicEmbed(SlashCommandInteractionEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);
        String slashId = event.getInteraction().getId();

        if(page == -1) {
            event.replyEmbeds(embed.build())
            .addActionRow(
                Button.secondary(createButtonId(user.getUserId(), slashId, "refresh")+"", "Refresh")
            ).queue();

        } else {
            event.replyEmbeds(embed.build())
            .addActionRow(
                Button.primary(createButtonId(user.getUserId(), slashId, "left")+"", "◀"),
                Button.primary(createButtonId(user.getUserId(), slashId, "right")+"", "▶"),
                Button.secondary(createButtonId(user.getUserId(), slashId, "refresh")+"", "Refresh")
            ).queue();
        }
        disp.setSlashId(slashId);
        disp.setPage(page);
    }

    public static void editEmbed(ButtonInteractionEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);

        event.editMessageEmbeds(embed.build()).queue();
    }
}
