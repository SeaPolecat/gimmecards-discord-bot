package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopCmds {

    public static void viewShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ShopDisplay disp = new ShopDisplay();

        user.addDisplay(disp);

        JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
    }
    
    public static void viewOldShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        OldShopDisplay disp = new OldShopDisplay();

        user.addDisplay(disp);

        JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
    }

    public static void viewRareShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        desc += EmoteConsts.star + " " + EmoteConsts.mascot + " Gimme Cards 🚫\n";
        for(int i = 0; i < CardSet.rareSets.length; i++) {
            CardSet set = CardSet.rareSets[i];

            desc += EmoteConsts.star + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(EmoteConsts.charmander + " Exclusive Packs Shop " + EmoteConsts.charmander);
        embed.setDescription(desc);
        embed.setColor(ColorConsts.rareshopColor);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void viewPromoShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        for(int i = 0; i < CardSet.promoSets.length; i++) {
            CardSet set = CardSet.promoSets[i];

            desc += EmoteConsts.star + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(EmoteConsts.bulbasaur + " Promo Packs Shop " + EmoteConsts.bulbasaur + " 🚫");
        embed.setDescription(desc);
        embed.setColor(ColorConsts.promoshopColor);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void unlockPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        try {
            CardSet set = CardSet.findCardSet(packName.getAsString());

            if(packName.getAsString().equalsIgnoreCase("gimme cards") || set.isRareSet()) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't need to unlock exclusive packs!");

            } else if(set.isPromoSet()) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't need to unlock promo packs!");

            } else if(set.isOldSet() && !user.ownsAnyShopPack()) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You must unlock a pack from **Poké Packs Shop** first!");

            } else {
                if(user.isPackUnlocked(set.getSetName())) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "This pack is already unlocked!");
    
                } else if(user.getKeys() < 1) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you're out of " + EmoteConsts.key + " **Keys**");
    
                } else {
                    String msg = "";
    
                    user.getPacks().add(set.getSetName());
                    
                    msg += FormatUtils.formatName(event) + " unlocked " + set.getSetEmote() + " **" + set.getSetName() + "**";
                    msg += user.updateKeys(-1, true);

                    if(set.isOldSet()) {
                        JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.squirtle, msg);
                    } else {
                        JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.pikachu, msg);
                    }
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            }
        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that pack...");
        }
    }
}
