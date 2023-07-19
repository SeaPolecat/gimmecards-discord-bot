package ca.gimmecards.MainInterfaces;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.ArrayList;

public interface IUser {

    public String checkLevelUp(SlashCommandInteractionEvent event);

    /**
     * setter that handles a player levelling up
     */
    public void levelUp();

    /**
     * handles XP
     * @param quantity the amount of XP to give
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateXP(int quantity, boolean isAtTop);

    /**
     * handles tokens
     * @param quantity the amount of tokens to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateTokens(int quantity, boolean isAtTop);

    /**
     * handles credits
     * @param quantity the amount of credits to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateCredits(int quantity, boolean isAtTop);

    /**
     * handles stars
     * @param quantity the amount of stars to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateStars(int quantity, boolean isAtTop);

    /**
     * handles keys
     * @param quantity the amount of keys to give (or remove)
     * @param isStart whether or not it's shown at the start of the embed
     * @return a string that goes into the embed
     */
    public String updateKeys(int quantity, boolean isAtTop);

    public void addSingleCard(Card card, boolean isFav);

    public ArrayList<Card> addNewCards(CardSet set);

    public void sortCards(String sortMethod, boolean sortIncreasing);

    public boolean ownsCard(Card card);

    public boolean ownsFavCard();

    public boolean ownsShopPack();

    public boolean ownsBadge(String badge);

    public boolean isPackUnlocked(String setName);

    public int countOwnedPacks(boolean isOld);

    public int countOwnedCards();

    public String formatNick(SlashCommandInteractionEvent event);

    public String formatNick(User mentionedUser, SlashCommandInteractionEvent event);

    public String formatBadge(SlashCommandInteractionEvent event, String badgeEmote, String badgeName);
}
