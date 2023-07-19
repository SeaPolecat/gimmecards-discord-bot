package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CollectionCmds extends Cmds {

    public static void viewCards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        CardDisplay disp = new CardDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping page = event.getOption("page");

        if(user.getCardContainers().size() < 1) {
            GameObject.sendMessage(event, red_, "❌", "You don't have any cards yet!");

        } else {
            if(page != null) {
                try {
                    GameObject.sendDynamicEmbed(event, user, null, disp, page.getAsInt());
                } catch(IndexOutOfBoundsException e) {
                    GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that page...");
                }
            } else {
                GameObject.sendDynamicEmbed(event, user, null, disp, 1);
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
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);

            if(cc.getIsFav()) {
                GameObject.sendMessage(event, red_, "❌", "That card is already in your favorites!");

            } else {
                cc.setIsFav(true);
    
                GameObject.sendMessage(event, user.getGameColor(), "❤", "Added **" + cardTitle + "** to your favorites!");
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void unfavouriteCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            int index = cardNum.getAsInt() - 1;
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);

            if(!cc.getIsFav()) {
                GameObject.sendMessage(event, red_, "❌", "That card is already non-favorited!");
                
            } else {
                cc.setIsFav(false);
    
                GameObject.sendMessage(event, user.getGameColor(), "💔", "Removed **" + cardTitle + "** from your favorites!");
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameObject.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void favouriteAll(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        boolean exists = false;

        for(CardContainer cc : user.getCardContainers()) {
            if(!cc.getIsFav() && cc.getCard().isShinyCard()) {
                exists = true;
                cc.setIsFav(true);
            }
        }
        if(!exists) {
            GameObject.sendMessage(event, red_, "❌", "Sorry, you have no shiny cards left to favorite!");

        } else {
            GameObject.sendMessage(event, user.getGameColor(), "💞", "Added all your shiny cards to your favorites!");
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
                user.setIsSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setIsSortIncreasing(false);
            }

        } else if(option.getAsString().equalsIgnoreCase("xp")) {
            user.setSortMethod("xp");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setIsSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setIsSortIncreasing(false);
            }

        } else if(option.getAsString().equalsIgnoreCase("quantity")) {
            user.setSortMethod("quantity");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setIsSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setIsSortIncreasing(false);
            }

        } else if(option.getAsString().equalsIgnoreCase("newest")) {
            user.setSortMethod("newest");

            if(order.getAsString().equalsIgnoreCase("increasing")) {
                user.setIsSortIncreasing(true);

            } else if(order.getAsString().equalsIgnoreCase("decreasing")) {
                user.setIsSortIncreasing(false);
            }

        }
        user.sortCards(user.getSortMethod(), user.getIsSortIncreasing());

        GameObject.sendMessage(event, user.getGameColor(), "🎴", "Your cards have been sorted!");
        try { User.saveUsers(); } catch(Exception e) {}
    }
}
