package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class GiftCmds {
    
    public static void giftToken(SlashCommandInteractionEvent event) {
        OptionMapping targetOption = event.getOption("user");
        OptionMapping amount = event.getOption("amount");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User target = User.findTargetUser(event, targetOption.getAsUser().getId());

            msg += ui.getUserPing() + " has received a gift of tokens!";
            msg += target.updateTokens(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, target.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftCredits(SlashCommandInteractionEvent event) {
        OptionMapping targetOption = event.getOption("user");
        OptionMapping amount = event.getOption("amount");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User target = User.findTargetUser(event, targetOption.getAsUser().getId());

            msg += ui.getUserPing() + " has received a gift of credits!";
            msg += target.updateCredits(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, target.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftStar(SlashCommandInteractionEvent event) {
        OptionMapping targetOption = event.getOption("user");
        OptionMapping amount = event.getOption("amount");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User target = User.findTargetUser(event, targetOption.getAsUser().getId());

            msg += ui.getUserPing() + " has received a gift of stars!";
            msg += target.updateStars(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, target.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftKey(SlashCommandInteractionEvent event) {
        OptionMapping targetOption = event.getOption("user");
        OptionMapping amount = event.getOption("amount");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            String msg = "";
            User target = User.findTargetUser(event, targetOption.getAsUser().getId());

            msg += ui.getUserPing() + " has received a gift of keys!";
            msg += target.updateKeys(amount.getAsInt(), true);

            JDAUtils.sendMessage(event, target.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void giftCard(SlashCommandInteractionEvent event) {
        OptionMapping targetOption = event.getOption("user");
        OptionMapping cardId = event.getOption("card-id");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getUserId().equals("454773340163538955") && !user.getUserId().equals("967695872689315890")) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Only Gimme Cards developers can use this command!");

        } else {
            try {
                String msg = "";
                User target = User.findTargetUser(event, targetOption.getAsUser().getId());
                UserInfo mi = new UserInfo(event, target);
                Card item = Card.findCardById(cardId.getAsString());
                String cardTitle = item.findCardTitle(true);
                String footer = mi.getUserName() + "'s gift";

                msg += "🎴 ";
                msg += ui.getUserPing() + " has received the gift of **" + cardTitle + "**";

                target.addSingleCard(item, true);
                
                item.displayCard(event, mi, msg, footer, true);
                try { DataUtils.saveUsers(); } catch(Exception e) {}
    
            } catch(NullPointerException e) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
