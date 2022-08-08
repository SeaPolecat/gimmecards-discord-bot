package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TradeCmds extends Cmds {
    
    public static void sendTrade(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        TradeDisplay disp;

        if(TradeDisplay.tradeExists(user.getUserId())) {
            JDA.sendMessage(event, jigglypuff_ + " You're in a trade already!");

        } else {
            try {
                String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
                User mention = User.findOtherUser(event, mentionId);
                String mentionName = event.getJDA().getUserById(mentionId).getName();
    
                if(mention.getUserId().equals(user.getUserId())) {
                    JDA.sendMessage(event, jigglypuff_ + " You can't trade with yourself!");
    
                } else {
                    TradeDisplay.addTradeDisplay(user, mention, mentionName);
                    disp = TradeDisplay.findTradeDisplay(user.getUserId());
                    
                    JDA.sendDynamicEmbed(event, user, null, disp, -1);
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }

    public static void makeOffer(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        TradeDisplay disp = TradeDisplay.findTradeDisplay(user.getUserId());

        if(disp == null) {
            JDA.sendMessage(event, jigglypuff_ + " You haven't started a trade yet!");

        } else {
            try {
                int index = Integer.parseInt(args[1]) - 1;
                Card c = user.getCards().get(index);

                if(TradeDisplay.isUser(user.getUserId(), disp)) {
                    disp.getUserOffer().add(c);

                } else {
                    disp.getMentionOffer().add(c);
                }

                Update.updateTradeDisplay(event, user);

            } catch(IndexOutOfBoundsException e) {
                JDA.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }
}
