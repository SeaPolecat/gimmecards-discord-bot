package ca.gimmecards.display_mp;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ViewDisplay_MP extends Display {

    public ViewDisplay_MP(SlashCommandInteractionEvent event, User target, int page) {
        super(event, target);

        setPage(page);
    }

    private void getValidPage(User target) {
        setMaxPage(target.getCardContainers().size());
        checkOverflow();
    }

    @Override
    public EmbedBuilder buildEmbed(User target, Server server) {
        if(target.getCardContainers().size() == 0)
            return null;

        getValidPage(target);

        UserInfo userInfo = getUserInfo(), targetInfo = getTargetInfo();
        int startIndex = getPage() - 1;
        CardContainer cc = target.getCardContainers().get(startIndex);
        Card card = cc.getCard();
        String cardTitle = card.findCardTitle(cc.getIsFav());
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**XP Value** ┇ " + card.formatXP(cc.getIsSellable()) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(userInfo.getUserName() + " ➜ " + targetInfo.getUserName()
        + "'s " + cardTitle);
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), userInfo.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
