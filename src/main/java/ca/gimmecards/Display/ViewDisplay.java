package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ViewDisplay extends Display {

    public ViewDisplay(SlashCommandInteractionEvent event, int page) {
        super(event);

        setPage(page);
    }

    private void getValidPage(User user) {
        setMaxPage(user.getCardContainers().size());
        checkOverflow();
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        if(user.getCardContainers().size() == 0)
            return null;

        getValidPage(user);

        UserInfo ui = getUserInfo();
        Card card = user.getCardContainers().get(getPage() - 1).getCard();
        CardContainer cc = user.getCardContainers().get(getPage() - 1);
        String cardTitle = card.findCardTitle(cc.getIsFav());
        Boolean isSellable = cc.getIsSellable();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

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
