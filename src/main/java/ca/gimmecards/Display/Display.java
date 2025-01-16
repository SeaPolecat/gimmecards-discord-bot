package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;

// should be an abstract class, but this CAN'T be an abstract class (due to complications with loading data on startup)
public class Display {

    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String slashId;     // the slash command ID of this display
    private Integer page;       // the page number of this display
    private Integer maxPage;    // the maximum page of this display

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * default constructor for Display
     */
    public Display() {
        this.slashId = "";
        this.page = 1;
        this.maxPage = 1;
    }

    //===============================================[ GETTERS ] ======================================================================
    
    public String getSlashId() { return this.slashId; }
    public int getPage() { return this.page; }
    public int getMaxPage() { return this.maxPage; }
    
    //================================================[ SETTERS ]======================================================================

    public void setSlashId(String slashId) { this.slashId = slashId; }
    public void setPage(int page) { this.page = page; }
    public void nextPage() { this.page++; }
    public void prevPage() { this.page--; }
    public void setMaxPage(int maxPage) { this.maxPage = maxPage; }
    public void addMaxPage() { this.maxPage++; }

    //==============================================[ PUBLIC NON-STATIC FUNCTIONS ]=====================================================

    /**
     * builds a dynamic embed (one that can be page-flipped or refreshed); this function should only be used by Display's subclasses
     * @param user the player
     * @param ui a UserInfo object containing the player's basic information
     * @param server the server
     * @param page the page that this embed should initially show
     * @return the completed embed
     */
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) { return null; }
}
