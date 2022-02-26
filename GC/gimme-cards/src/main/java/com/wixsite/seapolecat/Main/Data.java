package com.wixsite.seapolecat.Main;
import com.wixsite.seapolecat.Interfaces.*;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Hashtable;

public class Data implements StoragePaths {

    public static Hashtable<Integer, String> setCodes = new Hashtable<>();
    public static Hashtable<Integer, String> oldSetCodes = new Hashtable<>();
    //
    public static Data[] sets = new Data[42];
    public static Data[] oldSets = new Data[46];
    public static ArrayList<Data> promos = new ArrayList<Data>();
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

    public static void loadData() throws Exception {
        String path = "";

        if(Paths.get(dataPath).toFile().length() > 0) {
            path = dataPath;
        } else {
            path = header + dataPath;
        }
        Reader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
        sets = new Gson().fromJson(reader, new TypeToken<ArrayList<Data>>() {}.getType());

        reader.close();
    }

    public static void loadOldData() throws Exception {
        String path = "";

        if(Paths.get(oldDataPath).toFile().length() > 0) {
            path = oldDataPath;
        } else {
            path = header + oldDataPath;
        }
        Reader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
        oldSets = new Gson().fromJson(reader, new TypeToken<ArrayList<Data>>() {}.getType());

        reader.close();
    }

    public static void loadPromos() throws Exception {
        String path = "";

        if(Paths.get(promoPath).toFile().length() > 0) {
            path = promoPath;
        } else {
            path = header + promoPath;
        }
        Reader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
        promos = new Gson().fromJson(reader, new TypeToken<ArrayList<Data>>() {}.getType());

        reader.close();
    }

    public static void saveData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(dataPath), "UTF-8");
        gson.toJson(sets, writer);
        writer.close();
    }

    public static void saveOldData() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(oldDataPath), "UTF-8");
        gson.toJson(oldSets, writer);
        writer.close();
    }

    public static void savePromos() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(promoPath), "UTF-8");
        gson.toJson(promos, writer);
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
        return (int) (cardPrice * 100);
    }
}
