package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class SearchCmds {

    public static void searchCards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        SearchDisplay disp = new SearchDisplay(user.getUserId()).findDisplay();
        //
        ArrayList<Card> searchedCards = new ArrayList<Card>();
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
            GameManager.sendMessage(event, IColors.red, "❌", "Sorry, your search had no results!");

        } else {
            disp.setKey(keywords.getAsString());
            disp.setSearchedCards(searchedCards);
            
            GameManager.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }

    public static void viewAnyCard(SlashCommandInteractionEvent event) {
        UserInfo ui = new UserInfo(event);
        //
        OptionMapping cardId = event.getOption("card-id");

        if(cardId == null) { return; }

        try {
            Card card = Card.findCardById(cardId.getAsString());
            String footer = "Card ID: " + card.getCardId();

            card.displayCard(event, ui, "", footer, false);

        } catch(NullPointerException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }

    private static ArrayList<Card> searchCards(User user, String sortMethod, String key) {
        ArrayList<Card> searchedCards = new ArrayList<Card>();

        for(CardSet set : CardSet.sets) {
            crawlSet(searchedCards, set, sortMethod, key);
        }
        for(CardSet oldSet : CardSet.oldSets) {
            crawlSet(searchedCards, oldSet, sortMethod, key);
        }
        crawlSpecSets(searchedCards, sortMethod, key);

        return searchedCards;
    }

    private static String findName(Card card, String sortMethod) {
        if(sortMethod.equalsIgnoreCase("card")) {
            return card.getCardName();
        } else if(sortMethod.equalsIgnoreCase("pack")) {
            return card.getSetName();
        }
        return card.getCardRarity();
    }

    private static void crawlSet(ArrayList<Card> searchedCards, CardSet set, String sortMethod, String key) {
        for(Card card : set.getCommons()) {
            String name = findName(card, sortMethod);

            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getUncommons()) {
            String name = findName(card, sortMethod);

            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getRares()) {
            String name = findName(card, sortMethod);

            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getShinies()) {
            String name = findName(card, sortMethod);
            
            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(card);
            }
        }
    }

    private static void crawlSpecSets(ArrayList<Card> searchedCards, String sortMethod, String key) {
        for(CardSet specSet : CardSet.rareSets) {
            for(Card card : specSet.getSpecials()) {
                String name = findName(card, sortMethod);

                if(name.toLowerCase().contains(key.toLowerCase())) {
                    searchedCards.add(card);
                }
            }
        }
        for(CardSet specSet : CardSet.promoSets) {
            for(Card card : specSet.getSpecials()) {
                String name = findName(card, sortMethod);

                if(name.toLowerCase().contains(key.toLowerCase())) {
                    searchedCards.add(card);
                }
            }
        }
        for(Card card : ICustomCards.customs) {
            String name = findName(card, sortMethod);
            
            if(name.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(card);
            }
        }
    }
}
