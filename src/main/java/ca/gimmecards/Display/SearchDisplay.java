package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class SearchDisplay extends Display {

    private String location;
    private String filter;
    private boolean isExact;
    private String keywords;

    public SearchDisplay(String ui) {
        super(ui);
    }

    public String getLocation() { return this.location; }
    public String getFilter() { return this.filter; }
    public boolean isExact() { return this.isExact; }
    public String getKeywords() { return this.keywords; }

    public void modifyVariables(String location, String filter, boolean isExact, String keywords) {
        this.location = location;
        this.filter = filter;
        this.isExact = isExact;
        this.keywords = keywords;
    }

    @Override
    public SearchDisplay findDisplay() {
        String userId = getUserId();

        for(SearchDisplay s : IDisplays.searchDisplays) {
            if(s.getUserId().equals(userId)) {
                return s;
            }
        }
        IDisplays.searchDisplays.add(0, new SearchDisplay(userId));
        return IDisplays.searchDisplays.get(0);
    }

    public String findName(Card card) {
        if(this.filter.equalsIgnoreCase("card")) {
            return card.getCardName();

        } else if(this.filter.equalsIgnoreCase("pack")) {
            return card.getSetName();
        }
        return card.getCardRarity();
    }

    public ArrayList<Card> searchCollection(User user) {
        ArrayList<Card> searchedCards = new ArrayList<Card>();

        for(CardContainer cc : user.getCardContainers()) {
            Card card = cc.getCard();
            String name = findName(card);

            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        return searchedCards;
    }

    public ArrayList<Card> searchPokedex(User user) {
        ArrayList<Card> searchedCards = new ArrayList<Card>();

        for(CardSet set : CardSet.sets) {
            crawlSet(searchedCards, set);
        }
        for(CardSet oldSet : CardSet.oldSets) {
            crawlSet(searchedCards, oldSet);
        }
        crawlSpecialSets(searchedCards);

        return searchedCards;
    }

    public void crawlSet(ArrayList<Card> searchedCards, CardSet set) {
        for(Card card : set.getCommons()) {
            String name = findName(card);
            
            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getUncommons()) {
            String name = findName(card);

            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getRares()) {
            String name = findName(card);

            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
        for(Card card : set.getShinies()) {
            String name = findName(card);

            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
    }

    public void crawlSpecialSets(ArrayList<Card> searchedCards) {
        for(CardSet specSet : CardSet.rareSets) {
            for(Card card : specSet.getSpecials()) {
                String name = findName(card);

                if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                    searchedCards.add(card);
                }
            }
        }
        for(CardSet specSet : CardSet.promoSets) {
            for(Card card : specSet.getSpecials()) {
                String name = findName(card);

                if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                    searchedCards.add(card);
                }
            }
        }
        /*for(Card card : ICustomCards.customs) {
            String name = findName(card);

            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }*/
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(this.location.equalsIgnoreCase("collection")) {
            ArrayList<Card> searchedCards = searchCollection(user);

            setMaxPage(searchedCards.size() / 15);

            if(searchedCards.size() % 15 != 0) {
                addMaxPage();
            }
            desc += "***" + GameManager.formatNumber(searchedCards.size()) + "*** ";
            desc += this.isExact ? "exact " : "non-exact ";
            desc += "search results for `" + this.keywords + "`\n\n";
            desc += "`/view (card #)` to view a card\n";

            if(searchedCards.size() > 0) {
                desc += "┅┅\n";

                for(int i = startIndex; i < startIndex + 15; i++) {
                    Card searchedCard = searchedCards.get(i);

                    for(int k = 0; k < user.getCardContainers().size(); k++) {
                        CardContainer cc = user.getCardContainers().get(k);
                        Card card = user.getCardContainers().get(k).getCard();
                        int cardNum = k + 1;
                        
                        if(searchedCard.getCardId().equalsIgnoreCase(card.getCardId())) {
                            desc += "`#" + cardNum + "` " + card.findCardTitle(cc.getIsFav())
                            + " ┇ " + card.findRarityEmote() 
                            + " ┇ " + card.getSetEmote()
                            + " ┇ " + card.formatXP(cc.getIsSellable())
                            + " ┇ *x" + cc.getCardQuantity() + "*\n";
                        }
                    }
                    if(i >= searchedCards.size() - 1) {
                        break;
                    }
                }
                desc += "┅┅\n";

            } else {
                setMaxPage(1);
            }
            embed.setTitle(IEmotes.pokeball + " Searching " + ui.getUserName() + "'s Collection... " + IEmotes.pokeball);
            embed.setDescription(desc);
            embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
            embed.setColor(IColors.searchColor);

        } else if(this.location.equals("pokedex")) {
            ArrayList<Card> searchedCards = searchPokedex(user);

            setMaxPage(searchedCards.size() / 15);

            if(searchedCards.size() % 15 != 0) {
                addMaxPage();
            }
            desc += "***" + GameManager.formatNumber(searchedCards.size()) + "*** ";
            desc += this.isExact ? "exact " : "non-exact ";
            desc += "search results for `" + this.keywords + "`\n\n";
            desc += "`/sview (card ID)` to view a card\n";

            if(searchedCards.size() > 0) {
                desc += "┅┅\n";

                for(int i = startIndex; i < startIndex + 15; i++) {
                    Card searchedCard = searchedCards.get(i);

                    desc += searchedCard.findCardTitle(false)
                    + " ┇ " + searchedCard.findRarityEmote()
                    + " ┇ " + searchedCard.getSetEmote()
                    + " ┇ " + searchedCard.formatXP(searchedCard.isCardSellable())
                    + " ┇ `" + searchedCard.getCardId() + "`\n";
                    if(i >= searchedCards.size() - 1) {
                        break;
                    }
                }
                desc += "┅┅\n";
                
            } else {
                setMaxPage(1);
            }
            embed.setTitle(IEmotes.pokeball + " Searching the Pokédex... " + IEmotes.pokeball);
            embed.setDescription(desc);
            embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
            embed.setColor(IColors.searchColor);
        }
        return embed;
    }
}
