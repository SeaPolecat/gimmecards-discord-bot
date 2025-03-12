package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MarketDisplay extends Display {

    public MarketDisplay(SlashCommandInteractionEvent event, Server server, int page) {
        super(event);

        setPage(page);
        setMaxPage(server.getMarket().size());
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        int startIndex = getPage() - 1;
        Card card = server.getMarket().get(startIndex);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**Market Price** ┇ " + card.formatCredits() + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(card.findCardTitle(false));
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
