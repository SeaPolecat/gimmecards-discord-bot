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
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        TradeDisplay disp = new TradeDisplay();
        //
        OptionMapping user_ = event.getOption("user");

        user.addDisplay(disp);

        if(user_ == null) { return; }

        if(disp.tradeExists(user.getUserId())) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You're in a trade already!");
            
        } else {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            if(disp.tradeExists(mention.getUserId())) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "That user is in a trade already!");

            } else if(mention.getUserId().equals(user.getUserId())) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You can't trade with yourself!");

            } else {
                disp.setUser1(user);
                disp.setUser2(mention);
                disp.setUserInfo1(new UserInfo(event));
                disp.setUserInfo2(new UserInfo(mention, event));

                JDAUtils.sendDynamicEmbed(event, user, server, disp, -1);
            }
        }
    }

    public static void offerCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = (TradeDisplay) user.findDisplay(new TradeDisplay());
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            if(disp.oneAccepted()) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Both players must unaccept first!");

            } else {
                try {
                    int index = cardNum.getAsInt() - 1;
                    ArrayList<CardContainer> offers = disp.getOffers(user.getUserId());
                    CardContainer cc = user.getCardContainers().get(index);
                    Card card = cc.getCard();
                    String cardTitle = card.findCardTitle(false);

                    if(offers.size() >= 5) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, all your trade slots are full!");

                    } else if(!isValidOffer(offers, cc)) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You can't offer any more of that card!");

                    } else {
                        JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.ditto, FormatUtils.formatName(event) + " offers **" + cardTitle + "**");
                    }
                } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
                }
            }
            
        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You haven't started a trade yet!");
        }
    }

    public static void unofferCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = (TradeDisplay) user.findDisplay(new TradeDisplay());
        //
        OptionMapping tradeNum = event.getOption("trade-number");

        if(tradeNum == null) { return; }

        try {
            if(disp.oneAccepted()) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Both players must unaccept first!");

            } else {
                try {
                    int index = tradeNum.getAsInt() - 1;
                    ArrayList<CardContainer> offers = disp.getOffers(user.getUserId());
                    CardContainer offer = offers.get(index);
                    Card card = offer.getCard();
                    String cardTitle = card.findCardTitle(false);

                    if(offer.getCardQuantity() > 1) {
                        offer.minusCardQuantity();
                    } else {
                        offers.remove(index);
                    }
                    JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.ditto, FormatUtils.formatName(event) + " took back **" + cardTitle + "**");

                } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
                }
            }

        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You haven't started a trade yet!");
        }
    }

    public static void acceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = (TradeDisplay) user.findDisplay(new TradeDisplay());

        try {
            if(disp.getAccept(user.getUserId())) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You've already accepted the trade!");
                
            } else {
                disp.setAccept(user.getUserId(), true);

                if(!disp.bothAccepted()) {
                    JDAUtils.sendMessage(event, user.getGameColor(), "✅", FormatUtils.formatName(event) 
                    + " accepted the trade! Waiting for the other player...");

                } else {
                    String msg = "";

                    msg += FormatUtils.formatName(event) + " accepted and completed the trade!";
                    
                    tradeCards(disp.getUser1(), disp.getOffers1(), disp.getOffers2());
                    tradeCards(disp.getUser2(), disp.getOffers2(), disp.getOffers1());

                    disp.removeTradeDisplay();

                    JDAUtils.sendMessage(event, user.getGameColor(), "✅", msg);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            }

        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You haven't started a trade yet!");
        }
    }

    public static void unacceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = (TradeDisplay) user.findDisplay(new TradeDisplay());

        try {
            if(!disp.getAccept(user.getUserId())) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You've already unaccepted the trade!");

            } else {
                disp.setAccept(user.getUserId(), false);

                JDAUtils.sendMessage(event, user.getGameColor(), "⏳", FormatUtils.formatName(event) 
                + " needs more time to decide!");
            }

        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You haven't started a trade yet!");
        }
    }

    public static void rejectOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = (TradeDisplay) user.findDisplay(new TradeDisplay());

        try {
            disp.setReject(user.getUserId(), true);
            
            disp.removeTradeDisplay();

            JDAUtils.sendMessage(event, user.getGameColor(), "🛑", FormatUtils.formatName(event) 
            + " rejected the trade! Maybe next time...");

        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You haven't started a trade yet!");
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
