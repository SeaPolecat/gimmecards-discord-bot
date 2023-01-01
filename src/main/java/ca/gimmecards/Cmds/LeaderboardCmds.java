package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.LeaderboardDisplay;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
    
    public static void viewRanks(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        LeaderboardDisplay disp = new LeaderboardDisplay(user.getUserId()).findDisplay();
        Guild guild = event.getGuild();

        if(guild != null) {
            List<Member> members = guild.getMembers();
            ArrayList<User> players = new ArrayList<User>();
            ArrayList<UserInfo> playerInfos = new ArrayList<UserInfo>();
    
            for(User u : User.users) {
                for(Member member : members) {
                    if(u.getUserId().equals(member.getUser().getId())) {
                        players.add(u);
                        break;
                    }
                }
            }
            Collections.sort(players, new LeaderboardCmds());
    
            for(User player : players) {
                playerInfos.add(new UserInfo(player, event));
            }
            disp.setDispType("ranks");
            disp.setPlayers(players);
            disp.setPlayerInfos(playerInfos);
    
            JDA.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }

    public static void viewLeaderboard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        LeaderboardDisplay disp = new LeaderboardDisplay(user.getUserId()).findDisplay();
        ArrayList<User> players = new ArrayList<User>();
        ArrayList<UserInfo> playerInfos = new ArrayList<UserInfo>();

        for(User u : User.users) {
            try {
                new UserInfo(u, event);
                players.add(u);
            } catch(NullPointerException e) {}
        }
        Collections.sort(players, new LeaderboardCmds());

        for(User player : players) {
            playerInfos.add(new UserInfo(player, event));
        }

        disp.setDispType("leaderboard");
        disp.setPlayers(players);
        disp.setPlayerInfos(playerInfos);

        JDA.sendDynamicEmbed(event, user, null, disp, 1);
    }
}
