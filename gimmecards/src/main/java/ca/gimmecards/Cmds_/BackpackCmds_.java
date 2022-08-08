package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BackpackCmds_ extends Cmds {
    
    public static void viewBackpack_(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        BackpackDisplay_ disp;

        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);
            String mentionIcon = event.getJDA().getUserById(mentionId).getAvatarUrl();

            if(mentionIcon == null) {
                mentionIcon = event.getJDA().getUserById(mentionId).getDefaultAvatarUrl();
            }
            disp = new BackpackDisplay_(user.getUserId()).findDisplay();
            disp.setMention(mention);
            disp.setMentionInfo(new UserInfo(mention, event));

            JDA.sendDynamicEmbed(event, user, null, disp, -1);

        } catch(IndexOutOfBoundsException e) {
            JDA.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that user...");
        }
    }
}
