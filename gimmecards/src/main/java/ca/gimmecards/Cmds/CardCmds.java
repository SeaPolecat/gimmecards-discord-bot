package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class CardCmds extends Cmds {

    public static void viewCards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        CardDisplay disp = new CardDisplay(user.getUserId()).findDisplay();

        if(user.getCards().size() < 1) {
            JDA.sendMessage(event, red_, "❌", "You don't have any cards yet!");

        } else {
            if(args.length > 1) {
                try {
                    JDA.sendDynamicEmbed(event, user, null, disp, Integer.parseInt(args[1]));
                } catch(IndexOutOfBoundsException e) {
                    JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that page...");
                }
            } else {
                JDA.sendDynamicEmbed(event, user, null, disp, 1);
            }
        }
    }

    public static void favouriteCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Card card = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(card.getData(), false);

            if(card.getIsFav()) {
                JDA.sendMessage(event, red_, "❌", "That card is already in your favorites!");

            } else {
                card.setIsFav(true);

                Update.updateCardDisplay(event, user);
                Update.updateViewDisplay(event, user);
    
                JDA.sendMessage(event, user.getGameColor(), "❤", "Added **" + cardTitle + "** to your favorites!");
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void unfavouriteCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Card c = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(c.getData(), false);

            if(!c.getIsFav()) {
                JDA.sendMessage(event, red_, "❌", "That card is already non-favorited!");
                
            } else {
                c.setIsFav(false);

                Update.updateCardDisplay(event, user);
                Update.updateViewDisplay(event, user);
    
                JDA.sendMessage(event, user.getGameColor(), "💔", "Removed **" + cardTitle + "** from your favorites!");
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void favouriteAll(MessageReceivedEvent event) {
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
            Update.updateCardDisplay(event, user);
            Update.updateViewDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "💞", "Added all your shiny cards to your favorites!");
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void sortCards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            if(args.length < 3) {
                Integer.parseInt("$");
            }
            if(args[1].equalsIgnoreCase("alphabet")) {
                if(args[2].equalsIgnoreCase("increasing")) {
                    user.setSortMethod("alphabet");
                    user.setSortIncreasing(true);

                } else if(args[2].equalsIgnoreCase("decreasing")) {
                    user.setSortMethod("alphabet");
                    user.setSortIncreasing(false);

                } else {
                    Integer.parseInt("$");
                }

            } else if(args[1].equalsIgnoreCase("xp")) {
                if(args[2].equalsIgnoreCase("increasing")) {
                    user.setSortMethod("xp");
                    user.setSortIncreasing(true);

                } else if(args[2].equalsIgnoreCase("decreasing")) {
                    user.setSortMethod("xp");
                    user.setSortIncreasing(false);

                } else {
                    Integer.parseInt("$");
                }

            } else if(args[1].equalsIgnoreCase("quantity")) {
                if(args[2].equalsIgnoreCase("increasing")) {
                    user.setSortMethod("quantity");
                    user.setSortIncreasing(true);

                } else if(args[2].equalsIgnoreCase("decreasing")) {
                    user.setSortMethod("quantity");
                    user.setSortIncreasing(false);

                } else {
                    Integer.parseInt("$");
                }

            } else if(args[1].equalsIgnoreCase("newest")) {
                if(args[2].equalsIgnoreCase("increasing")) {
                    user.setSortMethod("newest");
                    user.setSortIncreasing(true);

                } else if(args[2].equalsIgnoreCase("decreasing")) {
                    user.setSortMethod("newest");
                    user.setSortIncreasing(false);

                } else {
                    Integer.parseInt("$");
                }

            } else {
                Integer.parseInt("$");
            }
            Card.sortCards(user, user.getSortMethod(), user.getSortIncreasing());

            Update.updateCardDisplay(event, user);

            JDA.sendMessage(event, user.getGameColor(), "🎴", "Your cards have been sorted!");
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException e) {
            Server server = Server.findServer(event);
            EmbedBuilder embed = new EmbedBuilder();
            String desc = "";

            desc += "**Format**\n";
            desc += UX.formatCmd(server, "sort (option) (order)") + "\n\n";

            desc += "**Option**\n";
            desc += "`alphabet/xp/quantity/newest`\n\n";

            desc += "**Order**\n";
            desc += "`increasing/decreasing`";

            embed.setTitle(eevee_ + " Sorting Help " + eevee_);
            embed.setDescription(desc);
            embed.setColor(help_);
            JDA.sendEmbed(event, embed);
            embed.clear();
        }
    }
}
