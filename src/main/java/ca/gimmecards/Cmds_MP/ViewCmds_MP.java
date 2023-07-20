package ca.gimmecards.Cmds_MP;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_MP.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds_MP extends Cmds {
    
    public static void viewCard_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ViewDisplay_MP disp = new ViewDisplay_MP(user.getUserId()).findDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");
        OptionMapping user_ = event.getOption("user");

        if(cardNum == null || user_ == null) { return; }

        try {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());
            int page = cardNum.getAsInt();

            if(mention.getCardContainers().size() < 1) {
                GameManager.sendMessage(event, IColors.red, "❌", "That user doesn't have any cards yet!");

            } else {
                disp.setUser(user);
                disp.setMention(mention);
                disp.setMentionInfo(new UserInfo(mention, event));
    
                GameManager.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
