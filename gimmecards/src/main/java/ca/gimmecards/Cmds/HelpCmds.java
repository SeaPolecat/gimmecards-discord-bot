package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class HelpCmds extends Cmds implements Comparator<User> {

    public int compare(User u1, User u2) {
        if(u1.getLevel() < u2.getLevel()) {
            return 1;
        } else if(u2.getLevel() < u1.getLevel()) {
            return -1;

        } else {
            if(u1.getXP() < u2.getXP()) {
                return 1;
            } else if(u2.getXP() < u1.getXP()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static void changePrefix(MessageReceivedEvent event, String[] args) {
        Server server = Server.findServer(event);

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            Rest.sendMessage(event, jigglypuff_+ " Sorry, you need administrator powers to change my prefix!");

        } else {
            server.setPrefix(args[1]);
            Rest.sendMessage(event, eevee_ + " My prefix has been set to " + UX.formatCmd(server, ""));
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
        embed.setColor(0xE9BB7A);
        Rest.sendEmbed(event, embed);
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
        embed.setColor(0xE9BB7A);
        Rest.sendEmbed(event, embed);
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
        embed.setColor(0xE9BB7A);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewChangelog(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        HelpDisplay disp = HelpDisplay.findHelpDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, server, disp, Changelog.changelog.length);
    }

    public static void viewRanks(MessageReceivedEvent event) {
        User user = User.findUser(event);
        List<Member> members = event.getGuild().getMembers();
        ArrayList<User> localUsers = new ArrayList<User>();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        int place = 0;

        for(Member m : members) {
            for(User u : User.users) {
                if(m.getUser().getId().equals(u.getUserId())) {
                    localUsers.add(u);
                    break;
                }
            }
        }
        Collections.sort(localUsers, new HelpCmds());

        for(int i = 0; i < localUsers.size(); i++) {
            if(localUsers.get(i).getUserId().equals(user.getUserId())) {
                place = i+1;
            }
        }
        desc += "┅┅\n";
        for(int i = 0; i < localUsers.size(); i++) {
            User u = localUsers.get(i);
            String userName = event.getGuild().getMemberById(u.getUserId()).getUser().getName();

            if(i == 0) {
                desc += "🥇";
            } else if(i == 1) {
                desc += "🥈";
            } else if(i == 2) {
                desc += "🥉";
            } else {
                desc += "`#" + (i+1) + "`";
            }
            desc += " ┇ **" + userName + "**"
            + " ┇ *" + "Lvl. " + u.getLevel() + "*"
            + " ┇ " + XP_ + " `" + UX.formatNumber(u.getXP()) + " / " + UX.formatNumber(u.getMaxXP()) + "`\n";

            if(i >= 9) {
                break;
            }
        }
        desc += "┅┅\n";
        desc += UX.formatNick(event) + " is `#" + place + "` in this server 🏝️";

        embed.setTitle(trainer_ + " Top Collectors Here " + trainer_);
        embed.setDescription(desc);
        embed.setColor(0xB0252B);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewLeaderboard(MessageReceivedEvent event) {
        User user = User.findUser(event);
        ArrayList<User> globalUsers = new ArrayList<User>();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        int count = 0, place = 0;

        for(User u : User.users) {
            globalUsers.add(u);
        }
        Collections.sort(globalUsers, new HelpCmds());

        for(int i = 0; i < globalUsers.size(); i++) {
            if(globalUsers.get(i).getUserId().equals(user.getUserId())) {
                place = i+1;
            }
        }
        desc += "┅┅\n";
        for (int i = 0; i < globalUsers.size(); i++) {
            try {
                User u = globalUsers.get(i);
                String userName = event.getJDA().getUserById(u.getUserId()).getName();
    
                if(count == 0) {
                    desc += "🥇";
                } else if(count == 1) {
                    desc += "🥈";
                } else if(count == 2) {
                    desc += "🥉";
                } else {
                    desc += "`#" + (count+1) + "`";
                }
                desc += " ┇ **" + userName + "**"
                + " ┇ *" + "Lvl. " + u.getLevel() + "*"
                + " ┇ " + XP_ + " `" + UX.formatNumber(u.getXP()) + " / " + UX.formatNumber(u.getMaxXP()) + "`\n";
                
                if(u.getUserId().equals(user.getUserId())) {
                    place = count+1;
                }
                if(count >= 9) {
                    break;
                }
                count++;
            } catch(NullPointerException e) {}
        }
        desc += "┅┅\n";
        desc += UX.formatNick(event) + " is `#" + place + "` in the world 🌎";

        embed.setTitle(logo_ + " World's Top Collectors " + logo_);
        embed.setDescription(desc);
        embed.setColor(0x408CFF);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }
}
