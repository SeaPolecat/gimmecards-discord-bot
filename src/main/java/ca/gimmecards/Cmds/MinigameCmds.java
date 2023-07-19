package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MinigameCmds extends Cmds {
    
    public static void startMinigame(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();;

        if(!GameObject.isCooldownDone(user.getMinigameEpoch(), 60, true)) {
            GameObject.sendMessage(event, red_, "⏰", "Please wait another " 
            + GameObject.findTimeLeft(user.getMinigameEpoch(), 60, true));

        } else {
            user.resetMinigameEpoch();

            disp.resetGame();
            
            GameObject.sendDynamicEmbed(event, user, server, disp, -1);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void makeGuess(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping rarity = event.getOption("rarity");

        if(rarity == null) { return; }

        if(disp.getIsOver()) {
            GameObject.sendMessage(event, red_, "❌", "You haven't started a minigame yet!");

        } else {
            if(disp.isGuessCorrect(rarity.getAsString())) {
                String msg = "";

                disp.endGame(true);

                msg += GameObject.formatNick(event) + " won the minigame!";
                msg += user.updateTokens(2, true);
                msg += user.updateCredits(GameObject.randRange(48, 60), false);

                GameObject.sendMessage(event, user.getGameColor(), "🏆", msg);
                try { User.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    disp.endGame(false);

                    msg += GameObject.formatNick(event) + " lost the minigame... But there's always next time!";
                    msg += user.updateCredits(GameObject.randRange(24, 30), true);

                    GameObject.sendMessage(event, user.getGameColor(), "😭", msg);
                    try { User.saveUsers(); } catch(Exception e) {}

                } else {
                    String msg = "";

                    msg += "Whoops, that's not the one... You have **" + disp.getTries() + "** ";
                    if(disp.getTries() == 1) {
                        msg += "try left!";
                    } else {
                        msg += "tries left!";
                    }
                    GameObject.sendMessage(event, user.getGameColor(), clefairy_, msg);
                }
            }
        }
    }
}
