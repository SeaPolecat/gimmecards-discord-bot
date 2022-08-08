package ca.gimmecards.Display_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class CardDisplay_ extends Display {

    private User mention;
    private UserInfo mentionInfo;

    public CardDisplay_(String ui) {
        super(ui);
    }

    public User getMention() { return mention; }
    public UserInfo getMentionInfo() { return mentionInfo; }
    //
    public void setMention(User m) { mention = m; }
    public void setMentionInfo(UserInfo mi) { mentionInfo = mi; }

    @Override
    public CardDisplay_ findDisplay() {
        String userId = getUserId();

        for(CardDisplay_ c : cardDisplays_) {
            if(c.getUserId().equals(userId)) {
                return c;
            }
        }
        cardDisplays_.add(0, new CardDisplay_(userId));
        return cardDisplays_.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = mention.getSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        disp.setMaxPage(mention.getCards().size() / 15);

        if(mention.getCards().size() % 15 != 0) {
            disp.addMaxPage();
        }
        desc += "Sorted by `" + mention.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card c = mention.getCards().get(i);
            Data data = c.getData();

            desc += UX.findCardTitle(data, c.getIsFav())
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data, c.getSellable())
            + " ┇ **x" + c.getCardQuantity() + "**\n";
            count++;
            if(i >= mention.getCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ditto_ + ui.getUserName() + " ➜ " + mentionInfo.getUserName() + "'s Cards " + ditto_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(0xA742D8);
        return embed;
    }
}
