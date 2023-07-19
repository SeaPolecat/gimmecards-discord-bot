package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ViewCmds extends Cmds implements CustomCards {

    public static void openPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        if(!Check.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " + Check.findTimeLeft(user.getOpenEpoch(), 5, false));

        } else {
            try {
                Data set = Data.findSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 1) {
                        JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName() + "'s exclusive card";
                        Data item = Card.pickCard(customs);

                        msg += charmander_ + " ";
                        msg += UX.formatNick(event) + " drew a card from " + logo_ + " **Gimme Cards**";
                        msg += user.updateCredits(UX.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        Card.addSingleCard(user, item, false);
                        
                        Display.displayCard(event, user, ui, item, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else if(Check.isRareSet(set) || Check.isPromoSet(set)) {
                    if(user.getStars() < 1) {
                        JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName();
                        Data item = Card.pickCard(set.getSpecs());

                        if(Check.isRareSet(set)) {
                            msg += charmander_ + " ";
                            footer += "'s exclusive card";
                        } else {
                            msg += bulbasaur_ + " ";
                            footer += "'s promo card";
                        }
                        msg += UX.formatNick(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(UX.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        Card.addSingleCard(user, item, false);

                        Display.displayCard(event, user, ui, item, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!Check.isPackUnlocked(user, set.getSetName())) {
                        JDA.sendMessage(event, red_, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + token_ + " **Tokens**");
        
                    } else {
                        String msg = "";

                        if(Check.isOldSet(set)) {
                            msg += squirtle_ + " ";
                        } else {
                            msg += pikachu_ + " ";
                        }
                        msg += UX.formatNick(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateCredits(UX.randRange(8, 10), false);
    
                        disp.setDispType("new");
                        disp.setNewCards(Card.addNewCards(user, set));
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        JDA.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that pack...");
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
                JDA.sendMessage(event, red_, "❌", "You don't have any cards yet!");

            } else {
                int page = cardNum.getAsInt();

                disp.setDispType("old");

                JDA.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
