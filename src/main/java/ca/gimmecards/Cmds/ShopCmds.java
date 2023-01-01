package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopCmds extends Cmds {

    public static void viewShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ShopDisplay disp = new ShopDisplay(user.getUserId()).findDisplay();

        JDA.sendDynamicEmbed(event, user, null, disp, 1);
    }
    
    public static void viewOldShop(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        OldShopDisplay disp = new OldShopDisplay(user.getUserId()).findDisplay();

        JDA.sendDynamicEmbed(event, user, null, disp, 1);
    }

    public static void viewRareShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        desc += star_ + " " + logo_ + " Gimme Cards 🚫\n";
        for(int i = 0; i < Data.rareSets.length; i++) {
            Data set = Data.rareSets[i];

            desc += star_ + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(charmander_ + " Exclusive Packs Shop " + charmander_);
        embed.setDescription(desc);
        embed.setColor(rareshop_);
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewPromoShop(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        for(int i = 0; i < Data.promoSets.length; i++) {
            Data set = Data.promoSets[i];

            desc += star_ + " " + set.getSetEmote() + " " + set.getSetName() + "\n";
        }
        desc += "┅┅\n";
        embed.setTitle(bulbasaur_ + " Promo Packs Shop " + bulbasaur_ + " 🚫");
        embed.setDescription(desc);
        embed.setColor(promoshop_);
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    public static void unlockPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        try {
            Data set = Data.findSet(packName.getAsString());

            if(packName.getAsString().equalsIgnoreCase("gimme cards") || Check.isRareSet(set)) {
                JDA.sendMessage(event, red_, "❌", "You don't need to unlock exclusive packs!");

            } else if(Check.isPromoSet(set)) {
                JDA.sendMessage(event, red_, "❌", "You don't need to unlock promo packs!");

            } else if(Check.isOldSet(set) && !Check.ownsShopPack(user)) {
                JDA.sendMessage(event, red_, "❌", "You must unlock a pack from **Poké Packs Shop** first!");

            } else {
                if(Check.isPackUnlocked(user, set.getSetName())) {
                    JDA.sendMessage(event, red_, "❌", "This pack is already unlocked!");
    
                } else if(user.getKeys() < 1) {
                    JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + key_ + " **Keys**");
    
                } else {
                    String msg = "";
    
                    user.getPacks().add(set.getSetName());
                    
                    msg += UX.formatNick(event) + " unlocked " + set.getSetEmote() + " **" + set.getSetName() + "**";
                    msg += user.updateKeys(-1, true);

                    if(Check.isOldSet(set)) {
                        JDA.sendMessage(event, user.getGameColor(), squirtle_, msg);
                    } else {
                        JDA.sendMessage(event, user.getGameColor(), pikachu_, msg);
                    }
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            }
        } catch(NullPointerException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that pack...");
        }
    }
}
