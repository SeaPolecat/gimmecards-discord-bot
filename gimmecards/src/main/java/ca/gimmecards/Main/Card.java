package ca.gimmecards.Main;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Display.*;
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

        } else if(setsChance == 3) {
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

    public static Card addSingleCard(User user, Data data, boolean sellable) {
        boolean exists = false;
        Card newCard = null;

        user.addCardCount();
        for(Card c : user.getCards()) {
            String cardId2 = c.getData().getCardId();

            if(data.getCardId().equals(cardId2)) {
                exists = true;
                newCard = c;
                c.addCardQuantity();
                break;
            }
        }
        if(!exists) {
            newCard = new Card(data, user.getCardCount(), sellable);
            user.getCards().add(newCard);
        }
        sortCards(user, user.getSortMethod(), user.getSortIncreasing());
        return newCard;
    }

    public static void addNewCards(User user, Data set) {
        InspectionDisplay disp = InspectionDisplay.findInspectionDisplay(user.getUserId());
        ArrayList<Data> commons = set.getCommons();
        ArrayList<Data> uncommons = set.getUncommons();
        ArrayList<Data> rares = set.getRares();
        ArrayList<Data> shinies = set.getShinies();
        ArrayList<Data> newCards = new ArrayList<Data>();
        int chance = new Random().nextInt(100) + 1;

        for(int i = 0; i < 6; i++) {
            newCards.add(pickCard(commons));
        }
        for(int i = 0; i < 3; i++) {
            newCards.add(pickCard(uncommons));
        }
        if(chance <= 10) {
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
                if(State.isOldSet(data)) {
                    user.getCards().add(new Card(data, user.getCardCount(), false));
                } else {
                    user.getCards().add(new Card(data, user.getCardCount(), true));
                }
            }
        }
        sortCards(user, user.getSortMethod(), user.getSortIncreasing());
        disp.setNewCards(newCards);
    }
    
    public static void sortCards(User user, String sortMethod, boolean sortIncreasing) {
        ArrayList<Card> cards = user.getCards();

        for(int i = 0; i < cards.size() - 1; i++) {
            for(int k = i + 1; k < cards.size(); k++) {
                Data d1 = cards.get(i).getData();
                Data d2 = cards.get(k).getData();

                if(sortMethod.equalsIgnoreCase("alphabet")) {
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

    public static ArrayList<Data> searchCards(User user, String key) {
        ArrayList<Data> searchedCards = new ArrayList<Data>();

        for(Data set : Data.sets) {
            crawlSet(searchedCards, set, key);
        }
        for(Data oldSet : Data.oldSets) {
            crawlSet(searchedCards, oldSet, key);
        }
        crawlSpecSets(searchedCards, key);

        return searchedCards;
    }

    private static void crawlSet(ArrayList<Data> searchedCards, Data set, String key) {
        for(Data data : set.getCommons()) {
            String cardName = data.getCardName();

            if(cardName.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
        for(Data data : set.getUncommons()) {
            String cardName = data.getCardName();

            if(cardName.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
        for(Data data : set.getRares()) {
            String cardName = data.getCardName();

            if(cardName.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
        for(Data data : set.getShinies()) {
            String cardName = data.getCardName();

            if(cardName.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
    }

    private static void crawlSpecSets(ArrayList<Data> searchedCards, String key) {
        for(Data specSet : Data.rareSets) {
            for(Data data : specSet.getSpecs()) {
                String cardName = data.getCardName();

                if(cardName.toLowerCase().contains(key.toLowerCase())) {
                    searchedCards.add(data);
                }
            }
        }
        for(Data specSet : Data.promoSets) {
            for(Data data : specSet.getSpecs()) {
                String cardName = data.getCardName();

                if(cardName.toLowerCase().contains(key.toLowerCase())) {
                    searchedCards.add(data);
                }
            }
        }
        for(Data data : customs) {
            String cardName = data.getCardName();

            if(cardName.toLowerCase().contains(key.toLowerCase())) {
                searchedCards.add(data);
            }
        }
    }

    private static void swapCards(User user, int i1, int i2) {
        ArrayList<Card> cards = user.getCards();
        Card temp = cards.get(i1);
        
        cards.set(i1, cards.get(i2));
        cards.set(i2, temp);
    }
}
