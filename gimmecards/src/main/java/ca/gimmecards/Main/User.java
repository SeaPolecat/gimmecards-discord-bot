package ca.gimmecards.Main;
import ca.gimmecards.Interfaces.*;
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

public class User implements StoragePaths {

    public static ArrayList<User> users = new ArrayList<User>();
    //
    private String userId; //ENCRYPTED
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
    private String sortMethod;
    private Boolean sortIncreasing;
    private ArrayList<String> badges;
    private Integer backpackColor;
    private String backpackCard;
    private ArrayList<String> packs;
    private ArrayList<Card> cards;

    public User(String ui) {
        userId = Main.encryptor.encrypt(ui);
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
        sortMethod = "newest";
        sortIncreasing = true;
        badges = new ArrayList<String>();
        backpackColor = 0;
        backpackCard = "";
        packs = new ArrayList<String>();
        cards = new ArrayList<Card>();
    }

    public String getUserId() { return Main.encryptor.decrypt(userId); }
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
    public String getSortMethod() { return sortMethod; }
    public boolean getSortIncreasing() { return sortIncreasing; }
    public ArrayList<String> getBadges() { return badges; }
    public int getBackpackColor() { return backpackColor; }
    public String getBackpackCard() { return backpackCard; }
    public ArrayList<String> getPacks() { return packs; }
    public ArrayList<Card> getCards() { return cards; }
    //
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
    public void setSortMethod(String sm) { sortMethod = sm; }
    public void setSortIncreasing(boolean si) { sortIncreasing = si; }
    public void setBackpackColor(int bColor) { backpackColor = bColor; }
    public void setBackpackCard(String bCard) { backpackCard = bCard; }

    public void levelUp() {
        int extraXP = XP - maxXP;

        level++;
        XP = 0;
        XP += extraXP;
        maxXP += 500;
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

    public static User findOtherUser(MessageReceivedEvent event, String authorId) {
        return searchForUser(authorId);
    }

    private static User searchForUser(String authorId) {
        for(User u : users) {
            if(u.getUserId().equals(authorId)) {
                return u;
            }
        }
        users.add(0, new User(authorId));
        return users.get(0);
    }
}
