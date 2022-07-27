/*package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class TradeDisplay extends Display {
    
    public static ArrayList<TradeDisplay> displays = new ArrayList<TradeDisplay>();
    //
    private User user;
    private User mention;
    private String mentionName;
    private boolean userAccept;
    private ArrayList<Card> userOffer;
    private boolean mentionAccept;
    private ArrayList<Card> mentionOffer;

    public TradeDisplay(String ui, User u, User m, String mn) {
        super(ui);
        user = u;
        mention = m;
        mentionName = mn;
        userAccept = false;
        userOffer = new ArrayList<Card>();
        mentionAccept = false;
        mentionOffer = new ArrayList<Card>();
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public String getMentionName() { return mentionName; }
    public boolean getUserAccept() { return userAccept; }
    public ArrayList<Card> getUserOffer() { return userOffer; }
    public boolean getMentionAccept() { return mentionAccept; }
    public ArrayList<Card> getMentionOffer() { return mentionOffer; }

    public static boolean isUser(String authorId, TradeDisplay disp) {
        if(authorId.equals(disp.getUserId())) {
            return true;
        }
        return false;
    }

    public static boolean tradeExists(String authorId) {
        for(TradeDisplay t : displays) {
            if(t.getUserId().equals(authorId) || t.getMention().getUserId().equals(authorId)) {
                return true;
            }
        }
        return false;
    }

    public static TradeDisplay findTradeDisplay(String authorId) {
        for(TradeDisplay t : displays) {
            if(t.getUserId().equals(authorId) || t.getMention().getUserId().equals(authorId)) {
                return t;
            }
        }
        return null;
    }

    public static void addTradeDisplay(User user, User mention, String mentionName) {
        removeTradeDisplay(user);
        displays.add(new TradeDisplay(user.getUserId(), user, mention, mentionName));
    }

    public static void removeTradeDisplay(User user) {
        for(int i = 0; i < displays.size(); i++) {
            if(displays.get(i).getUserId().equals(user.getUserId())) {
                displays.remove(i);
                break;
            }
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        TradeDisplay disp = findTradeDisplay(user.getUserId());
        User mention = disp.getMention();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += ui.getUserName() + "'s offer:\n\n";

        for(Card c : disp.getUserOffer()) {
            desc += c.getData().getCardName() + "\n";
        }

        desc += disp.getMentionName() + "'s offer:\n\n";

        for(Card c : disp.getMentionOffer()) {
            desc += c.getData().getCardName() + "\n";
        }

        embed.setTitle(ui.getUserName() + " trades " + disp.getMentionName());
        embed.setDescription(desc);
        embed.setFooter("yes");
        embed.setColor(0x000000);
        return embed;
    }

}*/
