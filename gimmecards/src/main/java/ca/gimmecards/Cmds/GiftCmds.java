package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GiftCmds extends Cmds {
    
    public static void giftToken(MessageReceivedEvent event, String[] args) {
        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);
            int amount = Integer.parseInt(args[2]);
            String msg = "";

            msg += UX.formatNick(mention, event) + " has received a gift of tokens!";
            msg += UX.updateTokens(mention, amount);

            Rest.sendMessage(event, msg);
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(IndexOutOfBoundsException | NumberFormatException e) {
            if(e.toString().startsWith("java.lang.IndexOutOfBoundsException:")) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that user...");
            } else if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                Rest.sendMessage(event, jigglypuff_ + " Please enter a valid amount!");
            }
        }
    }

    public static void giftStar(MessageReceivedEvent event, String[] args) {
        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);
            int amount = Integer.parseInt(args[2]);
            String msg = "";

            msg += UX.formatNick(mention, event) + " has received a gift of stars!";
            msg += UX.updateStars(mention, amount);

            Rest.sendMessage(event, msg);
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(IndexOutOfBoundsException | NumberFormatException e) {
            if(e.toString().startsWith("java.lang.IndexOutOfBoundsException:")) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that user...");
            } else if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                Rest.sendMessage(event, jigglypuff_ + " Please enter a valid amount!");
            }
        }
    }

    public static void giftBadge(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            String mentionId = event.getMessage().getMentions().getUsers().get(0).getId();
            User mention = User.findOtherUser(event, mentionId);

            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {

                    for(String badge2 : mention.getBadges()) {
                        if(badge2.equalsIgnoreCase("community")) {
                            Rest.sendMessage(event, jigglypuff_ + " This user owns that badge already!");
                            return;
                        }
                    }
                    mention.getBadges().add("community");
                    Rest.sendMessage(event, UX.formatBadge(mention, event, communityBadge_, "Community Helper"));

                    try { User.saveUsers(); } catch(Exception e) {}
                    return;
                }
            }
            Rest.sendMessage(event, jigglypuff_ + " Only *Gimme Cards* staff members can gift this badge!");

        } catch(IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that user...");
        }
    }
}
