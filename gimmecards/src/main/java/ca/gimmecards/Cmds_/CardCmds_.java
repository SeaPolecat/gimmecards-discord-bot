package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CardCmds_ extends Cmds {
    
    public static void viewCards_(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        CardDisplay_ disp;

        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);

            if(mention.getCards().size() < 1) {
                Rest.sendMessage(event, jigglypuff_ + " That user doesn't have any cards yet!");

            } else {
                String mentionName = event.getJDA().getUserById(mentionId).getName();

                disp = CardDisplay_.findCardDisplay_(user.getUserId());
                disp.setUser(user);
                disp.setMention(mention);
                disp.setMentionName(mentionName);

                Rest.sendDynamicEmbed(event, user, null, disp, 1);
            }
        } catch(IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that user...");
        }
    }
}
