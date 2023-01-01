package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
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
            JDA.sendMessage(event, red_, "❌", "You're in a trade already!");
            
        } else {
            User mention = User.findOtherUser(event, user_.getAsUser().getId());

            if(disp.tradeExists(mention.getUserId())) {
                JDA.sendMessage(event, red_, "❌", "That user is in a trade already!");

            } else if(mention.getUserId().equals(user.getUserId())) {
                JDA.sendMessage(event, red_, "❌", "You can't trade with yourself!");

            } else {
                disp.setUser1(user);
                disp.setUser2(mention);
                disp.setUserInfo1(new UserInfo(event));
                disp.setUserInfo2(new UserInfo(mention, event));

                JDA.sendDynamicEmbed(event, user, server, disp, -1);
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
            JDA.sendMessage(event, red_, "❌", "You haven't started a trade yet!");

        } else if(disp.oneAccepted()) {
            JDA.sendMessage(event, red_, "❌", "Both players must unaccept first!");

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                ArrayList<Card> offers = disp.getOffers(user.getUserId());
                Card card = user.getCards().get(index);
                Data data = card.getData();
                String cardTitle = UX.findCardTitle(data, false);

                if(offers.size() >= 5) {
                    JDA.sendMessage(event, red_, "❌", "Sorry, all your trade slots are full!");

                } else if(!isValidOffer(offers, card)) {
                    JDA.sendMessage(event, red_, "❌", "You can't offer any more of that card!");

                } else {
                    if(Check.isSellable(data)) {
                        disp.addTax(user.getUserId(), (int)(data.getCardPrice() * 0.25));
                    }

                    JDA.sendMessage(event, user.getGameColor(), ditto_, UX.formatNick(event) + " offers **" + cardTitle + "**");
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
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
            JDA.sendMessage(event, red_, "❌", "You haven't started a trade yet!");

        } else if(disp.oneAccepted()) {
            JDA.sendMessage(event, red_, "❌", "Both players must unaccept first!");

        } else {
            try {
                int index = tradeNum.getAsInt() - 1;
                ArrayList<Card> offers = disp.getOffers(user.getUserId());
                Card offer = offers.get(index);
                Data data = offer.getData();
                String cardTitle = UX.findCardTitle(data, false);

                if(offer.getCardQuantity() > 1) {
                    offer.minusCardQuantity();
                } else {
                    offers.remove(index);
                }
                if(Check.isSellable(data)) {
                    disp.addTax(user.getUserId(), -(int)(data.getCardPrice() * 0.25));
                }

                JDA.sendMessage(event, user.getGameColor(), ditto_, UX.formatNick(event) + " took back **" + cardTitle + "**");

            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }

    public static void acceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(disp.getUserId().isEmpty()) {
            JDA.sendMessage(event, red_, "❌", "You haven't started a trade yet!");

        } else if(disp.getAccept(user.getUserId())) {
            JDA.sendMessage(event, red_, "❌", "You've already accepted the trade!");

        } else if(disp.getUser(user.getUserId()).getCredits() < disp.getTax(user.getUserId())) {
            JDA.sendMessage(event, red_, "❌", "Sorry, you don't have enough " + credits_ + " **Credits**");

        } else {
            disp.setAccept(user.getUserId(), true);

            if(!disp.bothAccepted()) {
                JDA.sendMessage(event, user.getGameColor(), "✅", UX.formatNick(event) 
                + " accepted the trade! Waiting for the other player...");

            } else {
                String msg = "";

                msg += UX.formatNick(event) + " accepted and completed the trade!";
                msg += user.updateCredits(-disp.getTax(user.getUserId()), true) + "\n\n";

                if(disp.isUser1(user.getUserId())) {
                    msg += UX.formatNick(disp.getUser2(), event) + "'s Trading Fee:";
                    msg += disp.getUser2().updateCredits(-disp.getTax2(), true) + "\n";
                } else {
                    msg += UX.formatNick(disp.getUser1(), event) + "'s Trading Fee:";
                    msg += disp.getUser1().updateCredits(-disp.getTax1(), true) + "\n";
                }
                tradeCards(disp.getUser1(), disp.getOffers1(), disp.getOffers2());
                tradeCards(disp.getUser2(), disp.getOffers2(), disp.getOffers1());

                disp.removeTradeDisplay();

                JDA.sendMessage(event, user.getGameColor(), "✅", msg);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        }
    }

    public static void unacceptOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(disp.getUserId().isEmpty()) {
            JDA.sendMessage(event, red_, "❌", "You haven't started a trade yet!");

        } else if(!disp.getAccept(user.getUserId())) {
            JDA.sendMessage(event, red_, "❌", "You've already unaccepted the trade!");

        } else {
            disp.setAccept(user.getUserId(), false);

            JDA.sendMessage(event, user.getGameColor(), "⏳", UX.formatNick(event) 
            + " needs more time to decide!");
        }
    }

    public static void rejectOffer(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(disp.getUserId().isEmpty()) {
            JDA.sendMessage(event, red_, "❌", "You haven't started a trade yet!");

        } else {
            disp.setReject(user.getUserId(), true);
            
            disp.removeTradeDisplay();

            JDA.sendMessage(event, user.getGameColor(), "🛑", UX.formatNick(event) 
            + " rejected the trade! Maybe next time...");
        }
    }

    private static boolean isValidOffer(ArrayList<Card> offers, Card card) {
        for(Card offer : offers) {
            if(offer.getData().getCardId().equals(card.getData().getCardId())) {
                if(offer.getCardQuantity() >= card.getCardQuantity()) {
                    return false;

                } else {
                    offer.addCardQuantity();
                    return true;
                }
            }
        }
        offers.add(new Card(card.getData(), card.getCardNum(), card.getSellable()));
        return true;
    }

    private static void tradeCards(User user, ArrayList<Card> gives, ArrayList<Card> receives) {
        for(Card give : gives) {
            for(int i = 0; i < user.getCards().size(); i++) {
                Card card = user.getCards().get(i);
    
                if(give.getData().getCardId().equals(card.getData().getCardId())) {
                    if(card.getCardQuantity() > give.getCardQuantity()) {
                        card.setCardQuantity(card.getCardQuantity() - give.getCardQuantity());

                    } else {
                        user.getCards().remove(i);
                    }
                }
            }
        }
        for(Card receive : receives) {
            for(int i = 0; i < receive.getCardQuantity(); i++) {
                Card.addSingleCard(user, receive.getData(), false);
            }
        }
    }
}
