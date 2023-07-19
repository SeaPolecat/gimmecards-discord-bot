package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class MarketDisplay extends Display {

    public MarketDisplay(String ui) {
        super(ui);
    }

    @Override
    public MarketDisplay findDisplay() {
        String userId = getUserId();

        for(MarketDisplay m : marketDisplays) {
            if(m.getUserId().equals(userId)) {
                return m;
            }
        }
        marketDisplays.add(0, new MarketDisplay(userId));
        return marketDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        Card card = server.getMarket().get(startIndex);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(server.getMarket().size());

        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**Market Price** ┇ " + card.formatCredits() + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(card.findCardTitle(false));
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
