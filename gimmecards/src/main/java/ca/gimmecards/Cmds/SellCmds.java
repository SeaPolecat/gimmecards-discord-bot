package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SellCmds extends Cmds {

    public static void sellSingle(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        
        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, red_, "❌", "You don't have any cards to sell!");

        } else {
            try {
                int index = Integer.parseInt(args[1]) - 1;
                Card card = user.getCards().get(index);
                String cardTitle = UX.findCardTitle(card.getData(), card.getIsFav());
                int profit = findSingleProfit(user, index);
                int creditsReward = (int)(profit * 0.02);
                
                if(profit == -1) {
                    JDA.sendMessage(event, red_, "❌", "Sorry, that card is in your favourites!");

                } else {
                    String msg = "";

                    msg += UX.formatNick(event) + " sold **" + cardTitle + "**";
                    msg += user.updateXP(profit, true);
                    if(creditsReward > 0) {
                        msg += user.updateCredits(creditsReward, false);
                    }
                    
                    Update.updateBackpackDisplay(event, user);
                    Update.updateCardDisplay(event, user);
                    Update.updateViewDisplay(event, user);
                    
                    JDA.sendMessage(event, user.getGameColor(), "🎴", msg);
                    Check.checkLevelUp(event, user);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }

    public static void sellDuplicates(MessageReceivedEvent event) {
        User user = User.findUser(event);

        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, red_, "❌", "You don't have any cards to sell!");

        } else if(!hasDuplicates(user)) {
            JDA.sendMessage(event, red_, "❌", "You don't have any duplicate cards!");

        } else {
            int profit = findDuplicatesProfit(user);
            int creditsReward = (int)(profit * 0.02);
            
            if(profit == -1) {
                JDA.sendMessage(event, red_, "❌", "Sorry, all your duplicate cards are in your favourites!");
                
            } else {
                String msg = "";

                msg += UX.formatNick(event) + " sold all duplicates!";
                msg += user.updateXP(profit, true);
                if(creditsReward > 0) {
                    msg += user.updateCredits(creditsReward, false);
                }
    
                Update.updateBackpackDisplay(event, user);
                Update.updateCardDisplay(event, user);
                Update.updateViewDisplay(event, user);
    
                JDA.sendMessage(event, user.getGameColor(), "🎴", msg);
                Check.checkLevelUp(event, user);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    public static void sellAll(MessageReceivedEvent event) {
        User user = User.findUser(event);
        
        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, red_, "❌", "You don't have any cards to sell!");

        } else {
            int profit = findAllProfit(user);
            int creditsReward = (int)(profit * 0.02);

            if(profit == -1) {
                JDA.sendMessage(event, red_, "❌", "Sorry, all your cards are in your favourites!");

            } else {
                String msg = "";

                msg += UX.formatNick(event) + " sold all their cards! (except favourites)";
                msg += user.updateXP(profit, true);
                if(creditsReward > 0) {
                    msg += user.updateCredits(creditsReward, false);
                }

                Update.updateBackpackDisplay(event, user);
                Update.updateCardDisplay(event, user);
                Update.updateViewDisplay(event, user);
        
                JDA.sendMessage(event, user.getGameColor(), "🎴", msg);
                Check.checkLevelUp(event, user);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    private static int findSingleProfit(User user, int index) {
        int profit = 0;
        Card card = user.getCards().get(index);

        if(card.getIsFav()) {
            return -1;

        } else {
            if(card.getCardQuantity() == 1) {
                user.getCards().remove(index);
            } else {
                card.minusCardQuantity();
            }
            if(card.getSellable()) {
                profit += card.getData().getCardPrice();
            }
        }
        return profit;
    }

    private static boolean hasDuplicates(User user) {
        for(Card card : user.getCards()) {
            if(card.getCardQuantity() > 1) {
                return true;
            }
        }
        return false;
    }

    private static int findDuplicatesProfit(User user) {
        int profit = 0;
        boolean exists = false;

        for(Card card : user.getCards()) {
            if(!card.getIsFav() && card.getCardQuantity() > 1) {
                exists = true;
                while(card.getCardQuantity() > 1) {
                    card.minusCardQuantity();
                    if(card.getSellable()) {
                        profit += card.getData().getCardPrice();
                    }
                }
            }
        }
        if(!exists) {
            return -1;
        }
        return profit;
    }

    private static int findAllProfit(User user) {
        int profit = 0;
        boolean exists = false;

        for(int i = 0; i < user.getCards().size(); i++) {
            Card card = user.getCards().get(i);

            if(!card.getIsFav()) {
                exists = true;
                while(card.getCardQuantity() > 0) {
                    card.minusCardQuantity();
                    if(card.getSellable()) {
                        profit += card.getData().getCardPrice();
                    }
                    if(card.getCardQuantity() < 1) {
                        user.getCards().remove(i);
                        i--;
                    }
                }
            }
        }
        if(!exists) {
            return -1;
        }
        return profit;
    }
}
