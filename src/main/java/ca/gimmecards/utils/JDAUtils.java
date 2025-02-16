package ca.gimmecards.utils;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
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
    public static String createButtonId(Display disp, String buttonType, User[] editableUsers) {
        String buttonId = disp.getSlashId() + ";" + buttonType;

        for(User u : editableUsers)
            buttonId += ";" + u.getUserId();

        return buttonId;
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

    private static void addFlippableButtons(SlashCommandInteractionEvent event, Display disp, EmbedBuilder embed, User[] editableUsers) {
        event.getHook().editOriginalEmbeds(embed.build())
        .setActionRow(
            Button.primary(createButtonId(disp, "left", editableUsers), "◀"),
            Button.primary(createButtonId(disp, "right", editableUsers), "▶"),
            Button.secondary(createButtonId(disp, "refresh", editableUsers), "Refresh")
        ).queue();
    }

    private static void addNonFlippableButtons(SlashCommandInteractionEvent event, Display disp, EmbedBuilder embed, User[] editableUsers) {
        event.getHook().editOriginalEmbeds(embed.build())
        .setActionRow(
            Button.secondary(createButtonId(disp, "refresh", editableUsers), "Refresh")
        ).queue();
    }

    public static void sendDynamicEmbed(SlashCommandInteractionEvent event, Display disp, User user, @Nullable Server server, boolean isFlippable) {
        EmbedBuilder embed = disp.buildEmbed(user, server);

        if(isFlippable)
            addFlippableButtons(event, disp, embed, new User[]{user});
        else
            addNonFlippableButtons(event, disp, embed, new User[]{user});
    }

    // for multiplayer
    public static void sendDynamicEmbed(SlashCommandInteractionEvent event, Display disp, User user, User target, User[] editableUsers, @Nullable Server server, boolean isFlippable) {
        EmbedBuilder embed = disp.buildEmbed(target, server);

        if(isFlippable)
            addFlippableButtons(event, disp, embed, editableUsers);
        else
            addNonFlippableButtons(event, disp, embed, editableUsers);
    }

    /**
     * edits a dynamic embed (one that can be page-flipped or refreshed) - based on a button event
     * @param event the button event
     * @param user the player
     * @param server the server
     * @param disp the type of Display that this embed should show
     * @param page the page number that this embed should show after being edited
     */
    public static void editEmbed(ButtonInteractionEvent event, Display disp, User user, @Nullable Server server) {
        EmbedBuilder embed = disp.buildEmbed(user, server);

        if(embed == null) {
            event.getMessage().delete().queue();
            return;
        }
        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }
}
