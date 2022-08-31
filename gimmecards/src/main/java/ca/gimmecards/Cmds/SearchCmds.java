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
                searchedCards = searchCards(user, "card", key);

            } else if(args[1].equalsIgnoreCase("pack")) {
                searchedCards = searchCards(user, "pack", key);

            } else if(args[1].equalsIgnoreCase("rarity")) {
                searchedCards = searchCards(user, "rarity", key);

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
        UserInfo ui = new UserInfo(event);

        try {
            Data data = Card.findDataById(args[1]);
            String footer = "Card ID: " + data.getCardId();

            Display.displayCard(event, user, ui, data, "", footer, false);

        } catch(NullPointerException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    private static ArrayList<Data> searchCards(User user, String sortMethod, String key) {
        ArrayList<Data> searchedCards = new ArrayList<Data>();

        for(Data set : Data.sets) {
            crawlSet(searchedCards, set, sortMethod, key);
        }
        for(Data oldSet : Data.oldSets) {
            crawlSet(searchedCards, oldSet, sortMethod, key);
        }
        crawlSpecSets(searchedCards, sortMethod, key);

        return searchedCards;
    }

    private static String findName(Data data, String sortMethod) {
        if(sortMethod.equalsIgnoreCase("card")) {
            return data.getCardName();
        } else if(sortMethod.equalsIgnoreCase("pack")) {
            return data.getSetName();
        }
        return data.getCardRarity();
    }

    private static void crawlSet(ArrayList<Data> searchedCards, Data set, String sortMethod, String key) {
        for(Data data : set.getCommons()) {
            String name = findName(data, sortMethod);

            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
        for(Data data : set.getUncommons()) {
            String name = findName(data, sortMethod);

            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
        for(Data data : set.getRares()) {
            String name = findName(data, sortMethod);

            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
        for(Data data : set.getShinies()) {
            String name = findName(data, sortMethod);
            
            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
    }

    private static void crawlSpecSets(ArrayList<Data> searchedCards, String sortMethod, String key) {
        for(Data specSet : Data.rareSets) {
            for(Data data : specSet.getSpecs()) {
                String name = findName(data, sortMethod);

                if(name.toLowerCase().contains(key.toLowerCase())) {
                    searchedCards.add(data);
                }
            }
        }
        for(Data specSet : Data.promoSets) {
            for(Data data : specSet.getSpecs()) {
                String name = findName(data, sortMethod);

                if(name.toLowerCase().contains(key.toLowerCase())) {
                    searchedCards.add(data);
                }
            }
        }
        for(Data data : CustomCards.customs) {
            String name = findName(data, sortMethod);
            
            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
    }
}
