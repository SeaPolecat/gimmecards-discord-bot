package ca.gimmecards.MainInterfaces;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.ArrayList;

public interface IUser {

    /**
     * handles a player levelling up
     */
    public void levelUp();

    /**
     * checks whether the player has enough XP to level up
     * @param event a slash event
     * @return a string message telling the player that they levelled up; is attached under any XP message
     */
    public String checkLevelUp(SlashCommandInteractionEvent event);

    /**
     * handles XP
     * @param quantity the amount of XP to give
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their XP changed
     */
    public String updateXP(int quantity, boolean isAtTop);

    /**
     * handles tokens
     * @param quantity the amount of tokens to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their tokens changed
     */
    public String updateTokens(int quantity, boolean isAtTop);

    /**
     * handles credits
     * @param quantity the amount of credits to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their credits changed
     */
    public String updateCredits(int quantity, boolean isAtTop);

    /**
     * handles stars
     * @param quantity the amount of stars to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their stars changed
     */
    public String updateStars(int quantity, boolean isAtTop);

    /**
     * handles keys
     * @param quantity the amount of keys to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string message telling the player how their keys changed
     */
    public String updateKeys(int quantity, boolean isAtTop);

    /**
     * adds a single card to the player's CardContainer list
     * @param card the card to add
     * @param shouldAutoFav whether the card should be automatically favorited or not
     */
    public void addSingleCard(Card card, boolean shouldAutoFav);

    /**
     * adds a list of cards to the player's CardContainer list
     * @param set the card set that the list should be picked from
     * @return a list of the new cards
     */
    public ArrayList<Card> addNewCards(CardSet set);

    /**
     * sorts the player's cards based on either alphabetical, xp, quantity, or newest
     */
    public void sortCards();

    /**
     * checks whether the player owns a certain card
     * @param card the card to check
     * @return whether the player owns the card or not
     */
    public boolean ownsCard(Card card);

    /**
     * @return whether the player still owns their pinned card
     */
    public boolean ownsPinnedCard();

    /**
     * @return whether the player owns any pack from the regular shop
     */
    public boolean ownsAnyShopPack();

    /**
     * checks whether the player owns a certain badge
     * @param badge the badge to check
     * @return whether the player owns the badge or not
     */
    public boolean ownsBadge(String badge);

    /**
     * checks whether the player has unlocked a certain card pack in the shop
     * @param setName the card set name of the pack to check
     * @return whether the player has unlocked the pack or not
     */
    public boolean isPackUnlocked(String setName);

    /**
     * counts how many packs the player owns in the shop (or oldshop)
     * @param isOld whether this function should count packs from the shop or oldshop
     * @return the number of packs the player owns
     */
    public int countOwnedPacks(boolean isOld);

    /**
     * @return the current number of cards the player owns
     */
    public int countOwnedCards();

    /**
     * checks whether the player has the 'Ultra Rare Collector' role in the community server, based on a slash event
     * @param event the slash event
     * @return whether the player has the role or not
     */
    public boolean hasPremiumRole(SlashCommandInteractionEvent event);
}
