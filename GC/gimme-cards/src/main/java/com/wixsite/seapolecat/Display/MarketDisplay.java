package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class MarketDisplay extends Display {
    
    public static ArrayList<MarketDisplay> displays = new ArrayList<MarketDisplay>();

    public MarketDisplay(String ui) {
        super(ui);
    }

    public static MarketDisplay findMarketDisplay(String authorId) {
        for(MarketDisplay m : displays) {
            if(m.getUserId().equals(authorId)) {
                return m;
            }
        }
        displays.add(0, new MarketDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        int maxPage = server.getMarket().size();
        Data data = server.getMarket().get(startIndex);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**Market Price** ┇ " + UX.formatEnergyPrice(data) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(UX.findCardTitle(data, false));
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
