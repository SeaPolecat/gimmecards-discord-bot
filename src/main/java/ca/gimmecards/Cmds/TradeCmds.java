package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class TradeCmds {
    
    public static void sendTrade(SlashCommandInteractionEvent event) {
        OptionMapping userOption = event.getOption("user");
        //
        User user = User.findUser(event);
        User target = User.findTargetUser(event, userOption.getAsUser().getId());
        TradeDisplay userDisp = (TradeDisplay) user.findDisplay(TradeDisplay.class);
        TradeDisplay targetDisp = (TradeDisplay) target.findDisplay(TradeDisplay.class);

        if(userDisp != null && !userDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You're in a trade already!");
            
        } else if(targetDisp != null && !targetDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "That user is in a trade already!");

        } else if(user.getUserId().equals(target.getUserId())) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You can't trade with yourself!");

        } else {
            userDisp = (TradeDisplay) user.addDisplay(new TradeDisplay(event, user, target, true));

            target.addDisplay(new TradeDisplay(event, target, user, false));

            JDAUtils.sendDynamicEmbed(event, userDisp, user, target, new User[]{user, target}, null, false);
        }
    }

    public static void offerCard(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        TradeDisplay userDisp = (TradeDisplay) user.findDisplay(TradeDisplay.class);

        if(userDisp == null || userDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't started a trade yet!");

        } else {
            User target = User.findTargetUser(event, userDisp.getTargetId());
            TradeDisplay targetDisp = (TradeDisplay) target.findDisplay(TradeDisplay.class);

            if(userDisp.getHasAccepted() || targetDisp.getHasAccepted()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Both players must unaccept first!");

            } else {
                try {
                    int index = cardNum.getAsInt() - 1;
                    CardContainer offerFromCollection = user.getCardContainers().get(index);
                    String cardTitle = offerFromCollection.getCard().findCardTitle(false);

                    if(userDisp.getOffers().size() >= 5) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, all your trade slots are full!");

                    } else if(!isValidOffer(userDisp.getOffers(), offerFromCollection)) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You can't offer any more of that card!");

                    } else {
                        JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.DITTO, ui.getUserPing() + " offers **" + cardTitle + "**");
                    }
                    
                } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                    JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
                }
            }
        }
    }

    public static void unofferCard(SlashCommandInteractionEvent event) {
        OptionMapping tradeNum = event.getOption("trade-number");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        TradeDisplay userDisp = (TradeDisplay) user.findDisplay(TradeDisplay.class);

        if(userDisp == null || userDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't started a trade yet!");

        } else {
            User target = User.findTargetUser(event, userDisp.getTargetId());
            TradeDisplay targetDisp = (TradeDisplay) target.findDisplay(TradeDisplay.class);

            if(userDisp.getHasAccepted() || targetDisp.getHasAccepted()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Both players must unaccept first!");

            } else {
                try {
                    int index = tradeNum.getAsInt() - 1;
                    CardContainer offerFromDisplay = userDisp.getOffers().get(index);
                    String cardTitle = offerFromDisplay.getCard().findCardTitle(false);

                    if(offerFromDisplay.getCardQuantity() > 1)
                        offerFromDisplay.minusCardQuantity();
                    else
                        userDisp.getOffers().remove(index);

                    JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.DITTO, ui.getUserPing() + " took back **" + cardTitle + "**");

                } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                    JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
                }
            }
        }
    }

    public static void acceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        TradeDisplay userDisp = (TradeDisplay) user.findDisplay(TradeDisplay.class);

        if(userDisp == null || userDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't started a trade yet!");

        } else {
            User target = User.findTargetUser(event, userDisp.getTargetId());
            TradeDisplay targetDisp = (TradeDisplay) target.findDisplay(TradeDisplay.class);

            if(userDisp.getHasAccepted()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You've already accepted the trade!");
                
            } else {
                userDisp.setHasAccepted(true);

                if(!targetDisp.getHasAccepted()) {
                    JDAUtils.sendMessage(event, user.getGameColor(), "✅", ui.getUserPing()
                    + " accepted the trade! Waiting for the other player...");

                } else {
                    String msg = "";

                    msg += ui.getUserPing() + " accepted and completed the trade!";
                    
                    tradeCards(user, userDisp.getOffers(), targetDisp.getOffers());
                    tradeCards(target, targetDisp.getOffers(), userDisp.getOffers());

                    userDisp.setHasCompleted(true);
                    targetDisp.setHasCompleted(true);

                    JDAUtils.sendMessage(event, user.getGameColor(), "✅", msg);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            }
        }
    }

    public static void unacceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        TradeDisplay userDisp = (TradeDisplay) user.findDisplay(TradeDisplay.class);

        if(userDisp == null || userDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't started a trade yet!");

        } else {
            if(!userDisp.getHasAccepted()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You've already unaccepted the trade!");

            } else {
                userDisp.setHasAccepted(false);

                JDAUtils.sendMessage(event, user.getGameColor(), "⏳", ui.getUserPing()
                + " needs more time to decide!");
            }
        }
    }

    public static void rejectOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        TradeDisplay userDisp = (TradeDisplay) user.findDisplay(TradeDisplay.class);

        if(userDisp == null || userDisp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't started a trade yet!");

        } else {
            User target = User.findTargetUser(event, userDisp.getTargetId());
            TradeDisplay targetDisp = (TradeDisplay) target.findDisplay(TradeDisplay.class);

            userDisp.setHasRejected(true);
            
            userDisp.setHasCompleted(true);
            targetDisp.setHasCompleted(true);

            JDAUtils.sendMessage(event, user.getGameColor(), "🛑", ui.getUserPing() 
            + " rejected the trade! Maybe next time...");
        }
    }

    private static boolean isValidOffer(ArrayList<CardContainer> offers, CardContainer cc) {
        for(CardContainer offer : offers) {
            if(offer.getCard().getCardId().equals(cc.getCard().getCardId())) {
                if(offer.getCardQuantity() >= cc.getCardQuantity()) {
                    return false;

                } else {
                    offer.addCardQuantity();
                    return true;
                }
            }
        }
        offers.add(new CardContainer(cc.getCard(), cc.getCardNum(), cc.getIsSellable()));
        return true;
    }

    private static void tradeCards(User user, ArrayList<CardContainer> gives, ArrayList<CardContainer> receives) {
        for(CardContainer give : gives) {
            for(int i = 0; i < user.getCardContainers().size(); i++) {
                CardContainer cc = user.getCardContainers().get(i);
    
                if(give.getCard().getCardId().equals(cc.getCard().getCardId())) {
                    if(cc.getCardQuantity() > give.getCardQuantity()) {
                        cc.setCardQuantity(cc.getCardQuantity() - give.getCardQuantity());

                    } else {
                        user.getCardContainers().remove(i);
                    }
                }
            }
        }
        for(CardContainer receive : receives) {
            for(int i = 0; i < receive.getCardQuantity(); i++) {
                user.addSingleCard(receive.getCard(), false);
            }
        }
    }
}
