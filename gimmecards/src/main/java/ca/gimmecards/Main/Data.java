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

    public static Hashtable<Integer, String> setCodes = new Hashtable<>();
    public static Hashtable<Integer, String> oldSetCodes = new Hashtable<>();
    public static Hashtable<Integer, String> rareSetCodes = new Hashtable<>();
    public static Hashtable<Integer, String> promoSetCodes = new Hashtable<>();
    //
    public static Data[] sets = new Data[45];
    public static Data[] oldSets = new Data[46];
    public static Data[] rareSets = new Data[9];
    public static Data[] promoSets = new Data[6];
    //
    private String setEmote;
    private String setName;
    private String cardId;
    private String cardName;
    private String cardRarity;
    private String cardImage;
    private String cardSupertype;
    private String[] cardSubtypes;
    private Integer cardPrice;

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

    //for the custom cards
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

    public String getSetEmote() { return setEmote; }
    public String getSetName() { return setName; }
    public String getCardId() { return cardId; }
    public String getCardName() { return cardName; }
    public String getCardRarity() { return cardRarity; }
    public String getCardImage() { return cardImage; }
    public String getCardSupertype() { return cardSupertype; }
    public String[] getCardSubtypes() { return cardSubtypes; }
    public int getCardPrice() { return cardPrice; }

    private ArrayList<Data> commons;
    private ArrayList<Data> uncommons;
    private ArrayList<Data> rares;
    private ArrayList<Data> shinies;

    public Data(String se, String sn, ArrayList<Data> c, ArrayList<Data> u, ArrayList<Data> r, ArrayList<Data> s) {
        setEmote = se;
        setName = sn;
        commons = c;
        uncommons = u;
        rares = r;
        shinies = s;
    }

    public ArrayList<Data> getCommons() { return commons; }
    public ArrayList<Data> getUncommons() { return uncommons; }
    public ArrayList<Data> getRares() { return rares; }
    public ArrayList<Data> getShinies() { return shinies; }

    private ArrayList<Data> specs;

    public Data(String se, String sn, ArrayList<Data> s) {
        setEmote = se;
        setName = sn;
        specs = s;
    }

    public ArrayList<Data> getSpecs() { return specs; }

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

    public static void loadData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("new")), "UTF-8");
        sets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    public static void loadOldData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("old")), "UTF-8");
        oldSets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    public static void loadRareData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("rare")), "UTF-8");
        rareSets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    public static void loadPromoData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("promo")), "UTF-8");
        promoSets = new Gson().fromJson(reader, Data[].class);

        reader.close();
    }

    public static void saveData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("new")), "UTF-8");
        gson.toJson(sets, writer);
        writer.close();
    }

    public static void saveOldData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("old")), "UTF-8");
        gson.toJson(oldSets, writer);
        writer.close();
    }

    public static void saveRareData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("rare")), "UTF-8");
        gson.toJson(rareSets, writer);
        writer.close();
    }

    public static void savePromoData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("promo")), "UTF-8");
        gson.toJson(promoSets, writer);
        writer.close();
    }

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
