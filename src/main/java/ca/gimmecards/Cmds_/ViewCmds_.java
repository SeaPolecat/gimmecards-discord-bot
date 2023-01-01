package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds_ extends Cmds {
    
    public static void viewCard_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ViewDisplay_ disp = new ViewDisplay_(user.getUserId()).findDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");
        OptionMapping user_ = event.getOption("user");

        if(cardNum == null || user_ == null) { return; }

        try {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());
            int page = cardNum.getAsInt();

            if(mention.getCards().size() < 1) {
                JDA.sendMessage(event, red_, "❌", "That user doesn't have any cards yet!");

            } else {
                disp.setUser(user);
                disp.setMention(mention);
                disp.setMentionInfo(new UserInfo(mention, event));
    
                JDA.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
