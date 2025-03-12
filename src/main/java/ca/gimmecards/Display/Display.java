package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.Hashtable;
import java.util.LinkedList;

// should be an abstract class, but this CAN'T be an abstract class (due to complications with loading data on startup)
public class Display {

    public static Hashtable<String, LinkedList<Display>> globalDisplays = new Hashtable<>();

    private static LinkedList<Display> findDisplays(User user) {

        if(globalDisplays.get(user.getUserId()) == null)
            globalDisplays.put(user.getUserId(), new LinkedList<>());

        return globalDisplays.get(user.getUserId());
    }

    private static void removeDisplay(LinkedList<Display> displays, Display dispToRemove) {

        for(int i = 0; i < displays.size(); i++) {
            Display disp = displays.get(i);

            if(disp.getClass().equals(dispToRemove.getClass())) {
                displays.remove(i);
                break;
            }
        }
    }

    public static Display addDisplay(User user, Display dispToAdd) {
        LinkedList<Display> displays = findDisplays(user);

        removeDisplay(displays, dispToAdd);
        displays.addFirst(dispToAdd);

        return dispToAdd;
    }

    public static <T> Display findDisplay(User user, Class<T> clazz) {
        LinkedList<Display> displays = findDisplays(user);
        Display result = null;

        for(Display disp : displays) {
            if(clazz.isInstance(disp)) {
                result = disp;
                break;
            }
        }
        return result;
    }

    public static Display findDisplay(User user, String slashId) {
        LinkedList<Display> displays = findDisplays(user);
        Display result = null;

        for(Display disp : displays) {
            if(disp.getSlashId().equals(slashId)) {
                result = disp;
                break;
            }
        }
        return result;
    }

    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String slashId;     // the slash command ID of this display
    private Integer page;       // the page number of this display
    private Integer maxPage;    // the maximum page of this display
    private UserInfo userInfo;
    private UserInfo targetInfo;
    private String targetId;

    //=============================================[ CONSTRUCTORS ]====================================================================

    public Display(SlashCommandInteractionEvent event) {
        this.slashId = event.getInteraction().getId();
        this.page = 1;
        this.maxPage = 1;
        this.userInfo = new UserInfo(event);
        this.targetInfo = null;
        this.targetId = "";
    }

    // single-sided multiplayer
    public Display(SlashCommandInteractionEvent event, User target) {
        this(event);

        this.targetInfo = new UserInfo(event, target);
        this.targetId = target.getUserId();
    }

    // double-sided multiplayer
    public Display(SlashCommandInteractionEvent event, User user, User target) {
        this(event, target);
        
        this.userInfo = new UserInfo(event, user);
    }

    //===============================================[ GETTERS ] ======================================================================
    
    public String getSlashId() { return slashId; }
    public int getPage() { return page; }
    public int getMaxPage() { return maxPage; }
    public UserInfo getUserInfo() { return userInfo; }
    public UserInfo getTargetInfo() { return targetInfo; }
    public String getTargetId() { return targetId; }
    
    //================================================[ SETTERS ]======================================================================
    
    public void setPage(int page) { this.page = page; }
    public void setMaxPage(int maxPage) { this.maxPage = maxPage; }

    public void nextPage() {
        page++;

        if(page > maxPage)
            page = 1;
    }
    public void prevPage() {
        page--;

        if(page < 1)
            page = maxPage;
    }

    public void checkOverflow() {
        if(page < 0)
            page = 1;

        else if(page > maxPage + 1)
            page = maxPage;
    }

    //==============================================[ PUBLIC NON-STATIC FUNCTIONS ]=====================================================

    /**
     * builds a dynamic embed (one that can be page-flipped or refreshed); this function should only be used by Display's subclasses
     * @param user the player
     * @param ui a UserInfo object containing the player's basic information
     * @param server the server
     * @param page the page that this embed should initially show
     * @return the completed embed
     */
    public EmbedBuilder buildEmbed(User user, Server server) { return null; }
}
