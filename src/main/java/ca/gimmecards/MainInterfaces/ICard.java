package ca.gimmecards.MainInterfaces;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICard {
    
    /**
     * @return whether this card can be sold for XP
     */
    public boolean isCardSellable();

    /**
     * @return whether this card is from the oldshop
     */
    public boolean isOldCard();

    /**
     * @return whether this card is from the rareshop
     */
    public boolean isRareCard();

    /**
     * @return whether this card is from the promoshop
     */
    public boolean isPromoCard();

    /**
     * @return whether this card is a shiny card (a card that isn't common, uncommon, or rare)
     */
    public boolean isShinyCard();

    /**
     * finds the formatted title for this card
     * @param isFav whether or not this card has been favorited by the player
     * @return the formatted title
     */
    public String findCardTitle(boolean isFav);

    /**
     * @return finds the Discord emote that represents this card's rarity
     */
    public String findRarityEmote();

    /**
     * formats this card's XP amount
     * @param isSellable whether or not this card can be sold for XP
     * @return the formatted XP amount
     */
    public String formatXP(boolean isSellable);

    /**
     * @return a formatted string of this card's credits amount
     */
    public String formatCredits();

    /**
     * @return an embed color that's based on this card's supertype and/or subtype
     */
    public int findEmbedColour();

    /**
     * displays a single card via an embed
     * @param event the slash event
     * @param ui a UserInfo object containing the player's basic information
     * @param card the card to display
     * @param message the message that shows at the top of the embed
     * @param footer the footer of the embed
     * @param isFav whether or not the card should be displayed with the favorited symbol (a heart)
     */
    public void displayCard(SlashCommandInteractionEvent event, UserInfo ui, String message, String footer, boolean isFav);
}
