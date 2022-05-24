package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SellCmds extends Cmds {

    public static void sellSingle(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        
        if(user.getCards().size() < 1) {
            Rest.sendMessage(event, jigglypuff_ + " You don't have any cards to sell!");

        } else {
            try {
                int index = Integer.parseInt(args[1]) - 1;
                Card c = user.getCards().get(index);
                String cardTitle = UX.findCardTitle(c.getData(), c.getIsFav());
                int profit = findSingleProfit(user, index);
                int energyReward = (int)(profit * 0.02);
                
                if(profit == -1) {
                    Rest.sendMessage(event, jigglypuff_ + " Sorry, that card is in your favourites!");

                } else {
                    String msg = "";

                    msg += UX.formatNick(event) + " sold **" + cardTitle + "**";
                    msg += UX.updateXP(user, profit);
                    if(energyReward > 0) {
                        msg += UX.updateEnergy(user, energyReward);
                    }

                    State.updateCardDisplay(event, user);
                    State.updateBackpackDisplay(event, user);
                    State.updateInspectionDisplay(event, user);
                    State.updateFavDisplay(event, user);
                    
                    Rest.sendMessage(event, msg);
                    State.checkLevelUp(event, user);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
            }
        }
    }

    public static void sellGroup(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        if(user.getCards().size() < 1) {
            Rest.sendMessage(event, jigglypuff_ + " You don't have any cards to sell!");

        } else {
            try {
                int start = Integer.parseInt(args[1]) - 1;
                int end = Integer.parseInt(args[2]) - 1;
                int cardsSold = (end - start) + 1;
    
                if(start >= end) {
                    Integer.parseInt("$");

                } else if(start < 0 || end > user.getCards().size() - 1) {
                    Rest.sendMessage(event, jigglypuff_ + " Whoops, your numbers might be out of range...");
    
                } else {
                    int profit = findGroupProfit(user, start, end);
                    int energyReward = (int)(profit * 0.02);

                    if(profit == -1) {
                        Rest.sendMessage(event, jigglypuff_ + " Sorry, some of those cards are in your favourites!");

                    } else {
                        String msg = "";

                        msg += UX.formatNick(event) + " sold **" + cardsSold + "** cards!";
                        msg += UX.updateXP(user, profit);
                        if(energyReward > 0) {
                            msg += UX.updateEnergy(user, energyReward);
                        }

                        State.updateCardDisplay(event, user);
                        State.updateBackpackDisplay(event, user);
                        State.updateInspectionDisplay(event, user);
                        State.updateFavDisplay(event, user);
        
                        Rest.sendMessage(event, msg);
                        State.checkLevelUp(event, user);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find these cards...");
            }
        }
    }

    public static void sellDuplicates(MessageReceivedEvent event) {
        User user = User.findUser(event);
        int profit = findDuplicatesProfit(user);
        int energyReward = (int)(profit * 0.02);

        if(profit == 0) {
            Rest.sendMessage(event, jigglypuff_ + " You don't have any duplicate cards!");
            
        } else {
            String msg = "";

            msg += UX.formatNick(event) + " sold all duplicates!";
            msg += UX.updateXP(user, profit);
            if(energyReward > 0) {
                msg += UX.updateEnergy(user, energyReward);
            }

            State.updateCardDisplay(event, user);
            State.updateBackpackDisplay(event, user);
            State.updateInspectionDisplay(event, user);
            State.updateFavDisplay(event, user);

            Rest.sendMessage(event, msg);
            State.checkLevelUp(event, user);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void sellAll(MessageReceivedEvent event) {
        User user = User.findUser(event);
        
        if(user.getCards().size() < 1) {
            Rest.sendMessage(event, jigglypuff_ + " You don't have any cards to sell!");

        } else {
            int profit = findAllProfit(user);
            int energyReward = (int)(profit * 0.02);

            if(profit == -1) {
                Rest.sendMessage(event, jigglypuff_ + " Sorry, all of your cards are in your favourites!");

            } else {
                String msg = "";

                msg += UX.formatNick(event) + " sold all their cards! (except favourites)";
                msg += UX.updateXP(user, profit);
                if(energyReward > 0) {
                    msg += UX.updateEnergy(user, energyReward);
                }

                State.updateCardDisplay(event, user);
                State.updateBackpackDisplay(event, user);
                State.updateInspectionDisplay(event, user);
                State.updateFavDisplay(event, user);
        
                Rest.sendMessage(event, msg);
                State.checkLevelUp(event, user);
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
            if(!State.isOldSet(c.getData())) {
                profit += c.getData().getCardPrice();
            }
        }
        return profit;
    }

    private static int findGroupProfit(User user, int start, int end) {
        int profit = 0;

        for(int i = start; i <= end; i++) {
            if(user.getCards().get(i).getIsFav()) {
                return -1;
            }
        }
        for(int i = start; i <= end; i++) {
            Card c = user.getCards().get(start);

            if(c.getCardQuantity() == 1) {
                user.getCards().remove(start);
            } else {
                c.minusCardQuantity();
            }
            if(!State.isOldSet(c.getData())) {
                profit += c.getData().getCardPrice();
            }
        }
        return profit;
    }

    private static int findDuplicatesProfit(User user) {
        int profit = 0;

        for(Card c : user.getCards()) {
            while(c.getCardQuantity() > 1) {
                c.minusCardQuantity();
                if(!State.isOldSet(c.getData())) {
                    profit += c.getData().getCardPrice();
                }
            }
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
                    if(!State.isOldSet(c.getData())) {
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
