package ca.gimmecards.main;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class User implements Comparable<User> {

    /**
     * a list of type User that's saved and loaded from Users.json; edited on a regular basis
     */
    public static List<User> users;
    
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
    private Long voteEpoch;                             // used to keep track of the cooldown of /vote and /claim
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
    
    public User(String userId, Integer gameColor, Integer cardCount, Integer level, Integer xP, Integer maxXP,
            Integer tokens, Integer credits, Integer stars, Integer keys, Long openEpoch, Long voteEpoch,
            Long dailyEpoch, Long redeemEpoch, Long minigameEpoch, Long marketEpoch, String sortMethod,
            Boolean isSortIncreasing, String pinnedCard, ArrayList<String> badges, ArrayList<String> packs,
            ArrayList<CardContainer> cardContainers, LinkedList<Display> displays) {
        this.userId = userId;
        this.gameColor = gameColor;
        this.cardCount = cardCount;
        this.level = level;
        this.XP = xP;
        this.maxXP = maxXP;
        this.tokens = tokens;
        this.credits = credits;
        this.stars = stars;
        this.keys = keys;
        this.openEpoch = openEpoch;
        this.voteEpoch = voteEpoch;
        this.dailyEpoch = dailyEpoch;
        this.redeemEpoch = redeemEpoch;
        this.minigameEpoch = minigameEpoch;
        this.marketEpoch = marketEpoch;
        this.sortMethod = sortMethod;
        this.isSortIncreasing = isSortIncreasing;
        this.pinnedCard = pinnedCard;
        this.badges = badges;
        this.packs = packs;
        this.cardContainers = cardContainers;
    }

    /**
     * creates a new User
     * @param userId the player's Discord ID
     */
    public User(String userId) {
        this.userId = Main.encryptor.encrypt(userId);
        this.gameColor = 0;
        this.cardCount = 0;
        this.level = 1;
        this.XP = 0;
        this.maxXP = 500;
        this.tokens = 0;
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

    //===============================================[ GETTERS ] ======================================================================

    public String getUserId() { return Main.encryptor.decrypt(this.userId); }
    public String getUserIdUnencrypted() { return this.userId; } // for testing purposes

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
    public long getMarketEpoch() { return this.marketEpoch; }
    public String getSortMethod() { return this.sortMethod; }
    public boolean getIsSortIncreasing() { return this.isSortIncreasing; }
    public String getPinnedCard() { return this.pinnedCard; }
    public ArrayList<String> getBadges() { return this.badges; }
    public ArrayList<String> getPacks() { return this.packs; }
    public ArrayList<CardContainer> getCardContainers() { return this.cardContainers; }
    
    //================================================[ SETTERS ]======================================================================
    
    public void setGameColor(int gameColor) { this.gameColor = gameColor; }
    public void addCardCount() { this.cardCount++; }
    public void addXP(int XP) { this.XP += XP; }
    public void addTokens(int tokens) { this.tokens += tokens; }
    public void addCredits(int credits) { this.credits += credits; }
    public void addStars(int stars) { this.stars += stars; }
    public void addKeys(int keys) { this.keys += keys; }
    public void resetOpenEpoch() { this.openEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetVoteEpoch() { this.voteEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetDailyEpoch() { this.dailyEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetRedeemEpoch() { this.redeemEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetMinigameEpoch() { this.minigameEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void resetMarketEpoch() { this.marketEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }
    public void setSortMethod(String sortMethod) { this.sortMethod = sortMethod; } 
    public void setIsSortIncreasing(boolean isSortIncreasing) { this.isSortIncreasing = isSortIncreasing; }
    public void setPinnedCard(String pinnedCard) { this.pinnedCard = pinnedCard; }

    //=============================================[ STATIC METHODS ]==============================================================

    /**
     * finds the author User based on a slash event
     * @param event the slash event
     * @return the author User
     */
    public static User findUser(SlashCommandInteractionEvent event) {
        return searchForUser(event.getUser().getId());
    }

    /**
     * finds the author User based on a button event
     * @param event the button event
     * @return the author User
     */
    public static User findUser(ButtonInteractionEvent event) {
        return searchForUser(event.getUser().getId());
    }

    /**
     * finds a mentioned User based on a slash event
     * @param event the slash event
     * @param targetId the Discord ID of the mentioned User
     * @return the mentioned User
     */
    public static User findTargetUser(SlashCommandInteractionEvent event, String targetId) {
        return searchForUser(targetId);
    }

    public static User findTargetUser(ButtonInteractionEvent event, String targetId) {
        return searchForUser(targetId);
    }

    //==============================================[ INSTANCE METHODS ]=====================================================
    
    @Override
    public int compareTo(User other) {
        Long userId = Long.parseLong(this.getUserId());
        Long otherId = Long.parseLong(other.getUserId());

        return userId.compareTo(otherId);
    }

    public static final Comparator<User> BY_LEVEL_DESCENDING = Comparator.<User, Integer> comparing(user -> user.getLevel()).reversed();

    /**
     * handles a player levelling up
     */
    public void levelUp() {
        int extraXP = this.XP - this.maxXP;

        this.level++;
        this.XP = 0;
        this.XP += extraXP;
        this.maxXP += 500;
    }
    
    /**
     * checks whether the player has enough XP to level up
     * @param event a slash event
     * @return a string message telling the player that they levelled up; is attached under any XP message
     */
    public String checkLevelUp(SlashCommandInteractionEvent event) {
        UserInfo ui = new UserInfo(event);
        int prevLvl = this.level;
        int creditsReward = 0;
        int keyReward = 0;
        String msg = "";

        if(this.XP >= this.maxXP) {
            msg += "\n┅┅";
            msg += "\n" + ui.getUserPing() + "** LEVELED UP :tada:**";

            while(this.XP >= this.maxXP) {
                levelUp();

                creditsReward += ((this.level + 9) / 10) * RewardConsts.LEVELUP_CREDITS_MULTFACTOR;
                keyReward += RewardConsts.LEVELUP_KEYS;

                if(this.level == 50) {
                    this.badges.add("veteran");
                } else if(this.level == 100) {
                    this.badges.add("master");
                }
            }
            msg += "\nLevel **" + prevLvl + "** ➜ **" + this.level + "**";
            msg += updateCredits(creditsReward, false);
            msg += updateKeys(keyReward, false);
        }
        return msg;
    }

    /**
     * handles XP
     * @param quantity the amount of XP to give
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their XP changed
     */
    public String updateXP(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.XP += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += "+ " + FormatUtils.formatNumber(quantity);
        msg += " " + EmoteConsts.XP + " **XP**";

        return msg;
    }

    /**
     * handles tokens
     * @param quantity the amount of tokens to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their tokens changed
     */
    public String updateTokens(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.tokens += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += FormatUtils.formatNumber(Math.abs(quantity));

        if(quantity > 1 || quantity < -1) {
            msg += " " + EmoteConsts.TOKEN + " **Tokens**";
        } else {
            msg += " " + EmoteConsts.TOKEN + " **Token**";
        }
        return msg;
    }

    /**
     * handles credits
     * @param quantity the amount of credits to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their credits changed
     */
    public String updateCredits(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.credits += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += FormatUtils.formatNumber(Math.abs(quantity));
        msg += " " + EmoteConsts.CREDITS + " **Credits**";

        return msg;
    }

    /**
     * handles stars
     * @param quantity the amount of stars to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their stars changed
     */
    public String updateStars(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.stars += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += FormatUtils.formatNumber(Math.abs(quantity));

        if(quantity > 1 || quantity < -1) {
            msg += " " + EmoteConsts.STAR + " **Stars**";
        } else {
            msg += " " + EmoteConsts.STAR + " **Star**";
        }
        return msg;
    }

    /**
     * handles keys
     * @param quantity the amount of keys to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their keys changed
     */
    public String updateKeys(int quantity, boolean isAtTop) {
        String msg = "\n";

        this.keys += quantity;
        if(isAtTop) {
            msg += "┅┅\n";
        }
        msg += (quantity > 0) ? "+ " : "\\- ";
        msg += FormatUtils.formatNumber(Math.abs(quantity));
        msg += " " + EmoteConsts.KEY + " **Key**";

        return msg;
    }

    /**
     * adds a single card to the player's CardContainer list
     * @param card the card to add
     * @param shouldAutoFav whether the card should be automatically favorited or not
     */
    public void addSingleCard(Card card, boolean shouldAutoFav) {
        CardContainer newCard = null;

        this.cardCount++;
        for(CardContainer cc : this.cardContainers) {
            String cardId = cc.getCard().getCardId();

            if(card.getCardId().equals(cardId)) {
                cc.addCardQuantity();
                return;
            }
        }
        newCard = new CardContainer(card, getCardCount(), card.isCardSellable());

        if(shouldAutoFav) {
            newCard.setIsFav(true);
        }
        this.cardContainers.add(newCard);
        sortCards();
    }

    /**
     * adds a list of cards to the player's CardContainer list
     * @param set the card set that the list should be picked from
     * @return a list of the new cards
     */
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
        sortCards();
        return newCards;
    }

    /**
     * sorts the player's cards based on either alphabetical, xp, quantity, or newest
     */
    public void sortCards() {
        if(this.sortMethod.equalsIgnoreCase("alphabetical")) {
            if(this.isSortIncreasing)
                Collections.sort(this.cardContainers);
            else
                Collections.sort(this.cardContainers, CardContainer.BY_NAME_DESCENDING);

        } else if(this.sortMethod.equalsIgnoreCase("xp")) {
            if(this.isSortIncreasing)
                Collections.sort(this.cardContainers, CardContainer.BY_XP);
            else
                Collections.sort(this.cardContainers, CardContainer.BY_XP_DESCENDING);
                
        } else if(this.sortMethod.equalsIgnoreCase("quantity")) {
            if(this.isSortIncreasing)
                Collections.sort(this.cardContainers, CardContainer.BY_QUANTITY);
            else
                Collections.sort(this.cardContainers, CardContainer.BY_QUANTITY_DESCENDING);
                
        } else if(this.sortMethod.equalsIgnoreCase("newest")) {
            if(this.isSortIncreasing)
                Collections.sort(this.cardContainers, CardContainer.BY_NEWEST);
            else
                Collections.sort(this.cardContainers, CardContainer.BY_NEWEST_DESCENDING);
        }
    }

    /**
     * checks whether the player owns a certain card
     * @param card the card to check
     * @return whether the player owns the card or not
     */
    public boolean ownsCard(Card card) {
        for(CardContainer cc : this.cardContainers) {
            String cardId = cc.getCard().getCardId();

            if(cardId.equals(card.getCardId()) && cc.getCardQuantity() > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether the player still owns their pinned card
     */
    public boolean ownsPinnedCard() {
        for(CardContainer card : this.cardContainers) {
            String cardImage = card.getCard().getCardImage();

            if(this.pinnedCard.equals(cardImage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether the player owns any pack from the regular shop
     */
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

    /**
     * checks whether the player owns a certain badge
     * @param badge the badge to check
     * @return whether the player owns the badge or not
     */
    public boolean ownsBadge(String badge) {
        for(String b : this.badges) {
            if(b.equalsIgnoreCase(badge)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * checks whether the player has unlocked a certain card pack in the shop
     * @param setName the card set name of the pack to check
     * @return whether the player has unlocked the pack or not
     */
    public boolean isPackUnlocked(String setName) {
        for(String pack : this.packs) {
            if(pack.equalsIgnoreCase(setName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * counts how many packs the player owns in the shop (or oldshop)
     * @param isOld whether this function should count packs from the shop or oldshop
     * @return the number of packs the player owns
     */
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

    /**
     * @return the current number of cards the player owns
     */
    public int countOwnedCards() {
        int count = 0;

        for(CardContainer card : this.cardContainers) {
            count += card.getCardQuantity();
        }
        return count;
    }

    //===============================================[ HELPER METHODS ]=============================================================
    
    /**
     * searches through the User list for a specific player using the binary search algorithm
     * @param userId the ID of the player to search for
     * @return the player to be searched
     */
    private static User searchForUser(String userId) {
        User userToFind = new User(userId); // need to create a User object to compare with other users while searching

        synchronized(users) {
            int index = SearchUtils.binarySearch(users, userToFind);
    
            if(index == users.size() || userToFind.compareTo(users.get(index)) != 0) {
                users.add(index, userToFind);

            } else {
                userToFind = users.get(index);
            }
        }
        return userToFind;
    }
}
