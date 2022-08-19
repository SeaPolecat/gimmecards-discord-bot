package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ViewCmds extends Cmds implements CustomCards {

    public static void openPack(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();

        if(!Check.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            JDA.sendMessage(event, red_, "❌", "Please wait another " + Check.findTimeLeft(user.getOpenEpoch(), 5, false));

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
                        JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getAuthor().getName() + "'s exclusive card";
                        Data item = Card.pickCard(customs);

                        user.resetOpenEpoch();
                        Card.addSingleCard(user, item, false);
    
                        msg += UX.formatNick(event) + " drew a card from " + logo_ + " **Gimme Cards**";
                        msg += user.updateEnergy(UX.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);
    
                        Update.updateBackpackDisplay(event, user);
                        Update.updateCardDisplay(event, user);
                        Update.updateViewDisplay(event, user);
    
                        JDA.sendMessage(event, user.getGameColor(), charmander_, msg);
                        Display.displayCard(event, user, ui, item, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else if(Check.isRareSet(set) || Check.isPromoSet(set)) {
                    if(user.getStars() < 1) {
                        JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + star_ + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getAuthor().getName();
                        Data item = Card.pickCard(set.getSpecs());

                        if(Check.isRareSet(set)) {
                            footer += "'s exclusive card";
                        } else {
                            footer += "'s promo card";
                        }
                        user.resetOpenEpoch();
                        Card.addSingleCard(user, item, false);
    
                        msg += UX.formatNick(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateEnergy(UX.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);
    
                        Update.updateBackpackDisplay(event, user);
                        Update.updateCardDisplay(event, user);
                        Update.updateViewDisplay(event, user);

                        if(Check.isRareSet(set)) {
                            JDA.sendMessage(event, user.getGameColor(), charmander_, msg);
                        } else {
                            JDA.sendMessage(event, user.getGameColor(), bulbasaur_, msg);
                        }
                        Display.displayCard(event, user, ui, item, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!Check.isPackUnlocked(user, set.getSetName())) {
                        JDA.sendMessage(event, red_, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        JDA.sendMessage(event, red_, "❌", "Sorry, you're out of " + token_ + " **Tokens**");
        
                    } else {
                        String msg = "";
    
                        disp.setDispType("new");
                        disp.setNewCards(Card.addNewCards(user, set));

                        user.resetOpenEpoch();
                        
                        msg += UX.formatNick(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateEnergy(UX.randRange(8, 10), false);
    
                        Update.updateBackpackDisplay(event, user);
                        Update.updateCardDisplay(event, user);
                        Update.updateViewDisplay(event, user);
    
                        if(Check.isOldSet(set)) {
                            JDA.sendMessage(event, user.getGameColor(), squirtle_, msg);
                        } else {
                            JDA.sendMessage(event, user.getGameColor(), pikachu_, msg);
                        }
                        JDA.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that pack...");
            }
        }
    }

    public static void viewCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();

        try {
            if(user.getCards().size() < 1) {
                JDA.sendMessage(event, red_, "❌", "You don't have any cards yet!");

            } else {
                int page = Integer.parseInt(args[1]);

                disp.setDispType("old");

                JDA.sendDynamicEmbed(event, user, null, disp, page);
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
