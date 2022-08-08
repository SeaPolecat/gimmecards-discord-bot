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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        disp.setMaxPage(searchedCards.size() / 15);

        if(searchedCards.size() % 15 != 0) {
            disp.addMaxPage();
        }
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Data data = searchedCards.get(i);

            desc += UX.findCardTitle(data, false)
            + " ┇ `" + data.getCardId() + "`"
            + " ┇ " + UX.findRarityEmote(data)
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data, null) + "\n";
            if(i >= searchedCards.size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(pokeball_ + " Results for \"" + key + "\" " + pokeball_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(0xFB4D53);
        return embed;
    }
}
