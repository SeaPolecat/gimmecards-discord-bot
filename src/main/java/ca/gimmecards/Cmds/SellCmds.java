package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SellCmds {

    public static void sellSingle(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }
        
        if(user.getCardContainers().size() < 1) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't have any cards to sell!");

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                CardContainer cc = user.getCardContainers().get(index);
                String cardTitle = cc.getCard().findCardTitle(cc.getIsFav());
                int profit = findSingleProfit(user, index);
                int creditsReward = (int)(profit * RewardConsts.sellCredits_percent);
                
                if(profit == -1) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, that card is in your favourites!");

                } else {
                    String msg = "";

                    msg += FormatUtils.formatName(event) + " sold **" + cardTitle + "**";
                    msg += user.updateXP(profit, true);
                    if(creditsReward > 0) {
                        msg += user.updateCredits(creditsReward, false);
                    }
                    msg += user.checkLevelUp(event);
                    
                    JDAUtils.sendMessage(event, user.getGameColor(), "🎴", msg);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }

    public static void sellDuplicates(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);

        if(user.getCardContainers().size() < 1) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't have any cards to sell!");

        } else if(!hasDuplicates(user)) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't have any duplicate cards!");

        } else {
            int profit = findDuplicatesProfit(user);
            int creditsReward = (int)(profit * RewardConsts.sellCredits_percent);
            
            if(profit == -1) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, all your duplicate cards are in your favourites!");
                
            } else {
                String msg = "";

                msg += FormatUtils.formatName(event) + " sold all duplicates!";
                msg += user.updateXP(profit, true);
                if(creditsReward > 0) {
                    msg += user.updateCredits(creditsReward, false);
                }
                msg += user.checkLevelUp(event);
    
                JDAUtils.sendMessage(event, user.getGameColor(), "🎴", msg);
                try { DataUtils.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    public static void sellAll(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        
        if(user.getCardContainers().size() < 1) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You don't have any cards to sell!");

        } else {
            int profit = findAllProfit(user);
            int creditsReward = (int)(profit * RewardConsts.sellCredits_percent);

            if(profit == -1) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, all your cards are in your favourites!");

            } else {
                String msg = "";

                msg += FormatUtils.formatName(event) + " sold all their cards! (except favourites)";
                msg += user.updateXP(profit, true);
                if(creditsReward > 0) {
                    msg += user.updateCredits(creditsReward, false);
                }
                msg += user.checkLevelUp(event);
        
                JDAUtils.sendMessage(event, user.getGameColor(), "🎴", msg);
                try { DataUtils.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    private static int findSingleProfit(User user, int index) {
        int profit = 0;
        CardContainer cc = user.getCardContainers().get(index);

        if(cc.getIsFav()) {
            return -1;

        } else {
            if(cc.getCardQuantity() == 1) {
                user.getCardContainers().remove(index);
            } else {
                cc.minusCardQuantity();
            }
            if(cc.getIsSellable()) {
                profit += cc.getCard().getCardPrice();
            }
        }
        return profit;
    }

    private static boolean hasDuplicates(User user) {
        for(CardContainer cc : user.getCardContainers()) {
            if(cc.getCardQuantity() > 1) {
                return true;
            }
        }
        return false;
    }

    private static int findDuplicatesProfit(User user) {
        int profit = 0;
        boolean exists = false;

        for(CardContainer cc : user.getCardContainers()) {
            if(!cc.getIsFav() && cc.getCardQuantity() > 1) {
                exists = true;
                while(cc.getCardQuantity() > 1) {
                    cc.minusCardQuantity();
                    if(cc.getIsSellable()) {
                        profit += cc.getCard().getCardPrice();
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

        for(int i = 0; i < user.getCardContainers().size(); i++) {
            CardContainer cc = user.getCardContainers().get(i);

            if(!cc.getIsFav()) {
                exists = true;
                while(cc.getCardQuantity() > 0) {
                    cc.minusCardQuantity();
                    if(cc.getIsSellable()) {
                        profit += cc.getCard().getCardPrice();
                    }
                    if(cc.getCardQuantity() < 1) {
                        user.getCardContainers().remove(i);
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
