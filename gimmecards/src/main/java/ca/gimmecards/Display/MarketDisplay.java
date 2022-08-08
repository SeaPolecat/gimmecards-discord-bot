package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page - 1;
        Data data = server.getMarket().get(startIndex);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        disp.setMaxPage(server.getMarket().size());

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**Market Price** ┇ " + UX.formatEnergyPrice(data) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(UX.findCardTitle(data, false));
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
