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
        ShopDisplay disp = (ShopDisplay) user.addDisplay(new ShopDisplay(event));

        JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
    }
    
    public static void viewOldShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        OldShopDisplay disp = (OldShopDisplay) user.addDisplay(new OldShopDisplay(event));

        JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
    }

    public static void viewRareShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        desc += EmoteConsts.STAR + " " + EmoteConsts.MASCOT + " Gimme Cards 🚫\n";
        for(int i = 0; i < CardSet.rareSets.length; i++) {
            CardSet set = CardSet.rareSets[i];

            desc += EmoteConsts.STAR + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(EmoteConsts.CHARMANDER + " Exclusive Packs Shop " + EmoteConsts.CHARMANDER);
        embed.setDescription(desc);
        embed.setColor(ColorConsts.RARE_SHOP_COLOR);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void viewPromoShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        for(int i = 0; i < CardSet.promoSets.length; i++) {
            CardSet set = CardSet.promoSets[i];

            desc += EmoteConsts.STAR + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(EmoteConsts.BULBASAUR + " Promo Packs Shop " + EmoteConsts.BULBASAUR + " 🚫");
        embed.setDescription(desc);
        embed.setColor(ColorConsts.PROMO_SHOP_COLOR);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void unlockPack(SlashCommandInteractionEvent event) {
        OptionMapping packName = event.getOption("pack-name");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        try {
            CardSet set = CardSet.findCardSet(packName.getAsString());

            if(packName.getAsString().equalsIgnoreCase("gimme cards") || set.isRareSet()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You don't need to unlock exclusive packs!");

            } else if(set.isPromoSet()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You don't need to unlock promo packs!");

            } else if(set.isOldSet() && !user.ownsAnyShopPack()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You must unlock a pack from **Poké Packs Shop** first!");

            } else {
                if(user.isPackUnlocked(set.getSetName())) {
                    JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "This pack is already unlocked!");
    
                } else if(user.getKeys() < 1) {
                    JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you're out of " + EmoteConsts.KEY + " **Keys**");
    
                } else {
                    String msg = "";
    
                    user.getPacks().add(set.getSetName());
                    
                    msg += ui.getUserPing() + " unlocked " + set.getSetEmote() + " **" + set.getSetName() + "**";
                    msg += user.updateKeys(-1, true);

                    if(set.isOldSet()) {
                        JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.SQUIRTLE, msg);
                    } else {
                        JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.PIKACHU, msg);
                    }
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            }
        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that pack...");
        }
    }
}
