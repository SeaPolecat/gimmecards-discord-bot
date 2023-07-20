package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopCmds extends Cmds {

    public static void viewShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ShopDisplay disp = new ShopDisplay(user.getUserId()).findDisplay();

        GameManager.sendDynamicEmbed(event, user, null, disp, 1);
    }
    
    public static void viewOldShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        OldShopDisplay disp = new OldShopDisplay(user.getUserId()).findDisplay();

        GameManager.sendDynamicEmbed(event, user, null, disp, 1);
    }

    public static void viewRareShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        desc += IEmotes.star + " " + IEmotes.logo + " Gimme Cards 🚫\n";
        for(int i = 0; i < CardSet.rareSets.length; i++) {
            CardSet set = CardSet.rareSets[i];

            desc += IEmotes.star + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(IEmotes.charmander + " Exclusive Packs Shop " + IEmotes.charmander);
        embed.setDescription(desc);
        embed.setColor(IColors.rareshopColor);
        GameManager.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewPromoShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        for(int i = 0; i < CardSet.promoSets.length; i++) {
            CardSet set = CardSet.promoSets[i];

            desc += IEmotes.star + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(IEmotes.bulbasaur + " Promo Packs Shop " + IEmotes.bulbasaur + " 🚫");
        embed.setDescription(desc);
        embed.setColor(IColors.promoshopColor);
        GameManager.sendEmbed(event, embed);
        embed.clear();
    }

    public static void unlockPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        try {
            CardSet set = CardSet.findCardSet(packName.getAsString());

            if(packName.getAsString().equalsIgnoreCase("gimme cards") || set.isRareSet()) {
                GameManager.sendMessage(event, IColors.red, "❌", "You don't need to unlock exclusive packs!");

            } else if(set.isPromoSet()) {
                GameManager.sendMessage(event, IColors.red, "❌", "You don't need to unlock promo packs!");

            } else if(set.isOldSet() && !user.ownsShopPack()) {
                GameManager.sendMessage(event, IColors.red, "❌", "You must unlock a pack from **Poké Packs Shop** first!");

            } else {
                if(user.isPackUnlocked(set.getSetName())) {
                    GameManager.sendMessage(event, IColors.red, "❌", "This pack is already unlocked!");
    
                } else if(user.getKeys() < 1) {
                    GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.key + " **Keys**");
    
                } else {
                    String msg = "";
    
                    user.getPacks().add(set.getSetName());
                    
                    msg += GameManager.formatName(event) + " unlocked " + set.getSetEmote() + " **" + set.getSetName() + "**";
                    msg += user.updateKeys(-1, true);

                    if(set.isOldSet()) {
                        GameManager.sendMessage(event, user.getGameColor(), IEmotes.squirtle, msg);
                    } else {
                        GameManager.sendMessage(event, user.getGameColor(), IEmotes.pikachu, msg);
                    }
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            }
        } catch(NullPointerException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that pack...");
        }
    }
}
