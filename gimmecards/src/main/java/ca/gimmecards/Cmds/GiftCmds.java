package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class GiftCmds extends Cmds {
    
    public static void giftToken(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping amount = event.getOption("amount");

        if(user_ == null || amount == null) { return; }

        if(!user.getUserId().equals("454773340163538955")) {
            JDA.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += UX.formatNick(mention, event) + " has received a gift of tokens!";
            msg += mention.updateTokens(amount.getAsInt(), true);

            JDA.sendMessage(event, mention.getGameColor(), "🎒", msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftStar(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping amount = event.getOption("amount");

        if(user_ == null || amount == null) { return; }

        if(!user.getUserId().equals("454773340163538955")) {
            JDA.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += UX.formatNick(mention, event) + " has received a gift of stars!";
            msg += mention.updateStars(amount.getAsInt(), true);

            JDA.sendMessage(event, mention.getGameColor(), "🎒", msg);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping cardId = event.getOption("card-id");

        if(user_ == null || cardId == null) { return; }

        if(!user.getUserId().equals("454773340163538955")) {
            JDA.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            try {
                String msg = "";
                User mention = User.findOtherUser(event, user_.getAsUser().getId());
                UserInfo mi = new UserInfo(mention, event);
                Data item = Card.findDataById(cardId.getAsString());
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

    public static void giftRare(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        if(!user.getUserId().equals("454773340163538955")) {
            JDA.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

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

    public static void giftRadiantRare(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        if(!user.getUserId().equals("454773340163538955")) {
            JDA.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

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

    public static void removePatreonRewards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        if(!user.getUserId().equals("454773340163538955")) {
            JDA.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            mention.setIsRare(false);
            mention.setIsRadiantRare(false);
            mention.removeBadge("patreon");
            
            JDA.sendMessage(event, mention.getGameColor(), patreon_, 
            "Removed Patreon rewards from " + UX.formatNick(mention, event));
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftHelperBadge(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        if(!Check.ownsBadge(user, "staff")) {
            JDA.sendMessage(event, red_, "❌", "Only staff members in the official Gimme Cards server can use this command!");

        } else {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());
    
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

    public static void removeHelperBadge(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        if(!Check.ownsBadge(user, "staff")) {
            JDA.sendMessage(event, red_, "❌", "Only staff members in the official Gimme Cards server can use this command!");

        } else {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());
    
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
