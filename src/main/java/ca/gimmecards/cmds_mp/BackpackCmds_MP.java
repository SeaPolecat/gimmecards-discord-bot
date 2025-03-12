package ca.gimmecards.cmds_mp;
import ca.gimmecards.display.*;
import ca.gimmecards.display_mp.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BackpackCmds_MP {
    
    public static void viewBackpack_(SlashCommandInteractionEvent event) {
        OptionMapping targetOption = event.getOption("user");
        //
        User user = User.findUser(event);
        User target = User.findTargetUser(event, targetOption.getAsUser().getId());
        BackpackDisplay_MP disp = (BackpackDisplay_MP) Display.addDisplay(user, new BackpackDisplay_MP(event, target));

        JDAUtils.sendDynamicEmbed(event, disp, user, target, new User[]{user}, null, false);
    }
}
