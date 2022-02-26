package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ShopCmds extends Cmds {

    public static void viewShop(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);
        ShopDisplay disp = ShopDisplay.findShopDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, 1);
    }
    
    public static void viewOldShop(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);
        OldShopDisplay disp = OldShopDisplay.findOldShopDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, 1);
    }

    public static void unlockPack(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            String setName = "";
            for(int i = 1; i < args.length; i++) {
                setName += args[i] + " ";
            }
            setName = setName.trim();
            Data set = Data.findSet(setName);

            if(State.isPackUnlocked(user, set.getSetName())) {
                Rest.sendMessage(event, jigglypuff_ + " This pack is already unlocked!");

            } else if(user.getKeys() < 1) {
                Rest.sendMessage(event, jigglypuff_ + " Sorry, you're out of " + key_ + " **Keys**");

            } else {
                String msg = "";

                user.getPacks().add(set.getSetName());
                
                msg += UX.formatNick(event) + " unlocked " + set.getSetEmote() + " **" + set.getSetName() + "**";
                msg += UX.updateKeys(user, -1);

                State.updateBackpackDisplay(event, user);
                State.updateShopDisplay(event, user);
                State.updateOldShopDisplay(event, user);

                Rest.sendMessage(event, msg);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NullPointerException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that pack...");
        }
    }
}
