package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class CardDisplay extends Display {

    public CardDisplay(String ui) {
        super(ui);
    }

    @Override
    public CardDisplay findDisplay() {
        String userId = getUserId();

        for(CardDisplay c : cardDisplays) {
            if(c.getUserId().equals(userId)) {
                return c;
            }
        }
        cardDisplays.add(0, new CardDisplay(userId));
        return cardDisplays.get(0);
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
        embed.setTitle(ui.getUserName() + "'s Collection ┇ " + GameObject.formatNumber(user.countOwnedCards()) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(user.getGameColor());
        return embed;
    }
}
