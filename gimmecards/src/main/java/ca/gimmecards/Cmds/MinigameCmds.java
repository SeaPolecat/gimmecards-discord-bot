package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MinigameCmds extends Cmds {
    
    public static void startMinigame(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();;

        if(!Check.isCooldownDone(user.getMinigameEpoch(), 60, true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " + Check.findTimeLeft(user.getMinigameEpoch(), 60, true));

        } else {
            user.resetMinigameEpoch();
            
            JDA.sendDynamicEmbed(event, user, server, disp, -1);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void makeGuess(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();

        if(disp.getMessageId().isEmpty()) {
            JDA.sendMessage(event, red_, "❌", "You haven't started a minigame yet!");

        } else {
            String guess = "";
            for(int i = 1; i < args.length; i++) {
                guess += args[i] + " ";
            }

            if(disp.isGuessCorrect(guess)) {
                String msg = "";

                Update.updateMinigameDisplay(event, user);
                disp.removeMinigameDisplay();

                msg += UX.formatNick(event) + " won the minigame!";
                msg += user.updateTokens(2, true);
                msg += user.updateEnergy(UX.randRange(48, 60), false);

                Update.updateBackpackDisplay(event, user);

                JDA.sendMessage(event, user.getGameColor(), "🏆", msg);
                try { User.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    msg += UX.formatNick(event) + " lost the minigame... But there's always next time!";
                    msg += user.updateEnergy(UX.randRange(24, 30), true);

                    Update.updateBackpackDisplay(event, user);
                    Update.updateMinigameDisplay(event, user);
                    disp.removeMinigameDisplay();

                    JDA.sendMessage(event, user.getGameColor(), "😭", msg);
                    try { User.saveUsers(); } catch(Exception e) {}

                } else {
                    String msg = "";

                    msg += "Whoops, that's not the one... You have **" + disp.getTries() + "** ";
                    if(disp.getTries() == 1) {
                        msg += "try left!";
                    } else {
                        msg += "tries left!";
                    }
                    Update.updateMinigameDisplay(event, user);

                    JDA.sendMessage(event, user.getGameColor(), clefairy_, msg);
                }
            }
        }
    }
}
