package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class ViewDisplay extends Display {

    private String dispType;
    private ArrayList<Data> newCards;
    private String message;

    public ViewDisplay(String ui) {
        super(ui);
        newCards = new ArrayList<Data>();
        message = "";
    }
    
    public String getDispType() { return dispType; }
    public ArrayList<Data> getNewCards() { return newCards; }
    public String getMessage() { return message; }
    //
    public void setDispType(String dt) { dispType = dt; }
    public void setNewCards(ArrayList<Data> nc) { newCards = nc; }
    public void setMessage(String m) { message = m; }

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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        Data data = null;
        String cardTitle = "";
        Boolean sellable = null;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(dispType.equalsIgnoreCase("old")) {
            setMaxPage(user.getCardContainers().size());
        } else if(dispType.equalsIgnoreCase("new")) {
            setMaxPage(newCards.size());
        }
        if(dispType.equalsIgnoreCase("old")) {
            CardContainer cc = user.getCardContainers().get(startIndex);

            data = user.getCardContainers().get(startIndex).getData();
            cardTitle = UX.findCardTitle(data, cc.getIsFav());
            sellable = cc.getIsSellable();

        } else { // dispType.equalsIgnoreCase("new")
            data = newCards.get(startIndex);
            cardTitle = UX.findCardTitle(data, false);

            if(!Check.ownsCard(user, data)) {
                cardTitle += " 🆕";
            }
        }
        if(dispType.equalsIgnoreCase("new")) {
            desc += message;
            desc += "\n┅┅\n";
        }
        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXP(data, sellable) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
