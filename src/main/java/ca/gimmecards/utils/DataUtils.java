package ca.gimmecards.utils;
import ca.gimmecards.main.*;
import java.io.*;
import java.nio.file.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.ArrayList;

public class DataUtils {

    // these are all private, so they don't need to go in the Constants folder
    
    /**
     * the address of this game's local save files; please change this accordingly (remember the 2 backslashes at the end)
     */
    private static final String PREFIX = "C:\\Users\\wangw\\Documents\\DocumentsV2\\GimmeCards\\src\\main\\java\\ca\\gimmecards\\storage\\";

    private static final String SETS_ADDRESS = "CardSets.json";
    private static final String OLD_SETS_ADDRESS = "OldCardSets.json";
    private static final String RARE_SETS_ADDRESS = "RareCardSets.json";
    private static final String PROMO_SETS_ADDRESS = "PromoCardSets.json";
    private static final String USER_ADDRESS = "Users.json";
    private static final String SERVER_ADDRESS = "Servers.json";

    /**
     * determines whether or not to add a prefix to the address of a storage file
     * @param address the address in question
     * @return the address with a prefix if the file is on your local computer; no prefix is added if the file is being hosted on the VPS
     */
    private static String findSaveAddress(String address) {
        if(Paths.get(address).toFile().length() > 0) {
            return address;
        } else {
            return PREFIX + address;
        }
    }

    //=============================================[ LOADING DATA ]==============================================================

    /**
     * loads data from Users.json into the ArrayList at the top
     */
    public static void loadUsers() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(findSaveAddress(USER_ADDRESS)), "UTF-8");

            User.users = new Gson().fromJson(reader, new TypeToken<ArrayList<User>>() {}.getType());
            reader.close();

            // displays are supposed to be temporary, so clear any that were accidentally saved
            for(User u : User.users)
                u.getDisplays().clear();

            // make usersRanked a shallow copy of users
            User.usersRanked = new ArrayList<>(User.users);

            saveUsers();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * loads data from Servers.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
     */
    public static void loadServers() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(findSaveAddress(SERVER_ADDRESS)), "UTF-8");
            Server.servers = new Gson().fromJson(reader, new TypeToken<ArrayList<Server>>() {}.getType());
    
            for(Server s : Server.servers) {
                s.setServerId(Main.encryptor.decrypt(s.getServerId()));
            }
            reader.close();
            
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * loads data from CardSets.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
     */
    public static void loadSets() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(findSaveAddress(SETS_ADDRESS)), "UTF-8");
            CardSet.sets = new Gson().fromJson(reader, CardSet[].class);
    
            reader.close();
            
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * loads data from OldCardSets.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
     */
    public static void loadOldSets() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(findSaveAddress(OLD_SETS_ADDRESS)), "UTF-8");
            CardSet.oldSets = new Gson().fromJson(reader, CardSet[].class);
    
            reader.close();
            
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * loads data from RareCardSets.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
     */
    public static void loadRareSets() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(findSaveAddress(RARE_SETS_ADDRESS)), "UTF-8");
            CardSet.rareSets = new Gson().fromJson(reader, CardSet[].class);
    
            reader.close();
            
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * loads data from PromoCardSets.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
     */
    public static void loadPromoSets() {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(findSaveAddress(PROMO_SETS_ADDRESS)), "UTF-8");
            CardSet.promoSets = new Gson().fromJson(reader, CardSet[].class);
    
            reader.close();
            
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    //=============================================[ SAVING DATA ]==============================================================

    /**
     * saves data from the ArrayList at the top into Users.json
     */
    public static void saveUsers() {
        try {
            Gson gson = new GsonBuilder().create();
            Writer writer = new OutputStreamWriter(new FileOutputStream(findSaveAddress(USER_ADDRESS)), "UTF-8");

            gson.toJson(User.users, writer);
            writer.close();

        } catch(Exception e) {
            System.out.println(e.toString());

            System.out.println("AAAAAA HEREEEEE");
        }
    }

    /**
     * saves data from the ArrayList at the top into Servers.json
     * @throws Exception ignores all possible exceptions
     */
    public static void saveServers() {
        try {
            Gson gson = new GsonBuilder().create();
            Writer writer = new OutputStreamWriter(new FileOutputStream(findSaveAddress(SERVER_ADDRESS)), "UTF-8");
            ArrayList<Server> encServers = new ArrayList<Server>();
            
            for(Server s : Server.servers) {
                encServers.add(new Server(s));
            }
            gson.toJson(encServers, writer);
            writer.close();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * saves data from the ArrayList at the top into CardSets.json
     * @throws Exception ignores all possible exceptions
     */
    public static void saveSets() {
        try {
            Gson gson = new GsonBuilder().create();
            Writer writer = new OutputStreamWriter(new FileOutputStream(findSaveAddress(SETS_ADDRESS)), "UTF-8");

            gson.toJson(CardSet.sets, writer);
            writer.close();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * saves data from the ArrayList at the top into OldCardSets.json
     * @throws Exception ignores all possible exceptions
     */
    public static void saveOldSets() {
        try {
            Gson gson = new GsonBuilder().create();
            Writer writer = new OutputStreamWriter(new FileOutputStream(findSaveAddress(OLD_SETS_ADDRESS)), "UTF-8");
    
            gson.toJson(CardSet.oldSets, writer);
            writer.close();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * saves data from the ArrayList at the top into RareCardSets.json
     * @throws Exception ignores all possible exceptions
     */
    public static void saveRareSets() {
        try {
            Gson gson = new GsonBuilder().create();
            Writer writer = new OutputStreamWriter(new FileOutputStream(findSaveAddress(RARE_SETS_ADDRESS)), "UTF-8");
    
            gson.toJson(CardSet.rareSets, writer);
            writer.close();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * saves data from the ArrayList at the top into PromoCardSets.json
     * @throws Exception ignores all possible exceptions
     */
    public static void savePromoSets() {
        try {
            Gson gson = new GsonBuilder().create();
            Writer writer = new OutputStreamWriter(new FileOutputStream(findSaveAddress(PROMO_SETS_ADDRESS)), "UTF-8");
    
            gson.toJson(CardSet.promoSets, writer);
            writer.close();

        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
