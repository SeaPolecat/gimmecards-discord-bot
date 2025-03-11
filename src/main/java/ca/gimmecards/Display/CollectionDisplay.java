package ca.gimmecards.display;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CollectionDisplay extends Display {

    public CollectionDisplay(SlashCommandInteractionEvent event, int page) {
        super(event);
        
        setPage(page);
    }

    private void getValidPage(User user) {
        int numFullPages = user.getCardContainers().size() / 15;
        int remainder = user.getCardContainers().size() % 15;

        if(remainder == 0)
            setMaxPage(numFullPages);
        else
            setMaxPage(numFullPages + (remainder / remainder));

        checkOverflow();
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        if(user.getCardContainers().size() == 0)
            return null;

        getValidPage(user);

        UserInfo ui = getUserInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = user.getIsSortIncreasing() ? "increasing" : "decreasing";
        int startIndex = getPage() * 15 - 15;
        int count = startIndex + 1;

        desc += "Sorted by `" + user.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            CardContainer cc = user.getCardContainers().get(i);
            Card card = cc.getCard();

            desc += "`#" + count + "` " + card.findCardTitle(cc.getIsFav())
            + " ┇ " + card.findRarityEmote() 
            + " ┇ " + card.getSetEmote()
            + " ┇ " + card.formatXP(cc.getIsSellable())
            + " ┇ *x" + cc.getCardQuantity() + "*\n";
            count++;
            
            if(i >= user.getCardContainers().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ui.getUserName() + "'s Collection ┇ " + FormatUtils.formatNumber(user.countOwnedCards()) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(user.getGameColor());
        return embed;
    }
}
