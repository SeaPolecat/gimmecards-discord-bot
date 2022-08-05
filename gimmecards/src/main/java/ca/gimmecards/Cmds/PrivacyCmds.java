package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrivacyCmds extends Cmds {
    
    public static void deleteAccount(MessageReceivedEvent event) {
        User user = User.findUser(event);
        PrivacyDisplay disp;

        PrivacyDisplay.addPrivacyDisplay(user);

        disp = PrivacyDisplay.findPrivacyDisplay(user.getUserId());
        Rest.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void confirmDeletion(MessageReceivedEvent event) {
        User user = User.findUser(event);
        PrivacyDisplay disp = PrivacyDisplay.findPrivacyDisplay(user.getUserId());

        if(disp == null) {
            return;

        } else {
            for(int i = 0; i < User.users.size(); i++) {
                User u = User.users.get(i);

                if(u.getUserId().equals(user.getUserId())) {
                    User.users.remove(i);

                    disp.setIsSure(true);
                    State.updatePrivacyDisplay(event, user, false);
                    PrivacyDisplay.removePrivacyDisplay(user);

                    Rest.sendMessage(event, "`Your Gimme Cards account has been deleted. "
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
        PrivacyDisplay disp = PrivacyDisplay.findPrivacyDisplay(user.getUserId());

        if(disp == null) {
            return;

        } else {
            State.updatePrivacyDisplay(event, user, true);
            PrivacyDisplay.removePrivacyDisplay(user);

            Rest.sendMessage(event, "😊 We're glad you're not going away... *welcome back!*");
        }
    }
}
