package ca.gimmecards.Display_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ViewDisplay_ extends Display {

    private User mention;
    private UserInfo mentionInfo;

    public ViewDisplay_(String ui) {
        super(ui);
    }

    public User getMention() { return mention; }
    public UserInfo getMentionInfo() { return mentionInfo; }
    //
    public void setMention(User m) { mention = m; }
    public void setMentionInfo(UserInfo mi) { mentionInfo = mi; }

    @Override
    public ViewDisplay_ findDisplay() {
        String userId = getUserId();

        for(ViewDisplay_ i : viewDisplays_) {
            if(i.getUserId().equals(userId)) {
                return i;
            }
        }
        viewDisplays_.add(0, new ViewDisplay_(userId));
        return viewDisplays_.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page - 1;
        Card c = mention.getCards().get(startIndex);
        Data data = c.getData();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        disp.setMaxPage(mention.getCards().size());

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXPPrice(data, c.getSellable()) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(ui.getUserName() + " ➜ " + mentionInfo.getUserName() + "'s " + UX.findCardTitle(data, c.getIsFav()));
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
