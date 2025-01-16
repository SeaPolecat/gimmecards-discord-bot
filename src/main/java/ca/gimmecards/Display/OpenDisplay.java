package ca.gimmecards.display;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

import ca.gimmecards.main.*;

public class OpenDisplay extends Display {

    private ArrayList<Card> newCards;
    private String message;

    public OpenDisplay() {
        super();
        newCards = new ArrayList<Card>();
        message = "";
    }

    public ArrayList<Card> getNewCards() { return newCards; }
    public String getMessage() { return message; }
    //
    public void setNewCards(ArrayList<Card> nc) { newCards = nc; }
    public void setMessage(String m) { message = m; }
    
    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        Card card = newCards.get(page - 1);
        String cardTitle = card.findCardTitle(false);
        Boolean isSellable = card.isCardSellable();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(newCards.size());

        if(!user.ownsCard(card)) {
            cardTitle += " 🆕";
        }
        desc += message + "\n";
        desc += "┅┅\n";
        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**XP Value** ┇ " + card.formatXP(isSellable) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
