package com.wixsite.seapolecat.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class UserInfo {
    
    private String userName;
    private String userIcon;

    public UserInfo(GuildMessageReceivedEvent event) {
        userName = event.getAuthor().getName();
        userIcon = event.getAuthor().getAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getAuthor().getDefaultAvatarUrl();
        }
    }

    public UserInfo(GuildMessageReactionAddEvent event) {
        userName = event.getUser().getName();
        userIcon = event.getUser().getAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getUser().getDefaultAvatarUrl();
        }
    }

    public String getUserName() { return userName; }
    public String getUserIcon() { return userIcon; }
}
