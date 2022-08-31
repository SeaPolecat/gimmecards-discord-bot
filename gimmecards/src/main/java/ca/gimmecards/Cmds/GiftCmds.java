package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GiftCmds extends Cmds {
    
    public static void giftToken(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            try {
                String msg = "";
                User mention = User.findOtherUser(event, mentionId);
                int amount = Integer.parseInt(args[2]);

                msg += UX.formatNick(mention, event) + " has received a gift of tokens!";
                msg += mention.updateTokens(amount, true);
    
                JDA.sendMessage(event, mention.getGameColor(), "🎒", msg);
                try { User.saveUsers(); } catch(Exception e) {}
    
            } catch(NumberFormatException e) {
                JDA.sendMessage(event, red_, "❌", "Please enter a valid amount!");
            }
        }
    }

    public static void giftStar(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            try {
                String msg = "";
                User mention = User.findOtherUser(event, mentionId);
                int amount = Integer.parseInt(args[2]);

                msg += UX.formatNick(mention, event) + " has received a gift of stars!";
                msg += mention.updateStars(amount, true);
    
                JDA.sendMessage(event, mention.getGameColor(), "🎒", msg);
                try { User.saveUsers(); } catch(Exception e) {}
    
            } catch(NumberFormatException e) {
                JDA.sendMessage(event, red_, "❌", "Please enter a valid amount!");
            }
        }
    }

    public static void giftCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            try {
                String msg = "";
                User mention = User.findOtherUser(event, mentionId);
                UserInfo mi = new UserInfo(mention, event);
                Data item = Card.findDataById(args[2]);
                String cardTitle = UX.findCardTitle(item, true);
                String footer = mi.getUserName() + "'s gift";

                msg += "🎴 ";
                msg += UX.formatNick(mention, event) + " has received the gift of **" + cardTitle + "**";

                Card.addSingleCard(mention, item, true);
                
                Display.displayCard(event, mention, mi, item, msg, footer, true);
                try { User.saveUsers(); } catch(Exception e) {}
    
            } catch(NullPointerException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }

    public static void giftRare(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, mentionId);

            mention.setIsRare(true);
            if(!Check.ownsBadge(mention, "patreon")) {
                mention.getBadges().add("patreon");
            };

            msg += UX.formatNick(mention, event) + " is now a ⭐ **Rare Collector**\n\n";
            msg += "__**Rare Collector Rewards**__\n";
            msg += "🔹Earn **25%** more credits on weekends\n";
            msg += "🔹Boost your chances of drawing shiny cards by **10%**\n";
            msg += "🔹Receive the Patreon badge to display on your backpack\n";
            msg += "🔹Receive the *Rare Collector* role in our Discord server\n";
            msg += "🔹Access to Patreon-only giveaways in our Discord server\n";
            
            JDA.sendMessage(event, mention.getGameColor(), patreon_, msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftRadiantRare(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, mentionId);

            mention.setIsRadiantRare(true);
            if(!Check.ownsBadge(mention, "patreon")) {
                mention.getBadges().add("patreon");
            };

            msg += UX.formatNick(mention, event) + " is now a 🌟 **Radiant Rare Collector**\n\n";
            msg += "__**Radiant Rare Collector Rewards**__\n";
            msg += "🔹Receive all rewards in the *Rare Collector* tier\n";
            msg += "🔹Earn ***double*** tokens on weekends\n";
            msg += "🔹Shorten all your command cooldowns by **25%**\n";
            msg += "🔹Receive the *Radiant Rare Collector* role in our Discord server\n";
            
            JDA.sendMessage(event, mention.getGameColor(), patreon_, msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void removePatreonRewards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!user.getUserId().equals("454773340163538955")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            User mention = User.findOtherUser(event, mentionId);

            mention.setIsRare(false);
            mention.setIsRadiantRare(false);
            mention.removeBadge("patreon");
            
            JDA.sendMessage(event, mention.getGameColor(), patreon_, 
            "Removed Patreon rewards from " + UX.formatNick(mention, event));
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftHelperBadge(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!Check.ownsBadge(user, "staff")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            User mention = User.findOtherUser(event, mentionId);
    
            if(Check.ownsBadge(mention, "community")) {
                JDA.sendMessage(event, red_, "❌", "This user owns that badge already!");

            } else {
                mention.getBadges().add("community");

                JDA.sendMessage(event, mention.getGameColor(), "🎒", 
                UX.formatBadge(mention, event, communityBadge_, "Community Helper"));
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    public static void removeHelperBadge(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        String mentionId = JDA.findMentionId(event, args[1]);

        if(!Check.ownsBadge(user, "staff")) {
            return;

        } else if(mentionId == null) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that user...");

        } else {
            User mention = User.findOtherUser(event, mentionId);
    
            if(!Check.ownsBadge(mention, "community")) {
                JDA.sendMessage(event, red_, "❌", "This user doesn't own that badge!");

            } else {
                mention.removeBadge("community");

                JDA.sendMessage(event, mention.getGameColor(), "🎒", 
                "Removed the " + communityBadge_ + " **Community Helper** badge from " + UX.formatNick(mention, event));
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }
}
