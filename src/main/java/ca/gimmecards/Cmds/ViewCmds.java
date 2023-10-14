package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds {

    public static void viewCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            if(user.getCardContainers().size() < 1) {
                GameManager.sendMessage(event, IColors.red, "❌", "You don't have any cards yet!");

            } else {
                int page = cardNum.getAsInt();

                GameManager.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
