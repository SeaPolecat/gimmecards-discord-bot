package ca.gimmecards.Main;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Helpers.*;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import java.util.ArrayList;
import java.util.Calendar;

public class User implements StoragePaths, Emotes {

    public static ArrayList<User> users = new ArrayList<User>();
    //
    private String userId; //ENCRYPTED
    private Integer gameColor;
    private Integer cardCount;
    private Integer level;
    private Integer XP;
    private Integer maxXP;
    private Integer tokens;
    private Integer energy;
    private Integer keys;
    private Integer stars;
    private Long openEpoch;
    private long voteEpoch;
    private Long dailyEpoch;
    private Long redeemEpoch;
    private Long minigameEpoch;
    private Long marketEpoch;
    private String sortMethod;
    private Boolean sortIncreasing;
    private ArrayList<String> badges;
    private String pinCard;
    private ArrayList<String> packs;
    private ArrayList<Card> cards;

    public User(String ui) {
        userId = Main.encryptor.encrypt(ui);
        gameColor = 0;
        cardCount = 0;
        level = 1;
        XP = 0;
        maxXP = 500;
        tokens = 1;
        energy = 0;
        keys = 1;
        stars = 0;
        openEpoch = (long)(0);
        voteEpoch = (long)(0);
        dailyEpoch = (long)(0);
        redeemEpoch = (long)(0);
        minigameEpoch = (long)(0);
        marketEpoch = (long)(0);
        sortMethod = "newest";
        sortIncreasing = true;
        badges = new ArrayList<String>();
        pinCard = "";
        packs = new ArrayList<String>();
        cards = new ArrayList<Card>();
    }

    public String getUserId() { return Main.encryptor.decrypt(userId); }
    public int getGameColor() { return gameColor; }
    public int getCardCount() { return cardCount; }
    public int getLevel() { return level; }
    public int getXP() { return XP; }
    public int getMaxXP() { return maxXP; }
    public int getTokens() { return tokens; }
    public int getEnergy() { return energy; }
    public int getKeys() { return keys; }
    public int getStars() { return stars; }
    public long getOpenEpoch() { return openEpoch; }
    public long getVoteEpoch() { return voteEpoch; }
    public long getDailyEpoch() { return dailyEpoch; }
    public long getRedeemEpoch() { return redeemEpoch; }
    public long getMinigameEpoch() { return minigameEpoch; }
    public Long getMarketEpoch() { return marketEpoch; }
    public String getSortMethod() { return sortMethod; }
    public boolean getSortIncreasing() { return sortIncreasing; }
    public ArrayList<String> getBadges() { return badges; }
    public String getPinCard() { return pinCard; }
    public ArrayList<String> getPacks() { return packs; }
    public ArrayList<Card> getCards() { return cards; }
    //
    public void setGameColor(int gc) { gameColor = gc; }
    public void addCardCount() { cardCount++; }
    public void addXP(int xp) { XP += xp; }
    public void addTokens(int t) { tokens += t; }
    public void addEnergy(int e) { energy += e; }
    public void addKeys(int k) { keys += k; }
    public void addStars(int s) { stars += s; }
    public void resetOpenEpoch() { openEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetVoteEpoch() { voteEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetDailyEpoch() { dailyEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetRedeemEpoch() { redeemEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetMinigameEpoch() { minigameEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetMarketEpoch() { marketEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void setSortMethod(String sm) { sortMethod = sm; }
    public void setSortIncreasing(boolean si) { sortIncreasing = si; }
    public void setPinCard(String pc) { pinCard = pc; }

    public void levelUp() {
        level++;
        XP = 0;
        XP += XP - maxXP;
        maxXP += 500;
    }

    public String updateXP(int quantity, boolean isStart) {
        String msg = "\n";

        addXP(quantity);
        if(isStart) {
            msg += "┅┅\n";
        }
        msg += "+ " + UX.formatNumber(quantity);
        msg += " " + XP_ + " **XP**";

        return msg;
    }

    public String updateTokens(int quantity, boolean isStart) {
        String msg = "\n";

        addTokens(quantity);
        if(isStart) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "- ";
        msg += UX.formatNumber(Math.abs(quantity));

        if(quantity > 1 || quantity < -1) {
            msg += " " + token_ + " **Tokens**";
        } else {
            msg += " " + token_ + " **Token**";
        }
        return msg;
    }

    public String updateEnergy(int quantity, boolean isStart) {
        String msg = "\n";

        addEnergy(quantity);
        if(isStart) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "- ";
        msg += UX.formatNumber(Math.abs(quantity));
        msg += " " + energy_ + " **Credits**";

        return msg;
    }

    public String updateStars(int quantity, boolean isStart) {
        String msg = "\n";

        addStars(quantity);
        if(isStart) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "- ";
        msg += UX.formatNumber(Math.abs(quantity));

        if(quantity > 1 || quantity < -1) {
            msg += " " + star_ + " **Stars**";
        } else {
            msg += " " + star_ + " **Star**";
        }
        return msg;
    }

    public String updateKeys(int quantity, boolean isStart) {
        String msg = "\n";

        addKeys(quantity);
        if(isStart) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "- ";
        msg += UX.formatNumber(Math.abs(quantity));
        msg += " " + key_ + " **Key**";

        return msg;
    }

    private static String determinePath() {
        if(Paths.get(userPath).toFile().length() > 0) {
            return userPath;
        } else {
            return header + userPath;
        }
    }

    public static void loadUsers() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath()), "UTF-8");
        users = new Gson().fromJson(reader, new TypeToken<ArrayList<User>>() {}.getType());

        reader.close();
    }

    public static void saveUsers() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath()), "UTF-8");
        gson.toJson(users, writer);
        writer.close();
    }

    public static User findUser(MessageReceivedEvent event) {
        String authorId = event.getAuthor().getId();
        
        return searchForUser(authorId);
    }

    public static User findUser(MessageReactionAddEvent event) {
        String authorId = event.getUser().getId();

        return searchForUser(authorId);
    }

    public static User findOtherUser(MessageReceivedEvent event, String userId) {
        return searchForUser(userId);
    }

    private static User searchForUser(String userId) {
        for(User u : users) {
            if(u.getUserId().equals(userId)) {
                return u;
            }
        }
        users.add(0, new User(userId));
        return users.get(0);
    }
}
