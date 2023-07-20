package ca.gimmecards.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class UserInfo {
    
    // instance variables
    private String userName;
    private String userPing;
    private String userIcon;

    /**
     * constructor that uses a slash event to get the author User's info
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
     * constructor that uses a slash event to get a mentioned User's info
     * @param mention the mentioned User
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
     * constructor that uses a button event to get the author User's info
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

    // getters
    public String getUserName() { return userName; }
    public String getUserPing() { return userPing; }
    public String getUserIcon() { return userIcon; }
}
