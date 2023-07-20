package ca.gimmecards.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import ca.gimmecards.MainInterfaces.*;
import ca.gimmecards.OtherInterfaces.*;
import java.util.ArrayList;
import java.util.Random;

public class Card implements ICard {

    private String setEmote;
    private String setName;
    private String cardId;
    private String cardName;
    private String cardRarity;
    private String cardImage;
    private String cardSupertype;
    private String[] cardSubtypes;
    private Integer cardPrice;

    public Card(String setEmote, Integer cardPrice, JsonElement j) {
        this.setEmote = setEmote;
        setName = j.getAsJsonObject().get("set").getAsJsonObject().get("name").getAsString().replace("—", " ");
        cardId = j.getAsJsonObject().get("id").getAsString();
        cardName = j.getAsJsonObject().get("name").getAsString();
        cardRarity = j.getAsJsonObject().get("rarity").getAsString();
        cardImage = j.getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString();
        cardSupertype = j.getAsJsonObject().get("supertype").getAsString();
        cardSubtypes = findCardSubtypes(j);
        this.cardPrice = cardPrice;
    }

    public Card(String setEmote, String setName, String cardId, String cardName, String cardRarity, String cardImage,
            String cardSupertype, String[] cardSubtypes, Integer cardPrice) {
        this.setEmote = setEmote;
        this.setName = setName;
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardRarity = cardRarity;
        this.cardImage = cardImage;
        this.cardSupertype = cardSupertype;
        this.cardSubtypes = cardSubtypes;
        this.cardPrice = cardPrice;
    }

    public String getSetEmote() { return setEmote; }
    public String getSetName() { return setName; }
    public String getCardId() { return cardId; }
    public String getCardName() { return cardName; }
    public String getCardRarity() { return cardRarity; }
    public String getCardImage() { return cardImage; }
    public String getCardSupertype() { return cardSupertype; }
    public String[] getCardSubtypes() { return cardSubtypes; }
    public int getCardPrice() { return cardPrice; }

    //============================================[ PUBLIC STATIC FUNCTIONS ]==========================================================

    public static Card pickCard(ArrayList<Card> cards) {
        Random rand = new Random();

        return cards.get(rand.nextInt(cards.size()));
    }

    public static Card pickCard(Card[] cards) {
        Random rand = new Random();

        return cards[rand.nextInt(cards.length)];
    }

    public static Card pickRandomCard() {
        ArrayList<Card> cards = new ArrayList<Card>();
        Random rand = new Random();
        int setsChance = rand.nextInt(4);
        int rarityChance = rand.nextInt(4);
        CardSet set;

        if(setsChance == 0) {
            set = CardSet.sets[rand.nextInt(CardSet.sets.length)];

        } else if(setsChance == 1) {
            set = CardSet.oldSets[rand.nextInt(CardSet.oldSets.length)];

        } else if(setsChance == 2) {
            set = CardSet.rareSets[rand.nextInt(CardSet.rareSets.length)];
            return pickCard(set.getSpecials());

        } else { // setsChance == 3
            set = CardSet.promoSets[rand.nextInt(CardSet.promoSets.length)];
            return pickCard(set.getSpecials());
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

    public static Card pickRandomCard(String rarity) {
        ArrayList<Card> cards = new ArrayList<Card>();
        Random rand = new Random();
        int setsChance = rand.nextInt(2);
        CardSet set;

        if(setsChance == 0) {
            set = CardSet.sets[rand.nextInt(CardSet.sets.length)];
        } else {
            set = CardSet.oldSets[rand.nextInt(CardSet.oldSets.length)];
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

    public static Card findCardById(String cardId) {
        for(CardSet set : CardSet.sets) {
            Card card = crawlCardSet(set, cardId, false);

            if(card != null) {
                return card;
            }
        }
        for(CardSet set : CardSet.oldSets) {
            Card card = crawlCardSet(set, cardId, false);

            if(card != null) {
                return card;
            }
        }
        for(CardSet set : CardSet.rareSets) {
            Card card = crawlCardSet(set, cardId, true);

            if(card != null) {
                return card;
            }
        }
        for(CardSet set : CardSet.promoSets) {
            Card card = crawlCardSet(set, cardId, true);

            if(card != null) {
                return card;
            }
        }
        for(Card card : ICustomCards.customs) {
            if(card.getCardId().equalsIgnoreCase(cardId)) {
                return card;
            }
        }
        return null;
    }

    public static String formatCredits(int amount) {
        return IEmotes.credits + " **" + GameManager.formatNumber(amount) + "**";
    }

    //========================================[ PUBLIC NON-STATIC FUNCTIONS ]==========================================================

    @Override
    public boolean isCardSellable() {
        if(this.setName.equalsIgnoreCase("gimme cards")) {
            return false;
            
        } else if(isOldCard() || isPromoCard()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isOldCard() {        
        for(CardSet set : CardSet.oldSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRareCard() {        
        for(CardSet set : CardSet.rareSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPromoCard() {        
        for(CardSet set : CardSet.promoSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isShinyCard() {
        if(this.cardRarity.equalsIgnoreCase("common")
        || this.cardRarity.equalsIgnoreCase("uncommon")
        || this.cardRarity.equalsIgnoreCase("rare")) {
            return false;
        }
        return true;
    }

    @Override
    public String findCardTitle(boolean isFav) {
        String cardTitle = "";

        if(this.cardSubtypes == null) {
            if(this.cardSupertype.equalsIgnoreCase("trainer")) {
                cardTitle += "🔧";
            } else if(this.cardSupertype.equalsIgnoreCase("energy")) {
                cardTitle += "💡";
            }

        } else {
            for(int i = 0; i < this.cardSubtypes.length; i++) {
                String cardSubType = this.cardSubtypes[i];
    
                if(cardSubType.equalsIgnoreCase("water")) {
                    cardTitle += IEmotes.water;
                } else if(cardSubType.equalsIgnoreCase("psychic")) {
                    cardTitle += IEmotes.psychic;
                } else if(cardSubType.equalsIgnoreCase("metal")) {
                    cardTitle += IEmotes.metal;
                } else if(cardSubType.equalsIgnoreCase("lightning")) {
                    cardTitle += IEmotes.lightning;
                } else if(cardSubType.equalsIgnoreCase("grass")) {
                    cardTitle += IEmotes.grass;
                } else if(cardSubType.equalsIgnoreCase("fire")) {
                    cardTitle += IEmotes.fire;
                } else if(cardSubType.equalsIgnoreCase("fighting")) {
                    cardTitle += IEmotes.fighting;
                } else if(cardSubType.equalsIgnoreCase("fairy")) {
                    cardTitle += IEmotes.fairy;
                } else if(cardSubType.equalsIgnoreCase("dragon")) {
                    cardTitle += IEmotes.dragon;
                } else if(cardSubType.equalsIgnoreCase("darkness")) {
                    cardTitle += IEmotes.darkness;
                } else if(cardSubType.equalsIgnoreCase("colorless")) {
                    cardTitle += IEmotes.colorless;
                }
            }
        }
        cardTitle += " " + this.cardName;
        if(isFav) {
            return cardTitle + " ❤";
        }
        return cardTitle;
    }

    @Override
    public String findRarityEmote() {
        String rarityEmote = "";

        if(this.cardRarity.equalsIgnoreCase("common")) {
            rarityEmote = "⚪ ";
        } else if(this.cardRarity.equalsIgnoreCase("uncommon")) {
            rarityEmote = "🔷 ";
        } else if(this.cardRarity.equalsIgnoreCase("rare")) {
            rarityEmote = "⭐ ";
        } else if(this.cardRarity.equalsIgnoreCase("promo")) {
            rarityEmote = "🎁 ";
        } else if(this.cardRarity.equalsIgnoreCase("custom")) {
            rarityEmote = "✨ ";
        }else if(this.cardRarity.equalsIgnoreCase("merch")) {
            rarityEmote = "🛍️ ";
        } else {
            rarityEmote = "🌟 ";
        }
        return rarityEmote;
    }

    @Override
    public String formatXP(Boolean sellable) {
        String formattedXP = IEmotes.XP + " **" + GameManager.formatNumber(this.cardPrice) + "**";

        if(sellable == null) {
            if(!isCardSellable()) {
                return formattedXP + " 🚫";
            }

        } else if(!sellable) {
            return formattedXP + " 🚫";
        }
        return formattedXP;
    }

    @Override
    public String formatCredits() {
        String formattedCredits = IEmotes.credits + " **" + GameManager.formatNumber(this.cardPrice) + "**";

        if(!isCardSellable()) {
            return formattedCredits + " 🚫";
        }
        return formattedCredits;
    }

    @Override
    public int findEmbedColour() {
        int embedColour = 0;

        if(this.cardSubtypes == null) {
            if(this.cardSupertype.equalsIgnoreCase("trainer")) {
                embedColour = 0x768696;
            } else if(this.cardSupertype.equalsIgnoreCase("energy")) {
                embedColour = 0xFED171;
            }

        } else {
            String cardSubType = this.cardSubtypes[0];

            if(cardSubType.equalsIgnoreCase("water")) {
                embedColour = 0x1C9EDD;
            } else if(cardSubType.equalsIgnoreCase("psychic")) {
                embedColour = 0x862E88;
            } else if(cardSubType.equalsIgnoreCase("metal")) {
                embedColour = 0x788088;
            } else if(cardSubType.equalsIgnoreCase("lightning")) {
                embedColour = 0xF9C425;
            } else if(cardSubType.equalsIgnoreCase("grass")) {
                embedColour = 0x207733;
            } else if(cardSubType.equalsIgnoreCase("fire")) {
                embedColour = 0xCC0920;
            } else if(cardSubType.equalsIgnoreCase("fighting")) {
                embedColour = 0x97241B;
            } else if(cardSubType.equalsIgnoreCase("fairy")) {
                embedColour = 0xB9408D;
            } else if(cardSubType.equalsIgnoreCase("dragon")) {
                embedColour = 0x967925;
            } else if(cardSubType.equalsIgnoreCase("darkness")) {
                embedColour = 0x0F2541;
            } else if(cardSubType.equalsIgnoreCase("colorless")) {
                embedColour = 0xE8EBEE;
            }
        }
        return embedColour;
    }

    //=================================================[ PRIVATE FUNCTIONS ]============================================================

    private static String[] findCardSubtypes(JsonElement j) {
        String[] cardSubtypes;

        try {
            JsonArray jsonArrSubtypes = j.getAsJsonObject().get("types").getAsJsonArray();
            cardSubtypes = new String[jsonArrSubtypes.size()];

            for(int i = 0; i < jsonArrSubtypes.size(); i++) {
                cardSubtypes[i] = jsonArrSubtypes.get(i).toString().replaceAll("\"", "");
            }
            return cardSubtypes;
        } catch(NullPointerException e) {
            return null;
        }
    }

    private static Card crawlCardSet(CardSet set, String cardId, boolean isSpecial) {
        if(!isSpecial) {
            for(Card card : set.getCommons()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
            for(Card card : set.getUncommons()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
            for(Card card : set.getRares()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
            for(Card card : set.getShinies()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }

        } else {
            for(Card card : set.getSpecials()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
        }
        return null;
    }
}
