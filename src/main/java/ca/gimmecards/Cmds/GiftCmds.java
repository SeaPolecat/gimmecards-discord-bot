package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class GiftCmds {
    
    public static void giftToken(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping amount = event.getOption("amount");

        if(user_ == null || amount == null) { return; }

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += FormatUtils.formatName(mention, event) + " has received a gift of tokens!";
            msg += mention.updateTokens(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, mention.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftCredits(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping amount = event.getOption("amount");

        if(user_ == null || amount == null) { return; }

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += FormatUtils.formatName(mention, event) + " has received a gift of credits!";
            msg += mention.updateCredits(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, mention.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftStar(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping amount = event.getOption("amount");

        if(user_ == null || amount == null) { return; }

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += FormatUtils.formatName(mention, event) + " has received a gift of stars!";
            msg += mention.updateStars(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, mention.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftKey(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping amount = event.getOption("amount");

        if(user_ == null || amount == null) { return; }

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            msg += FormatUtils.formatName(mention, event) + " has received a gift of keys!";
            msg += mention.updateKeys(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, mention.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping user_ = event.getOption("user");
        OptionMapping cardId = event.getOption("card-id");

        if(user_ == null || cardId == null) { return; }

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            try {
                String msg = "";
                User mention = User.findOtherUser(event, user_.getAsUser().getId());
                UserInfo mi = new UserInfo(mention, event);
                Card item = Card.findCardById(cardId.getAsString());
                String cardTitle = item.findCardTitle(true);
                String footer = mi.getUserName() + "'s gift";

                msg += "🎴 ";
                msg += FormatUtils.formatName(mention, event) + " has received the gift of **" + cardTitle + "**";

                mention.addSingleCard(item, true);
                
                item.displayCard(event, mi, msg, footer, true);
                try { DataUtils.saveUsers(); } catch(Exception e) {}
    
            } catch(NullPointerException e) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
