package ca.gimmecards.Main;
import ca.gimmecards.Interfaces.*;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Hashtable;
import java.net.URL;

public class Data implements StoragePaths {

    // hashtables of all the set codes
    public static Hashtable<Integer, String> setCodes = new Hashtable<>();
    public static Hashtable<Integer, String> oldSetCodes = new Hashtable<>();
    public static Hashtable<Integer, String> rareSetCodes = new Hashtable<>();
    public static Hashtable<Integer, String> promoSetCodes = new Hashtable<>();
    
    // lists of card Data
    public static Data[] sets = new Data[45];
    public static Data[] oldSets = new Data[46];
    public static Data[] rareSets = new Data[9];
    public static Data[] promoSets = new Data[6];
    
    // instance variables for card Data
    private String setEmote;
    private String setName;
    private String cardId;
    private String cardName;
    private String cardRarity;
    private String cardImage;
    private String cardSupertype;
    private String[] cardSubtypes;
    private Integer cardPrice;

    /**
     * constructor for card Data
     * @param se setEmote
     * @param cp cardPrice
     * @param j JsonElement representation of a card
     */
    public Data(String se, int cp, JsonElement j) {
        setEmote = se;
        setName = j.getAsJsonObject().get("set").getAsJsonObject().get("name").getAsString().replace("—", " ");
        cardId = j.getAsJsonObject().get("id").getAsString();
        cardName = j.getAsJsonObject().get("name").getAsString();
        cardRarity = j.getAsJsonObject().get("rarity").getAsString();
        cardImage = j.getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString();
        cardSupertype = j.getAsJsonObject().get("supertype").getAsString();
        cardSubtypes = findCardSubtypes(j);
        cardPrice = cp;
    }

    /**
     * constructor for custom card Data
     * @param se setEmote
     * @param sn setName
     * @param cId cardId
     * @param cn cardName
     * @param cr cardRarity
     * @param ci cardImage
     * @param cSuper cardSupertype
     * @param cSub cardSubtypes
     * @param cp cardPrice
     */
    public Data(String se, String sn, String cId, String cn, String cr, String ci, String cSuper, String[] cSub, int cp) {
        setEmote = se;
        setName = sn;
        cardId = cId;
        cardName = cn;
        cardRarity = cr;
        cardImage = ci;
        cardSupertype = cSuper;
        cardSubtypes = cSub;
        cardPrice = cp;
    }

    // getters for card Data
    public String getSetEmote() { return setEmote; }
    public String getSetName() { return setName; }
    public String getCardId() { return cardId; }
    public String getCardName() { return cardName; }
    public String getCardRarity() { return cardRarity; }
    public String getCardImage() { return cardImage; }
    public String getCardSupertype() { return cardSupertype; }
    public String[] getCardSubtypes() { return cardSubtypes; }
    public int getCardPrice() { return cardPrice; }

    // instance variables for Data lists
    private ArrayList<Data> commons;
    private ArrayList<Data> uncommons;
    private ArrayList<Data> rares;
    private ArrayList<Data> shinies;

    /**
     * constructor for Data lists
     * @param se setEmote
     * @param sn setName
     * @param c commons
     * @param u uncommons
     * @param r rares
     * @param s shinies
     */
    public Data(String se, String sn, ArrayList<Data> c, ArrayList<Data> u, ArrayList<Data> r, ArrayList<Data> s) {
        setEmote = se;
        setName = sn;
        commons = c;
        uncommons = u;
        rares = r;
        shinies = s;
    }

    // getters for Data lists
    public ArrayList<Data> getCommons() { return commons; }
    public ArrayList<Data> getUncommons() { return uncommons; }
    public ArrayList<Data> getRares() { return rares; }
    public ArrayList<Data> getShinies() { return shinies; }

    // instance variable for special Data lists
    private ArrayList<Data> specs;

    /**
     * constructor for special Data lists
     * @param se setEmote
     * @param sn setName
     * @param s specs
     */
    public Data(String se, String sn, ArrayList<Data> s) {
        setEmote = se;
        setName = sn;
        specs = s;
    }

    // getter for special Data lists
    public ArrayList<Data> getSpecs() { return specs; }

    /**
     * determines the path that card Data will be saved to
     * @param dataType whether the Data category is new, old, rare, or promo
     * @return the path with header if the file is in an external location, and no header if it's local
     */
    private static String determinePath(String dataType) {
        String path = "";

        if(dataType.equalsIgnoreCase("new")) {
            path = dataPath;
        } else if(dataType.equalsIgnoreCase("old")) {
            path = oldDataPath;
        } else if(dataType.equalsIgnoreCase("rare")) {
            path = rareDataPath;
        } else if(dataType.equalsIgnoreCase("promo")) {
            path = promDataPath;
        }

        if(Paths.get(path).toFile().length() > 0) {
            return path;
        } else {
            return header + path;
        }
    }

    /**
     * loads new card Data from Data.json into 'sets'
     * @throws Exception just needs it
     */
    public static void loadData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("new")), "UTF-8");
        sets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    /**
     * loads old card Data from Data.json into 'oldSets'
     * @throws Exception just needs it
     */
    public static void loadOldData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("old")), "UTF-8");
        oldSets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    /**
     * loads rare card Data from Data.json into 'rareSets'
     * @throws Exception just needs it
     */
    public static void loadRareData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("rare")), "UTF-8");
        rareSets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    /**
     * loads promo card Data from Data.json into 'promoSets'
     * @throws Exception just needs it
     */
    public static void loadPromoData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("promo")), "UTF-8");
        promoSets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    /**
     * saves new card Data into the file Data.json
     * @throws Exception just needs it
     */
    public static void saveData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("new")), "UTF-8");
        gson.toJson(sets, writer);
        writer.close();
    }

    /**
     * saves old card Data into the file OldData.json
     * @throws Exception just needs it
     */
    public static void saveOldData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("old")), "UTF-8");
        gson.toJson(oldSets, writer);
        writer.close();
    }

    /**
     * saves rare card Data into the file RareData.json
     * @throws Exception just needs it
     */
    public static void saveRareData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("rare")), "UTF-8");
        gson.toJson(rareSets, writer);
        writer.close();
    }

    /**
     * saves promo card Data into the file PromoData.json
     * @throws Exception just needs it
     */
    public static void savePromoData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("promo")), "UTF-8");
        gson.toJson(promoSets, writer);
        writer.close();
    }

    /**
     * find a specific card set
     * @param setName the name of the card set
     * @return the card set to find
     */
    public static Data findSet(String setName) {
        Data set = null;

        for(Data d : sets) {
            if(d.getSetName().equalsIgnoreCase(setName)) {
                set = d;
                break;
            }
        }
        for(Data d : oldSets) {
            if(d.getSetName().equalsIgnoreCase(setName)) {
                set = d;
                break;
            }
        }
        for(Data d : rareSets) {
            if(d.getSetName().equalsIgnoreCase(setName)) {
                set = d;
                break;
            }
        }
        for(Data d : promoSets) {
            if(d.getSetName().equalsIgnoreCase(setName)) {
                set = d;
                break;
            }
        }
        return set;
    }

    /**
     * find a card Data's subtypes
     * @param j JsonElement representation of a card
     * @return a string list of each of the card Data's subtypes
     */
    private static String[] findCardSubtypes(JsonElement j) {
        String[] cardSubtypes;

        try {
            JsonArray rawSubtypes = j.getAsJsonObject().get("types").getAsJsonArray();
            cardSubtypes = new String[rawSubtypes.size()];

            for(int i = 0; i < rawSubtypes.size(); i++) {
                cardSubtypes[i] = rawSubtypes.get(i).toString().replaceAll("\"", "");
            }
            return cardSubtypes;
        } catch(NullPointerException e) {
            return null;
        }
    }

    /**
     * find a card Data's price
     * @param j JsonElement representation of a card
     * @return the card Data's price
     */
    private static int findCardPrice(JsonElement j) {
        JsonObject prices = null;

        try {
            prices = j.getAsJsonObject().get("tcgplayer").getAsJsonObject().get("prices").getAsJsonObject();
            return findTcgplayerPrice(prices);

        } catch(NullPointerException e) {
            try {
                prices = j.getAsJsonObject().get("cardmarket").getAsJsonObject().get("prices").getAsJsonObject();
                return findCardmarketPrice(prices);

            } catch(NullPointerException error) {
                System.out.println(j + "\n");
                return -1;
            }
        }
    }

    /**
     * find the 
     * @param prices
     * @return
     */
    private static int findTcgplayerPrice(JsonObject prices) {
        JsonObject rawPrice = null;
        double finalPrice = 0;

        try {
            rawPrice = prices.get("normal").getAsJsonObject();
        } catch(NullPointerException e) {
            try {
                rawPrice = prices.get("reverseHolofoil").getAsJsonObject();
            } catch(NullPointerException e2) {
                try {
                    rawPrice = prices.get("holofoil").getAsJsonObject();
                } catch(NullPointerException e3) {
                    try {
                        rawPrice = prices.get("unlimited").getAsJsonObject();
                    } catch(NullPointerException e4) {
                        try {
                            rawPrice = prices.get("unlimitedReverseHolofoil").getAsJsonObject();
                        } catch(NullPointerException e5) {
                            try {
                                rawPrice = prices.get("unlimitedHolofoil").getAsJsonObject();
                            } catch(NullPointerException e6) {
                                try {
                                    rawPrice = prices.get("1stEditionHolofoil").getAsJsonObject();

                                } catch(NullPointerException error) {
                                    System.out.println(prices + "\n");
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            finalPrice = rawPrice.get("market").getAsDouble();
        } catch(NullPointerException e) {
            try {
                finalPrice = rawPrice.get("mid").getAsDouble();

            } catch(NullPointerException error) {
                System.out.println(prices + "\n");
                return -1;
            }
        }
        return (int)(finalPrice * 100);
    }

    private static int findCardmarketPrice(JsonObject prices) {
        double finalPrice = 0;

        try {
            finalPrice = prices.get("averageSellPrice").getAsDouble();
        } catch(NullPointerException e) {
            try {
                finalPrice = prices.get("trendPrice").getAsDouble();
            } catch(NullPointerException e2) {
                try {
                    finalPrice = prices.get("lowPrice").getAsDouble();

                } catch(NullPointerException error) {
                    System.out.println(prices + "\n");
                    return -1;
                }
            }
        }
        return (int)(finalPrice * 100);
    }

    public static Data findContents(String setEmote, String setCode) throws IOException {
        JsonArray rawContents = crawlDatabase(setCode);
        ArrayList<Data> commons = new ArrayList<Data>();
        ArrayList<Data> uncommons = new ArrayList<Data>();
        ArrayList<Data> rares = new ArrayList<Data>();
        ArrayList<Data> shinies = new ArrayList<Data>();

        for(JsonElement j : rawContents) {
            String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

            if(!cardRarity.equalsIgnoreCase("promo")) {
                int cardPrice = findCardPrice(j);

                if(cardPrice != -1) {
                    if(cardRarity.equalsIgnoreCase("common")) {
                        commons.add(new Data(setEmote, cardPrice, j));

                    } else if(cardRarity.equalsIgnoreCase("uncommon")) {
                        uncommons.add(new Data(setEmote, cardPrice, j));

                    } else if(cardRarity.equalsIgnoreCase("rare")) {
                        rares.add(new Data(setEmote, cardPrice, j));

                    } else {
                        shinies.add(new Data(setEmote, cardPrice, j));
                    }
                }
            }
        }
        return new Data(setEmote, commons.get(0).getSetName(), commons, uncommons, rares, shinies);
    }

    public static Data findRareContents(String setEmote, String setCode) throws IOException {
        JsonArray rawContents = crawlDatabase(setCode);
        ArrayList<Data> specs = new ArrayList<Data>();

        for(JsonElement j : rawContents) {
            int cardPrice = findCardPrice(j);

            if(cardPrice != -1) {
                try {
                    specs.add(new Data(setEmote, cardPrice, j));
                } catch(NullPointerException e) {}
            }
        }
        return new Data(setEmote, specs.get(0).getSetName(), specs);
    }

    public static Data findPromoContents(String setEmote, String setCode) throws IOException {
        JsonArray rawContents = crawlDatabase(setCode);
        ArrayList<Data> specs = new ArrayList<Data>();

        for(JsonElement j : rawContents) {
            String cardRarity = "";

            try {
                cardRarity = j.getAsJsonObject().get("rarity").getAsString();
            } catch(NullPointerException e) {
                continue;
            }
            if(cardRarity.equalsIgnoreCase("promo")) {
                int cardPrice = findCardPrice(j);

                if(cardPrice != -1) {
                    try {
                        specs.add(new Data(setEmote, cardPrice, j));
                    } catch(NullPointerException e) {}
                }
            }
        }
        return new Data(setEmote, specs.get(0).getSetName(), specs);
    }

    public static JsonArray crawlDatabase(String setCode) throws IOException {
        JsonArray rawContents = new JsonArray();
        int page = 1;

        while(true) {
            URL url = new URL("https://api.pokemontcg.io/v2/cards?q=set.ptcgoCode:" + setCode + "%20&page=" + page + "/key=1bfc1133-79a4-46f6-93d6-6a4dab4b7335");
            String jsonStr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine();
            JsonArray jsonArr = JsonParser.parseString(jsonStr).getAsJsonObject().getAsJsonArray("data");

            if(jsonArr.size() < 1) {
                break;
            }
            for(JsonElement j : jsonArr) {
                rawContents.add(j);
            }
            page++;
        }
        return rawContents;
    }
}
