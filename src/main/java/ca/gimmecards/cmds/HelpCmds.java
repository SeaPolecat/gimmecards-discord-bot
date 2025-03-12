package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpCmds {
    
    public static void viewHelp(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "*[Terms of Service & Privacy Policy](https://docs.google.com/document/d/1agd1vDsueTEL2khcavynOSptHKTLy0c2jymmX3xW5mc/edit?usp=sharing)*\n";
        desc += "┅┅\n";

        desc += "**How to Play**\n";
        desc += "[Click here](https://www.gimmecards.ca/) to visit our website, "
        + "where you'll find a quick tutorial and list of commands.\n\n";

        desc += "**Community Server**\n";
        desc += "[Click here](https://discord.gg/wmVvK2cyzM) to join our community server, "
        + "where you can ask questions, give ideas, or simply hang out with other collectors.\n\n";

        desc += "**Invite Me!**\n";
        desc += "[Click here](https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=0&scope=bot%20applications.commands) "
        //https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=354368&scope=bot
        + "to invite me to your own server!";

        embed.setTitle(EmoteConsts.EEVEE + " Getting Started " + EmoteConsts.EEVEE);
        embed.setDescription(desc);
        embed.setColor(ColorConsts.HELP_COLOR);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void viewRarities(SlashCommandInteractionEvent event) {
        String[] shinies = new String[]{"Amazing Rare","Classic Collection","LEGEND","Radiant Rare","Rare ACE","Rare BREAK","Rare Holo","Rare Holo EX","Rare Holo GX","Rare Holo LV.X","Rare Holo Star","Rare Holo V","Rare Holo VMAX","Rare Holo VSTAR","Rare Prime","Rare Prism Star","Rare Rainbow","Rare Secret","Rare Shining","Rare Shiny","Rare Shiny GX","Rare Ultra","V","VM"};
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Normal Cards**\n";
        desc += "┅┅\n";
        desc += "⚪ Common\n";
        desc += "🔷 Uncommon\n";
        desc += "⭐ Rare\n\n";

        desc += "**Shiny Cards**\n";
        desc += "┅┅\n";
        desc += "🎁 Promo\n";
        for(String rarity : shinies) {
            desc += "🌟 " + rarity + "\n";
        }
        desc += "\n**Special Cards**\n";
        desc += "┅┅\n";
        desc += "✨ Custom\n";
        desc += "🛍️ Merch\n";

        embed.setTitle(EmoteConsts.EEVEE + " All Rarities " + EmoteConsts.EEVEE);
        embed.setDescription(desc);
        embed.setColor(ColorConsts.HELP_COLOR);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void viewBadges(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += EmoteConsts.DEV_BADGE + " **Gimme Cards Developer**\n";
        desc += "Given to the developer of Gimme Cards, *SeaPolecat*\n\n";

        desc += EmoteConsts.STAFF_BADGE + " **Gimme Cards Staff**\n";
        desc += "Given to the staff members in our official [support server](https://discord.gg/urtHCGcE7y)\n\n";

        desc += EmoteConsts.VETERAN_BADGE + " **Veteran Collector**\n";
        desc += "Given to collectors who are level 50+\n\n";

        desc += EmoteConsts.MASTER_BADGE + " **Master Collector**\n";
        desc += "Given to collectors who are level 100+\n\n";

        desc += EmoteConsts.BDAY_BADGE + " **1 Year Anniversary**\n";
        desc += "Given to collectors who participated in the Gimme Cards 1 Year Anniversary event\n\n";

        desc += EmoteConsts.ORIGINAL_BADGE + " **Original Collector**\n";
        desc += "Given to collectors who created their account before Gimme Cards was verified (2023)";

        embed.setTitle(EmoteConsts.EEVEE + " All Badges " + EmoteConsts.EEVEE);
        embed.setDescription(desc);
        embed.setColor(ColorConsts.HELP_COLOR);
        JDAUtils.sendEmbed(event, embed);
    }

    public static void viewChangelog(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        HelpDisplay disp = (HelpDisplay) Display.addDisplay(user, new HelpDisplay(event));

        JDAUtils.sendDynamicEmbed(event, disp, user, server, true);
    }
}
