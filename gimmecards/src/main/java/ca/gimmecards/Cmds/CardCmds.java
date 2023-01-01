package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CardCmds extends Cmds {

    public static void viewCards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        CardDisplay disp = new CardDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping page = event.getOption("page");

        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, red_, "❌", "You don't have any cards yet!");

        } else {
            if(page != null) {
                try {
                    JDA.sendDynamicEmbed(event, user, null, disp, page.getAsInt());
                } catch(IndexOutOfBoundsException e) {
                    JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that page...");
                }
            } else {
                JDA.sendDynamicEmbed(event, user, null, disp, 1);
            }
        }
    }

    public static void favouriteCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            int index = cardNum.getAsInt() - 1;
            Card card = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(card.getData(), false);

            if(card.getIsFav()) {
                JDA.sendMessage(event, red_, "❌", "That card is already in your favorites!");

            } else {
                card.setIsFav(true);
    
                JDA.sendMessage(event, user.getGameColor(), "❤", "Added **" + cardTitle + "** to your favorites!");
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void unfavouriteCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            int index = cardNum.getAsInt() - 1;
            Card c = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(c.getData(), false);

            if(!c.getIsFav()) {
                JDA.sendMessage(event, red_, "❌", "That card is already non-favorited!");
                
            } else {
                c.setIsFav(false);
    
                JDA.sendMessage(event, user.getGameColor(), "💔", "Removed **" + cardTitle + "** from your favorites!");
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void favouriteAll(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        boolean exists = false;

        for(Card c : user.getCards()) {
            if(!c.getIsFav() && Check.isShinyCard(c.getData())) {
                exists = true;
                c.setIsFav(true);
            }
        }
        if(!exists) {
            JDA.sendMessage(event, red_, "❌", "Sorry, you have no shiny cards left to favorite!");

        } else {
            JDA.sendMessage(event, user.getGameColor(), "💞", "Added all your shiny cards to your favorites!");
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void sortCards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping option = event.getOption("option");
        OptionMapping order = event.getOption("order");

        if(option == null || order == null) { return; }

        if(option.getAsString().equalsIgnoreCase("alphabetical")) {
            user.setSortMethod("alphabetical");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setSortIncreasing(false);
            }

        } else if(option.getAsString().equalsIgnoreCase("xp")) {
            user.setSortMethod("xp");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setSortIncreasing(false);
            }

        } else if(option.getAsString().equalsIgnoreCase("quantity")) {
            user.setSortMethod("quantity");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setSortIncreasing(false);
            }

        } else if(option.getAsString().equalsIgnoreCase("newest")) {
            user.setSortMethod("newest");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setSortIncreasing(false);
            }

        }
        Card.sortCards(user, user.getSortMethod(), user.getSortIncreasing());

        JDA.sendMessage(event, user.getGameColor(), "🎴", "Your cards have been sorted!");
        try { User.saveUsers(); } catch(Exception e) {}
    }
}
