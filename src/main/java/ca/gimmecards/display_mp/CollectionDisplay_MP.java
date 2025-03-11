package ca.gimmecards.display_mp;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CollectionDisplay_MP extends Display {

    public CollectionDisplay_MP(SlashCommandInteractionEvent event, User target, int page) {
        super(event, target);
        
        setPage(page);
    }

    private void getValidPage(User target) {
        int numFullPages = target.getCardContainers().size() / 15;
        int remainder = target.getCardContainers().size() % 15;

        if(remainder == 0)
            setMaxPage(numFullPages);
        else
            setMaxPage(numFullPages + (remainder / remainder));
            
        checkOverflow();
    }

    @Override
    public EmbedBuilder buildEmbed(User target, Server server) {
        if(target.getCardContainers().size() == 0)
            return null;

        getValidPage(target);

        UserInfo userInfo = getUserInfo(), targetInfo = getTargetInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = target.getIsSortIncreasing() ? "increasing" : "decreasing";
        int startIndex = getPage() * 15 - 15;
        int count = startIndex + 1;

        desc += "Sorted by `" + target.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            CardContainer cc = target.getCardContainers().get(i);
            Card card = cc.getCard();

            desc += "`#" + count + "` " + card.findCardTitle(cc.getIsFav())
            + " ┇ " + card.findRarityEmote() 
            + " ┇ " + card.getSetEmote()
            + " ┇ " + card.formatXP(cc.getIsSellable())
            + " ┇ *x" + cc.getCardQuantity() + "*\n";
            count++;
            
            if(i >= target.getCardContainers().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(userInfo.getUserName() + " ➜ " + targetInfo.getUserName() 
        + "'s Collection ┇ " + FormatUtils.formatNumber(target.countOwnedCards()) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), userInfo.getUserIcon());
        embed.setColor(target.getGameColor());
        return embed;
    }
}
