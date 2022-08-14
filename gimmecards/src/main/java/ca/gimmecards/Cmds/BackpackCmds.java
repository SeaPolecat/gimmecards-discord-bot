package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BackpackCmds extends Cmds {

    public static void viewBackpack(MessageReceivedEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = new BackpackDisplay(user.getUserId()).findDisplay();

        JDA.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(MessageReceivedEvent event) {
        User user = User.findUser(event);

        if(!Check.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " + Check.findTimeLeft(user.getRedeemEpoch(), 30, true));

        } else {
            String msg = "";

            user.resetRedeemEpoch();

            msg += UX.formatNick(event) + " redeemed a token!";
            msg += user.updateTokens(1, true);
            msg += user.updateEnergy(UX.randRange(24, 30), false);
            msg += "\n\n" + Main.updateMsg + "\n";

            Update.updateBackpackDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "🎒", msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(MessageReceivedEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!Check.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " + Check.findTimeLeft(user.getDailyEpoch(), 1440, true));

        } else {
            Data item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = ui.getUserName() + "'s shiny card";

            user.resetDailyEpoch();

            msg += UX.formatNick(event) + " claimed their daily shiny card!";
            msg += user.updateEnergy(UX.randRange(240, 300), true);

            Card.addSingleCard(user, item);

            Update.updateBackpackDisplay(event, user);
            Update.updateCardDisplay(event, user);
            Update.updateViewDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "🎴", msg);
            Display.displayCard(event, user, item, footer);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignBackpackColor(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int color = Integer.parseInt(args[1], 16);

            user.setGameColor(color);

            Update.updateBackpackDisplay(event, user);
            Update.updateCardDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), eevee_, "Set your backpack color to **" + args[1].toUpperCase() + "**");
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException e) {
            JDA.sendMessage(event, red_, "❌", "That's not a valid hex code!");
        }
    }

    public static void pinBackpackCard(MessageReceivedEvent event, String[] args) {
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
}
