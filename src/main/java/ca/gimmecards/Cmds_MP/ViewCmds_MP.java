package ca.gimmecards.cmds_mp;
import ca.gimmecards.consts.*;
import ca.gimmecards.display_mp.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds_MP {
    
    public static void viewCard_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ViewDisplay_MP disp = new ViewDisplay_MP();
        //
        OptionMapping cardNum = event.getOption("card-number");
        OptionMapping user_ = event.getOption("user");

        user.addDisplay(disp);

        if(cardNum == null || user_ == null) { return; }

        try {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());
            int page = cardNum.getAsInt();

            if(mention.getCardContainers().size() < 1) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "That user doesn't have any cards yet!");

            } else {
                disp.setUser(user);
                disp.setMention(mention);
                disp.setMentionInfo(new UserInfo(mention, event));
    
                JDAUtils.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
