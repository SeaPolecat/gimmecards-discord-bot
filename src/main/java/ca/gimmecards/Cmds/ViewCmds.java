package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds {

    public static void openPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        if(!User.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " + User.findTimeLeft(user.getOpenEpoch(), 5, false));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(!user.hasPremiumRole(event)) {
                        GameManager.sendPremiumMessage(event, IColors.kofiColor, IEmotes.kofi, "Sorry, this is a premium feature!", null);

                    } else if(user.getStars() < 1) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.star + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName() + "'s exclusive card";
                        Card item = Card.pickCard(ICustomCards.customs);

                        msg += IEmotes.charmander + " ";
                        msg += GameManager.formatName(event) + " drew a card from " + IEmotes.logo + " **Gimme Cards**";
                        msg += user.updateCredits(GameManager.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);
                        
                        item.displayCard(event, ui, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else if(set.isRareSet() || set.isPromoSet()) {
                    if(!user.hasPremiumRole(event)) {
                        GameManager.sendPremiumMessage(event, IColors.kofiColor, IEmotes.kofi, "Sorry, this is a premium feature!", null);

                    } else if(user.getStars() < 1) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.star + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName();
                        Card item = Card.pickCard(set.getSpecials());

                        if(set.isRareSet()) {
                            msg += IEmotes.charmander + " ";
                            footer += "'s exclusive card";
                        } else {
                            msg += IEmotes.bulbasaur + " ";
                            footer += "'s promo card";
                        }
                        msg += GameManager.formatName(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(GameManager.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);

                        item.displayCard(event, ui, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        GameManager.sendMessage(event, IColors.red, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.token + " **Tokens**");
        
                    } else {
                        String msg = "";

                        if(set.isOldSet()) {
                            msg += IEmotes.squirtle + " ";
                        } else {
                            msg += IEmotes.pikachu + " ";
                        }
                        msg += GameManager.formatName(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateCredits(GameManager.randRange(8, 10), false);
    
                        disp.setDispType("new");
                        disp.setNewCards(user.addNewCards(set));
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        GameManager.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that pack...");
            }
        }
    }

    public static void viewCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            if(user.getCardContainers().size() < 1) {
                GameManager.sendMessage(event, IColors.red, "❌", "You don't have any cards yet!");

            } else {
                int page = cardNum.getAsInt();

                disp.setDispType("old");

                GameManager.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
