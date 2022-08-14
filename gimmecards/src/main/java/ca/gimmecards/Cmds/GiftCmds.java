package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GiftCmds extends Cmds {
    
    public static void giftToken(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else {
            try {
                String msg = "";
                String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
                User mention = User.findOtherUser(event, mentionId);
                int amount = Integer.parseInt(args[2]);

                msg += UX.formatNick(mention, event) + " has received a gift of tokens!";
                msg += user.updateTokens(amount, true);
    
                JDA.sendMessage(event, mention.getGameColor(), "🎒", msg);
                try { User.saveUsers(); } catch(Exception e) {}
    
            } catch(IndexOutOfBoundsException | NumberFormatException e) {
                if(e.toString().startsWith("java.lang.IndexOutOfBoundsException:")) {
                    JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");
                } else if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                    JDA.sendMessage(event, red_, "❌", "Please enter a valid amount!");
                }
            }
        }
    }

    public static void giftStar(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else {
            try {
                String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
                User mention = User.findOtherUser(event, mentionId);
                int amount = Integer.parseInt(args[2]);
                String msg = "";

                msg += UX.formatNick(mention, event) + " has received a gift of stars!";
                msg += user.updateStars(amount, true);
    
                JDA.sendMessage(event, mention.getGameColor(), "🎒", msg);
                try { User.saveUsers(); } catch(Exception e) {}
    
            } catch(IndexOutOfBoundsException | NumberFormatException e) {
                if(e.toString().startsWith("java.lang.IndexOutOfBoundsException:")) {
                    JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");
                } else if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                    JDA.sendMessage(event, red_, "❌", "Please enter a valid amount!");
                }
            }
        }
    }

    public static void giftBadge(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        if(!Check.ownsBadge(user, "staff")) {
            return;

        } else {
            try {
                String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
                User mention = User.findOtherUser(event, mentionId);
    
                if(Check.ownsBadge(mention, "community")) {
                    JDA.sendMessage(event, red_, "❌", "This user owns that badge already!");
    
                } else {
                    mention.getBadges().add("community");
    
                    JDA.sendMessage(event, mention.getGameColor(), "🎒", UX.formatBadge(mention, event, communityBadge_, "Community Helper"));
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(IndexOutOfBoundsException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");
            }
        }
    }
}
