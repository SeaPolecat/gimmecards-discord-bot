package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MinigameCmds extends Cmds {
    
    public static void startMinigame(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!State.isCooldownDone(user.getMinigameEpoch(), 60, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getMinigameEpoch(), 60, true));

        } else {
            MinigameDisplay.addMinigameDisplay(user);
            MinigameDisplay disp = MinigameDisplay.findMinigameDisplay(user.getUserId());
            
            user.resetMinigameEpoch();
            
            Rest.sendDynamicEmbed(event, user, server, disp, -1);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void makeGuess(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        MinigameDisplay disp = MinigameDisplay.findMinigameDisplay(user.getUserId());

        try {
            String guess = "";
            for(int i = 1; i < args.length; i++) {
                guess += args[i] + " ";
            }
            guess = guess.trim();

            if(MinigameDisplay.isGuessCorrect(event, user, guess)) {
                String msg = "";

                MinigameDisplay.removeMinigameDisplay(user);

                msg += UX.formatNick(event) + " won the minigame!";
                msg += UX.updateTokens(user, 2);
                msg += UX.updateEnergy(user, UX.randRange(80, 100));

                State.updateBackpackDisplay(event, user);

                Rest.sendMessage(event, msg);
                try { User.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    MinigameDisplay.removeMinigameDisplay(user);

                    msg += UX.formatNick(event) + " lost the minigame... But there's always next time!";
                    msg += UX.updateEnergy(user, UX.randRange(40, 50));

                    Rest.sendMessage(event, msg);
                    try { User.saveUsers(); } catch(Exception e) {}

                } else {
                    String wrong = "";

                    wrong += clefairy_ + " Whoops, that's not the one... You have **" + disp.getTries() + "** ";
                    if(disp.getTries() == 1) {
                        wrong += "try left!";
                    } else {
                        wrong += "tries left!";
                    }
                    Rest.sendMessage(event, wrong);
                }
            }
        } catch(NullPointerException e) {
            Rest.sendMessage(event, jigglypuff_ + " You haven't started a minigame yet!");
        }
    }
}
