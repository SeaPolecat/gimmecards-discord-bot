package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InspectionCmds extends Cmds implements CustomCards {

    public static void openPack(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        InspectionDisplay disp = InspectionDisplay.findInspectionDisplay(user.getUserId());

        if(!State.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getOpenEpoch(), 5, false));

        } else {
            try {
                String setName = "";
                for(int i = 1; i < args.length; i++) {
                    setName += args[i] + " ";
                }
                setName = setName.trim();
                Data set = Data.findSet(setName);

                if(setName.equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 1) {
                        Rest.sendMessage(event, jigglypuff_ + " Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        Card c;
                        Data item = Card.pickCard(customs);
                        String msg = "";
                        String footer = event.getAuthor().getName() + "'s exclusive card";

                        user.resetOpenEpoch();
                        c = Card.addSingleCard(user, item, false);
    
                        msg += UX.formatNick(event) + " drew a card from " + logo_ + " **Gimme Cards**";
                        msg += UX.updateEnergy(user, UX.randRange(8, 10));
                        msg += UX.updateStars(user, -1);
    
                        State.updateBackpackDisplay(event, user);
                        State.updateCardDisplay(event, user);
    
                        Rest.sendMessage(event, msg);
                        Display.displayCard(event, user, item, c, footer);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else if(State.isRareSet(set) || State.isPromoSet(set)) {
                    int starCost;

                    if(State.isRareSet(set)) {
                        starCost = 1;
                    } else {
                        starCost = 2;
                    }
                    if(user.getStars() < starCost) {
                        if(State.isRareSet(set)) {
                            Rest.sendMessage(event, jigglypuff_ + " Sorry, you're out of " + star_ + " **Stars**");
                        } else {
                            Rest.sendMessage(event, jigglypuff_ + " Sorry, you don't have enough " + star_ + " **Stars**");
                        }

                    } else {
                        Card c;
                        Data item = Card.pickCard(set.getSpecs());
                        String msg = "";
                        String footer = event.getAuthor().getName();

                        if(State.isRareSet(set)) {
                            footer += "'s exclusive card";
                        } else {
                            footer += "'s promo card";
                        }
                        user.resetOpenEpoch();
                        c = Card.addSingleCard(user, item, true);
    
                        msg += UX.formatNick(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += UX.updateEnergy(user, UX.randRange(8, 10));
                        msg += UX.updateStars(user, -starCost);
    
                        State.updateBackpackDisplay(event, user);
                        State.updateCardDisplay(event, user);
    
                        Rest.sendMessage(event, msg);
                        Display.displayCard(event, user, item, c, footer);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!State.isPackUnlocked(user, set.getSetName())) {
                        Rest.sendMessage(event, jigglypuff_ + " This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        Rest.sendMessage(event, jigglypuff_ + " Sorry, you're out of " + token_ + " **Tokens**");
        
                    } else {
                        String msg = "";
    
                        user.resetOpenEpoch();
                        Card.addNewCards(user, set);
                        disp.setDispType("new");
                        
                        msg += UX.formatNick(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += UX.updateTokens(user, -1);
                        msg += UX.updateEnergy(user, UX.randRange(8, 10));
    
                        State.updateBackpackDisplay(event, user);
                        State.updateCardDisplay(event, user);
    
                        Rest.sendMessage(event, msg);
                        Rest.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that pack...");
            }
        }
    }

    public static void viewCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        InspectionDisplay disp = InspectionDisplay.findInspectionDisplay(user.getUserId());
        SearchDisplay disp2 = SearchDisplay.findSearchDisplay(user.getUserId());

        try {
            int page = Integer.parseInt(args[1]);

            disp.setDispType("old");
            Rest.sendDynamicEmbed(event, user, null, disp, page);
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            boolean exists = false;

            for(int i = 0; i < disp2.getSearchedCards().size(); i++) {
                String cardId = disp2.getSearchedCards().get(i).getCardId();

                if(cardId.equals(args[1])) {
                    exists = true;

                    disp.setDispType("search");
                    Rest.sendDynamicEmbed(event, user, null, disp, (i+1));
                    break;
                }
            }
            if(!exists) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }
}
