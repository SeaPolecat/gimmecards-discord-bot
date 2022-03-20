package com.wixsite.seapolecat.Main;
import com.wixsite.seapolecat.Interfaces.*;
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
    public static Hashtable<Integer, String> specSetCodes = new Hashtable<>();
    //
    public static Data[] sets = new Data[44];
    public static Data[] oldSets = new Data[46];
    public static Data[] specSets = new Data[11];
    //
    private String setEmote;
    private String setName;
    private String cardId;
    private String cardName;
    private String cardRarity;
    private String cardImage;
    private String cardSupertype;
    private JsonArray cardSubtypes;
    private Integer cardPrice;

    public Data(String se, JsonElement j) {
        setEmote = se;
        setName = j.getAsJsonObject().get("set").getAsJsonObject().get("name").getAsString().replace("—", " ");
        cardId = j.getAsJsonObject().get("id").getAsString();
        cardName = j.getAsJsonObject().get("name").getAsString();
        cardRarity = j.getAsJsonObject().get("rarity").getAsString();
        cardImage = j.getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString();
        cardSupertype = j.getAsJsonObject().get("supertype").getAsString();
        try {
            cardSubtypes = j.getAsJsonObject().get("types").getAsJsonArray();
        } catch(NullPointerException e) {
            cardSubtypes = null;
        }
        cardPrice = findCardPrice(j);
    }

    public String getSetEmote() { return setEmote; }
    public String getSetName() { return setName; }
    public String getCardId() { return cardId; }
    public String getCardName() { return cardName; }
    public String getCardRarity() { return cardRarity; }
    public String getCardImage() { return cardImage; }
    public String getCardSupertype() { return cardSupertype; }
    public JsonArray getCardSubtypes() { return cardSubtypes; }
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
        } else if(dataType.equalsIgnoreCase("spec")) {
            path = specPath;
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

    public static void loadSpecData() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath("spec")), "UTF-8");
        specSets = new Gson().fromJson(reader, Data[].class);

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

    public static void saveSpecData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath("spec")), "UTF-8");
        gson.toJson(specSets, writer);
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
        for(Data d : specSets) {
            if(d.getSetName().equalsIgnoreCase(setName)) {
                set = d;
                break;
            }
        }
        return set;
    }

    private static int findCardPrice(JsonElement card) {
        double cardPrice;
        String cardRarity = card.getAsJsonObject().get("rarity").getAsString();
        JsonObject prices = card.getAsJsonObject()
        .get("tcgplayer").getAsJsonObject()
        .get("prices").getAsJsonObject();

        if(cardRarity.equalsIgnoreCase("common") || cardRarity.equalsIgnoreCase("uncommon") || cardRarity.equalsIgnoreCase("rare")) {
            try {
                cardPrice = prices.get("normal").getAsJsonObject().get("market").getAsDouble();
            } catch(NullPointerException | UnsupportedOperationException e) {
                try {
                    cardPrice = prices.get("reverseholofoil").getAsJsonObject().get("market").getAsDouble();
                } catch(NullPointerException | UnsupportedOperationException e2) {
                    cardPrice = prices.get("holofoil").getAsJsonObject().get("market").getAsDouble();
                }
            }
        } else {
            try {
                cardPrice = prices.get("holofoil").getAsJsonObject().get("market").getAsDouble();
            } catch(NullPointerException | UnsupportedOperationException e) {
                try {
                    cardPrice = prices.get("reverseholofoil").getAsJsonObject().get("market").getAsDouble();
                } catch(NullPointerException | UnsupportedOperationException e2) {
                    cardPrice = prices.get("normal").getAsJsonObject().get("market").getAsDouble();
                }
            }
        }
        return (int)(cardPrice * 100);
    }

    public static ArrayList<Data> crawlSetContent(String setEmote, String setCode, String rarity) throws IOException {
        ArrayList<Data> contents = new ArrayList<Data>();
        int page = 1;

        while(true) {
            URL url = new URL("https://api.pokemontcg.io/v2/cards?q=set.ptcgoCode:" + setCode + "%20&page=" + page + "/key=1bfc1133-79a4-46f6-93d6-6a4dab4b7335");
            String jsonStr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine();
            JsonArray jsonArr = JsonParser.parseString(jsonStr).getAsJsonObject().getAsJsonArray("data");

            if(jsonArr.size() < 1) {
                break;
            }
            for(JsonElement j : jsonArr) {
                try {
                    String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

                    if(rarity.equalsIgnoreCase("shiny")) {
                        if(cardRarity.toLowerCase().contains("rare") && cardRarity.length() > 4 
                        || cardRarity.equalsIgnoreCase("legend")) {
                            contents.add(new Data(setEmote, j));
                        }
        
                    } else if(rarity.equalsIgnoreCase("promo")) {
                        try {
                            if(cardRarity.equalsIgnoreCase(rarity)) {
                                contents.add(new Data(setEmote, j));
                            }
                        } catch(UnsupportedOperationException e) {}

                    } else {
                        if(cardRarity.equalsIgnoreCase(rarity)) {
                            contents.add(new Data(setEmote, j));
                        }
                    }
                } catch(NullPointerException e) {}
            }
            page++;
        }
        return contents;
    }

    public static ArrayList<Data> crawlSpecSetContent(String setEmote, String setCode) throws IOException {
        ArrayList<Data> contents = new ArrayList<Data>();
        int page = 1;

        while(true) {
            URL url = new URL("https://api.pokemontcg.io/v2/cards?q=set.ptcgoCode:" + setCode + "%20&page=" + page + "/key=1bfc1133-79a4-46f6-93d6-6a4dab4b7335");
            String jsonStr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine();
            JsonArray jsonArr = JsonParser.parseString(jsonStr).getAsJsonObject().getAsJsonArray("data");

            if(jsonArr.size() < 1) {
                break;
            }
            for(JsonElement j : jsonArr) {
                try {
                    contents.add(new Data(setEmote, j));
                } catch(NullPointerException e) {}
            }
            page++;
        }
        return contents;
    }
}
