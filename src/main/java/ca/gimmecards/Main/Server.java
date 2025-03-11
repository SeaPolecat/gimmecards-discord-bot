package ca.gimmecards.main;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import ca.gimmecards.utils.*;

public class Server extends ListenerAdapter implements Comparable<Server> {
    
    /**
     * a list of type Server that's saved and loaded from Servers.json; edited on a regular basis
     */
    public static List<Server> servers;
    
    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String serverId;            // the server's Discord ID
    private ArrayList<Card> market;     // a list of cards that makes up the market; is different in each server
    private Long marketEpoch;           // used to keep track of the market's refresh cooldown

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * default constructor for Server
     */
    public Server() {}

    /**
     * creates a new Server
     * @param serverId the server's Discord ID
     */
    public Server(String serverId) {
        this.serverId = Main.encryptor.encrypt(serverId);
        this.market = new ArrayList<Card>();
        this.marketEpoch = (long)(0);
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getServerId() { return Main.encryptor.decrypt(this.serverId); }
    public ArrayList<Card> getMarket() { return this.market; }
    public long getMarketEpoch() { return this.marketEpoch; }
    
    //================================================[ SETTERS ]======================================================================

    public void resetMarketEpoch() { this.marketEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }

    //=============================================[ STATIC METHODS ]==============================================================

    /**
     * finds the local Server based on a ready event
     * @param event the ready event
     * @return the local Server
     */
    public static Server findServer(GuildReadyEvent event) {
        String serverId = "";
        Guild guild = event.getGuild();

        if(guild != null)
            serverId = guild.getId();

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

        if(guild != null)
            serverId = guild.getId();

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

        if(guild != null)
            serverId = guild.getId();

        return searchForServer(serverId);
    }

    //==============================================[ INSTANCE METHODS ]=====================================================

    @Override
    public int compareTo(Server other) {
        Long serverId = Long.parseLong(this.getServerId());
        Long otherId = Long.parseLong(other.getServerId());

        return serverId.compareTo(otherId);
    }

    /**
     * refreshes the card market in the server
     */
    public void refreshMarket() {
        this.market.clear();
        resetMarketEpoch();

        for(int i = 0; i < 15; i++) {
            this.market.add(Card.pickRandomCard("shiny"));
        }
        try { DataUtils.saveServers(); } catch(Exception e) {}
    }

    //===============================================[ HELPER METHODS ]=============================================================

    /**
     * searches through the Server list for a specific server
     * @param serverId the ID of the server to search for
     * @return the server to be searched
     */
    private static Server searchForServer(String serverId) {
        Server serverToFind = new Server(serverId);

        synchronized(servers) {
            int index = SearchUtils.binarySearch(servers, serverToFind);
    
            if(index == servers.size() || serverToFind.compareTo(servers.get(index)) != 0) {
                servers.add(index, serverToFind);

            } else {
                serverToFind = servers.get(index);
            }
        }
        return serverToFind;
    }

    //==============================================[ JDA EVENT METHODS ]================================================================

    /**
     * an event function that's called whenever the bot leaves a server; used to delete a server's data within Servers.json if the bot leaves that server
     * @param event the GuildLeave event
     */
    public void onGuildLeave(GuildLeaveEvent event) {
        for(int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);

            if(s.getServerId().equals(event.getGuild().getId())) {
                servers.remove(i);
                try { DataUtils.saveServers(); } catch(Exception e) {}
                break;
            }
        }
    }
}
