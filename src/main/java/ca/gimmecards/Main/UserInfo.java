package ca.gimmecards.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class UserInfo {
    
    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String userName;    // the player's effective Discord username
    private String userPing;    // the player's ping tag
    private String userIcon;    // the player's profile picture image link

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * creates a new UserInfo based on a slash event
     * @param event the slash event
     */
    public UserInfo(SlashCommandInteractionEvent event) {
        userName = event.getUser().getEffectiveName();
        userPing = event.getUser().getAsMention();
        userIcon = event.getUser().getEffectiveAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getUser().getDefaultAvatarUrl();
        }
    }

    /**
     * creates a new UserInfo based on a mentioned player and a slash event
     * @param mention the mentioned player
     * @param event the slash event
     */
    public UserInfo(User mention, SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.User user = event.getJDA().getUserById(mention.getUserId()+"");

        if(user != null) {
            userName = user.getName();
            userPing = user.getAsMention();
            userIcon = user.getAvatarUrl();

            if(userIcon == null) {
                userIcon = user.getDefaultAvatarUrl();
            }
        }
    }

    /**
     * creates a new UserInfo based on a button event
     * @param event the button event
     */
    public UserInfo(ButtonInteractionEvent event) {
        userName = event.getUser().getEffectiveName();
        userPing = event.getUser().getAsMention();
        userIcon = event.getUser().getEffectiveAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getUser().getDefaultAvatarUrl();
        }
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getUserName() { return userName; }
    public String getUserPing() { return userPing; }
    public String getUserIcon() { return userIcon; }
}
