package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;

public class OpenDisplay extends Display {

    private static final int PACK_SIZE = 10;

    private ArrayList<Card> newCards;
    private String message;

    public OpenDisplay(SlashCommandInteractionEvent event) {
        super(event);
        newCards = new ArrayList<Card>();

        setMaxPage(PACK_SIZE);
    }

    public ArrayList<Card> getNewCards() { return newCards; }
    public String getMessage() { return message; }
    //
    public void setNewCards(ArrayList<Card> nc) { newCards = nc; }
    public void setMessage(String m) { message = m; }
    
    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        Card card = newCards.get(getPage() - 1);
        String cardTitle = card.findCardTitle(false);
        Boolean isSellable = card.isCardSellable();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

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
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
