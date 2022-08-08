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
        Server server = Server.findServer(event);

        if(!Check.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            JDA.sendMessage(event, jigglypuff_ + " Please wait another " + Check.findTimeLeft(user.getRedeemEpoch(), 30, true));

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            String msg = "";

            user.resetRedeemEpoch();

            msg += UX.formatNick(event) + " redeemed a token!";
            msg += UX.updateTokens(user, 1);
            msg += UX.updateEnergy(user, UX.randRange(24, 30));

            msg += "\n\n🟣 New update on 8/4/2022 ┇ " + UX.formatCmd(server, "changelog") + "\n";

            Update.updateBackpackDisplay(event, user);

            embed.setDescription(msg);
            embed.setColor(0x408CFF);
            JDA.sendEmbed(event, embed);
            embed.clear();
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(MessageReceivedEvent event) {
        User user = User.findUser(event);

        if(!Check.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            JDA.sendMessage(event, jigglypuff_ + " Please wait another " + Check.findTimeLeft(user.getDailyEpoch(), 1440, true));

        } else {
            Card c;
            Data item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = event.getAuthor().getName() + "'s shiny card";

            user.resetDailyEpoch();

            msg += UX.formatNick(event) + " claimed their daily shiny card!";
            msg += UX.updateEnergy(user, UX.randRange(240, 300));

            if(Check.isOldSet(item)) {
                c = Card.addSingleCard(user, item, false);
            } else {
                c = Card.addSingleCard(user, item, true);
            }

            Update.updateBackpackDisplay(event, user);
            Update.updateCardDisplay(event, user);

            JDA.sendMessage(event, msg);
            Display.displayCard(event, user, item, c, footer);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignBackpackColor(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int color = Integer.parseInt(args[1], 16);

            user.setBackpackColor(color);
            Update.updateBackpackDisplay(event, user);

            JDA.sendMessage(event, eevee_ + " Set your backpack color to **" + args[1].toUpperCase() + "**");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException e) {
            JDA.sendMessage(event, jigglypuff_ + " That's not a valid hex code!");
        }
    }

    public static void assignBackpackCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Card c = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(c.getData(), false);
            String cardImage = c.getData().getCardImage();

            user.setBackpackCard(cardImage);
            Update.updateBackpackDisplay(event, user);

            JDA.sendMessage(event, "**" + cardTitle + "** has been pinned to your backpack!");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }
}
