package ca.gimmecards.cmds;
import java.util.Collections;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LeaderboardCmds {

    public static void viewLeaderboard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        LeaderboardDisplay disp = (LeaderboardDisplay) user.addDisplay(new LeaderboardDisplay(event));

        synchronized(User.usersRanked) {
            Collections.sort(User.usersRanked);

            disp.setSelfRank(User.searchForUserRanked(user));
        }
        JDAUtils.sendDynamicEmbed(event, disp, user, null, false);
    }
}
