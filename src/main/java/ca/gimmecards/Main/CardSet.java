package ca.gimmecards.Main;
import ca.gimmecards.MainInterfaces.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Hashtable;
import java.net.URL;

public class CardSet extends GameObject implements ICardSet {

    public static Hashtable<Integer, String> setCodes = new Hashtable<>();
    public static Hashtable<Integer, String> oldSetCodes = new Hashtable<>();
    public static Hashtable<Integer, String> rareSetCodes = new Hashtable<>();
    public static Hashtable<Integer, String> promoSetCodes = new Hashtable<>();

    public static CardSet[] sets = new CardSet[47];
    public static CardSet[] oldSets = new CardSet[46];
    public static CardSet[] rareSets = new CardSet[9];
    public static CardSet[] promoSets = new CardSet[6];

    private String setEmote;
    private String setName;
    private ArrayList<Card> commons;
    private ArrayList<Card> uncommons;
    private ArrayList<Card> rares;
    private ArrayList<Card> shinies;
    private ArrayList<Card> specials;

    public CardSet(String setEmote, String setName, ArrayList<Card> commons, ArrayList<Card> uncommons,
            ArrayList<Card> rares, ArrayList<Card> shinies) {
        this.setEmote = setEmote;
        this.setName = setName;
        this.commons = commons;
        this.uncommons = uncommons;
        this.rares = rares;
        this.shinies = shinies;
    }

    public CardSet(String setEmote, String setName, ArrayList<Card> specials) {
        this.setEmote = setEmote;
        this.setName = setName;
        this.specials = specials;
    }

    public String getSetEmote() { return setEmote; }
    public String getSetName() { return setName; }
    public ArrayList<Card> getCommons() { return commons; }
    public ArrayList<Card> getUncommons() { return uncommons; }
    public ArrayList<Card> getRares() { return rares; }
    public ArrayList<Card> getShinies() { return shinies; }
    public ArrayList<Card> getSpecials() { return specials; }

    //============================================[ PUBLIC STATIC FUNCTIONS ]==========================================================

    public static void loadSets() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameObject.findSavePath(GameObject.setsPath)), "UTF-8");
        sets = new Gson().fromJson(reader, CardSet[].class);

        reader.close();
    }

    public static void saveSets() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameObject.findSavePath(GameObject.setsPath)), "UTF-8");
        gson.toJson(sets, writer);
        writer.close();
    }

    public static void loadOldSets() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameObject.findSavePath(GameObject.oldSetsPath)), "UTF-8");
        oldSets = new Gson().fromJson(reader, CardSet[].class);

        reader.close();
    }

    public static void saveOldSets() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameObject.findSavePath(GameObject.oldSetsPath)), "UTF-8");
        gson.toJson(oldSets, writer);
        writer.close();
    }

    public static void loadRareSets() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameObject.findSavePath(GameObject.rareSetsPath)), "UTF-8");
        rareSets = new Gson().fromJson(reader, CardSet[].class);

        reader.close();
    }

    public static void saveRareSets() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameObject.findSavePath(GameObject.rareSetsPath)), "UTF-8");
        gson.toJson(rareSets, writer);
        writer.close();
    }

    public static void loadPromoSets() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameObject.findSavePath(GameObject.promoSetsPath)), "UTF-8");
        promoSets = new Gson().fromJson(reader, CardSet[].class);

        reader.close();
    }

    public static void savePromoSets() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameObject.findSavePath(GameObject.promoSetsPath)), "UTF-8");
        gson.toJson(promoSets, writer);
        writer.close();
    }

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

    //========================================[ PUBLIC NON-STATIC FUNCTIONS ]==========================================================

    @Override
    public boolean isSetSellable() {
        if(this.setName.equalsIgnoreCase("gimme cards")) {
            return false;
            
        } else if(isOldSet() || isPromoSet()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isOldSet() {        
        for(CardSet set : CardSet.oldSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRareSet() {        
        for(CardSet set : CardSet.rareSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPromoSet() {        
        for(CardSet set : CardSet.promoSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    //=================================================[ PRIVATE FUNCTIONS ]============================================================

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

                } catch(NullPointerException | UnsupportedOperationException error) {
                    return -1;
                }
            }
        }
        return (int)(finalPrice * 100);
    }
}
