package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class SearchDisplay extends Display {

    private String key;
    private ArrayList<Card> searchedCards;

    public SearchDisplay(String ui) {
        super(ui);
        searchedCards = new ArrayList<Card>();
    }

    public String getKey() { return key; }
    public ArrayList<Card> getSearchedCards() { return searchedCards; }
    //
    public void setKey(String k) { key = k; }
    public void setSearchedCards(ArrayList<Card> sc) { searchedCards = sc; }

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

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(searchedCards.size() / 15);

        if(searchedCards.size() % 15 != 0) {
            addMaxPage();
        }
        desc += "`" + GameManager.formatNumber(searchedCards.size()) + "` search results\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card card = searchedCards.get(i);

            desc += card.findCardTitle(false)
            + " ┇ " + card.findRarityEmote()
            + " ┇ " + card.getSetEmote()
            + " ┇ " + card.formatXP(null)
            + " ┇ `" + card.getCardId() + "`\n";
            if(i >= searchedCards.size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(IEmotes.pokeball + " Searching for \"" + key + "\" " + IEmotes.pokeball);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(IColors.searchColor);
        return embed;
    }
}
