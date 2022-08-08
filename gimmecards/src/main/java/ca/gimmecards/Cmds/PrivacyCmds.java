package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrivacyCmds extends Cmds {
    
    public static void deleteAccount(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        PrivacyDisplay disp = new PrivacyDisplay(user.getUserId()).findDisplay();;
        
        JDA.sendDynamicEmbed(event, user, server, disp, -1);
    }

    public static void confirmDeletion(MessageReceivedEvent event) {
        User user = User.findUser(event);
        PrivacyDisplay disp = new PrivacyDisplay(user.getUserId()).findDisplay();

        if(disp.getMessageId().isEmpty()) {
            return;

        } else {
            for(int i = 0; i < User.users.size(); i++) {
                User u = User.users.get(i);

                if(u.getUserId().equals(user.getUserId())) {
                    User.users.remove(i);

                    disp.setIsSure(true);
                    Update.updatePrivacyDisplay(event, user, false);
                    PrivacyDisplay.removePrivacyDisplay(user);

                    JDA.sendMessage(event, "`Your Gimme Cards account has been deleted. "
                    + "To ensure that your user data does not get recorded again, "
                    + "do not use any commands from this point onwards.`");
                    try { User.saveUsers(); } catch(Exception e) {}
                    return;
                }
            }
        }
    }

    public static void denyDeletion(MessageReceivedEvent event) {
        User user = User.findUser(event);
        PrivacyDisplay disp = new PrivacyDisplay(user.getUserId()).findDisplay();

        if(disp.getMessageId().isEmpty()) {
            return;

        } else {
            Update.updatePrivacyDisplay(event, user, true);
            PrivacyDisplay.removePrivacyDisplay(user);

            JDA.sendMessage(event, "😊 We're glad you're not going away... *welcome back!*");
        }
    }
}
