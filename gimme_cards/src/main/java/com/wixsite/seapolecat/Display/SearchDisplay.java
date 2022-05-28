package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class SearchDisplay extends Display {
    
    public static ArrayList<SearchDisplay> displays = new ArrayList<SearchDisplay>();
    //
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

    public static SearchDisplay findSearchDisplay(String authorId) {
        for(SearchDisplay s : displays) {
            if(s.getUserId().equals(authorId)) {
                return s;
            }
        }
        displays.add(0, new SearchDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        SearchDisplay disp = SearchDisplay.findSearchDisplay(user.getUserId());
        int startIndex = page * 15 - 15;
        int maxPage = disp.getSearchedCards().size() / 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(disp.getSearchedCards().size() % 15 != 0) {
            maxPage++;
        }
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Data data = disp.getSearchedCards().get(i);

            desc += UX.findCardTitle(data, false)
            + " ┇ `" + data.getCardId() + "`"
            + " ┇ " + UX.findRarityEmote(data)
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data, null) + "\n";
            if(i >= disp.getSearchedCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(pokeball_ + " Results for \"" + disp.getKey() + "\" " + pokeball_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0xFB4D53);
        return embed;
    }
}
