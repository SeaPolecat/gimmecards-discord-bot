package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.CustomCards;
import ca.gimmecards.Display.*;
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

        if(!GameObject.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            GameObject.sendMessage(event, red_, "⏰", "Please wait another " + GameObject.findTimeLeft(user.getOpenEpoch(), 5, false));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 1) {
                        GameObject.sendMessage(event, red_, "❌", "Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName() + "'s exclusive card";
                        Card item = Card.pickCard(customs);

                        msg += charmander_ + " ";
                        msg += GameObject.formatNick(event) + " drew a card from " + logo_ + " **Gimme Cards**";
                        msg += user.updateCredits(GameObject.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);
                        
                        Display.displayCard(event, user, ui, item, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else if(set.isRareSet() || set.isPromoSet()) {
                    if(user.getStars() < 1) {
                        GameObject.sendMessage(event, red_, "❌", "Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName();
                        Card item = Card.pickCard(set.getSpecials());

                        if(set.isRareSet()) {
                            msg += charmander_ + " ";
                            footer += "'s exclusive card";
                        } else {
                            msg += bulbasaur_ + " ";
                            footer += "'s promo card";
                        }
                        msg += GameObject.formatNick(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(GameObject.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);

                        Display.displayCard(event, user, ui, item, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        GameObject.sendMessage(event, red_, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        GameObject.sendMessage(event, red_, "❌", "Sorry, you're out of " + token_ + " **Tokens**");
        
                    } else {
                        String msg = "";

                        if(set.isOldSet()) {
                            msg += squirtle_ + " ";
                        } else {
                            msg += pikachu_ + " ";
                        }
                        msg += GameObject.formatNick(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateCredits(GameObject.randRange(8, 10), false);
    
                        disp.setDispType("new");
                        disp.setNewCards(user.addNewCards(set));
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        GameObject.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that pack...");
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
                GameObject.sendMessage(event, red_, "❌", "You don't have any cards yet!");

            } else {
                int page = cardNum.getAsInt();

                disp.setDispType("old");

                GameObject.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
