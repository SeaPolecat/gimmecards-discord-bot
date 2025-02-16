package ca.gimmecards.cmds_mp;
import ca.gimmecards.consts.*;
import ca.gimmecards.display_mp.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CollectionCmds_MP {
    
    public static void viewCards_(SlashCommandInteractionEvent event) {
        OptionMapping page = event.getOption("page");
        OptionMapping targetOption = event.getOption("user");
        //
        User user = User.findUser(event);
        User target = User.findTargetUser(event, targetOption.getAsUser().getId());
        CollectionDisplay_MP disp = null;

        if(page == null)
            disp = new CollectionDisplay_MP(event, target, 1);
        else
            disp = new CollectionDisplay_MP(event, target, page.getAsInt());

        user.addDisplay(disp);

        if(target.getCardContainers().size() < 1) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "That user doesn't have any cards yet!");

        } else {
            try {
                JDAUtils.sendDynamicEmbed(event, disp, user, target, new User[]{user}, null, true);

            } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that page...");
            }
        }
    }
}
