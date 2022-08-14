package ca.gimmecards.Display;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class Display extends ListenerAdapter implements Displays, Emotes, Colors {

    private String userId;
    private String channelId;
    private String messageId;
    private Integer page;
    private Integer maxPage;

    public Display() {}

    public Display(String ui) {
        userId = ui;
        channelId = "";
        messageId = "";
        page = 1;
        maxPage = 0;
    }

    public String getUserId() { return userId; }
    public String getChannelId() { return channelId; }
    public String getMessageId() { return messageId; }
    public int getPage() { return page; }
    public int getMaxPage() { return maxPage; }
    //
    public void setChannelId(String ci) { channelId = ci; }
    public void setMessageId(String mi) { messageId = mi; }
    public void setPage(int p) { page = p; }
    public void nextPage() { page++; }
    public void prevPage() { page--; }
    public void setMaxPage(int mp) { maxPage = mp; }
    public void addMaxPage() { maxPage++; }

    public Display findDisplay() {
        return null;
    }

    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        return null;
    }

    public static void displayCard(MessageReceivedEvent event, User user, Data data, String footer) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXP(data, Check.isSellable(data)) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(UX.findCardTitle(data, false));
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter(footer, ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    private void flipPage(MessageReactionAddEvent event, User user, Server server, Display disp) {
        if(event.getUser().getId().equals(disp.getUserId()) && event.getMessageId().equals(disp.getMessageId())) {
            if(event.getEmoji().getName().equals("◀")) {
                if(disp.getPage() <= 1) {
                    disp.setPage(disp.getMaxPage());
                } else {
                    disp.prevPage();
                }

            } else if(event.getEmoji().getName().equals("▶")) {
                if(disp.getPage() >= disp.getMaxPage()) {
                    disp.setPage(1);
                } else {
                    disp.nextPage();
                }
            }
            JDA.editEmbed(event, user, server, disp, disp.getPage());
            JDA.removeReaction(event);
        }
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getUser().isBot() == true) { return; }
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        //
        CardDisplay cardDisp = new CardDisplay(user.getUserId()).findDisplay();
        CardDisplay_ cardDisp_ = new CardDisplay_(user.getUserId()).findDisplay();
        HelpDisplay helpDisp = new HelpDisplay(user.getUserId()).findDisplay();
        LeaderboardDisplay leaderboardDisp = new LeaderboardDisplay(user.getUserId()).findDisplay();
        MarketDisplay marketDisp = new MarketDisplay(user.getUserId()).findDisplay();
        OldShopDisplay oldShopDisp = new OldShopDisplay(user.getUserId()).findDisplay();
        SearchDisplay searchDisp = new SearchDisplay(user.getUserId()).findDisplay();
        ShopDisplay shopDisp = new ShopDisplay(user.getUserId()).findDisplay();
        ViewDisplay viewDisp = new ViewDisplay(user.getUserId()).findDisplay();
        ViewDisplay_ viewDisp_ = new ViewDisplay_(user.getUserId()).findDisplay();

        if(event.getMessageId().equals(cardDisp.getMessageId())) {
            flipPage(event, user, server, cardDisp);
        }
        if(event.getMessageId().equals(cardDisp_.getMessageId())) {
            flipPage(event, user, server, cardDisp_);
        }
        if(event.getMessageId().equals(helpDisp.getMessageId())) {
            flipPage(event, user, server, helpDisp);
        }
        if(event.getMessageId().equals(leaderboardDisp.getMessageId())) {
            flipPage(event, user, server, leaderboardDisp);
        }
        if(event.getMessageId().equals(marketDisp.getMessageId())) {
            flipPage(event, user, server, marketDisp);
        }
        if(event.getMessageId().equals(oldShopDisp.getMessageId())) {
            flipPage(event, user, server, oldShopDisp);
        }
        if(event.getMessageId().equals(searchDisp.getMessageId())) {
            flipPage(event, user, server, searchDisp);
        }
        if(event.getMessageId().equals(shopDisp.getMessageId())) {
            flipPage(event, user, server, shopDisp);
        }
        if(event.getMessageId().equals(viewDisp.getMessageId())) {
            flipPage(event, user, server, viewDisp);
        }
        if(event.getMessageId().equals(viewDisp_.getMessageId())) {
            flipPage(event, user, server, viewDisp_);
        }
    }
}
