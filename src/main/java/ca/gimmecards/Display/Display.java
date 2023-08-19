package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_MP.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class Display extends ListenerAdapter {

    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String userId;      // the player's Discord ID
    private String slashId;     // the slash command ID of this display
    private Integer page;       // the page number of this display
    private Integer maxPage;    // the maximum page of this display

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * default constructor for Display
     */
    public Display() {}

    /**
     * creates a new Display
     * @param userId the player's Discord ID
     */
    public Display(String userId) {
        this.userId = userId;
        this.slashId = "";
        this.page = 1;
        this.maxPage = 1;
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getUserId() { return this.userId; }
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
     * @return the Display subclass; this function should only be used by Display's subclasses
     */
    public Display findDisplay() {
        return null;
    }

    /**
     * builds a dynamic embed (one that can be page-flipped or refreshed); this function should only be used by Display's subclasses
     * @param user the player
     * @param ui a UserInfo object containing the player's basic information
     * @param server the server
     * @param page the page that this embed should initially show
     * @return the completed embed
     */
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        return null;
    }

    //==============================================[ EVENT FUNCTIONS ]================================================================

    /**
     * an event function that's called whenever a user clicks a button; used to handle page-flipping
     * @param event the button event
     */
    public void onButtonInteraction(ButtonInteractionEvent event) {
        User user = User.findUser(event);
        String buttonId = event.getComponentId();
        int semiColIndex = buttonId.indexOf(";");
        int underscoreIndex = buttonId.indexOf("_");
        String userId = buttonId.substring(0, semiColIndex);
        String slashId = buttonId.substring(semiColIndex + 1, underscoreIndex);
        String buttonType = buttonId.substring(underscoreIndex + 1);

        if(!user.getUserId().equals(userId)) {
            event.reply("This is not your button!").setEphemeral(true).queue();

        } else {
            if(buttonType.equals("deleteaccount_yes")) {
                PrivacyCmds.confirmDeletion(event);

            } else if(buttonType.equals("deleteaccount_no")) {
                PrivacyCmds.denyDeletion(event);

            } else if(buttonType.equals("premium")) {
                HelpCmds.viewPremium(event);

            } else {
                ArrayList<Display> displays = new ArrayList<Display>();
                Server server = Server.findServer(event);

                displays.add(new BackpackDisplay(user.getUserId()).findDisplay());
                displays.add(new CollectionDisplay(user.getUserId()).findDisplay());
                displays.add(new HelpDisplay(user.getUserId()).findDisplay());
                displays.add(new LeaderboardDisplay(user.getUserId()).findDisplay());
                displays.add(new MarketDisplay(user.getUserId()).findDisplay());
                displays.add(new MinigameDisplay(user.getUserId()).findDisplay());
                displays.add(new OldShopDisplay(user.getUserId()).findDisplay());
                displays.add(new SearchDisplay(user.getUserId()).findDisplay());
                displays.add(new ShopDisplay(user.getUserId()).findDisplay());
                displays.add(new TradeDisplay(user.getUserId()).findDisplay());
                displays.add(new ViewDisplay(user.getUserId()).findDisplay());
                //
                displays.add(new BackpackDisplay_MP(user.getUserId()).findDisplay());
                displays.add(new CardDisplay_MP(user.getUserId()).findDisplay());
                displays.add(new ViewDisplay_MP(user.getUserId()).findDisplay());

                for(Display disp : displays) {
                    if(slashId.equals(disp.getSlashId())) {
                        flipPage(event, user, server, disp);
                        return;
                    }
                }
                event.reply("This button is outdated. Please send a new command!").setEphemeral(true).queue();
            }
        }
    }

    //===============================================[ PRIVATE FUNCTIONS ]=============================================================

    /**
     * flips the page on a dynamic embed (one that can be page-flipped or refreshed)
     * @param event the button event
     * @param user the player
     * @param server the server
     * @param disp the type of display that's being page-flipped
     */
    private void flipPage(ButtonInteractionEvent event, User user, Server server, Display disp) {
        String buttonId = event.getComponentId();
        int underscoreIndex = buttonId.indexOf("_");
        String buttonType = buttonId.substring(underscoreIndex + 1);

        if(disp instanceof CollectionDisplay) {
            if(user.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(user.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                while(user.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                    disp.prevPage();
                }
                GameManager.editEmbed(event, user, server, disp, disp.getPage());
                return;
            }

        } else if(disp instanceof CardDisplay_MP) {
            CardDisplay_MP disp_ = new CardDisplay_MP(user.getUserId()).findDisplay();
            User mention = disp_.getMention();

            if(mention.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(mention.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                while(mention.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                    disp.prevPage();
                }
                GameManager.editEmbed(event, user, server, disp, disp.getPage());
                return; 
            }

        } else if(disp instanceof ViewDisplay) {
            if(user.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(user.getCardContainers().size() < disp.getPage()) {
                while(user.getCardContainers().size() < disp.getPage()) {
                    disp.prevPage();
                }
                GameManager.editEmbed(event, user, server, disp, disp.getPage());
                return;
            }

        } else if(disp instanceof ViewDisplay_MP) {
            ViewDisplay_MP disp_ = new ViewDisplay_MP(user.getUserId()).findDisplay();
            User mention = disp_.getMention();

            if(mention.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(mention.getCardContainers().size() < disp.getPage()) {
                while(mention.getCardContainers().size() < disp.getPage()) {
                    disp.prevPage();
                }
                GameManager.editEmbed(event, user, server, disp, disp.getPage());
                return;
            }
        }

        if(buttonType.equals("left")) {
            if(disp.getPage() <= 1) {
                disp.setPage(disp.getMaxPage());
            } else {
                disp.prevPage();
            }

        } else if(buttonType.equals("right")) {
            if(disp.getPage() >= disp.getMaxPage()) {
                disp.setPage(1);
            } else {
                disp.nextPage();
            }
        }
        // if the buttonType is not 'left' or 'right', flip to the same page
        GameManager.editEmbed(event, user, server, disp, disp.getPage());
    }
}
