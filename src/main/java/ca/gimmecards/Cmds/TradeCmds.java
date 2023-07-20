package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class TradeCmds extends Cmds {
    
    public static void sendTrade(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        if(disp.tradeExists(user.getUserId())) {
            GameManager.sendMessage(event, IColors.red, "❌", "You're in a trade already!");
            
        } else {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            if(disp.tradeExists(mention.getUserId())) {
                GameManager.sendMessage(event, IColors.red, "❌", "That user is in a trade already!");

            } else if(mention.getUserId().equals(user.getUserId())) {
                GameManager.sendMessage(event, IColors.red, "❌", "You can't trade with yourself!");

            } else {
                disp.setUser1(user);
                disp.setUser2(mention);
                disp.setUserInfo1(new UserInfo(event));
                disp.setUserInfo2(new UserInfo(mention, event));

                GameManager.sendDynamicEmbed(event, user, server, disp, -1);
            }
        }
    }

    public static void offerCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        if(disp.getUserId().isEmpty()) {
            GameManager.sendMessage(event, IColors.red, "❌", "You haven't started a trade yet!");

        } else if(disp.oneAccepted()) {
            GameManager.sendMessage(event, IColors.red, "❌", "Both players must unaccept first!");

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                ArrayList<CardContainer> offers = disp.getOffers(user.getUserId());
                CardContainer cc = user.getCardContainers().get(index);
                Card card = cc.getCard();
                String cardTitle = card.findCardTitle(false);

                if(offers.size() >= 5) {
                    GameManager.sendMessage(event, IColors.red, "❌", "Sorry, all your trade slots are full!");

                } else if(!isValidOffer(offers, cc)) {
                    GameManager.sendMessage(event, IColors.red, "❌", "You can't offer any more of that card!");

                } else {
                    if(card.isCardSellable()) {
                        disp.addTax(user.getUserId(), (int)(card.getCardPrice() * 0.25));
                    }

                    GameManager.sendMessage(event, user.getGameColor(), IEmotes.ditto, GameManager.formatName(event) + " offers **" + cardTitle + "**");
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }

    public static void unofferCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping tradeNum = event.getOption("trade-number");

        if(tradeNum == null) { return; }

        if(disp.getUserId().isEmpty()) {
            GameManager.sendMessage(event, IColors.red, "❌", "You haven't started a trade yet!");

        } else if(disp.oneAccepted()) {
            GameManager.sendMessage(event, IColors.red, "❌", "Both players must unaccept first!");

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
                if(card.isCardSellable()) {
                    disp.addTax(user.getUserId(), -(int)(card.getCardPrice() * 0.25));
                }

                GameManager.sendMessage(event, user.getGameColor(), IEmotes.ditto, GameManager.formatName(event) + " took back **" + cardTitle + "**");

            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }

    public static void acceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(disp.getUserId().isEmpty()) {
            GameManager.sendMessage(event, IColors.red, "❌", "You haven't started a trade yet!");

        } else if(disp.getAccept(user.getUserId())) {
            GameManager.sendMessage(event, IColors.red, "❌", "You've already accepted the trade!");

        } else if(disp.getUser(user.getUserId()).getCredits() < disp.getTax(user.getUserId())) {
            GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you don't have enough " + IEmotes.credits + " **Credits**");

        } else {
            disp.setAccept(user.getUserId(), true);

            if(!disp.bothAccepted()) {
                GameManager.sendMessage(event, user.getGameColor(), "✅", GameManager.formatName(event) 
                + " accepted the trade! Waiting for the other player...");

            } else {
                String msg = "";

                msg += GameManager.formatName(event) + " accepted and completed the trade!";
                msg += user.updateCredits(-disp.getTax(user.getUserId()), true) + "\n\n";

                if(disp.isUser1(user.getUserId())) {
                    msg += GameManager.formatName(disp.getUser2(), event) + "'s Trading Fee:";
                    msg += disp.getUser2().updateCredits(-disp.getTax2(), true) + "\n";
                } else {
                    msg += GameManager.formatName(disp.getUser1(), event) + "'s Trading Fee:";
                    msg += disp.getUser1().updateCredits(-disp.getTax1(), true) + "\n";
                }
                tradeCards(disp.getUser1(), disp.getOffers1(), disp.getOffers2());
                tradeCards(disp.getUser2(), disp.getOffers2(), disp.getOffers1());

                disp.removeTradeDisplay();

                GameManager.sendMessage(event, user.getGameColor(), "✅", msg);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    public static void unacceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(disp.getUserId().isEmpty()) {
            GameManager.sendMessage(event, IColors.red, "❌", "You haven't started a trade yet!");

        } else if(!disp.getAccept(user.getUserId())) {
            GameManager.sendMessage(event, IColors.red, "❌", "You've already unaccepted the trade!");

        } else {
            disp.setAccept(user.getUserId(), false);

            GameManager.sendMessage(event, user.getGameColor(), "⏳", GameManager.formatName(event) 
            + " needs more time to decide!");
        }
    }

    public static void rejectOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(disp.getUserId().isEmpty()) {
            GameManager.sendMessage(event, IColors.red, "❌", "You haven't started a trade yet!");

        } else {
            disp.setReject(user.getUserId(), true);
            
            disp.removeTradeDisplay();

            GameManager.sendMessage(event, user.getGameColor(), "🛑", GameManager.formatName(event) 
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
