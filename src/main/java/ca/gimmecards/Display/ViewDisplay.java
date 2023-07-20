package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class ViewDisplay extends Display {

    private String dispType;
    private ArrayList<Card> newCards;
    private String message;

    public ViewDisplay(String ui) {
        super(ui);
        newCards = new ArrayList<Card>();
        message = "";
    }
    
    public String getDispType() { return dispType; }
    public ArrayList<Card> getNewCards() { return newCards; }
    public String getMessage() { return message; }
    //
    public void setDispType(String dt) { dispType = dt; }
    public void setNewCards(ArrayList<Card> nc) { newCards = nc; }
    public void setMessage(String m) { message = m; }

    @Override
    public ViewDisplay findDisplay() {
        String userId = getUserId();

        for(ViewDisplay v : IDisplays.viewDisplays) {
            if(v.getUserId().equals(userId)) {
                return v;
            }
        }
        IDisplays.viewDisplays.add(0, new ViewDisplay(userId));
        return IDisplays.viewDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        Card card = null;
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

            card = user.getCardContainers().get(startIndex).getCard();
            cardTitle = card.findCardTitle(cc.getIsFav());
            sellable = cc.getIsSellable();

        } else { // dispType.equalsIgnoreCase("new")
            card = newCards.get(startIndex);
            cardTitle = card.findCardTitle(false);

            if(!user.ownsCard(card)) {
                cardTitle += " 🆕";
            }
        }
        if(dispType.equalsIgnoreCase("new")) {
            desc += message;
            desc += "\n┅┅\n";
        }
        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**XP Value** ┇ " + card.formatXP(sellable) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
