package ca.gimmecards.Main;
import ca.gimmecards.Interfaces.*;
import java.nio.file.Paths;
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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import java.util.ArrayList;
import java.util.Calendar;

public class Server implements StoragePaths {
    
    public static ArrayList<Server> servers = new ArrayList<Server>();
    //
    private String serverId; //ENCRYPTED
    private String prefix;
    private ArrayList<Data> market;
    private Long marketEpoch;

    public Server(String si) {
        serverId = si;
        prefix = "?";
        market = new ArrayList<Data>();
        marketEpoch = (long)(0);
    }

    public Server(Server server) {
        serverId = Main.encryptor.encrypt(server.getServerId());
        prefix = server.getPrefix();
        market = server.getMarket();
        marketEpoch = server.getMarketEpoch();
    }

    public String getServerId() { return serverId; }
    public String getPrefix() { return prefix; }
    public ArrayList<Data> getMarket() { return market; }
    public long getMarketEpoch() { return marketEpoch; }
    //
    public void setServerId(String si) { serverId = si; }
    public void setPrefix(String p) { prefix = p; }
    public void resetMarketEpoch() { marketEpoch = Calendar.getInstance().getTimeInMillis() / 60000; }

    public void refreshMarket() {
        market.clear();
        resetMarketEpoch();

        for(int i = 0; i < 15; i++) {
            market.add(Card.pickRandomCard("shiny"));
        }
        try { saveServers(); } catch(Exception e) {}
    }

    private static String determinePath() {
        if(Paths.get(serverPath).toFile().length() > 0) {
            return serverPath;
        } else {
            return header + serverPath;
        }
    }
    
    public static void loadServers() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(determinePath()), "UTF-8");
        servers = new Gson().fromJson(reader, new TypeToken<ArrayList<Server>>() {}.getType());

        for(Server s : servers) {
            s.setServerId(Main.encryptor.decrypt(s.getServerId()));
        }
        reader.close();
    }

    public static void saveServers() throws Exception {
        Gson gson = new GsonBuilder().create();
        Writer writer = new OutputStreamWriter(new FileOutputStream(determinePath()), "UTF-8");
        ArrayList<Server> encServers = new ArrayList<Server>();
        
        for(Server s : servers) {
            encServers.add(new Server(s));
        }
        gson.toJson(encServers, writer);
        writer.close();
    }

    public static Server findServer(GuildReadyEvent event) {
        String serverId = event.getGuild().getId();

        return searchForServer(serverId);
    }

    public static Server findServer(MessageReceivedEvent event) {
        String serverId = event.getGuild().getId();

        return searchForServer(serverId);
    }

    public static Server findServer(MessageReactionAddEvent event) {
        String serverId = event.getGuild().getId();

        return searchForServer(serverId);
    }

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
