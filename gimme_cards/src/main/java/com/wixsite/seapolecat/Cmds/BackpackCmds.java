package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BackpackCmds extends Cmds {

    public static void viewBackpack(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = BackpackDisplay.findBackpackDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);

        if(!State.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getRedeemEpoch(), 30, true));

        } else {
            String msg = "";

            user.resetRedeemEpoch();

            msg += UX.formatNick(event) + " redeemed a token!";
            msg += UX.updateTokens(user, 1);
            msg += UX.updateEnergy(user, UX.randRange(40, 50));

            State.updateBackpackDisplay(event, user);

            Rest.sendMessage(event, msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);

        if(!State.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getDailyEpoch(), 1440, true));

        } else {
            String msg = "";

            user.resetDailyEpoch();

            msg += UX.formatNick(event) + " claimed their daily reward!";
            msg += UX.updateTokens(user, 10);
            msg += UX.updateEnergy(user, UX.randRange(400, 500));
            msg += UX.updateStars(user, 2);

            State.updateBackpackDisplay(event, user);

            Rest.sendMessage(event, msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignBackpackColor(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int color = Integer.parseInt(args[1], 16);

            user.setBackpackColor(color);
            State.updateBackpackDisplay(event, user);

            Rest.sendMessage(event, eevee_ + " Set your backpack color to **" + color + "**");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException e) {
            Rest.sendMessage(event, jigglypuff_ + " That's not a valid hex code!");
        }
    }

    public static void assignBackpackCard(GuildMessageReceivedEvent event, String[] args) {
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
