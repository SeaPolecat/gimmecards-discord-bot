package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds {

    public static void viewCard(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);
        ViewDisplay disp = (ViewDisplay) Display.addDisplay(user, new ViewDisplay(event, cardNum.getAsInt()));

        try {
            if(user.getCardContainers().size() < 1) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You don't have any cards yet!");

            } else {
                JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
            }
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
