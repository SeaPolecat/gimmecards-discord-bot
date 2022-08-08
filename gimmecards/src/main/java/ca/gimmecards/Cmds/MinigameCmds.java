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
            JDA.sendMessage(event, jigglypuff_ + " Please wait another " + Check.findTimeLeft(user.getMinigameEpoch(), 60, true));

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
            JDA.sendMessage(event, jigglypuff_ + " You haven't started a minigame yet!");

        } else {
            String guess = "";
            for(int i = 1; i < args.length; i++) {
                guess += args[i] + " ";
            }

            if(MinigameDisplay.isGuessCorrect(event, user, guess)) {
                String msg = "";

                MinigameDisplay.removeMinigameDisplay(user);

                msg += UX.formatNick(event) + " won the minigame!";
                msg += UX.updateTokens(user, 2);
                msg += UX.updateEnergy(user, UX.randRange(48, 60));

                Update.updateBackpackDisplay(event, user);

                JDA.sendMessage(event, msg);
                try { User.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    MinigameDisplay.removeMinigameDisplay(user);

                    msg += UX.formatNick(event) + " lost the minigame... But there's always next time!";
                    msg += UX.updateEnergy(user, UX.randRange(24, 30));

                    JDA.sendMessage(event, msg);
                    try { User.saveUsers(); } catch(Exception e) {}

                } else {
                    String wrong = "";

                    wrong += clefairy_ + " Whoops, that's not the one... You have **" + disp.getTries() + "** ";
                    if(disp.getTries() == 1) {
                        wrong += "try left!";
                    } else {
                        wrong += "tries left!";
                    }
                    JDA.sendMessage(event, wrong);
                }
            }
        }
    }
}
