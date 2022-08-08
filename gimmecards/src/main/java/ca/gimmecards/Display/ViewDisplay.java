package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class ViewDisplay extends Display {

    private String dispType;
    private ArrayList<Data> newCards;

    public ViewDisplay(String ui) {
        super(ui);
        newCards = new ArrayList<Data>();
    }
    
    public String getDispType() { return dispType; }
    public ArrayList<Data> getNewCards() { return newCards; }
    //
    public void setDispType(String dt) { dispType = dt; }
    public void setNewCards(ArrayList<Data> nc) { newCards = nc; }

    @Override
    public ViewDisplay findDisplay() {
        String userId = getUserId();

        for(ViewDisplay i : viewDisplays) {
            if(i.getUserId().equals(userId)) {
                return i;
            }
        }
        viewDisplays.add(0, new ViewDisplay(userId));
        return viewDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        SearchDisplay searchDisp = new SearchDisplay(user.getUserId()).findDisplay();
        int startIndex = page - 1;
        Data data;
        String cardTitle;
        Boolean sellable = null;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(dispType.equals("old")) {
            Card c = user.getCards().get(startIndex);

            disp.setMaxPage(user.getCards().size());

            data = user.getCards().get(startIndex).getData();
            cardTitle = UX.findCardTitle(data, c.getIsFav());
            sellable = c.getSellable();

        } else if(dispType.equals("new")) {
            disp.setMaxPage(newCards.size());

            data = newCards.get(startIndex);
            cardTitle = UX.findCardTitle(data, false) + " 🆕";

        } else {
            disp.setMaxPage(searchDisp.getSearchedCards().size());

            data = searchDisp.getSearchedCards().get(startIndex);
            cardTitle = UX.findCardTitle(data, false);
        }
        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXPPrice(data, sellable) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
