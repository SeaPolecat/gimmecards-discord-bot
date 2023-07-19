package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
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
            GameObject.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += GameObject.formatNick(mention, event) + " has received a gift of tokens!";
            msg += mention.updateTokens(amount.getAsInt(), true);

            GameObject.sendMessage(event, mention.getGameColor(), "🎒", msg);
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
            GameObject.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += GameObject.formatNick(mention, event) + " has received a gift of stars!";
            msg += mention.updateStars(amount.getAsInt(), true);

            GameObject.sendMessage(event, mention.getGameColor(), "🎒", msg);
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
            GameObject.sendMessage(event, red_, "❌", "Only the owner of Gimme Cards can use this command!");

        } else {
            try {
                String msg = "";
                User mention = User.findOtherUser(event, user_.getAsUser().getId());
                UserInfo mi = new UserInfo(mention, event);
                Card item = Card.findCardById(cardId.getAsString());
                String cardTitle = item.findCardTitle(true);
                String footer = mi.getUserName() + "'s gift";

                msg += "🎴 ";
                msg += GameObject.formatNick(mention, event) + " has received the gift of **" + cardTitle + "**";

                mention.addSingleCard(item, true);
                
                Display.displayCard(event, mention, mi, item, msg, footer, true);
                try { User.saveUsers(); } catch(Exception e) {}
    
            } catch(NullPointerException e) {
                GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
