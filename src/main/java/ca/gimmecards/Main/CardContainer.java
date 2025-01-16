package ca.gimmecards.main;

public class CardContainer {

    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private Card card;              // the card object that's stored within this container
    private Integer cardNum;        // a number that's used to sort the player's cards by newest (larger the number = newer the card)
    private Integer cardQuantity;   // how many of the card the player owns
    private Boolean isSellable;     // whether or not the card can be sold for XP
    private Boolean isFav;          // whether or not the player has favorited the card

    //=============================================[ CONSTRUCTORS ]====================================================================

    public CardContainer(Card card, int cardNum, boolean isSellable) {
        this.card = card;
        this.cardNum = cardNum;
        this.cardQuantity = 1;
        this.isSellable = isSellable;
        this.isFav = false;
    }

    //===============================================[ GETTERS ] ======================================================================

    public Card getCard() { return this.card; }
    public int getCardNum() { return this.cardNum; }
    public int getCardQuantity() { return this.cardQuantity; }
    public boolean getIsSellable() { return this.isSellable; }
    public boolean getIsFav() { return this.isFav; }
    
    //================================================[ SETTERS ]======================================================================

    public void addCardQuantity() { this.cardQuantity++; }
    public void minusCardQuantity() { this.cardQuantity--; }
    public void setCardQuantity(int cardQuantity) { this.cardQuantity = cardQuantity; }
    public void setIsFav(boolean isFav) { this.isFav = isFav; }
}
