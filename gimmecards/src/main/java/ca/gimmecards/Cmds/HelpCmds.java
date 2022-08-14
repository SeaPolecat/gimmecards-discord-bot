package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class HelpCmds extends Cmds {

    public static void changePrefix(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            JDA.sendMessage(event, red_, "❌", "Sorry, you need administrator powers to change my prefix!");

        } else {
            server.setPrefix(args[1]);
            
            JDA.sendMessage(event, user.getGameColor(), eevee_, "My prefix has been set to " + UX.formatCmd(server, ""));
            try { Server.saveServers(); } catch(Exception e) {}
        }
    }
    
    public static void viewHelp(MessageReceivedEvent event) {
        Server server = Server.findServer(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "*[Terms of Service & Privacy Policy](https://docs.google.com/document/d/1agd1vDsueTEL2khcavynOSptHKTLy0c2jymmX3xW5mc/edit?usp=sharing)*\n";
        desc += "┅┅\n";

        desc += "**Prefix**\n";
        desc += "My prefix for this server is " + UX.formatCmd(server, "") + "\n\n";

        desc += "**How to Play**\n";
        desc += "[Click here](https://www.gimmecards.ca/) to visit our website, "
        + "where you'll find a quick tutorial and list of commands.\n\n";

        desc += "**Official Server**\n";
        desc += "[Click here](https://discord.gg/urtHCGcE7y) to join our official server, "
        + "where you can ask questions, give ideas, or simply hang out with other collectors.\n\n";

        desc += "**Invite Me!**\n";
        desc += "[Click here](https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=354368&scope=bot) "
        + "to invite me to your own server!";

        embed.setTitle(eevee_ + " Getting Started " + eevee_);
        embed.setDescription(desc);
        embed.setColor(help_);
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewRarities(MessageReceivedEvent event) {
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
        desc += "✨ Custom\n";
        for(String rarity : shinies) {
            desc += "🌟 " + rarity + "\n";
        }
        embed.setTitle(eevee_ + " All Rarities " + eevee_);
        embed.setDescription(desc);
        embed.setColor(help_);
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewBadges(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += devBadge_ + " **Gimme Cards Developer**\n";
        desc += "Given to the developer of Gimme Cards, *SeaPolecat*\n\n";

        desc += staffBadge_ + " **Gimme Cards Staff**\n";
        desc += "Given to the staff members in our official [support server](https://discord.gg/urtHCGcE7y)\n\n";

        desc += communityBadge_ + " **Community Helper**\n";
        desc += "Given to collectors who often help newer players. Only staff members can award you this badge\n\n";

        desc += veteranBadge_ + " **Veteran Collector**\n";
        desc += "Given to collectors who are level 50+\n\n";

        desc += masterBadge_ + " **Master Collector**\n";
        desc += "Given to collectors who are level 100+\n\n";

        desc += bdayBadge_ + " **1 Year Anniversary**\n";
        desc += "Given to collectors who participated in the Gimme Cards 1 Year Anniversary event";

        embed.setTitle(eevee_ + " All Badges " + eevee_);
        embed.setDescription(desc);
        embed.setColor(help_);
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewChangelog(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        HelpDisplay disp = new HelpDisplay(user.getUserId()).findDisplay();

        JDA.sendDynamicEmbed(event, user, server, disp, Changelog.changelog.length);
    }
}
