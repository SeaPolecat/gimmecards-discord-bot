package ca.gimmecards.Display_MP;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ViewDisplay_MP extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public ViewDisplay_MP(String ui) {
        super(ui);
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public UserInfo getMentionInfo() { return mentionInfo; }
    //
    public void setUser(User u) { user = u; }
    public void setMention(User m) { mention = m; }
    public void setMentionInfo(UserInfo mi) { mentionInfo = mi; }

    @Override
    public ViewDisplay_MP findDisplay() {
        String userId = getUserId();

        for(ViewDisplay_MP v : IDisplays.viewDisplays_MP) {
            if(v.getUserId().equals(userId)) {
                return v;
            }
        }
        IDisplays.viewDisplays_MP.add(0, new ViewDisplay_MP(userId));
        return IDisplays.viewDisplays_MP.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        CardContainer cc = mention.getCardContainers().get(startIndex);
        Card card = cc.getCard();
        String cardTitle = card.findCardTitle(cc.getIsFav());
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(mention.getCardContainers().size());

        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**XP Value** ┇ " + card.formatXP(cc.getIsSellable()) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(ui.getUserName() + " ➜ " + mentionInfo.getUserName()
        + "'s " + cardTitle);
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
