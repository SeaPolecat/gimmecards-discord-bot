/*package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TradeCmds extends Cmds {
    
    public static void sendTrade(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        TradeDisplay disp;

        if(TradeDisplay.tradeExists(user.getUserId())) {
            Rest.sendMessage(event, jigglypuff_ + " You're in a trade already!");

        } else {
            try {
                String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
                User mention = User.findOtherUser(event, mentionId);
                String mentionName = event.getJDA().getUserById(mentionId).getName();
    
                if(mention.getUserId().equals(user.getUserId())) {
                    Rest.sendMessage(event, jigglypuff_ + " You can't trade with yourself!");
    
                } else {
                    TradeDisplay.addTradeDisplay(user, mention, mentionName);
                    disp = TradeDisplay.findTradeDisplay(user.getUserId());
                    
                    Rest.sendDynamicEmbed(event, user, null, disp, -1);
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }

    public static void makeOffer(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        TradeDisplay disp = TradeDisplay.findTradeDisplay(user.getUserId());

        if(disp == null) {
            Rest.sendMessage(event, jigglypuff_ + " You haven't started a trade yet!");

        } else {
            try {
                int index = Integer.parseInt(args[1]) - 1;
                Card c = user.getCards().get(index);

                if(TradeDisplay.isUser(user.getUserId(), disp)) {
                    disp.getUserOffer().add(c);

                } else {
                    disp.getMentionOffer().add(c);
                }

                State.updateTradeDisplay(event, user);

            } catch(IndexOutOfBoundsException e) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }
}*/
