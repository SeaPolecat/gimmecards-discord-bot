package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackCmds extends Cmds {

    public static void viewBackpack(MessageReceivedEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = BackpackDisplay.findBackpackDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!State.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getRedeemEpoch(), 30, true));

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            String msg = "";

            user.resetRedeemEpoch();

            msg += UX.formatNick(event) + " redeemed a token!";
            msg += UX.updateTokens(user, 1);
            msg += UX.updateEnergy(user, UX.randRange(24, 30));

            msg += "\n\n:orange_circle: New update on 6/4/2022 ┇ " + UX.formatCmd(server, "changelog");

            State.updateBackpackDisplay(event, user);

            embed.setDescription(msg);
            embed.setColor(0xFEBE54);
            Rest.sendEmbed(event, embed);
            embed.clear();
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(MessageReceivedEvent event) {
        User user = User.findUser(event);

        if(!State.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getDailyEpoch(), 1440, true));

        } else {
            Card c;
            Data item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = event.getAuthor().getName() + "'s shiny card";

            user.resetDailyEpoch();

            msg += UX.formatNick(event) + " claimed their daily shiny card!";
            msg += UX.updateEnergy(user, UX.randRange(240, 300));

            if(State.isOldSet(item)) {
                c = Card.addSingleCard(user, item, false);
            } else {
                c = Card.addSingleCard(user, item, true);
            }

            State.updateBackpackDisplay(event, user);
            State.updateCardDisplay(event, user);

            Rest.sendMessage(event, msg);
            Display.displayCard(event, user, item, c, footer);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignBackpackColor(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int color = Integer.parseInt(args[1], 16);

            user.setBackpackColor(color);
            State.updateBackpackDisplay(event, user);

            Rest.sendMessage(event, eevee_ + " Set your backpack color to **" + args[1].toUpperCase() + "**");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException e) {
            Rest.sendMessage(event, jigglypuff_ + " That's not a valid hex code!");
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
            State.updateBackpackDisplay(event, user);

            Rest.sendMessage(event, "**" + cardTitle + "** will now be displayed on your backpack!");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }
}
