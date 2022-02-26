package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.Random;

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
            int energyReward = (new Random().nextInt(10) + 1);
            String msg = "";

            user.resetRedeemEpoch();

            msg += UX.formatNick(event) + " redeemed a token!";
            msg += UX.updateTokens(user, 1);
            msg += UX.updateEnergy(user, energyReward);

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
