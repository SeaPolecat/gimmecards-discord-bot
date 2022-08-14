package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class SearchDisplay extends Display {

    private String key;
    private ArrayList<Data> searchedCards;

    public SearchDisplay(String ui) {
        super(ui);
        searchedCards = new ArrayList<Data>();
    }

    public String getKey() { return key; }
    public ArrayList<Data> getSearchedCards() { return searchedCards; }
    //
    public void setKey(String k) { key = k; }
    public void setSearchedCards(ArrayList<Data> sc) { searchedCards = sc; }

    @Override
    public SearchDisplay findDisplay() {
        String userId = getUserId();

        for(SearchDisplay s : searchDisplays) {
            if(s.getUserId().equals(userId)) {
                return s;
            }
        }
        searchDisplays.add(0, new SearchDisplay(userId));
        return searchDisplays.get(0);
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
        desc += "`" + UX.formatNumber(searchedCards.size()) + "` search results\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Data data = searchedCards.get(i);

            desc += UX.findCardTitle(data, false)
            + " ┇ " + UX.findRarityEmote(data)
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXP(data, null)
            + " ┇ `" + data.getCardId() + "`\n";
            if(i >= searchedCards.size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(pokeball_ + " Searching for \"" + key + "\" " + pokeball_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(search_);
        return embed;
    }
}
