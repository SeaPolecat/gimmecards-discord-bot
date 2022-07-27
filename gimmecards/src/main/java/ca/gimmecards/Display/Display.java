package ca.gimmecards.Display;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class Display extends ListenerAdapter implements Emotes {

    private String userId;
    private String channelId;
    private String messageId;
    private Integer page;

    public Display() {}

    public Display(String ui) {
        userId = ui;
        channelId = "";
        messageId = "";
        page = 1;
    }

    public String getUserId() { return userId; }
    public String getChannelId() { return channelId; }
    public String getMessageId() { return messageId; }
    public int getPage() { return page; }
    //
    public void setChannelId(String ci) { channelId = ci; }
    public void setMessageId(String mi) { messageId = mi; }
    public void setPage(int p) { page = p; }
    public void nextPage() { page++; }
    public void prevPage() { page--; }

    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        return null;
    }

    public static void displayCard(MessageReceivedEvent event, User user, Data data, Card c, String footer) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXPPrice(data, c.getSellable()) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(UX.findCardTitle(data, false));
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter(footer, ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        Rest.sendEmbed(event, embed);
        embed.clear();
    }
    
    private static void flipPage(MessageReactionAddEvent event, User user, Server server, Display disp, int maxPage) {
        if(event.getUser().getId().equals(disp.getUserId()) && event.getMessageId().equals(disp.getMessageId())) {
            if(event.getEmoji().getName().equals("◀")) {
                if(disp.getPage() <= 1) {
                    disp.setPage(maxPage);
                } else {
                    disp.prevPage();
                }

            } else if(event.getEmoji().getName().equals("▶")) {
                if(disp.getPage() >= maxPage) {
                    disp.setPage(1);
                } else {
                    disp.nextPage();
                }
            }
            Rest.editEmbed(event, user, server, disp, disp.getPage());
            Rest.removeReaction(event);
        }
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getUser().isBot() == true) { return; }
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        ShopDisplay shopDisp = ShopDisplay.findShopDisplay(user.getUserId());
        OldShopDisplay oldShopDisp = OldShopDisplay.findOldShopDisplay(user.getUserId());
        CardDisplay cardDisp = CardDisplay.findCardDisplay(user.getUserId());
        CardDisplay_ cardDisp_ = CardDisplay_.findCardDisplay_(user.getUserId());
        InspectionDisplay inspectionDisp = InspectionDisplay.findInspectionDisplay(user.getUserId());
        InspectionDisplay_ inspectionDisp_ = InspectionDisplay_.findInspectionDisplay_(user.getUserId());
        SearchDisplay searchDisp = SearchDisplay.findSearchDisplay(user.getUserId());
        FavDisplay favDisp = FavDisplay.findFavDisplay(user.getUserId());
        HelpDisplay helpDisp = HelpDisplay.findHelpDisplay(user.getUserId());
        MarketDisplay marketDisp = MarketDisplay.findMarketDisplay(user.getUserId());

        if(event.getMessageId().equals(shopDisp.getMessageId())) {
            int maxPage = Data.sets.length / 8;

            if(Data.sets.length % 8 != 0) {
                maxPage++;
            }
            flipPage(event, user, server, shopDisp, maxPage);

        } else if(event.getMessageId().equals(oldShopDisp.getMessageId())) {
            int maxPage = Data.oldSets.length / 8;

            if(Data.oldSets.length % 8 != 0) {
                maxPage++;
            }
            flipPage(event, user, server, oldShopDisp, maxPage);

        } else if(event.getMessageId().equals(cardDisp.getMessageId())) {
            int maxPage = user.getCards().size() / 15;

            if(user.getCards().size() % 15 != 0) {
                maxPage++;
            }
            flipPage(event, user, server, cardDisp, maxPage);

        } else if(event.getMessageId().equals(cardDisp_.getMessageId())) {
            int maxPage = cardDisp_.getMention().getCards().size() / 15;

            if(cardDisp_.getMention().getCards().size() % 15 != 0) {
                maxPage++;
            }
            flipPage(event, user, server, cardDisp_, maxPage);

        } else if(event.getMessageId().equals(inspectionDisp.getMessageId())) {
            int maxPage;

            if(user.getCards().size() > 0) {
                if(inspectionDisp.getDispType().equals("old")) {
                    maxPage = user.getCards().size();
                } else if(inspectionDisp.getDispType().equals("new")) {
                    maxPage = inspectionDisp.getNewCards().size();
                } else {
                    maxPage = searchDisp.getSearchedCards().size();
                }
                flipPage(event, user, server, inspectionDisp, maxPage);
            }

        } else if(event.getMessageId().equals(inspectionDisp_.getMessageId())) {
            int maxPage = inspectionDisp_.getMention().getCards().size();

            flipPage(event, user, server, inspectionDisp_, maxPage);

        } else if(event.getMessageId().equals(searchDisp.getMessageId())) {
            int maxPage = searchDisp.getSearchedCards().size() / 15;

            if(searchDisp.getSearchedCards().size() % 15 != 0) {
                maxPage++;
            }
            flipPage(event, user, server, searchDisp, maxPage);

        } else if(event.getMessageId().equals(favDisp.getMessageId())) {
            ArrayList<Card> favCards = FavDisplay.findFavCards(user);
            int maxPage = favCards.size() / 15;

            if(favCards.size() % 15 != 0) {
                maxPage++;
            }
            flipPage(event, user, server, favDisp, maxPage);

        } else if(event.getMessageId().equals(helpDisp.getMessageId())) {
            int maxPage = Changelog.changelog.length;

            flipPage(event, user, server, helpDisp, maxPage);

        } else if(event.getMessageId().equals(marketDisp.getMessageId())) {
            int maxPage = server.getMarket().size();

            flipPage(event, user, server, marketDisp, maxPage);
        }
    }
}
