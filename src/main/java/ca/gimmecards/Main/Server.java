package ca.gimmecards.Main;
import ca.gimmecards.MainInterfaces.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.entities.Guild;
import java.util.ArrayList;
import java.util.Calendar;

public class Server implements IServer {
    
    /**
     * a list of type Server that's saved and loaded from Servers.json; edited on a regular basis
     */
    public static ArrayList<Server> servers = new ArrayList<Server>();
    
    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String serverId;            // the server's Discord ID
    private ArrayList<Card> market;     // a list of cards that makes up the market; is different in each server
    private Long marketEpoch;           // used to keep track of the market's refresh cooldown

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * creates a new Server
     * @param serverId the server's Discord ID
     */
    public Server(String serverId) {
        this.serverId = serverId;
        this.market = new ArrayList<Card>();
        this.marketEpoch = (long)(0);
    }

    /**
     * duplicates a Server, and encrypts their Discord ID to comply with Discord security guidelines
     * @param server the Server to duplicate
     */
    public Server(Server server) {
        this.serverId = Main.encryptor.encrypt(server.getServerId());
        this.market = server.getMarket();
        this.marketEpoch = server.getMarketEpoch();
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getServerId() { return this.serverId; }
    public ArrayList<Card> getMarket() { return this.market; }
    public long getMarketEpoch() { return this.marketEpoch; }
    
    //================================================[ SETTERS ]======================================================================

    public void setServerId(String serverId) { this.serverId = serverId; }
    public void resetMarketEpoch() { this.marketEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }

    //=============================================[ PUBLIC STATIC FUNCTIONS ]==============================================================

    /**
     * loads data from Servers.json into the ArrayList at the top
     * @throws Exception ignores all possible exceptions
     */
    public static void loadServers() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameManager.findSavePath(GameManager.serverPath)), "UTF-8");
        servers = new Gson().fromJson(reader, new TypeToken<ArrayList<Server>>() {}.getType());

        for(Server s : servers) {
            s.setServerId(Main.encryptor.decrypt(s.getServerId()));
        }
        reader.close();
    }

    /**
     * saves data from the ArrayList at the top into Servers.json
     * @throws Exception ignores all possible exceptions
     */
    public static void saveServers() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameManager.findSavePath(GameManager.serverPath)), "UTF-8");
        ArrayList<Server> encServers = new ArrayList<Server>();
        
        for(Server s : servers) {
            encServers.add(new Server(s));
        }
        gson.toJson(encServers, writer);
        writer.close();
    }

    /**
     * finds the local Server based on a ready event
     * @param event the ready event
     * @return the local Server
     */
    public static Server findServer(GuildReadyEvent event) {
        String serverId = event.getGuild().getId();

        return searchForServer(serverId);
    }

    /**
     * finds the local Server based on a slash event
     * @param event the slash event
     * @return the local Server
     */
    public static Server findServer(SlashCommandInteractionEvent event) {
        String serverId = "";
        Guild guild = event.getGuild();

        if(guild != null) {
            serverId = guild.getId();
        }
        return searchForServer(serverId);
    }

    /**
     * finds the local Server based on a button event
     * @param event the button event
     * @return the local Server
     */
    public static Server findServer(ButtonInteractionEvent event) {
        String serverId = "";
        Guild guild = event.getGuild();

        if(guild != null) {
            serverId = guild.getId();
        }
        return searchForServer(serverId);
    }

    //==============================================[ PUBLIC NON-STATIC FUNCTIONS ]=====================================================

    @Override
    public void refreshMarket() {
        this.market.clear();
        resetMarketEpoch();

        for(int i = 0; i < 15; i++) {
            this.market.add(Card.pickRandomCard("shiny"));
        }
        try { saveServers(); } catch(Exception e) {}
    }

    //===============================================[ PRIVATE FUNCTIONS ]=============================================================

    /**
     * searches through the Server list for a specific server
     * @param serverId the ID of the server to search for
     * @return the server to be searched
     */
    private static Server searchForServer(String serverId) {
        for(Server s : servers) {
            if(s.getServerId().equals(serverId)) {
                return s;
            }
        }
        servers.add(0, new Server(serverId));
        return servers.get(0);
    }
}
