package ca.gimmecards.main;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import java.util.ArrayList;
import java.util.Calendar;

import ca.gimmecards.utils.*;

public class Server extends ListenerAdapter implements Comparable<Server> {
    
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
     * default constructor for Server
     */
    public Server() {}

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
    public void resetMarketEpoch() { this.marketEpoch = Calendar.getInstance().getTimeInMillis() / 1000; }

    //=============================================[ STATIC METHODS ]==============================================================

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

    //==============================================[ INSTANCE METHODS ]=====================================================

    @Override
    public int compareTo(Server other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
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
        for(Server s : servers) {
            if(s.getServerId().equals(serverId)) {
                return s;
            }
        }
        servers.add(0, new Server(serverId));
        return servers.get(0);
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
