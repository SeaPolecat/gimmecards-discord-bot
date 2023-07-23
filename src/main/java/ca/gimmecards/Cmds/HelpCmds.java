package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import ca.gimmecards.Display.*;
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

        desc += "**Official Server**\n";
        desc += "[Click here](https://discord.gg/urtHCGcE7y) to join our official server, "
        + "where you can ask questions, give ideas, or simply hang out with other collectors.\n\n";

        desc += "**Invite Me!**\n";
        desc += "[Click here](https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=0&scope=bot%20applications.commands) "
        //https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=354368&scope=bot
        + "to invite me to your own server!";

        embed.setTitle(IEmotes.eevee + " Getting Started " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
        embed.clear();
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

        embed.setTitle(IEmotes.eevee + " All Rarities " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewBadges(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += IEmotes.devBadge + " **Gimme Cards Developer**\n";
        desc += "Given to the developer of Gimme Cards, *SeaPolecat*\n\n";

        desc += IEmotes.staffBadge + " **Gimme Cards Staff**\n";
        desc += "Given to the staff members in our official [support server](https://discord.gg/urtHCGcE7y)\n\n";

        desc += IEmotes.communityBadge + " **Community Helper**\n";
        desc += "Given to collectors who often help newer players or contribute to new ideas. "
        + "Only staff members can award you this badge\n\n";

        desc += IEmotes.patreonBadge + " **Patreon**\n";
        desc += "Given to our Patreon supporters, AKA *Rare Collectors* or *Radiant Rare Collectors*\n\n";

        desc += IEmotes.veteranBadge + " **Veteran Collector**\n";
        desc += "Given to collectors who are level 50+\n\n";

        desc += IEmotes.masterBadge + " **Master Collector**\n";
        desc += "Given to collectors who are level 100+\n\n";

        desc += IEmotes.bdayBadge + " **1 Year Anniversary**\n";
        desc += "Given to collectors who participated in the Gimme Cards 1 Year Anniversary event\n\n";

        desc += IEmotes.originalBagde + " **Original Collector**\n";
        desc += "Given to collectors who created their account before Gimme Cards was verified (e.g. before 2023)";

        embed.setTitle(IEmotes.eevee + " All Badges " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewPatreon(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "[Click here](https://www.patreon.com/gimmecards) to join us on " + IEmotes.patreon + " **Patreon**\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(IEmotes.eevee + " Patreon Rewards " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setImage("https://media.discordapp.net/attachments/1012866222657896558/1012866246393471027/Screen_Shot_2022-08-26_at_4.28.35_PM.png?width=694&height=598");
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewChangelog(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        HelpDisplay disp = new HelpDisplay(user.getUserId()).findDisplay();

        GameManager.sendDynamicEmbed(event, user, server, disp, IChangelog.changelog.length);
    }
}
