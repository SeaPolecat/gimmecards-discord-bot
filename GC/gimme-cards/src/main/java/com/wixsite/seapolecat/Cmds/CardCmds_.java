package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CardCmds_ extends Cmds {
    
    public static void viewCards_(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        CardDisplay_ disp = CardDisplay_.findCardDisplay_(user.getUserId());

        if(args.length > 1) {
            try {
                String mentionId = event.getMessage().getMentionedUsers().get(0).getId();
                User mention = User.findOtherUser(event, mentionId);
    
                if(mention.getCards().size() < 1) {
                    Rest.sendMessage(event, jigglypuff_ + " That user doesn't have any cards yet!");
    
                } else {
                    String mentionName = event.getJDA().getUserById(mentionId).getName();

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
}
