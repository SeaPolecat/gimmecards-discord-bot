package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopCmds extends Cmds {

    public static void viewShop(MessageReceivedEvent event) {
        User user = User.findUser(event);
        ShopDisplay disp = ShopDisplay.findShopDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, 1);
    }
    
    public static void viewOldShop(MessageReceivedEvent event) {
        User user = User.findUser(event);
        OldShopDisplay disp = OldShopDisplay.findOldShopDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, 1);
    }

    public static void viewRareShop(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        desc += logo_ + " Gimme Cards ┇ " + star_ + "\n";
        for(int i = 0; i < Data.rareSets.length; i++) {
            Data set = Data.rareSets[i];

            desc += set.getSetEmote() + " " + set.getSetName() + " ┇ " + star_ + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(charmander_ + " Exclusive Packs Shop " + charmander_);
        embed.setDescription(desc);
        embed.setColor(0xFC8035);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewPromoShop(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        for(int i = 0; i < Data.promoSets.length; i++) {
            Data set = Data.promoSets[i];

            desc += set.getSetEmote() + " " + set.getSetName() + " ┇ " + star_ + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(bulbasaur_ + " Promo Packs Shop " + bulbasaur_);
        embed.setDescription(desc);
        embed.setColor(0x63A127);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void unlockPack(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            String setName = "";
            for(int i = 1; i < args.length; i++) {
                setName += args[i] + " ";
            }
            setName = setName.trim();
            Data set = Data.findSet(setName);

            if(setName.equalsIgnoreCase("gimme cards") || State.isRareSet(set)) {
                Rest.sendMessage(event, jigglypuff_ + " You don't need to unlock exclusive packs!");

            } else if(State.isPromoSet(set)) {
                Rest.sendMessage(event, jigglypuff_ + " You don't need to unlock promo packs!");

            } else {
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
            }
        } catch(NullPointerException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that pack...");
        }
    }
}
