package ca.gimmecards.Main;
import ca.gimmecards.Helpers.*;
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
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import java.util.ArrayList;
import java.util.Calendar;

public class User implements StoragePaths, Emotes {

    // list of Users
    public static ArrayList<User> users = new ArrayList<User>();
    
    // instance variables
    private String userId;
    private Integer gameColor;
    private Integer cardCount;
    private Integer level;
    private Integer XP;
    private Integer maxXP;
    private Integer tokens;
    private Integer credits;
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
    private Boolean isRare;
    private Boolean isRadiantRare;

    /**
     * constructor for User
     * @param ui userId
     */
    public User(String ui) {
        userId = ui;
        gameColor = 0;
        cardCount = 0;
        level = 1;
        XP = 0;
        maxXP = 500;
        tokens = 1;
        credits = 0;
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
        isRare = false;
        isRadiantRare = false;
    }

    /**
     * constructor that duplicates a User, and encrypts userId
     * @param user the User to duplicate
     */
    public User(User user) {
        userId = Main.encryptor.encrypt(user.getUserId());
        gameColor = user.getGameColor();
        cardCount = user.getCardCount();
        level = user.getLevel();
        XP = user.getXP();
        maxXP = user.getMaxXP();
        tokens = user.getTokens();
        credits = user.getCredits();
        keys = user.getKeys();
        stars = user.getStars();
        openEpoch = user.getOpenEpoch();
        voteEpoch = user.getVoteEpoch();
        dailyEpoch = user.getDailyEpoch();
        redeemEpoch = user.getRedeemEpoch();
        minigameEpoch = user.getMinigameEpoch();
        marketEpoch = user.getMarketEpoch();
        sortMethod = user.getSortMethod();
        sortIncreasing = user.getSortIncreasing();
        badges = user.getBadges();
        pinCard = user.getPinCard();
        packs = user.getPacks();
        cards = user.getCards();
        isRare = user.getIsRare();
        isRadiantRare = user.getIsRadiantRare();
    }

    // getters
    public String getUserId() { return userId; }
    public int getGameColor() { return gameColor; }
    public int getCardCount() { return cardCount; }
    public int getLevel() { return level; }
    public int getXP() { return XP; }
    public int getMaxXP() { return maxXP; }
    public int getTokens() { return tokens; }
    public int getCredits() { return credits; }
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
    public boolean getIsRare() { return isRare; }
    public boolean getIsRadiantRare() { return isRadiantRare; }
    
    // setters
    public void setUserId(String ui) { userId = ui; }
    public void setGameColor(int gc) { gameColor = gc; }
    public void addCardCount() { cardCount++; }
    public void addXP(int xp) { XP += xp; }
    public void addTokens(int t) { tokens += t; }
    public void addCredits(int c) { credits += c; }
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
    public void setIsRare(boolean ir) { isRare = ir; }
    public void setIsRadiantRare(boolean irr) { isRadiantRare = irr; }

    /**
     * setter that handles a player levelling up
     */
    public void levelUp() {
        int extraXP = XP - maxXP;

        level++;
        XP = 0;
        XP += extraXP;
        maxXP += 500;
    }

    /**
     * checks whether or not it's the weekend
     * @return true if it's the weekend, false otherwise
     */
    private boolean isWeekend() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if(day == 7 || day == 1) {
            return true;
        }
        return false;
    }

    /**
     * handles a badge being removed
     * @param badge the badge name
     */
    public void removeBadge(String badge) {
        for(int i = 0; i < badges.size(); i++) {
            if(badges.get(i).equalsIgnoreCase(badge)) {
                badges.remove(i);
            }
        }
    }

    /**
     * handles XP
     * @param quantity the amount of XP to give
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
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

    /**
     * handles tokens
     * @param quantity the amount of tokens to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateTokens(int quantity, boolean isStart) {
        String msg = "\n";

        if(getIsRadiantRare() && isWeekend() && quantity > 0) {
            quantity = quantity * 2;
        }

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

    /**
     * handles credits
     * @param quantity the amount of credits to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateCredits(int quantity, boolean isStart) {
        String msg = "\n";

        if(getIsRare() || getIsRadiantRare() && isWeekend() && quantity > 0) {
            quantity = (int)(quantity * 1.25);
        }

        addCredits(quantity);
        if(isStart) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "- ";
        msg += UX.formatNumber(Math.abs(quantity));
        msg += " " + credits_ + " **Credits**";

        return msg;
    }

    /**
     * handles stars
     * @param quantity the amount of stars to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
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

    /**
     * handles keys
     * @param quantity the amount of keys to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
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

    /**
     * determines the path that User data will be saved to
     * @return the path with header if the file is in an external location, and no header if it's local
     */
    private static String determinePath() {
        if(Paths.get(userPath).toFile().length() > 0) {
            return userPath;
        } else {
            return header + userPath;
        }
    }

    /**
     * loads player data from Users.json into the User list
     * @throws Exception just needs it
     */
    public static void loadUsers() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath()), "UTF-8");
        users = new Gson().fromJson(reader, new TypeToken<ArrayList<User>>() {}.getType());

        for(User u : users) {
            u.setUserId(Main.encryptor.decrypt(u.getUserId()));
        }
        reader.close();
    }

    /**
     * saves player data into the file Users.json
     * @throws Exception just needs it
     */
    public static void saveUsers() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath()), "UTF-8");
        ArrayList<User> encUsers = new ArrayList<User>();

        for(User u : users) {
            encUsers.add(new User(u));
        }
        gson.toJson(encUsers, writer);
        writer.close();
    }

    /**
     * finds the author User based on a slash event
     * @param event the slash event
     * @return the author User
     */
    public static User findUser(SlashCommandInteractionEvent event) {
        String authorId = event.getUser().getId();
        
        return searchForUser(authorId);
    }

    /**
     * finds the author User based on a button event
     * @param event the button event
     * @return the author User
     */
    public static User findUser(ButtonInteractionEvent event) {
        String authorId = event.getUser().getId();

        return searchForUser(authorId);
    }

    /**
     * finds a mentioned User based on a slash event
     * @param event the slash event
     * @param userId the ID of the mentioned User
     * @return the mentioned User
     */
    public static User findOtherUser(SlashCommandInteractionEvent event, String userId) {
        return searchForUser(userId);
    }

    /**
     * searches through the User list for a specific User
     * @param userId the ID of the User to search for
     * @return the User to be searched
     */
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
