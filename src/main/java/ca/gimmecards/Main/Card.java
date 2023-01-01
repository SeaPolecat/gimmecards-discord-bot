package ca.gimmecards.Main;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Helpers.*;
import java.util.ArrayList;
import java.util.Random;

public class Card implements CustomCards {

    private Data data;
    private Integer cardNum;
    private Integer cardQuantity;
    private Boolean sellable;
    private Boolean isFav;

    public Card(Data d, int cn, boolean s) {
        data = d;
        cardNum = cn;
        cardQuantity = 1;
        sellable = s;
        isFav = false;
    }

    public Data getData() { return data; }
    public int getCardNum() { return cardNum; }
    public int getCardQuantity() { return cardQuantity; }
    public boolean getSellable() { return sellable; }
    public boolean getIsFav() { return isFav; }
    //
    public void addCardQuantity() { cardQuantity++; }
    public void minusCardQuantity() { cardQuantity--; }
    public void setCardQuantity(int cq) { cardQuantity = cq; }
    public void setIsFav(boolean f) { isFav = f; }

    public static Data pickCard(ArrayList<Data> cards) {
        Random rand = new Random();

        return cards.get(rand.nextInt(cards.size()));
    }

    public static Data pickCard(Data[] cards) {
        Random rand = new Random();

        return cards[rand.nextInt(cards.length)];
    }

    public static Data pickRandomCard() {
        ArrayList<Data> cards = new ArrayList<Data>();
        Random rand = new Random();
        int setsChance = rand.nextInt(4);
        int rarityChance = rand.nextInt(4);
        Data set = null;

        if(setsChance == 0) {
            set = Data.sets[rand.nextInt(Data.sets.length)];

        } else if(setsChance == 1) {
            set = Data.oldSets[rand.nextInt(Data.oldSets.length)];

        } else if(setsChance == 2) {
            set = Data.rareSets[rand.nextInt(Data.rareSets.length)];
            return pickCard(set.getSpecs());

        } else { // setsChance == 3
            set = Data.promoSets[rand.nextInt(Data.promoSets.length)];
            return pickCard(set.getSpecs());
        }

        if(rarityChance == 0) {
            cards = set.getCommons();
        } else if(rarityChance == 1) {
            cards = set.getUncommons();
        } else if(rarityChance == 2) {
            cards = set.getRares();
        } else if(rarityChance == 3) {
            cards = set.getShinies();
        }
        return pickCard(cards);
    }

    public static Data pickRandomCard(String rarity) {
        ArrayList<Data> cards = new ArrayList<Data>();
        Random rand = new Random();
        int setsChance = rand.nextInt(2);
        Data set;

        if(setsChance == 0) {
            set = Data.sets[rand.nextInt(Data.sets.length)];
        } else {
            set = Data.oldSets[rand.nextInt(Data.oldSets.length)];
        }
        if(rarity.equalsIgnoreCase("common")) {
            cards = set.getCommons();
        } else if(rarity.equalsIgnoreCase("uncommon")) {
            cards = set.getUncommons();
        } else if(rarity.equalsIgnoreCase("rare")) {
            cards = set.getRares();
        } else if(rarity.equalsIgnoreCase("shiny")) {
            cards = set.getShinies();
        }
        return pickCard(cards);
    }

    public static void addSingleCard(User user, Data data, boolean isFav) {
        Card newCard = null;

        user.addCardCount();
        for(Card c : user.getCards()) {
            String cardId = c.getData().getCardId();

            if(data.getCardId().equals(cardId)) {
                c.addCardQuantity();
                return;
            }
        }
        newCard = new Card(data, user.getCardCount(), Check.isSellable(data));

        if(isFav) {
            newCard.setIsFav(true);
        }
        user.getCards().add(newCard);
        sortCards(user, user.getSortMethod(), user.getSortIncreasing());
    }

    public static ArrayList<Data> addNewCards(User user, Data set) {
        ArrayList<Data> commons = set.getCommons();
        ArrayList<Data> uncommons = set.getUncommons();
        ArrayList<Data> rares = set.getRares();
        ArrayList<Data> shinies = set.getShinies();
        ArrayList<Data> newCards = new ArrayList<Data>();
        int chance = new Random().nextInt(100) + 1;
        int percentage;

        if(user.getIsRare() || user.getIsRadiantRare()) {
            percentage = 20;
        } else {
            percentage = 10;
        }
        for(int i = 0; i < 6; i++) {
            newCards.add(pickCard(commons));
        }
        for(int i = 0; i < 3; i++) {
            newCards.add(pickCard(uncommons));
        }
        if(chance <= percentage) {
            newCards.add(pickCard(shinies));
        } else {
            newCards.add(pickCard(rares));
        }
        for(Data data : newCards) {
            boolean exists = false;

            user.addCardCount();
            for(Card c : user.getCards()) {
                String cardId = c.getData().getCardId();

                if(data.getCardId().equals(cardId)) {
                    exists = true;
                    c.addCardQuantity();
                    break;
                }
            }
            if(!exists) {
                user.getCards().add(new Card(data, user.getCardCount(), Check.isSellable(data)));
            }
        }
        sortCards(user, user.getSortMethod(), user.getSortIncreasing());
        return newCards;
    }

    private static void swapCards(User user, int i1, int i2) {
        ArrayList<Card> cards = user.getCards();
        Card temp = cards.get(i1);
        
        cards.set(i1, cards.get(i2));
        cards.set(i2, temp);
    }
    
    public static void sortCards(User user, String sortMethod, boolean sortIncreasing) {
        ArrayList<Card> cards = user.getCards();

        for(int i = 0; i < cards.size() - 1; i++) {
            for(int k = i + 1; k < cards.size(); k++) {
                Data d1 = cards.get(i).getData();
                Data d2 = cards.get(k).getData();

                if(sortMethod.equalsIgnoreCase("alphabetical")) {
                    String cardName1 = d1.getCardName();
                    String cardName2 = d2.getCardName();
    
                    if(sortIncreasing) {
                        if(cardName1.compareToIgnoreCase(cardName2) > 0) {
                            swapCards(user, i, k);
                        }
                    } else {
                        if(cardName1.compareToIgnoreCase(cardName2) < 0) {
                            swapCards(user, i, k);
                        }
                    }

                } else if(sortMethod.equalsIgnoreCase("xp")) {
                    int xp1 = d1.getCardPrice();
                    int xp2 = d2.getCardPrice();
    
                    if(sortIncreasing) {
                        if(xp1 > xp2) {
                            swapCards(user, i, k);
                        }
                    } else {
                        if(xp1 < xp2) {
                            swapCards(user, i, k);
                        }
                    }

                } else if(sortMethod.equalsIgnoreCase("quantity")) {
                    int quantity1 = cards.get(i).getCardQuantity();
                    int quantity2 = cards.get(k).getCardQuantity();
    
                    if(sortIncreasing) {
                        if(quantity1 > quantity2) {
                            swapCards(user, i, k);
                        }
                    } else {
                        if(quantity1 < quantity2) {
                            swapCards(user, i, k);
                        }
                    }

                } else if(sortMethod.equalsIgnoreCase("newest")) {
                    int num1 = cards.get(i).getCardNum();
                    int num2 = cards.get(k).getCardNum();

                    if(sortIncreasing) {
                        if(num1 > num2) {
                            swapCards(user, i, k);
                        }
                    } else {
                        if(num1 < num2) {
                            swapCards(user, i, k);
                        }
                    }
                }
            }
        }
    }

    public static Data findDataById(String cardId) {
        for(Data set : Data.sets) {
            Data data = crawlSetById(set, cardId, false);

            if(data != null) {
                return data;
            }
        }
        for(Data set : Data.oldSets) {
            Data data = crawlSetById(set, cardId, false);

            if(data != null) {
                return data;
            }
        }
        for(Data set : Data.rareSets) {
            Data data = crawlSetById(set, cardId, true);

            if(data != null) {
                return data;
            }
        }
        for(Data set : Data.promoSets) {
            Data data = crawlSetById(set, cardId, true);

            if(data != null) {
                return data;
            }
        }
        for(Data data : CustomCards.customs) {
            if(data.getCardId().equalsIgnoreCase(cardId)) {
                return data;
            }
        }
        return null;
    }

    private static Data crawlSetById(Data set, String cardId, boolean isSpec) {
        if(!isSpec) {
            for(Data data : set.getCommons()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
            for(Data data : set.getUncommons()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
            for(Data data : set.getRares()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
            for(Data data : set.getShinies()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }

        } else {
            for(Data data : set.getSpecs()) {
                if(data.getCardId().equalsIgnoreCase(cardId)) {
                    return data;
                }
            }
        }
        return null;
    }
}
