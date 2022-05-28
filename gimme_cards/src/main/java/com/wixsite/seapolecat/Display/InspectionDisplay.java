package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class InspectionDisplay extends Display {

    public static ArrayList<InspectionDisplay> displays = new ArrayList<InspectionDisplay>();
    //
    private String dispType;
    private ArrayList<Data> newCards;

    public InspectionDisplay(String ui) {
        super(ui);
        newCards = new ArrayList<Data>();
    }
    
    public String getDispType() { return dispType; }
    public ArrayList<Data> getNewCards() { return newCards; }
    //
    public void setDispType(String dt) { dispType = dt; }
    public void setNewCards(ArrayList<Data> nc) { newCards = nc; }

    public static InspectionDisplay findInspectionDisplay(String authorId) {
        for(InspectionDisplay i : displays) {
            if(i.getUserId().equals(authorId)) {
                return i;
            }
        }
        displays.add(0, new InspectionDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        InspectionDisplay disp = findInspectionDisplay(user.getUserId());
        SearchDisplay disp2 = SearchDisplay.findSearchDisplay(user.getUserId());
        int startIndex = page - 1;
        int maxPage;
        Card c = user.getCards().get(startIndex);
        Data data;
        String cardTitle;
        Boolean sellable = null;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(disp.getDispType().equals("old")) {
            maxPage = user.getCards().size();
            data = user.getCards().get(startIndex).getData();
            sellable = c.getSellable();

        } else if(disp.getDispType().equals("new")) {
            maxPage = disp.getNewCards().size();
            data = disp.getNewCards().get(startIndex);

        } else {
            maxPage = disp2.getSearchedCards().size();
            data = disp2.getSearchedCards().get(startIndex);
        }
        if(disp.getDispType().equals("old")) {
            cardTitle = UX.findCardTitle(c.getData(), c.getIsFav());
        } else {
            cardTitle = UX.findCardTitle(data, false);
        }

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXPPrice(data, sellable) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
