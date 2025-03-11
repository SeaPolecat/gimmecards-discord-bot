package ca.gimmecards.cmds;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LeaderboardCmds {

    public static void viewLeaderboard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        LeaderboardDisplay disp = (LeaderboardDisplay) Display.addDisplay(user, new LeaderboardDisplay(event));

        JDAUtils.sendDynamicEmbed(event, disp, user, null, false);
    }
}
