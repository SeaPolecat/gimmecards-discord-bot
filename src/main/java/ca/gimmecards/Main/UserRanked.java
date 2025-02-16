package ca.gimmecards.main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.ArrayList;

public class UserRanked implements Comparable<UserRanked> {

    public static ArrayList<UserRanked> usersRanked = new ArrayList<>();
    
    private String userName;
    private User user;

    public UserRanked(String userName, User user) {
        this.userName = userName;
        this.user = user;
    }

    public String getUserName() { return userName; }
    public User getUser() { return user; }

    public void setUserName(String userName) { this.userName = userName; }

    public static void findUserNames(SlashCommandInteractionEvent event) {
        for(UserRanked ur : usersRanked) {
            String userId = ur.getUser().getUserId();

            ur.setUserName(event.getJDA().getUserById(userId).getEffectiveName());
        }
    }

    @Override
    public int compareTo(UserRanked other) {
        if(this.user.getLevel() < other.getUser().getLevel()) {
            return -1;

        } else if(this.user.getLevel() > other.getUser().getLevel()) {
            return 1;

        } else {
            Integer thisXP = this.getUser().getXP();
            Integer otherXP = other.getUser().getXP();

            return thisXP.compareTo(otherXP);
        }
    }
}
