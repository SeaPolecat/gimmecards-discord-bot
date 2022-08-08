package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class TradeDisplay extends Display {

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
        for(TradeDisplay t : tradeDisplays) {
            if(t.getUserId().equals(authorId) || t.getMention().getUserId().equals(authorId)) {
                return true;
            }
        }
        return false;
    }

    public static TradeDisplay findTradeDisplay(String authorId) {
        for(TradeDisplay t : tradeDisplays) {
            if(t.getUserId().equals(authorId) || t.getMention().getUserId().equals(authorId)) {
                return t;
            }
        }
        return null;
    }

    public static void addTradeDisplay(User user, User mention, String mentionName) {
        removeTradeDisplay(user);
        tradeDisplays.add(new TradeDisplay(user.getUserId(), user, mention, mentionName));
    }

    public static void removeTradeDisplay(User user) {
        for(int i = 0; i < tradeDisplays.size(); i++) {
            if(tradeDisplays.get(i).getUserId().equals(user.getUserId())) {
                tradeDisplays.remove(i);
                break;
            }
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += ui.getUserName() + "'s offer:\n\n";

        for(Card c : userOffer) {
            desc += c.getData().getCardName() + "\n";
        }

        desc += mentionName + "'s offer:\n\n";

        for(Card c : mentionOffer) {
            desc += c.getData().getCardName() + "\n";
        }

        embed.setTitle(ui.getUserName() + " trades " + mentionName);
        embed.setDescription(desc);
        embed.setFooter("yes");
        embed.setColor(0x000000);
        return embed;
    }

}
