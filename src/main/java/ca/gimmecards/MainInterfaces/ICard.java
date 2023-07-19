package ca.gimmecards.MainInterfaces;

public interface ICard {
    
    public boolean isCardSellable();

    public boolean isOldCard();

    public boolean isRareCard();

    public boolean isPromoCard();

    public boolean isShinyCard();

    public String findCardTitle(boolean isFav);

    public String findRarityEmote();

    public String formatXP(Boolean sellable);

    public String formatCredits();

    public int findEmbedColour();
}
