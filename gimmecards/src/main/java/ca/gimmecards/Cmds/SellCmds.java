package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SellCmds extends Cmds {

    public static void sellSingle(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        
        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, jigglypuff_ + " You don't have any cards to sell!");

        } else {
            try {
                int index = Integer.parseInt(args[1]) - 1;
                Card c = user.getCards().get(index);
                String cardTitle = UX.findCardTitle(c.getData(), c.getIsFav());
                int profit = findSingleProfit(user, index);
                int energyReward = (int)(profit * 0.02);
                
                if(profit == -1) {
                    JDA.sendMessage(event, jigglypuff_ + " Sorry, that card is in your favourites!");

                } else {
                    String msg = "";

                    msg += UX.formatNick(event) + " sold **" + cardTitle + "**";
                    msg += UX.updateXP(user, profit);
                    if(energyReward > 0) {
                        msg += UX.updateEnergy(user, energyReward);
                    }
                    
                    Update.updateBackpackDisplay(event, user);
                    Update.updateCardDisplay(event, user);
                    Update.updateViewDisplay(event, user);
                    
                    JDA.sendMessage(event, msg);
                    Check.checkLevelUp(event, user);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }

    public static void sellDuplicates(MessageReceivedEvent event) {
        User user = User.findUser(event);

        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, jigglypuff_ + " You don't have any cards to sell!");

        } else {
            int profit = findDuplicatesProfit(user);
            int energyReward = (int)(profit * 0.02);

            if(profit == -1) {
                JDA.sendMessage(event, jigglypuff_ + " You don't have any duplicate cards!");
                
            } else {
                String msg = "";

                msg += UX.formatNick(event) + " sold all duplicates!";
                msg += UX.updateXP(user, profit);
                if(energyReward > 0) {
                    msg += UX.updateEnergy(user, energyReward);
                }
    
                Update.updateBackpackDisplay(event, user);
                Update.updateCardDisplay(event, user);
                Update.updateViewDisplay(event, user);
    
                JDA.sendMessage(event, msg);
                Check.checkLevelUp(event, user);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    public static void sellAll(MessageReceivedEvent event) {
        User user = User.findUser(event);
        
        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, jigglypuff_ + " You don't have any cards to sell!");

        } else {
            int profit = findAllProfit(user);
            int energyReward = (int)(profit * 0.02);

            if(profit == -1) {
                JDA.sendMessage(event, jigglypuff_ + " Sorry, all your cards are in your favourites!");

            } else {
                String msg = "";

                msg += UX.formatNick(event) + " sold all their cards! (except favourites)";
                msg += UX.updateXP(user, profit);
                if(energyReward > 0) {
                    msg += UX.updateEnergy(user, energyReward);
                }

                Update.updateBackpackDisplay(event, user);
                Update.updateCardDisplay(event, user);
                Update.updateViewDisplay(event, user);
        
                JDA.sendMessage(event, msg);
                Check.checkLevelUp(event, user);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    private static int findSingleProfit(User user, int index) {
        int profit = 0;
        Card c = user.getCards().get(index);

        if(c.getIsFav()) {
            return -1;

        } else {
            if(c.getCardQuantity() == 1) {
                user.getCards().remove(index);
            } else {
                c.minusCardQuantity();
            }
            if(c.getSellable()) {
                profit += c.getData().getCardPrice();
            }
        }
        return profit;
    }

    private static int findDuplicatesProfit(User user) {
        int profit = 0;
        boolean exists = false;

        for(Card c : user.getCards()) {
            if(c.getCardQuantity() > 1) {
                exists = true;
                while(c.getCardQuantity() > 1) {
                    c.minusCardQuantity();
                    if(c.getSellable()) {
                        profit += c.getData().getCardPrice();
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
            Card c = user.getCards().get(i);

            if(!c.getIsFav()) {
                exists = true;
                while(c.getCardQuantity() > 0) {
                    c.minusCardQuantity();
                    if(c.getSellable()) {
                        profit += c.getData().getCardPrice();
                    }
                    if(c.getCardQuantity() < 1) {
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
