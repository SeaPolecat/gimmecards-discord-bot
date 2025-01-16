package ca.gimmecards.display_mp;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class CollectionDisplay_MP extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public CollectionDisplay_MP() {
        super();
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
        + "'s Collection ┇ " + FormatUtils.formatNumber(mention.countOwnedCards()) + " Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(mention.getGameColor());
        return embed;
    }
}
