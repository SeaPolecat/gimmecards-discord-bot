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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = user.getSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        disp.setMaxPage(user.getCards().size() / 15);

        if(user.getCards().size() % 15 != 0) {
            disp.addMaxPage();
        }
        desc += "Sorted by `" + user.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card c = user.getCards().get(i);
            Data data = c.getData();

            desc += UX.findCardTitle(data, c.getIsFav())
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data, c.getSellable())
            + " ┇ **x" + c.getCardQuantity() + "**\n";
            count++;
            if(i >= user.getCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ditto_ + " " + ui.getUserName() + "'s Cards " + ditto_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(0xA742D8);
        return embed;
    }
}
