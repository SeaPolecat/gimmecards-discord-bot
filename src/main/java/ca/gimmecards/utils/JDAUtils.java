package ca.gimmecards.utils;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.Nullable;

public class JDAUtils {

    /**
     * creates a button ID that's used to handle page-flipping
     * @param userId the Discord ID of the player
     * @param slashId the ID of a specific slash command
     * @param buttonType the type of button (left arrow, right arrow, or refresh)
     * @return a button ID
     */
    public static String createButtonId(SlashCommandInteractionEvent event, String buttonType) {
        String userId = event.getUser().getId();
        String slashId = event.getInteraction().getId();

        return userId + ";" + slashId + "_" + buttonType;
    }

    /**
     * sends a formatted embed message in the server based on a message event
     * @param event the message event
     * @param color the color that the embed should appear in
     * @param emote the emote that should come before the message
     * @param msg the string message you want to send
     */
    public static void sendMessage(MessageReceivedEvent event, int color, String emote, String msg) {
        try {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setDescription(emote + " " + msg);
            embed.setColor(color);
            sendEmbed(event, embed);
        } catch(InsufficientPermissionException e) {}
    }

    /**
     * sends a formatted embed message in the server based on a slash event
     * @param event the slash event
     * @param color the color that the embed should appear in
     * @param emote the emote that should come before the message
     * @param msg the string message you want to send
     */
    public static void sendMessage(SlashCommandInteractionEvent event, int color, String emote, String msg) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setDescription(emote + " " + msg);
        embed.setColor(color);
        sendEmbed(event, embed);
    }

     /**
      * sends a formatted embed message in the server based on a slash event, with a 'how to get Premium' button attached below
      * @param event the slash event
      * @param color the color that the embed should appear in
      * @param emote the emote that should come before the message
      * @param msg the string message you want to send
      * @param adCard a sample premium card to show in /redeem messages; can be null if you don't want this to show
      */
    public static void sendPremiumMessage(SlashCommandInteractionEvent event, int color, String emote, String msg, @Nullable Card adCard) {
        EmbedBuilder embed = new EmbedBuilder();
        Emoji kofiEmote = event.getJDA().getEmojiById("1140389615379959860");

        embed.setDescription(emote + " " + msg);

        if(adCard != null) {
            embed.setImage(adCard.getCardImage());
        }
        embed.setColor(color);
        event.getHook().editOriginalEmbeds(embed.build())
        .setActionRow(
            Button.primary(createButtonId(event, "premium"), "How to get Premium").withEmoji(kofiEmote)
        ).queue();
    }

    /**
     * sends a plain embed in the server based on a message event
     * @param event the message event
     * @param embed the embed to send
     */
    public static void sendEmbed(MessageReceivedEvent event, EmbedBuilder embed) {
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } catch(InsufficientPermissionException e) {}
    }

    /**
     * sends a plain embed in the server based on a slash event
     * @param event the slash event
     * @param embed the embed to send
     */
    public static void sendEmbed(SlashCommandInteractionEvent event, EmbedBuilder embed) {
        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }

    /**
     * sends an embed in the server that can be page-flipped or refreshed - based on a slash event
     * @param event the slash event
     * @param user the player
     * @param server the server
     * @param disp the type of Display that this embed should show
     * @param page the page number that this embed should initially show
     */
    public static void sendDynamicEmbed(SlashCommandInteractionEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);
        String slashId = event.getInteraction().getId();

        if(page == -1) {
            event.getHook().editOriginalEmbeds(embed.build())
            .setActionRow(
                Button.secondary(createButtonId(event, "refresh"), "Refresh")
            ).queue();

        } else {
            event.getHook().editOriginalEmbeds(embed.build())
            .setActionRow(
                Button.primary(createButtonId(event, "left"), "◀"),
                Button.primary(createButtonId(event, "right"), "▶"),
                Button.secondary(createButtonId(event, "refresh"), "Refresh")
            ).queue();
        }
        disp.setSlashId(slashId);
        disp.setPage(page);
    }

    /**
     * edits a dynamic embed (one that can be page-flipped or refreshed) - based on a button event
     * @param event the button event
     * @param user the player
     * @param server the server
     * @param disp the type of Display that this embed should show
     * @param page the page number that this embed should show after being edited
     */
    public static void editEmbed(ButtonInteractionEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);
        
        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }
}
