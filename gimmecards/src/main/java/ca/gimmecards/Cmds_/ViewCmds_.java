package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ViewCmds_ extends Cmds {
    
    public static void viewCard_(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        ViewDisplay_ disp = new ViewDisplay_(user.getUserId()).findDisplay();
        String mentionId = JDA.findMentionId(event, args[2]);

        if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            try {
                User mention = User.findOtherUser(event, mentionId);
                int page = Integer.parseInt(args[1]);
    
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
}
