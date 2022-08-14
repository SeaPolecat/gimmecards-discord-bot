package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class SearchCmds extends Cmds {

    public static void searchCards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        SearchDisplay disp = new SearchDisplay(user.getUserId()).findDisplay();

        try {
            String key = "";
            ArrayList<Data> searchedCards = new ArrayList<Data>();

            if(args.length < 3) {
                Integer.parseInt("$");
            }
            for(int i = 2; i < args.length; i++) {
                key += args[i] + " ";
            }
            key = key.trim();

            if(args[1].equalsIgnoreCase("card")) {
                searchedCards = Card.searchCards(user, "card", key);

            } else if(args[1].equalsIgnoreCase("pack")) {
                searchedCards = Card.searchCards(user, "pack", key);

            } else if(args[1].equalsIgnoreCase("rarity")) {
                searchedCards = Card.searchCards(user, "rarity", key);

            } else {
                Integer.parseInt("$");
            }

            if(searchedCards.size() < 1) {
                JDA.sendMessage(event, red_, "❌", "Sorry, your search had no results!");
    
            } else {
                disp.setKey(key);
                disp.setSearchedCards(searchedCards);
                
                JDA.sendDynamicEmbed(event, user, null, disp, 1);
            }
        } catch(NumberFormatException e) {
            Server server = Server.findServer(event);
            EmbedBuilder embed = new EmbedBuilder();
            String desc = "";

            desc += "**Format**\n";
            desc += UX.formatCmd(server, "search (option) (keyword)") + "\n\n";

            desc += "**Option**\n";
            desc += "`card/pack/rarity`\n\n";

            desc += "**Keyword**\n";
            desc += "Can be anything you want!";

            embed.setTitle(eevee_ + " Searching Help " + eevee_);
            embed.setDescription(desc);
            embed.setColor(help_);
            JDA.sendEmbed(event, embed);
            embed.clear();
        }
    }

    public static void viewAnyCard(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);

        try {
            Data data = findDataById(args[1]);
            String footer = "Card ID: " + data.getCardId();

            Display.displayCard(event, user, data, footer);

        } catch(NullPointerException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    private static Data findDataById(String cardId) {
        for(Data set : Data.sets) {
            Data data = searchSet(set, cardId, false);

            if(data != null) {
                return data;
            }
        }
        for(Data set : Data.oldSets) {
            Data data = searchSet(set, cardId, false);

            if(data != null) {
                return data;
            }
        }
        for(Data set : Data.rareSets) {
            Data data = searchSet(set, cardId, true);

            if(data != null) {
                return data;
            }
        }
        for(Data set : Data.promoSets) {
            Data data = searchSet(set, cardId, true);

            if(data != null) {
                return data;
            }
        }
        for(Data data : CustomCards.customs) {
            if(data.getCardId().equalsIgnoreCase(cardId)) {
                return data;
            }
        }
        return null;
    }

    private static Data searchSet(Data set, String cardId, boolean isSpec) {
        if(!isSpec) {
            for(Data data : set.getCommons()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
            for(Data data : set.getUncommons()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
            for(Data data : set.getRares()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
            for(Data data : set.getShinies()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }

        } else {
            for(Data data : set.getSpecs()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
        }
        return null;
    }
}
