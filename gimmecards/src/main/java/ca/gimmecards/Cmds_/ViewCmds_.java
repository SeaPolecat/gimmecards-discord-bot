package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ViewCmds_ extends Cmds {
    
    public static void viewCard_(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        ViewDisplay_ disp;

        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);
            int page = Integer.parseInt(args[1]);

            disp = new ViewDisplay_(user.getUserId()).findDisplay();
            disp.setMention(mention);
            disp.setMentionInfo(new UserInfo(mention, event));

            JDA.sendDynamicEmbed(event, user, null, disp, page);
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }
}
