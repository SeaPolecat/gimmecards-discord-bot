package ca.gimmecards.Main;

public class CardContainer {
    
    private Data data;
    private Integer cardNum;
    private Integer cardQuantity;
    private Boolean isSellable;
    private Boolean isFav;

    public CardContainer(Data d, int cn, boolean is) {
        data = d;
        cardNum = cn;
        cardQuantity = 1;
        isSellable = is;
        isFav = false;
    }

    public Data getData() { return data; }
    public int getCardNum() { return cardNum; }
    public int getCardQuantity() { return cardQuantity; }
    public boolean getIsSellable() { return isSellable; }
    public boolean getIsFav() { return isFav; }
    //
    public void addCardQuantity() { cardQuantity++; }
    public void minusCardQuantity() { cardQuantity--; }
    public void setCardQuantity(int cq) { cardQuantity = cq; }
    public void setIsFav(boolean f) { isFav = f; }
}
