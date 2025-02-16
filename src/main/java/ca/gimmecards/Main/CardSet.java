package ca.gimmecards.main;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.net.URL;

public class CardSet {

    //=============================================[ CARD SETS ]========================================================================

    public static CardSet[] sets = new CardSet[47];         // a list of type CardSet that's saved and loaded from CardSets.json
    public static CardSet[] oldSets = new CardSet[46];      // a list of type CardSet that's saved and loaded from OldCardSets.json
    public static CardSet[] rareSets = new CardSet[9];      // a list of type CardSet that's saved and loaded from RareCardSets.json
    public static CardSet[] promoSets = new CardSet[6];     // a list of type CardSet that's saved and loaded from PromoCardSets.json

    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String setEmote;                // the Discord emote for this set
    private String setName;                 // the name of this set
    private ArrayList<Card> commons;        // the common cards of this set
    private ArrayList<Card> uncommons;      // the uncommon cards of this set
    private ArrayList<Card> rares;          // the rare cards of this set
    private ArrayList<Card> shinies;        // any cards from this set that aren't common, uncommon, or rare (EX, GX, V, etc.)
    private ArrayList<Card> specials;       // separate from normal sets; this list contains all cards from any special event set (those in rareshop and/or promoshop)

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * creates a new CardSet; the params are all the instance variables (except specials)
     * @param setEmote
     * @param setName
     * @param commons
     * @param uncommons
     * @param rares
     * @param shinies
     */
    public CardSet(String setEmote, String setName, ArrayList<Card> commons, ArrayList<Card> uncommons,
            ArrayList<Card> rares, ArrayList<Card> shinies) {
        this.setEmote = setEmote;
        this.setName = setName;
        this.commons = commons;
        this.uncommons = uncommons;
        this.rares = rares;
        this.shinies = shinies;
    }

    /**
     * creates a new special CardSet (those in rareshop and/or promoshop)
     * @param setEmote the Discord emote for this set
     * @param setName the name of this set
     * @param specials separate from normal sets; this list contains all cards from any special event set (those in rareshop and/or promoshop)
     */
    public CardSet(String setEmote, String setName, ArrayList<Card> specials) {
        this.setEmote = setEmote;
        this.setName = setName;
        this.specials = specials;
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getSetEmote() { return setEmote; }
    public String getSetName() { return setName; }
    public ArrayList<Card> getCommons() { return commons; }
    public ArrayList<Card> getUncommons() { return uncommons; }
    public ArrayList<Card> getRares() { return rares; }
    public ArrayList<Card> getShinies() { return shinies; }
    public ArrayList<Card> getSpecials() { return specials; }

    //=============================================[ STATIC METHODS ]==============================================================

    /**
     * crawls through the Pokemon TCG API to search for new cards to add to the game
     * @param setCode the Pokemon TCG Online code of the set to crawl for
     * @return a JsonArray of the cards that this function found
     * @throws IOException an error that occurs when you reach the API's rate limit
     */
    public static JsonArray crawlPokemonAPI(String setCode) throws IOException {
        JsonArray contents = new JsonArray();
        int page = 1;

        while(true) {
            URL url = new URL("https://api.pokemontcg.io/v2/cards?q=set.ptcgoCode:" + setCode + "%20&page=" + page + "/key=1bfc1133-79a4-46f6-93d6-6a4dab4b7335");
            String jsonStr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine();
            JsonArray jsonArr = JsonParser.parseString(jsonStr).getAsJsonObject().getAsJsonArray("data");

            if(jsonArr.size() < 1) {
                break;
            }
            for(JsonElement j : jsonArr) {
                contents.add(j);
            }
            page++;
        }
        return contents;
    }

    /**
     * creates all the Card objects within a specific card set
     * @param setEmote the Discord Emote of the set to find
     * @param setCode the Pokemon TCG Online code of the set to find
     * @return a CardSet containing all the Card objects within it
     * @throws IOException an error that occurs when you reach the API's rate limit
     */
    public static CardSet findContents(String setEmote, String setCode) throws IOException {
        JsonArray contents = crawlPokemonAPI(setCode);
        ArrayList<Card> commons = new ArrayList<Card>();
        ArrayList<Card> uncommons = new ArrayList<Card>();
        ArrayList<Card> rares = new ArrayList<Card>();
        ArrayList<Card> shinies = new ArrayList<Card>();
        int nullCount = 0;

        for(JsonElement j : contents) {
            try {
                String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

                if(!cardRarity.equalsIgnoreCase("promo")) {
                    int cardPrice = findCardPrice(j);

                    if(cardPrice != -1) {
                        if(cardRarity.equalsIgnoreCase("common")) {
                            commons.add(new Card(setEmote, cardPrice, j));

                        } else if(cardRarity.equalsIgnoreCase("uncommon")) {
                            uncommons.add(new Card(setEmote, cardPrice, j));

                        } else if(cardRarity.equalsIgnoreCase("rare")) {
                            rares.add(new Card(setEmote, cardPrice, j));

                        } else {
                            shinies.add(new Card(setEmote, cardPrice, j));
                        }

                    } else {
                        nullCount++;
                    }
                }
            } catch(NullPointerException e) {
                nullCount++;
            }
        }
        if(nullCount > 0) {
            System.out.println(nullCount + " bad cards found!");
        }
        return new CardSet(setEmote, commons.get(0).getSetName(), commons, uncommons, rares, shinies);
    }

    /**
     * creates all the Card objects within a specific rareshop card set
     * @param setEmote the Discord Emote of the set to find
     * @param setCode the Pokemon TCG Online code of the set to find
     * @return a CardSet containing a specials list with all the rareshop Card objects within it
     * @throws IOException an error that occurs when you reach the API's rate limit
     */
    public static CardSet findRareContents(String setEmote, String setCode) throws IOException {
        JsonArray contents = crawlPokemonAPI(setCode);
        ArrayList<Card> specials = new ArrayList<Card>();
        int nullCount = 0;

        for(JsonElement j : contents) {
            try {
                int cardPrice = findCardPrice(j);

                if(cardPrice != -1) {
                    specials.add(new Card(setEmote, cardPrice, j));
                } else {
                    nullCount++;
                }
            } catch(NullPointerException e) {
                nullCount++;
            }
        }
        if(nullCount > 0) {
            System.out.println(nullCount + " bad cards found!");
        }
        return new CardSet(setEmote, specials.get(0).getSetName(), specials);
    }

    /**
     * creates all the Card objects within a specific promoshop card set
     * @param setEmote the Discord Emote of the set to find
     * @param setCode the Pokemon TCG Online code of the set to find
     * @return a CardSet containing a specials list with all the promoshop Card objects within it
     * @throws IOException an error that occurs when you reach the API's rate limit
     */
    public static CardSet findPromoContents(String setEmote, String setCode) throws IOException {
        JsonArray contents = crawlPokemonAPI(setCode);
        ArrayList<Card> specials = new ArrayList<Card>();
        int nullCount = 0;

        for(JsonElement j : contents) {
            try {
                String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

                if(cardRarity.equalsIgnoreCase("promo")) {
                    int cardPrice = findCardPrice(j);

                    if(cardPrice != -1) {
                        specials.add(new Card(setEmote, cardPrice, j));
                    } else {
                        nullCount++;
                    }
                }
            } catch(NullPointerException e) {
                nullCount++;
            }
        }
        if(nullCount > 0) {
            System.out.println(nullCount + " bad cards found!");
        }
        return new CardSet(setEmote, specials.get(0).getSetName(), specials);
    }

    /**
     * finds a specific card set
     * @param setName the name of the set to find
     * @return the set to be found
     */
    public static CardSet findCardSet(String setName) {
        CardSet set = null;

        for(CardSet cs : sets) {
            if(cs.getSetName().equalsIgnoreCase(setName)) {
                set = cs;
                break;
            }
        }
        for(CardSet cs : oldSets) {
            if(cs.getSetName().equalsIgnoreCase(setName)) {
                set = cs;
                break;
            }
        }
        for(CardSet cs : rareSets) {
            if(cs.getSetName().equalsIgnoreCase(setName)) {
                set = cs;
                break;
            }
        }
        for(CardSet cs : promoSets) {
            if(cs.getSetName().equalsIgnoreCase(setName)) {
                set = cs;
                break;
            }
        }
        return set;
    }

    /**
     * finds the XP value of a card
     * @param j the JsonElement crawled from the Pokemon TCG API
     * @return the XP value
     */
    public static int findCardPrice(JsonElement j) {
        JsonObject prices;

        try {
            prices = j.getAsJsonObject().get("tcgplayer").getAsJsonObject().get("prices").getAsJsonObject();
            return findTcgplayerPrice(prices);

        } catch(NullPointerException e) {
            try {
                prices = j.getAsJsonObject().get("cardmarket").getAsJsonObject().get("prices").getAsJsonObject();
                return findCardmarketPrice(prices);

            } catch(NullPointerException realError) {
                return -1;
            }
        }
    }

    //==============================================[ INSTANCE METHODS ]=====================================================

    /**
     * @return whether this card set can be sold for XP
     */
    public boolean isSetSellable() {
        if(this.setName.equalsIgnoreCase("gimme cards")) {
            return false;
            
        } else if(isOldSet() || isPromoSet()) {
            return false;
        }
        return true;
    }

    /**
     * @return whether this card set is from the oldshop
     */
    public boolean isOldSet() {        
        for(CardSet set : CardSet.oldSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether this card set is from the rareshop
     */
    public boolean isRareSet() {        
        for(CardSet set : CardSet.rareSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether this card set is from the promoshop
     */
    public boolean isPromoSet() {        
        for(CardSet set : CardSet.promoSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    //===============================================[ HELPER METHODS ]=============================================================

    /**
     * finds the XP value of a card based on its real-life price in the Pokemon TCG Online game
     * @param prices the price data found in the API
     * @return the XP value; returns -1 if the price data doesn't exist (in that case, this card isn't added to the game)
     */
    private static int findTcgplayerPrice(JsonObject prices) {
        JsonObject priceCategory;
        double finalPrice;

        try {
            priceCategory = prices.get("normal").getAsJsonObject();
        } catch(NullPointerException e) {
            try {
                priceCategory = prices.get("reverseHolofoil").getAsJsonObject();
            } catch(NullPointerException e2) {
                try {
                    priceCategory = prices.get("holofoil").getAsJsonObject();
                } catch(NullPointerException e3) {
                    try {
                        priceCategory = prices.get("unlimited").getAsJsonObject();
                    } catch(NullPointerException e4) {
                        try {
                            priceCategory = prices.get("unlimitedReverseHolofoil").getAsJsonObject();
                        } catch(NullPointerException e5) {
                            try {
                                priceCategory = prices.get("unlimitedHolofoil").getAsJsonObject();
                            } catch(NullPointerException e6) {
                                try {
                                    priceCategory = prices.get("1stEditionHolofoil").getAsJsonObject();

                                } catch(NullPointerException realError) {
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            finalPrice = priceCategory.get("market").getAsDouble();
        } catch(NullPointerException | UnsupportedOperationException e) {
            try {
                finalPrice = priceCategory.get("mid").getAsDouble();

            } catch(NullPointerException | UnsupportedOperationException realError) {
                return -1;
            }
        }
        return (int)(finalPrice * 100);
    }

    /**
     * finds the XP value of a card based on its real-life price in the online card market
     * @param prices the price data found in the API
     * @return the XP value; returns -1 if the price data doesn't exist (in that case, this card isn't added to the game)
     */
    private static int findCardmarketPrice(JsonObject prices) {
        double finalPrice;

        try {
            finalPrice = prices.get("averageSellPrice").getAsDouble();
        } catch(NullPointerException | UnsupportedOperationException e) {
            try {
                finalPrice = prices.get("trendPrice").getAsDouble();
            } catch(NullPointerException | UnsupportedOperationException e2) {
                try {
                    finalPrice = prices.get("lowPrice").getAsDouble();

                } catch(NullPointerException | UnsupportedOperationException realError) {
                    return -1;
                }
            }
        }
        return (int)(finalPrice * 100);
    }
}
