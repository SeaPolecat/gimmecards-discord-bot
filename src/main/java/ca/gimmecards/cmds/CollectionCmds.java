package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CollectionCmds {

    public static void viewCards(SlashCommandInteractionEvent event) {
        OptionMapping page = event.getOption("page");
        //
        User user = User.findUser(event);
        CollectionDisplay disp = null;

        if(page == null)
            disp = new CollectionDisplay(event, 1);
        else
            disp = new CollectionDisplay(event, page.getAsInt());

        Display.addDisplay(user, disp);

        if(user.getCardContainers().size() < 1) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You don't have any cards yet!");

        } else {
            try {
                JDAUtils.sendDynamicEmbed(event, disp, user, null, true);

            } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that page...");
            }
        }
    }

    public static void favouriteCard(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);

        try {
            int index = cardNum.getAsInt() - 1;
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);

            if(cc.getIsFav()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "That card is already in your favorites!");

            } else {
                cc.setIsFav(true);
    
                JDAUtils.sendMessage(event, user.getGameColor(), "❤", "Added **" + cardTitle + "** to your favorites!");
                try { DataUtils.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void unfavouriteCard(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);

        try {
            int index = cardNum.getAsInt() - 1;
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);

            if(!cc.getIsFav()) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "That card is already non-favorited!");
                
            } else {
                cc.setIsFav(false);
    
                JDAUtils.sendMessage(event, user.getGameColor(), "💔", "Removed **" + cardTitle + "** from your favorites!");
                try { DataUtils.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
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
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you have no shiny cards left to favorite!");

        } else {
            JDAUtils.sendMessage(event, user.getGameColor(), "💞", "Added all your shiny cards to your favorites!");
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void sortCards(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("option");
        OptionMapping order = event.getOption("order");
        //
        User user = User.findUser(event);

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
        user.sortCards();

        JDAUtils.sendMessage(event, user.getGameColor(), "🎴", "Your cards have been sorted!");
        try { DataUtils.saveUsers(); } catch(Exception e) {}
    }
}
