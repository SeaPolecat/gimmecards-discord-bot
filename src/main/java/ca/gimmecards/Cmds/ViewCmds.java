package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds {

    public static void viewCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ViewDisplay disp = new ViewDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");

        user.addDisplay(disp);

        if(cardNum == null) { return; }

        try {
            if(user.getCardContainers().size() < 1) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't have any cards yet!");

            } else {
                int page = cardNum.getAsInt();

                JDAUtils.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
