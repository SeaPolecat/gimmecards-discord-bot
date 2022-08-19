package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
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
        String sortOrder = user.getSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        setMaxPage(user.getCards().size() / 15);

        if(user.getCards().size() % 15 != 0) {
            addMaxPage();
        }
        desc += "Sorted by `" + user.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card card = user.getCards().get(i);
            Data data = card.getData();

            desc += "`#" + count + "` " + UX.findCardTitle(data, card.getIsFav())
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXP(data, card.getSellable())
            + " ┇ *x" + card.getCardQuantity() + "*\n";
            count++;
            
            if(i >= user.getCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ui.getUserName() + "'s Collection ┇ " + UX.formatNumber(Check.countOwnedCards(user)) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(user.getGameColor());
        return embed;
    }
}
