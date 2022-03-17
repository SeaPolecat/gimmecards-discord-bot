package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Interfaces.*;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class HelpCmds extends Cmds {

    public static void changePrefix(GuildMessageReceivedEvent event, String[] args) {
        Server server = Server.findServer(event);

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            Rest.sendMessage(event, jigglypuff_+ " Sorry, you need administrator powers to change my prefix!");

        } else {
            server.setPrefix(args[1]);
            Rest.sendMessage(event, eevee_ + " My prefix has been set to " + UX.formatCmd(server, ""));
            try { Server.saveServers(); } catch(Exception e) {}
        }
    }
    
    public static void viewHelp(GuildMessageReceivedEvent event) {
        Server server = Server.findServer(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Prefix**\n";
        desc += "My prefix for this server is " + UX.formatCmd(server, "") + "\n\n";

        desc += "**Tutorial**\n";
        desc += "For a quick tutorial and list of commands, please visit the [help website](https://seapolecat.wixsite.com/gimmecards)\n\n";

        desc += "**Support Server**\n";
        desc += "Have questions or ideas for new updates? Or just wanna hang out with other collectors? "
        + "Come join our [support server](https://discord.gg/urtHCGcE7y)\n\n";

        desc += "**Invite Me!**\n";
        desc += "Click [here](https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=354368&scope=bot) "
        + "to invite me to your own server!";

        embed.setTitle(eevee_ + " Getting Started " + eevee_);
        embed.setDescription(desc);
        embed.setColor(0xE9BB7A);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewRarities(GuildMessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Normal Cards**\n";
        desc += "┅┅\n";
        desc += "⚪ Common\n";
        desc += "🔷 Uncommon\n";
        desc += "⭐ Rare\n\n";

        desc += "**🌟 Shiny Cards**\n";
        desc += "┅┅\n";
        desc += "Amazing Rare\n";
        desc += "LEGEND\n";
        desc += "Rare ACE\n";
        desc += "Rare BREAK\n";
        desc += "Rare Holo\n";
        desc += "Rare Holo EX\n";
        desc += "Rare Holo GX\n";
        desc += "Rare Holo LV.X\n";
        desc += "Rare Holo Star\n";
        desc += "Rare Holo V\n";
        desc += "Rare Holo VMAX\n";
        desc += "Rare Prime\n";
        desc += "Rare Prism Star\n";
        desc += "Rare Rainbow\n";
        desc += "Rare Secret\n";
        desc += "Rare Shining\n";
        desc += "Rare Shiny\n";
        desc += "Rare Shiny GX\n";
        desc += "Rare Ultra";

        embed.setTitle(eevee_ + " All Rarities " + eevee_);
        embed.setDescription(desc);
        embed.setColor(0xE9BB7A);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewChangelog(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        HelpDisplay disp = HelpDisplay.findHelpDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, server, disp, Changelog.changelog.length);
    }
}
