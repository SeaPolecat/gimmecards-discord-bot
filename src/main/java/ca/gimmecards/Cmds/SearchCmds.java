package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class SearchCmds {

    public static void searchCards(SlashCommandInteractionEvent event) {
        OptionMapping location = event.getOption("location");
        OptionMapping filter = event.getOption("filter");
        OptionMapping isExact = event.getOption("exact-match");
        OptionMapping keywords = event.getOption("keywords");
        //
        User user = User.findUser(event);
        SearchDisplay disp = (SearchDisplay) Display.addDisplay(user, new SearchDisplay(event));

        if(location.getAsString().equalsIgnoreCase("collection")) {
            if(user.getCardContainers().size() < 1) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You don't have any cards yet!");

            } else {
                disp.modifyVariables(
                    "collection", 
                    filter.getAsString(),
                    isExact.getAsBoolean(),
                    keywords.getAsString()
                );
                disp.setSearchedCards(searchCollection(user, disp));

                JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
            }

        } else if(location.getAsString().equalsIgnoreCase("pokedex")) {
            disp.modifyVariables(
                "pokedex",
                filter.getAsString(),
                isExact.getAsBoolean(),
                keywords.getAsString()
            );
            disp.setSearchedCards(searchPokedex(user, disp));

            JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
        }
    }

    public static void viewAnyCard(SlashCommandInteractionEvent event) {
        OptionMapping cardId = event.getOption("card-id");
        //
        UserInfo ui = new UserInfo(event);

        try {
            Card card = Card.findCardById(cardId.getAsString());
            String footer = "Card ID: " + card.getCardId();

            card.displayCard(event, ui, "", footer, false);

        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }

    private static String findName(Card card, SearchDisplay disp) {
        if(disp.getFilter().equalsIgnoreCase("card")) {
            return card.getCardName();

        } else if(disp.getFilter().equalsIgnoreCase("pack")) {
            return card.getSetName();
        }
        return card.getCardRarity();
    }

    private static ArrayList<Card> searchCollection(User user, SearchDisplay disp) {
        ArrayList<Card> searchedCards = new ArrayList<Card>();

        for(CardContainer cc : user.getCardContainers()) {
            Card card = cc.getCard();
            String name = findName(card, disp);
            boolean isExact = disp.getIsExact();
            String keywords = disp.getKeywords();

            if(isExact ? name.equalsIgnoreCase(keywords) : name.toLowerCase().contains(keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        return searchedCards;
    }

    private static ArrayList<Card> searchPokedex(User user, SearchDisplay disp) {
        ArrayList<Card> searchedCards = new ArrayList<Card>();

        for(CardSet set : CardSet.sets) {
            crawlSet(searchedCards, set, disp);
        }
        for(CardSet oldSet : CardSet.oldSets) {
            crawlSet(searchedCards, oldSet, disp);
        }
        crawlSpecialSets(searchedCards, disp);

        return searchedCards;
    }

    private static void crawlSet(ArrayList<Card> searchedCards, CardSet set, SearchDisplay disp) {
        boolean isExact = disp.getIsExact();
        String keywords = disp.getKeywords();

        for(Card card : set.getCommons()) {
            String name = findName(card, disp);
            
            if(isExact ? name.equalsIgnoreCase(disp.getKeywords()) : name.toLowerCase().contains(keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getUncommons()) {
            String name = findName(card, disp);

            if(isExact ? name.equalsIgnoreCase(disp.getKeywords()) : name.toLowerCase().contains(keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getRares()) {
            String name = findName(card, disp);

            if(isExact ? name.equalsIgnoreCase(disp.getKeywords()) : name.toLowerCase().contains(keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getShinies()) {
            String name = findName(card, disp);

            if(isExact ? name.equalsIgnoreCase(disp.getKeywords()) : name.toLowerCase().contains(keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
    }

    private static void crawlSpecialSets(ArrayList<Card> searchedCards, SearchDisplay disp) {
        boolean isExact = disp.getIsExact();
        String keywords = disp.getKeywords();

        for(CardSet specSet : CardSet.rareSets) {
            for(Card card : specSet.getSpecials()) {
                String name = findName(card, disp);

                if(isExact ? name.equalsIgnoreCase(keywords) : name.toLowerCase().contains(keywords.toLowerCase())) {
                    searchedCards.add(card);
                }
            }
        }
        for(CardSet specSet : CardSet.promoSets) {
            for(Card card : specSet.getSpecials()) {
                String name = findName(card, disp);

                if(isExact ? name.equalsIgnoreCase(keywords) : name.toLowerCase().contains(keywords.toLowerCase())) {
                    searchedCards.add(card);
                }
            }
        }
        for(Card card : CustomCardConsts.CUSTOM_CARDS) {
            String name = findName(card, disp);

            if(isExact ? name.equalsIgnoreCase(keywords) : name.toLowerCase().contains(keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
    }
}
