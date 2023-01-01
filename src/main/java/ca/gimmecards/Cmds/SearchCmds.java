package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class SearchCmds extends Cmds {

    public static void searchCards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        SearchDisplay disp = new SearchDisplay(user.getUserId()).findDisplay();
        //
        ArrayList<Data> searchedCards = new ArrayList<Data>();
        OptionMapping option = event.getOption("option");
        OptionMapping keywords = event.getOption("keywords");

        if(option == null || keywords == null) { return; }

        if(option.getAsString().equalsIgnoreCase("card")) {
            searchedCards = searchCards(user, "card", keywords.getAsString());

        } else if(option.getAsString().equalsIgnoreCase("pack")) {
            searchedCards = searchCards(user, "pack", keywords.getAsString());

        } else if(option.getAsString().equalsIgnoreCase("rarity")) {
            searchedCards = searchCards(user, "rarity", keywords.getAsString());
        }

        if(searchedCards.size() < 1) {
            JDA.sendMessage(event, red_, "❌", "Sorry, your search had no results!");

        } else {
            disp.setKey(keywords.getAsString());
            disp.setSearchedCards(searchedCards);
            
            JDA.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }

    public static void viewAnyCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        //
        OptionMapping cardId = event.getOption("card-id");

        if(cardId == null) { return; }

        try {
            Data data = Card.findDataById(cardId.getAsString());
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
