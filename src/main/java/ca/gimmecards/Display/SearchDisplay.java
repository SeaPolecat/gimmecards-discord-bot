package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.ArrayList;

public class SearchDisplay extends Display {

    private String location;
    private String filter;
    private boolean isExact;
    private String keywords;

    public SearchDisplay(SlashCommandInteractionEvent event) {
        super(event);
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
        for(Card card : CustomCardConsts.CUSTOM_CARDS) {
            String name = findName(card);

            if(this.isExact ? name.equalsIgnoreCase(this.keywords) : name.toLowerCase().contains(this.keywords.toLowerCase())) {
                searchedCards.add(card);
            }
        }
    }

    private void getValidPage(ArrayList<Card> searchedCards) {
        int numFullPages = searchedCards.size() / 15;
        int remainder = searchedCards.size() % 15;

        if(remainder == 0)
            setMaxPage(numFullPages);
        else
            setMaxPage(numFullPages + (remainder / remainder));
            
        checkOverflow();
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        int startIndex = getPage() * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(this.location.equalsIgnoreCase("collection")) {
            ArrayList<Card> searchedCards = searchCollection(user);

            getValidPage(searchedCards);

            desc += "***" + FormatUtils.formatNumber(searchedCards.size()) + "*** ";
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
            embed.setTitle(EmoteConsts.POKEBALL + " Searching " + ui.getUserName() + "'s Collection... " + EmoteConsts.POKEBALL);
            embed.setDescription(desc);
            embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
            embed.setColor(ColorConsts.SEARCH_COLOR);

        } else if(this.location.equals("pokedex")) {
            ArrayList<Card> searchedCards = searchPokedex(user);

            getValidPage(searchedCards);

            desc += "***" + FormatUtils.formatNumber(searchedCards.size()) + "*** ";
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
            embed.setTitle(EmoteConsts.POKEBALL + " Searching the Pokédex... " + EmoteConsts.POKEBALL);
            embed.setDescription(desc);
            embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
            embed.setColor(ColorConsts.SEARCH_COLOR);
        }
        return embed;
    }
}
