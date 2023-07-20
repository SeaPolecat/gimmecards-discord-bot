package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class CollectionDisplay extends Display {

    public CollectionDisplay(String ui) {
        super(ui);
    }

    @Override
    public CollectionDisplay findDisplay() {
        String userId = getUserId();

        for(CollectionDisplay c : IDisplays.collectionDisplays) {
            if(c.getUserId().equals(userId)) {
                return c;
            }
        }
        IDisplays.collectionDisplays.add(0, new CollectionDisplay(userId));
        return IDisplays.collectionDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = user.getIsSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        setMaxPage(user.getCardContainers().size() / 15);

        if(user.getCardContainers().size() % 15 != 0) {
            addMaxPage();
        }
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
        embed.setTitle(ui.getUserName() + "'s Collection ┇ " + GameManager.formatNumber(user.countOwnedCards()) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(user.getGameColor());
        return embed;
    }
}
