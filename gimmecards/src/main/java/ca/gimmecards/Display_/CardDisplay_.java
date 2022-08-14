package ca.gimmecards.Display_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class CardDisplay_ extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public CardDisplay_(String ui) {
        super(ui);
        mention = null;
        mentionInfo = null;
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public UserInfo getMentionInfo() { return mentionInfo; }
    //
    public void setUser(User u) { user = u; }
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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = mention.getSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        setMaxPage(mention.getCards().size() / 15);

        if(mention.getCards().size() % 15 != 0) {
            addMaxPage();
        }
        desc += "Sorted by `" + mention.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card card = mention.getCards().get(i);
            Data data = card.getData();

            desc += "`#" + count + "` " + UX.findCardTitle(data, card.getIsFav())
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXP(data, card.getSellable())
            + " ┇ *x" + card.getCardQuantity() + "*\n";
            count++;
            
            if(i >= mention.getCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ui.getUserName() + " ➜ " + mentionInfo.getUserName() 
        + "'s Collection ┇ " + Check.countOwnedCards(mention) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(mention.getGameColor());
        return embed;
    }
}
