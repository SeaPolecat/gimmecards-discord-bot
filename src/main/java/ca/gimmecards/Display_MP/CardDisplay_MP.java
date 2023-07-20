package ca.gimmecards.Display_MP;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class CardDisplay_MP extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public CardDisplay_MP(String ui) {
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
    public CardDisplay_MP findDisplay() {
        String userId = getUserId();

        for(CardDisplay_MP c : IDisplays.cardDisplays_MP) {
            if(c.getUserId().equals(userId)) {
                return c;
            }
        }
        IDisplays.cardDisplays_MP.add(0, new CardDisplay_MP(userId));
        return IDisplays.cardDisplays_MP.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = mention.getIsSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        setMaxPage(mention.getCardContainers().size() / 15);

        if(mention.getCardContainers().size() % 15 != 0) {
            addMaxPage();
        }
        desc += "Sorted by `" + mention.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            CardContainer cc = mention.getCardContainers().get(i);
            Card card = cc.getCard();

            desc += "`#" + count + "` " + card.findCardTitle(cc.getIsFav())
            + " ┇ " + card.findRarityEmote() 
            + " ┇ " + card.getSetEmote()
            + " ┇ " + card.formatXP(cc.getIsSellable())
            + " ┇ *x" + cc.getCardQuantity() + "*\n";
            count++;
            
            if(i >= mention.getCardContainers().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ui.getUserName() + " ➜ " + mentionInfo.getUserName() 
        + "'s Collection ┇ " + GameManager.formatNumber(mention.countOwnedCards()) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(mention.getGameColor());
        return embed;
    }
}
