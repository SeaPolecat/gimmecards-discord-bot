package ca.gimmecards.Main;
import ca.gimmecards.MainInterfaces.*;
import ca.gimmecards.OtherInterfaces.IEmotes;
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
import java.util.Random;

// make formatUserName public static?

public class User implements IUser {

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
    private Integer stars;
    private Integer keys;
    private Long openEpoch;
    private long voteEpoch;
    private Long dailyEpoch;
    private Long redeemEpoch;
    private Long minigameEpoch;
    private Long marketEpoch;
    private String sortMethod;
    private Boolean isSortIncreasing;
    private String pinnedCard;
    private ArrayList<String> badges;
    private ArrayList<String> packs;
    private ArrayList<CardContainer> cardContainers;

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
        stars = 0;
        keys = 1;
        openEpoch = (long)(0);
        voteEpoch = (long)(0);
        dailyEpoch = (long)(0);
        redeemEpoch = (long)(0);
        minigameEpoch = (long)(0);
        marketEpoch = (long)(0);
        sortMethod = "newest";
        isSortIncreasing = true;
        pinnedCard = "";
        badges = new ArrayList<String>();
        packs = new ArrayList<String>();
        cardContainers = new ArrayList<CardContainer>();
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
        stars = user.getStars();
        keys = user.getKeys();
        openEpoch = user.getOpenEpoch();
        voteEpoch = user.getVoteEpoch();
        dailyEpoch = user.getDailyEpoch();
        redeemEpoch = user.getRedeemEpoch();
        minigameEpoch = user.getMinigameEpoch();
        marketEpoch = user.getMarketEpoch();
        sortMethod = user.getSortMethod();
        isSortIncreasing = user.getIsSortIncreasing();
        pinnedCard = user.getPinnedCard();
        badges = user.getBadges();
        packs = user.getPacks();
        cardContainers = user.getCardContainers();
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
    public int getStars() { return stars; }
    public int getKeys() { return keys; }
    public long getOpenEpoch() { return openEpoch; }
    public long getVoteEpoch() { return voteEpoch; }
    public long getDailyEpoch() { return dailyEpoch; }
    public long getRedeemEpoch() { return redeemEpoch; }
    public long getMinigameEpoch() { return minigameEpoch; }
    public Long getMarketEpoch() { return marketEpoch; }
    public String getSortMethod() { return sortMethod; }
    public boolean getIsSortIncreasing() { return isSortIncreasing; }
    public String getPinnedCard() { return pinnedCard; }
    public ArrayList<String> getBadges() { return badges; }
    public ArrayList<String> getPacks() { return packs; }
    public ArrayList<CardContainer> getCardContainers() { return cardContainers; }
    
    // setters
    public void setUserId(String ui) { userId = ui; }
    public void setGameColor(int gc) { gameColor = gc; }
    public void addCardCount() { cardCount++; }
    public void addXP(int xp) { XP += xp; }
    public void addTokens(int t) { tokens += t; }
    public void addCredits(int c) { credits += c; }
    public void addStars(int s) { stars += s; }
    public void addKeys(int k) { keys += k; }
    public void resetOpenEpoch() { openEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetVoteEpoch() { voteEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetDailyEpoch() { dailyEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetRedeemEpoch() { redeemEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetMinigameEpoch() { minigameEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetMarketEpoch() { marketEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void setSortMethod(String sm) { sortMethod = sm; }
    public void setIsSortIncreasing(boolean isi) { isSortIncreasing = isi; }
    public void setPinnedCard(String pc) { pinnedCard = pc; }

    //===================================================[ PRIVATE FUNCTIONS ]==============================================================

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

    private void swapCards(int i1, int i2) {
        CardContainer temp = this.cardContainers.get(i1);
        
        this.cardContainers.set(i1, this.cardContainers.get(i2));
        this.cardContainers.set(i2, temp);
    }

    //=============================================[ PUBLIC STATIC FUNCTIONS ]==============================================================

    /**
     * loads player data from Users.json into the User list
     * @throws Exception just needs it
     */
    public static void loadUsers() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameManager.findSavePath(GameManager.userPath)), "UTF-8");
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
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameManager.findSavePath(GameManager.userPath)), "UTF-8");
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
    public static User findOtherUser(SlashCommandInteractionEvent event, String otherUserId) {
        return searchForUser(otherUserId);
    }

    public static boolean isCooldownDone(Long epoch, int cooldown, boolean isMins) {
        long current = isMins ? Calendar.getInstance().getTimeInMillis() / 60000 : Calendar.getInstance().getTimeInMillis() / 1000;

        if(current - epoch >= cooldown) {
            return true;
        }
        return false;
    }

    public static String findTimeLeft(Long epoch, int cooldown, boolean isMins) {
        long current;

        if(isMins) {
            current = Calendar.getInstance().getTimeInMillis() / 60000;
            long minutes = cooldown - (current - epoch);

            if(minutes > 60) {
                int hours = (int)(minutes / 60);
    
                if(hours < 2) {
                    return "**" + hours + " hour " + (minutes % 60) + " minutes**";
                } else {
                    return "**" + hours + " hours " + (minutes % 60) + " minutes**";
                }
            } else {
                return "**" + minutes + " minutes**";
            }

        } else {
            current = Calendar.getInstance().getTimeInMillis() / 1000;
            long seconds = cooldown - (current - epoch);

            if(seconds > 60) {
                int minutes = (int)(seconds / 60);
    
                if(minutes < 2) {
                    return "**" + minutes + " minute " + (seconds % 60) + " seconds**";
                } else {
                    return "**" + minutes + " minutes " + (seconds % 60) + " seconds**";
                }
            } else {
                return "**" + seconds + " seconds**";
            }
        }
    }

    //==============================================[ PUBLIC NON-STATIC FUNCTIONS ]=====================================================

    @Override
    public String checkLevelUp(SlashCommandInteractionEvent event) {
        int prevLvl = this.level;
        int tokenReward = 0;
        int creditsReward = 0;
        int keyReward = 0;
        int starReward = 0;
        String msg = "";

        if(this.XP >= this.maxXP) {
            msg += "\n┅┅";
            msg += "\n" + GameManager.formatName(event) + "** LEVELED UP :tada:**";

            while(this.XP >= this.maxXP) {
                levelUp();
    
                tokenReward += 2;
                creditsReward += ((this.level + 9) / 10) * 100;
                keyReward++;
                starReward++;
    
                if(this.level == 50) {
                    this.badges.add("veteran");
                } else if(this.level == 100) {
                    this.badges.add("master");
                }
            }
            msg += "\nLevel **" + prevLvl + "** ➜ **" + this.level + "**";
            msg += updateTokens(tokenReward, true);
            msg += updateCredits(creditsReward, false);
            msg += updateKeys(keyReward, false);
            msg += updateStars(starReward, false);
        }
        return msg;
    }

    @Override
    public void levelUp() {
        int extraXP = this.XP - this.maxXP;

        this.level++;
        this.XP = 0;
        this.XP += extraXP;
        this.maxXP += 500;
    }

    @Override
    public String updateXP(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.XP += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += "+ " + GameManager.formatNumber(quantity);
        msg += " " + XP + " **XP**";

        return msg;
    }

    @Override
    public String updateTokens(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.tokens += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += GameManager.formatNumber(Math.abs(quantity));

        if(quantity > 1 || quantity < -1) {
            msg += " " + IEmotes.token + " **Tokens**";
        } else {
            msg += " " + IEmotes.token + " **Token**";
        }
        return msg;
    }

    @Override
    public String updateCredits(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.credits += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += GameManager.formatNumber(Math.abs(quantity));
        msg += " " + IEmotes.credits + " **Credits**";

        return msg;
    }

    @Override
    public String updateStars(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.stars += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += GameManager.formatNumber(Math.abs(quantity));

        if(quantity > 1 || quantity < -1) {
            msg += " " + IEmotes.star + " **Stars**";
        } else {
            msg += " " + IEmotes.star + " **Star**";
        }
        return msg;
    }

    @Override
    public String updateKeys(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.keys += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += GameManager.formatNumber(Math.abs(quantity));
        msg += " " + IEmotes.key + " **Key**";

        return msg;
    }

    @Override
    public void addSingleCard(Card card, boolean isFav) {
        CardContainer newCard = null;

        addCardCount();
        for(CardContainer cc : this.cardContainers) {
            String cardId = cc.getCard().getCardId();

            if(card.getCardId().equals(cardId)) {
                cc.addCardQuantity();
                break;
            }
        }
        newCard = new CardContainer(card, getCardCount(), card.isCardSellable());

        if(isFav) {
            newCard.setIsFav(true);
        }
        this.cardContainers.add(newCard);
        sortCards(this.sortMethod, this.isSortIncreasing);
    }

    @Override
    public ArrayList<Card> addNewCards(CardSet set) {
        ArrayList<Card> commons = set.getCommons();
        ArrayList<Card> uncommons = set.getUncommons();
        ArrayList<Card> rares = set.getRares();
        ArrayList<Card> shinies = set.getShinies();
        ArrayList<Card> newCards = new ArrayList<Card>();
        int chance = new Random().nextInt(100) + 1;

        for(int i = 0; i < 6; i++) {
            newCards.add(Card.pickCard(commons));
        }
        for(int i = 0; i < 3; i++) {
            newCards.add(Card.pickCard(uncommons));
        }
        if(chance <= 10) {
            newCards.add(Card.pickCard(shinies));
        } else {
            newCards.add(Card.pickCard(rares));
        }
        for(Card card : newCards) {
            boolean exists = false;

            addCardCount();
            for(CardContainer cc : this.cardContainers) {
                String cardId = cc.getCard().getCardId();

                if(card.getCardId().equals(cardId)) {
                    exists = true;
                    cc.addCardQuantity();
                    break;
                }
            }
            if(!exists) {
                this.cardContainers.add(new CardContainer(card, this.cardCount, card.isCardSellable()));
            }
        }
        sortCards(this.sortMethod, this.isSortIncreasing);
        return newCards;
    }

    @Override
    public void sortCards(String sortMethod, boolean sortIncreasing) {

        for(int i = 0; i < this.cardContainers.size() - 1; i++) {
            for(int k = i + 1; k < this.cardContainers.size(); k++) {
                Card card1 = this.cardContainers.get(i).getCard();
                Card card2 = this.cardContainers.get(k).getCard();

                if(sortMethod.equalsIgnoreCase("alphabetical")) {
                    String cardName1 = card1.getCardName();
                    String cardName2 = card2.getCardName();
    
                    if(sortIncreasing) {
                        if(cardName1.compareToIgnoreCase(cardName2) > 0) {
                            swapCards(i, k);
                        }
                    } else {
                        if(cardName1.compareToIgnoreCase(cardName2) < 0) {
                            swapCards(i, k);
                        }
                    }

                } else if(sortMethod.equalsIgnoreCase("xp")) {
                    int xp1 = card1.getCardPrice();
                    int xp2 = card2.getCardPrice();
    
                    if(sortIncreasing) {
                        if(xp1 > xp2) {
                            swapCards(i, k);
                        }
                    } else {
                        if(xp1 < xp2) {
                            swapCards(i, k);
                        }
                    }

                } else if(sortMethod.equalsIgnoreCase("quantity")) {
                    int quantity1 = this.cardContainers.get(i).getCardQuantity();
                    int quantity2 = this.cardContainers.get(k).getCardQuantity();
    
                    if(sortIncreasing) {
                        if(quantity1 > quantity2) {
                            swapCards(i, k);
                        }
                    } else {
                        if(quantity1 < quantity2) {
                            swapCards(i, k);
                        }
                    }

                } else if(sortMethod.equalsIgnoreCase("newest")) {
                    int num1 = this.cardContainers.get(i).getCardNum();
                    int num2 = this.cardContainers.get(k).getCardNum();

                    if(sortIncreasing) {
                        if(num1 > num2) {
                            swapCards(i, k);
                        }
                    } else {
                        if(num1 < num2) {
                            swapCards(i, k);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean ownsCard(Card card) {
        for(CardContainer cc : this.cardContainers) {
            String cardId = cc.getCard().getCardId();

            if(cardId.equals(card.getCardId()) && cc.getCardQuantity() > 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ownsFavCard() {
        for(CardContainer card : this.cardContainers) {
            String cardImage = card.getCard().getCardImage();

            if(this.pinnedCard.equals(cardImage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ownsShopPack() {
        for(String pack : this.packs) {
            for(CardSet set : CardSet.sets) {
                if(pack.equalsIgnoreCase(set.getSetName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean ownsBadge(String badge) {
        for(String b : this.badges) {
            if(b.equalsIgnoreCase(badge)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isPackUnlocked(String setName) {
        for(String pack : this.packs) {
            if(pack.equalsIgnoreCase(setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int countOwnedPacks(boolean isOld) {
        int count = 0;
        CardSet[] sets = isOld ? CardSet.oldSets : CardSet.sets;

        for(String name : this.packs) {
            for(CardSet set : sets) {
                if(name.equalsIgnoreCase(set.getSetName())) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countOwnedCards() {
        int count = 0;

        for(CardContainer card : this.cardContainers) {
            count += card.getCardQuantity();
        }
        return count;
    }
}
