package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class TradeDisplay extends Display {

    private User user1;
    private User user2;
    private UserInfo userInfo1;
    private UserInfo userInfo2;
    private ArrayList<CardContainer> offers1;
    private ArrayList<CardContainer> offers2;
    private boolean accept1;
    private boolean accept2;
    private boolean reject1;
    private boolean reject2;
    private int tax1;
    private int tax2;

    public TradeDisplay(String ui) {
        super(ui);
        user1 = null;
        user2 = null;
        userInfo1 = null;
        userInfo2 = null;
        offers1 = new ArrayList<CardContainer>();
        offers2 = new ArrayList<CardContainer>();
        accept1 = false;
        accept2 = false;
        reject1 = false;
        reject2 = false;
        tax1 = 0;
        tax2 = 0;
    }

    public User getUser1() { return user1; }
    public User getUser2() { return user2; }
    public ArrayList<CardContainer> getOffers1() { return offers1; }
    public ArrayList<CardContainer> getOffers2() { return offers2; }
    public int getTax1() { return tax1; }
    public int getTax2() { return tax2; }
    //
    public User getUser(String userId) {
        if(isUser1(userId)) {
            return user1;
        }
        return user2;
    }
    public UserInfo getUserInfo(String userId) {
        if(isUser1(userId)) {
            return userInfo1;
        }
        return userInfo2;
    }
    public ArrayList<CardContainer> getOffers(String userId) {
        if(isUser1(userId)) {
            return offers1;
        }
        return offers2;
    }
    public boolean getAccept(String userId) {
        if(isUser1(userId)) {
            return accept1;
        }
        return accept2;
    }
    public int getTax(String userId) {
        if(isUser1(userId)) {
            return tax1;
        }
        return tax2;
    }
    //
    public void setUser1(User u1) { user1 = u1; }
    public void setUser2(User u2) { user2 = u2; }
    public void setUserInfo1(UserInfo ui1) { userInfo1 = ui1; }
    public void setUserInfo2(UserInfo ui2) { userInfo2 = ui2; }

    public void setAccept(String userId, boolean a) {
        if(isUser1(userId)) {
            accept1 = a;
        } else {
            accept2 = a;
        }
    }
    public void setReject(String userId, boolean a) {
        if(isUser1(userId)) {
            reject1 = a;
        } else {
            reject2 = a;
        }
    }
    public void addTax(String userId, int amount) {
        if(isUser1(userId)) {
            tax1 += amount;
        } else {
            tax2 += amount;
        }
    }
    public void removeTax(String userId, int amount) {
        if(isUser1(userId)) {
            tax1 -= amount;
        } else {
            tax2 -= amount;
        }
    }

    @Override
    public TradeDisplay findDisplay() {
        String userId = getUserId();

        for(TradeDisplay t : IDisplays.tradeDisplays) {
            if(t.getUser1() != null 
            && t.getUser(userId).getUserId().equals(userId)) {
                return t;
            }
        }
        IDisplays.tradeDisplays.add(0, new TradeDisplay(userId));
        return IDisplays.tradeDisplays.get(0);
    }

    public void removeTradeDisplay() {
        for(int i = 0; i < IDisplays.tradeDisplays.size(); i++) {
            if(IDisplays.tradeDisplays.get(i).getUserId().equals(this.getUserId())) {
                IDisplays.tradeDisplays.remove(i);
                break;
            }
        }
    }

    public boolean tradeExists(String userId) {
        for(TradeDisplay disp : IDisplays.tradeDisplays) {
            if(disp.getUser1() != null 
            && disp.getUser(userId).getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUser1(String userId) {
        if(userId.equals(user1.getUserId())) {
            return true;
        }
        return false;
    }

    public boolean oneAccepted() {
        if(accept1 || accept2) {
            return true;
        }
        return false;
    }

    public boolean bothAccepted() {
        if(accept1 && accept2) {
            return true;
        }
        return false;
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += server.formatCmd("offer (card #)") + " to offer a card\n";
        desc += server.formatCmd("unoffer (trade #)") + " remove an offer\n";
        desc += server.formatCmd("accept") + " / " + server.formatCmd("unaccept") + " to lock your offer\n";
        desc += server.formatCmd("reject") + " to end trade\n\n";

        desc += userInfo1.getUserPing() + "\n";
        desc += "┅┅\n";
        desc += "**Offer Status** ┇ ";
        if(reject1) {
            desc += "🛑 Rejected\n";
        } else if(accept1) {
            desc += "✅ Accepted\n";
        } else {
            desc += "⏳ Deciding\n";
        }
        desc += "**Trading Fee** ┇ " + Card.formatCredits(tax1) + "\n";
        desc += "┅┅\n";

        for(int i = 0; i < 5; i++) {
            try {
                CardContainer cc = offers1.get(i);
                Card card = cc.getCard();

                desc += "`#" + (i+1) + "` "  + card.findCardTitle(false)
                + " ┇ " + card.findRarityEmote() 
                + " ┇ " + card.getSetEmote()
                + " ┇ " + card.formatXP(cc.getIsSellable())
                + " ┇ *x" + cc.getCardQuantity() + "*\n";

            } catch(IndexOutOfBoundsException e) {
                desc += "`#" + (i+1) + "`\n";
            }
        }
        desc += "\n";

        desc += userInfo2.getUserPing() + "\n";
        desc += "┅┅\n";
        desc += "**Offer Status** ┇ ";
        if(reject2) {
            desc += "🛑 Rejected\n";
        } else if(accept2) {
            desc += "✅ Accepted\n";
        } else {
            desc += "⏳ Deciding\n";
        }
        desc += "**Trading Fee** ┇ " + Card.formatCredits(tax2) + "\n";
        desc += "┅┅\n";

        for(int i = 0; i < 5; i++) {
            try {
                CardContainer cc = offers2.get(i);
                Card card = cc.getCard();

                desc += "`#" + (i+1) + "` "  + card.findCardTitle(false)
                + " ┇ " + card.findRarityEmote() 
                + " ┇ " + card.getSetEmote()
                + " ┇ " + card.formatXP(cc.getIsSellable())
                + " ┇ *x" + cc.getCardQuantity() + "*\n";

            } catch(IndexOutOfBoundsException e) {
                desc += "`#" + (i+1) + "`\n";
            }
        }
        embed.setTitle(IEmotes.ditto + " Trading Center " + IEmotes.ditto);
        embed.setDescription(desc);
        embed.setColor(IColors.tradeColor);
        return embed;
    }
}
