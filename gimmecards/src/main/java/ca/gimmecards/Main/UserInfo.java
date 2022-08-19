package ca.gimmecards.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class UserInfo {
    
    private String userName;
    private String userPing;
    private String userIcon;

    public UserInfo(MessageReceivedEvent event) {
        userName = event.getAuthor().getName();
        userPing = event.getAuthor().getAsMention();
        userIcon = event.getAuthor().getAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getAuthor().getDefaultAvatarUrl();
        }
    }

    public UserInfo(User mention, MessageReceivedEvent event) {
        userName = event.getJDA().getUserById(mention.getUserId()).getName();
        userPing = event.getJDA().getUserById(mention.getUserId()).getAsMention();
        userIcon = event.getJDA().getUserById(mention.getUserId()).getAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getJDA().getUserById(mention.getUserId()).getDefaultAvatarUrl();
        }
    }

    public UserInfo(MessageReactionAddEvent event) {
        userName = event.getUser().getName();
        userPing = event.getUser().getAsMention();
        userIcon = event.getUser().getAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getUser().getDefaultAvatarUrl();
        }
    }

    public String getUserName() { return userName; }
    public String getUserPing() { return userPing; }
    public String getUserIcon() { return userIcon; }
}
