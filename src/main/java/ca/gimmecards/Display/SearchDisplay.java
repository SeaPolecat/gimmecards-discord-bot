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
    ArrayList<Card> searchedCards; 

    public SearchDisplay(SlashCommandInteractionEvent event) {
        super(event);
    }

    public String getLocation() { return this.location; }
    public String getFilter() { return this.filter; }
    public boolean getIsExact() { return this.isExact; }
    public String getKeywords() { return this.keywords; }

    public void modifyVariables(String location, String filter, boolean isExact, String keywords) {
        this.location = location;
        this.filter = filter;
        this.isExact = isExact;
        this.keywords = keywords;
    }
    public void setSearchedCards(ArrayList<Card> searchedCards) { this.searchedCards = searchedCards; }

    private void getValidPage() {
        int numFullPages = searchedCards.size() / 15;
        int remainder = searchedCards.size() % 15;

        if(remainder == 0)
            setMaxPage(numFullPages);
        else
            setMaxPage(numFullPages + (remainder / remainder));
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        int startIndex = getPage() * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        getValidPage();

        if(this.location.equalsIgnoreCase("collection")) {

            desc += "***" + FormatUtils.formatNumber(searchedCards.size()) + "*** ";
            desc += this.isExact ? "exact " : "non-exact ";
            desc += "search results for `" + this.keywords + "`\n\n";
            desc += "`/view (card #)` to view a card\n";
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
            embed.setTitle(EmoteConsts.POKEBALL + " Searching " + ui.getUserName() + "'s Collection... " + EmoteConsts.POKEBALL);
            embed.setDescription(desc);
            embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
            embed.setColor(ColorConsts.SEARCH_COLOR);

        } else if(this.location.equals("pokedex")) {

            desc += "***" + FormatUtils.formatNumber(searchedCards.size()) + "*** ";
            desc += this.isExact ? "exact " : "non-exact ";
            desc += "search results for `" + this.keywords + "`\n\n";
            desc += "`/sview (card ID)` to view a card\n";
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
            embed.setTitle(EmoteConsts.POKEBALL + " Searching the Pokédex... " + EmoteConsts.POKEBALL);
            embed.setDescription(desc);
            embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
            embed.setColor(ColorConsts.SEARCH_COLOR);
        }
        return embed;
    }
}
