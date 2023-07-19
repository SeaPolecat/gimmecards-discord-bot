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
    
    // list of Servers
    public static ArrayList<Server> servers = new ArrayList<Server>();
    
    // instance variables
    private String serverId;
    private ArrayList<Card> market;
    private Long marketEpoch;

    /**
     * constructor for Server
     * @param si serverId
     */
    public Server(String si) {
        serverId = si;
        market = new ArrayList<Card>();
        marketEpoch = (long)(0);
    }

    /**
     * constructor that duplicates a Server, and encrypts serverId
     * @param server the Server to duplicate
     */
    public Server(Server server) {
        serverId = Main.encryptor.encrypt(server.getServerId());
        market = server.getMarket();
        marketEpoch = server.getMarketEpoch();
    }

    // getters
    public String getServerId() { return serverId; }
    public ArrayList<Card> getMarket() { return market; }
    public long getMarketEpoch() { return marketEpoch; }
    
    // setters
    public void setServerId(String si) { serverId = si; }
    public void resetMarketEpoch() { marketEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }

    //===================================================[ PRIVATE FUNCTIONS ]==============================================================

    /**
     * searches through the Server list for a specific Server
     * @param serverId the ID of the Server to search for
     * @return the Server to be searched
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

    //=============================================[ PUBLIC STATIC FUNCTIONS ]==============================================================

    /**
     * loads server data from Servers.json into the Server list
     * @throws Exception just needs it
     */
    public static void loadServers() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(GameObject.findSavePath(GameObject.serverPath)), "UTF-8");
        servers = new Gson().fromJson(reader, new TypeToken<ArrayList<Server>>() {}.getType());

        for(Server s : servers) {
            s.setServerId(Main.encryptor.decrypt(s.getServerId()));
        }
        reader.close();
    }

    /**
     * saves server data into the file Servers.json
     * @throws Exception just needs it
     */
    public static void saveServers() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(GameObject.findSavePath(GameObject.serverPath)), "UTF-8");
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
        market.clear();
        resetMarketEpoch();

        for(int i = 0; i < 15; i++) {
            market.add(Card.pickRandomCard("shiny"));
        }
        try { saveServers(); } catch(Exception e) {}
    }

    @Override
    public String formatCmd(String cmd) {
        return "`/" + cmd + "`";
    }
}
