package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InspectionCmds_ extends Cmds {
    
    public static void viewCard_(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        InspectionDisplay_ disp;

        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);
            String mentionName = event.getJDA().getUserById(mentionId).getName();
            int page = Integer.parseInt(args[1]);

            disp = InspectionDisplay_.findInspectionDisplay_(user.getUserId());
            disp.setUser(user);
            disp.setMention(mention);
            disp.setMentionName(mentionName);

            Rest.sendDynamicEmbed(event, user, null, disp, page);
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }
}
