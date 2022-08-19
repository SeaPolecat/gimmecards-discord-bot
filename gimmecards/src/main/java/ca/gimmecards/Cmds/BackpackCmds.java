package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackCmds extends Cmds {

    public static void viewBackpack(MessageReceivedEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = new BackpackDisplay(user.getUserId()).findDisplay();

        JDA.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(MessageReceivedEvent event) {
        User user = User.findUser(event);

        if(!Check.isCooldownDone(user.getRedeemEpoch(), Check.findCooldown(user, 30), true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " 
            + Check.findTimeLeft(user.getRedeemEpoch(), Check.findCooldown(user, 30), true));

        } else {
            String msg = "";

            user.resetRedeemEpoch();

            msg += UX.formatNick(event) + " redeemed a token!";
            msg += user.updateTokens(1, true);
            msg += user.updateEnergy(UX.randRange(24, 30), false);

            msg += "\n\nLooking to boost your TCG experience?\n"
            + "[Click here](https://www.patreon.com/gimmecards) to support us on " + patreon_ + " **Patreon**";

            msg += "\n\n" + Main.updateMsg + "\n";

            Update.updateBackpackDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "🎒", msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(MessageReceivedEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!Check.isCooldownDone(user.getDailyEpoch(), Check.findCooldown(user, 1440), true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " 
            + Check.findTimeLeft(user.getDailyEpoch(), Check.findCooldown(user, 1440), true));

        } else {
            Data item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = ui.getUserName() + "'s shiny card";

            user.resetDailyEpoch();

            msg += UX.formatNick(event) + " claimed their daily shiny card!";
            msg += user.updateEnergy(UX.randRange(240, 300), true);

            Card.addSingleCard(user, item, false);

            Update.updateBackpackDisplay(event, user);
            Update.updateCardDisplay(event, user);
            Update.updateViewDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "🎴", msg);
            Display.displayCard(event, user, ui, item, footer, false);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignGameColor(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int color = Integer.parseInt(args[1], 16);

            user.setGameColor(color);

            Update.updateBackpackDisplay(event, user);
            Update.updateCardDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), eevee_, "Set your game's theme color to **" + args[1].toUpperCase() + "**");
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException e) {
            JDA.sendMessage(event, red_, "❌", "That's not a valid hex code!");
        }
    }

    public static void pinCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Card card = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(card.getData(), false);
            String cardImage = card.getData().getCardImage();

            user.setPinCard(cardImage);
            
            Update.updateBackpackDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "🎒", "**" + cardTitle + "** has been pinned to your backpack!");
            try { User.saveUsers(); } catch(Exception e) {}
            
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void viewCooldowns(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += UX.formatCmd(server, "redeem") + " ┇ ";
        if(Check.isCooldownDone(user.getRedeemEpoch(), Check.findCooldown(user, 30), true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + Check.findTimeLeft(user.getRedeemEpoch(), 
            Check.findCooldown(user, 30), true) + "\n\n";
        }
        desc += UX.formatCmd(server, "minigame") + " ┇ ";
        if(Check.isCooldownDone(user.getMinigameEpoch(), Check.findCooldown(user, 60), true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + Check.findTimeLeft(user.getMinigameEpoch(), 
            Check.findCooldown(user, 60), true) + "\n\n";
        }
        desc += UX.formatCmd(server, "vote") + " ┇ ";
        if(Check.isCooldownDone(user.getVoteEpoch(), Check.findCooldown(user, 720), true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + Check.findTimeLeft(user.getVoteEpoch(), 
            Check.findCooldown(user, 720), true) + "\n\n";
        }
        desc += UX.formatCmd(server, "daily") + " ┇ ";
        if(Check.isCooldownDone(user.getDailyEpoch(), Check.findCooldown(user, 1440), true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + Check.findTimeLeft(user.getDailyEpoch(), 
            Check.findCooldown(user, 1440), true) + "\n\n";
        }
        desc += UX.formatCmd(server, "buy") + " ┇ ";
        if(Check.isCooldownDone(user.getMarketEpoch(), Check.findCooldown(user, 15), true)) {
            desc += "✅";
        } else {
            desc += "⏰ " + Check.findTimeLeft(user.getMarketEpoch(), 
            Check.findCooldown(user, 15), true);
        }
        embed.setTitle(ui.getUserName() + "'s Cooldowns");
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getGameColor());
        JDA.sendEmbed(event, embed);
        embed.clear();
    }
}
