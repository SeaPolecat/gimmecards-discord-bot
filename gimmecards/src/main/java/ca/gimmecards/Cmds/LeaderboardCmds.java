package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardCmds extends Cmds implements Comparator<User> {

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
        Collections.sort(localUsers, new LeaderboardCmds());

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
        JDA.sendEmbed(event, embed);
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
        Collections.sort(globalUsers, new LeaderboardCmds());

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
        JDA.sendEmbed(event, embed);
        embed.clear();
    }
}
