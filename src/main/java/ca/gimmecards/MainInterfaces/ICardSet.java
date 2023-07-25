package ca.gimmecards.MainInterfaces;

public interface ICardSet {
    
    /**
     * @return whether this card set can be sold for XP
     */
    public boolean isSetSellable();

    /**
     * @return whether this card set is from the oldshop
     */
    public boolean isOldSet();

    /**
     * @return whether this card set is from the rareshop
     */
    public boolean isRareSet();

    /**
     * @return whether this card set is from the promoshop
     */
    public boolean isPromoSet();
}
