package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ViewDisplay extends Display {

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        Card card = user.getCardContainers().get(page - 1).getCard();
        CardContainer cc = user.getCardContainers().get(page - 1);
        String cardTitle = card.findCardTitle(cc.getIsFav());
        Boolean isSellable = cc.getIsSellable();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(user.getCardContainers().size());

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
