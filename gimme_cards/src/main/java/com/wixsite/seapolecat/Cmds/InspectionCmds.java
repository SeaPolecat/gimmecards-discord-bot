package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class InspectionCmds extends Cmds {

    public static void openPack(GuildMessageReceivedEvent event, String[] args) {
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

                if(State.isSpecSet(set)) {
                    if(user.getStars() < 1) {
                        Rest.sendMessage(event, jigglypuff_ + " Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        Data item = Card.pickCard(set.getSpecs());
                        String msg = "";
                        String footer = event.getAuthor().getName() + "'s exclusive card";
    
                        user.resetOpenEpoch();
                        Card.addSingleCard(user, item);
    
                        msg += UX.formatNick(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += UX.updateEnergy(user, UX.randRange(16, 20));
                        msg += UX.updateStars(user, -1);
    
                        State.updateBackpackDisplay(event, user);
                        State.updateCardDisplay(event, user);
    
                        Rest.sendMessage(event, msg);
                        Display.displayCard(event, user, item, footer);
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
                        msg += UX.updateEnergy(user, UX.randRange(16, 20));
    
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

    public static void viewCard(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        InspectionDisplay disp = InspectionDisplay.findInspectionDisplay(user.getUserId());
        SearchDisplay disp2 = SearchDisplay.findSearchDisplay(user.getUserId());

        if(args.length < 3) {
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
}
