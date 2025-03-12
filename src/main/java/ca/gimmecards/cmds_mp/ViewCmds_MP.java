package ca.gimmecards.cmds_mp;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.display_mp.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds_MP {
    
    public static void viewCard_(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        OptionMapping targetOption = event.getOption("user");
        //
        User user = User.findUser(event);
        User target = User.findTargetUser(event, targetOption.getAsUser().getId());
        ViewDisplay_MP disp = (ViewDisplay_MP) Display.addDisplay(user, new ViewDisplay_MP(event, target, cardNum.getAsInt())); 

        try {
            if(target.getCardContainers().size() < 1)
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "That user doesn't have any cards yet!");
            else
                JDAUtils.sendDynamicEmbed(event, disp, user, target, new User[]{user}, null, true);

        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
