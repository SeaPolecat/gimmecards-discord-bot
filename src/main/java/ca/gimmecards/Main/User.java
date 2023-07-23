package ca.gimmecards.Main;
import ca.gimmecards.MainInterfaces.*;
import ca.gimmecards.OtherInterfaces.*;
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

public class User implements IUser {

    /**
     * a list of type User that's saved and loaded from Users.json; edited on a regular basis
     */
    public static ArrayList<User> users = new ArrayList<User>();
    
    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String userId;                              // the player's Discord ID
    private Integer gameColor;                          // the colour that the player's embeds appear in (can be selected in-game)
    private Integer cardCount;                          // a number that increases with every card a player gets (doesn't equal the total # of cards the player has)
    private Integer level;                              // in-game level
    private Integer XP;                                 // amount of XP the player has
    private Integer maxXP;                              // amount of XP the player needs to reach the next level
    private Integer tokens;                             // number of tokens the player has
    private Integer credits;                            // number of credits the player has
    private Integer stars;                              // number of stars the player has
    private Integer keys;                               // number of keys the player has
    private Long openEpoch;                             // used to keep track of the cooldown of /open
    private long voteEpoch;                             // used to keep track of the cooldown of /vote and /claim
    private Long dailyEpoch;                            // used to keep track of the cooldown of /daily
    private Long redeemEpoch;                           // used to keep track of the cooldown of /redeem
    private Long minigameEpoch;                         // used to keep track of the cooldown of /minigame
    private Long marketEpoch;                           // used to keep track of the cooldown of /buy
    private String sortMethod;                          // the card sorting method that the player is using (can be chosen in-game)
    private Boolean isSortIncreasing;                   // whether the player's sorting method is increasing or decreasing (can be chosen in-game)
    private String pinnedCard;                          // the image link of the card that's pinned to the player's backpack
    private ArrayList<String> badges;                   // list of names of the badges the player owns
    private ArrayList<String> packs;                    // list of names of the packs the player owns
    private ArrayList<CardContainer> cardContainers;    // the player's cards

    //=============================================[ CONSTRUCTORS ]====================================================================
    
    /**
     * creates a new User
     * @param userId the player's Discord ID
     */
    public User(String userId) {
        this.userId = userId;
        this.gameColor = 0;
        this.cardCount = 0;
        this.level = 1;
        this.XP = 0;
        this.maxXP = 500;
        this.tokens = 1;
        this.credits = 0;
        this.stars = 0;
        this.keys = 1;
        this.openEpoch = (long)(0);
        this.voteEpoch = (long)(0);
        this.dailyEpoch = (long)(0);
        this.redeemEpoch = (long)(0);
        this.minigameEpoch = (long)(0);
        this.marketEpoch = (long)(0);
        this.sortMethod = "newest";
        this.isSortIncreasing = true;
        this.pinnedCard = "";
        this.badges = new ArrayList<String>();
        this.packs = new ArrayList<String>();
        this.cardContainers = new ArrayList<CardContainer>();
    }

    /**
     * duplicates a User, and encrypts their Discord ID to comply with Discord security guidelines
     * @param user the User to duplicate
     */
    public User(User user) {
        this.userId = Main.encryptor.encrypt(user.getUserId());
        this.gameColor = user.getGameColor();
        this.cardCount = user.getCardCount();
        this.level = user.getLevel();
        this.XP = user.getXP();
        this.maxXP = user.getMaxXP();
        this.tokens = user.getTokens();
        this.credits = user.getCredits();
        this.stars = user.getStars();
        this.keys = user.getKeys();
        this.openEpoch = user.getOpenEpoch();
        this.voteEpoch = user.getVoteEpoch();
        this.dailyEpoch = user.getDailyEpoch();
        this.redeemEpoch = user.getRedeemEpoch();
        this.minigameEpoch = user.getMinigameEpoch();
        this.marketEpoch = user.getMarketEpoch();
        this.sortMethod = user.getSortMethod();
        this.isSortIncreasing = user.getIsSortIncreasing();
        this.pinnedCard = user.getPinnedCard();
        this.badges = user.getBadges();
        this.packs = user.getPacks();
        this.cardContainers = user.getCardContainers();
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getUserId() { return this.userId; }
    public int getGameColor() { return this.gameColor; }
    public int getCardCount() { return this.cardCount; }
    public int getLevel() { return this.level; }
    public int getXP() { return this.XP; }
    public int getMaxXP() { return this.maxXP; }
    public int getTokens() { return this.tokens; }
    public int getCredits() { return this.credits; }
    public int getStars() { return this.stars; }
    public int getKeys() { return this.keys; }
    public long getOpenEpoch() { return this.openEpoch; }
    public long getVoteEpoch() { return this.voteEpoch; }
    public long getDailyEpoch() { return this.dailyEpoch; }
    public long getRedeemEpoch() { return this.redeemEpoch; }
    public long getMinigameEpoch() { return this.minigameEpoch; }
    public Long getMarketEpoch() { return this.marketEpoch; }
    public String getSortMethod() { return this.sortMethod; }
    public boolean getIsSortIncreasing() { return this.isSortIncreasing; }
    public String getPinnedCard() { return this.pinnedCard; }
    public ArrayList<String> getBadges() { return this.badges; }
    public ArrayList<String> getPacks() { return this.packs; }
    public ArrayList<CardContainer> getCardContainers() { return this.cardContainers; }
    
    //================================================[ SETTERS ]======================================================================

    public void setUserId(String userId) { this.userId = userId; }
    public void setGameColor(int gameColor) { this.gameColor = gameColor; }
    public void addCardCount() { this.cardCount++; }
    public void addXP(int XP) { this.XP += XP; }
    public void addTokens(int tokens) { this.tokens += tokens; }
    public void addCredits(int credits) { this.credits += credits; }
    public void addStars(int stars) { this.stars += stars; }
    public void addKeys(int keys) { this.keys += keys; }
    public void resetOpenEpoch() { this.openEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetVoteEpoch() { this.voteEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetDailyEpoch() { this.dailyEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetRedeemEpoch() { this.redeemEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetMinigameEpoch() { this.minigameEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void resetMarketEpoch() { this.marketEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }
    public void setSortMethod(String sortMethod) { this.sortMethod = sortMethod; }
    public void setIsSortIncreasing(boolean isSortIncreasing) { this.isSortIncreasing = isSortIncreasing; }
    public void setPinnedCard(String pinnedCard) { this.pinnedCard = pinnedCard; }

    //=============================================[ PUBLIC STATIC FUNCTIONS ]==============================================================
    
    /**
     * loads data from Users.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
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
     * saves data from the ArrayList at the top into Users.json
     * @throws Exception ignores all possible exceptions
     */
    public static void saveUsers() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameManager.findSavePath(GameManager.userPath)), "UTF-8");
        ArrayList<User> encryptedUsers = new ArrayList<User>();

        for(User u : users) {
            encryptedUsers.add(new User(u));
        }
        gson.toJson(encryptedUsers, writer);
        writer.close();
    }

    /**
     * finds the author User based on a slash event
     * @param event the slash event
     * @return the author User
     */
    public static User findUser(SlashCommandInteractionEvent event) {
        String discordId = event.getUser().getId();
        
        return searchForUser(discordId);
    }

    /**
     * finds the author User based on a button event
     * @param event the button event
     * @return the author User
     */
    public static User findUser(ButtonInteractionEvent event) {
        String discordId = event.getUser().getId();

        return searchForUser(discordId);
    }

    /**
     * finds a mentioned User based on a slash event
     * @param event the slash event
     * @param otherUserId the Discord ID of the mentioned User
     * @return the mentioned User
     */
    public static User findOtherUser(SlashCommandInteractionEvent event, String otherUserId) {
        return searchForUser(otherUserId);
    }

    /**
     * checks if a specific command's cooldown is complete
     * @param epoch one of the player's epoch times above
     * @param cooldown the cooldown of the command that's being checked
     * @param isMins whether the cooldown is measured in minutes or seconds
     * @return whether the cooldown is complete
     */
    public static boolean isCooldownDone(Long epoch, int cooldown, boolean isMins) {
        long current = isMins ? Calendar.getInstance().getTimeInMillis() / 60000 : Calendar.getInstance().getTimeInMillis() / 1000;

        if(current - epoch >= cooldown) {
            return true;
        }
        return false;
    }

    /**
     * finds the amount of cooldown time left for a specific command
     * @param epoch one of the player's epoch times above
     * @param cooldown the cooldown of the command that's being checked
     * @param isMins whether the cooldown is measured in minutes or seconds
     * @return a string message telling the player how much cooldown time is left for the command they're using
     */
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
    public void levelUp() {
        int extraXP = this.XP - this.maxXP;

        this.level++;
        this.XP = 0;
        this.XP += extraXP;
        this.maxXP += 500;
    }
    
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
    public String updateXP(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.XP += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += "+ " + GameManager.formatNumber(quantity);
        msg += " " + IEmotes.XP + " **XP**";

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
    public void addSingleCard(Card card, boolean shouldAutoFav) {
        CardContainer newCard = null;

        this.cardCount++;
        for(CardContainer cc : this.cardContainers) {
            String cardId = cc.getCard().getCardId();

            if(card.getCardId().equals(cardId)) {
                cc.addCardQuantity();
                break;
            }
        }
        newCard = new CardContainer(card, getCardCount(), card.isCardSellable());

        if(shouldAutoFav) {
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
        int shinyChance = new Random().nextInt(100) + 1;

        for(int i = 0; i < 6; i++) {
            newCards.add(Card.pickCard(commons));
        }
        for(int i = 0; i < 3; i++) {
            newCards.add(Card.pickCard(uncommons));
        }
        if(shinyChance <= 10) {
            newCards.add(Card.pickCard(shinies));
        } else {
            newCards.add(Card.pickCard(rares));
        }
        for(Card card : newCards) {
            boolean exists = false;

            this.cardCount++;
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
    public void sortCards(String methodToSort, boolean shouldSortIncreasing) {

        for(int i = 0; i < this.cardContainers.size() - 1; i++) {
            for(int k = i + 1; k < this.cardContainers.size(); k++) {
                Card card1 = this.cardContainers.get(i).getCard();
                Card card2 = this.cardContainers.get(k).getCard();

                if(methodToSort.equalsIgnoreCase("alphabetical")) {
                    String cardName1 = card1.getCardName();
                    String cardName2 = card2.getCardName();
    
                    if(shouldSortIncreasing) {
                        if(cardName1.compareToIgnoreCase(cardName2) > 0) {
                            swapCards(i, k);
                        }
                    } else {
                        if(cardName1.compareToIgnoreCase(cardName2) < 0) {
                            swapCards(i, k);
                        }
                    }

                } else if(methodToSort.equalsIgnoreCase("xp")) {
                    int xp1 = card1.getCardPrice();
                    int xp2 = card2.getCardPrice();
    
                    if(shouldSortIncreasing) {
                        if(xp1 > xp2) {
                            swapCards(i, k);
                        }
                    } else {
                        if(xp1 < xp2) {
                            swapCards(i, k);
                        }
                    }

                } else if(methodToSort.equalsIgnoreCase("quantity")) {
                    int quantity1 = this.cardContainers.get(i).getCardQuantity();
                    int quantity2 = this.cardContainers.get(k).getCardQuantity();
    
                    if(shouldSortIncreasing) {
                        if(quantity1 > quantity2) {
                            swapCards(i, k);
                        }
                    } else {
                        if(quantity1 < quantity2) {
                            swapCards(i, k);
                        }
                    }

                } else if(methodToSort.equalsIgnoreCase("newest")) {
                    int num1 = this.cardContainers.get(i).getCardNum();
                    int num2 = this.cardContainers.get(k).getCardNum();

                    if(shouldSortIncreasing) {
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
    public boolean ownsPinnedCard() {
        for(CardContainer card : this.cardContainers) {
            String cardImage = card.getCard().getCardImage();

            if(this.pinnedCard.equals(cardImage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ownsAnyShopPack() {
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

    //===============================================[ PRIVATE FUNCTIONS ]=============================================================
    
    /**
     * searches through the User list for a specific player
     * @param userId the ID of the player to search for
     * @return the player to be searched
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

    /**
     * swaps two CardContainers within the player's CardContainer list
     * @param i1 first index
     * @param i2 second index
     */
    private void swapCards(int i1, int i2) {
        CardContainer temp = this.cardContainers.get(i1);
        
        this.cardContainers.set(i1, this.cardContainers.get(i2));
        this.cardContainers.set(i2, temp);
    }
}
