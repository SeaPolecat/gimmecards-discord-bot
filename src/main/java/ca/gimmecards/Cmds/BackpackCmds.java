package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackCmds extends Cmds {

    public static void viewBackpack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = new BackpackDisplay(user.getUserId()).findDisplay();

        GameObject.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);

        if(!GameObject.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            GameObject.sendMessage(event, red_, "⏰", "Please wait another " 
            + GameObject.findTimeLeft(user.getRedeemEpoch(), 30, true));

        } else {
            String msg = "";

            user.resetRedeemEpoch();

            msg += GameObject.formatNick(event) + " redeemed a token!";
            msg += user.updateTokens(1, true);
            msg += user.updateCredits(GameObject.randRange(24, 30), false);

            msg += "\n\n[Click here](https://www.patreon.com/gimmecards) to join us on " 
            + patreon_ + " **Patreon** ┇ `/patreon`";

            msg += "\n\n" + Main.updateMsg + "\n";

            GameObject.sendMessage(event, user.getGameColor(), "🎒", msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!GameObject.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            GameObject.sendMessage(event, red_, "⏰", "Please wait another " 
            + GameObject.findTimeLeft(user.getDailyEpoch(), 1440, true));

        } else {
            Card item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = ui.getUserName() + "'s shiny card";

            msg += "🎴 ";
            msg += GameObject.formatNick(event) + " claimed their daily shiny card!";
            msg += user.updateCredits(GameObject.randRange(240, 300), true);

            user.resetDailyEpoch();

            user.addSingleCard(item, false);
            
            Display.displayCard(event, user, ui, item, msg, footer, false);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignGameColor(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping hexCode = event.getOption("hex-code");

        if(hexCode == null) { return; }

        try {
            int color = Integer.parseInt(hexCode.getAsString(), 16);

            user.setGameColor(color);

            GameObject.sendMessage(event, user.getGameColor(), eevee_, 
            "Set your game's theme color to **" + hexCode.getAsString().toUpperCase() + "**");
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException e) {
            e.printStackTrace();
            GameObject.sendMessage(event, red_, "❌", "That's not a valid hex code!");
        }
    }

    public static void pinCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            int index = cardNum.getAsInt() - 1;
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);
            String cardImage = cc.getCard().getCardImage();

            user.setPinnedCard(cardImage);

            GameObject.sendMessage(event, user.getGameColor(), "🎒", "**" + cardTitle + "** has been pinned to your backpack!");
            try { User.saveUsers(); } catch(Exception e) {}
            
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void viewCooldowns(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += server.formatCmd("redeem") + " ┇ ";
        if(GameObject.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + GameObject.findTimeLeft(user.getRedeemEpoch(), 30, true) + "\n\n";
        }
        desc += server.formatCmd("minigame") + " ┇ ";
        if(GameObject.isCooldownDone(user.getMinigameEpoch(), 60, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + GameObject.findTimeLeft(user.getMinigameEpoch(), 60, true) + "\n\n";
        }
        desc += server.formatCmd("vote") + " ┇ ";
        if(GameObject.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + GameObject.findTimeLeft(user.getVoteEpoch(), 720, true) + "\n\n";
        }
        desc += server.formatCmd("daily") + " ┇ ";
        if(GameObject.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + GameObject.findTimeLeft(user.getDailyEpoch(), 1440, true) + "\n\n";
        }
        desc += server.formatCmd("buy") + " ┇ ";
        if(GameObject.isCooldownDone(user.getMarketEpoch(), 15, true)) {
            desc += "✅";
        } else {
            desc += "⏰ " + GameObject.findTimeLeft(user.getMarketEpoch(), 15, true);
        }
        embed.setTitle(ui.getUserName() + "'s Cooldowns");
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getGameColor());
        GameObject.sendEmbed(event, embed);
        embed.clear();
    }
}
