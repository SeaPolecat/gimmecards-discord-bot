package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class CardCmds extends Cmds {

    public static void viewCards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        CardDisplay disp = CardDisplay.findCardDisplay(user.getUserId());

        if(args.length < 2) {
            if(user.getCards().size() < 1) {
                Rest.sendMessage(event, jigglypuff_ + " You don't have any cards yet!");
    
            } else {
                Rest.sendDynamicEmbed(event, user, null, disp, 1);
            }
        }
    }

    public static void favouriteCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Card c = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(c.getData(), false);

            c.setIsFav(true);
            State.updateCardDisplay(event, user);
            State.updateFavDisplay(event, user);

            Rest.sendMessage(event, "❤ Added **" + cardTitle + "** to your favourites!");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }

    public static void unfavouriteCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Card c = user.getCards().get(index);
            String cardTitle = UX.findCardTitle(c.getData(), false);

            c.setIsFav(false);
            State.updateCardDisplay(event, user);
            State.updateFavDisplay(event, user);

            Rest.sendMessage(event, "💔 Removed **" + cardTitle + "** from your favourites!");
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
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
            State.updateCardDisplay(event, user);
            State.updateFavDisplay(event, user);

            Rest.sendMessage(event, ditto_ + " Your cards have been sorted!");
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
            embed.setColor(0xE9BB7A);
            Rest.sendEmbed(event, embed);
            embed.clear();
        }
    }
}
