package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MinigameCmds {
    
    public static void startMinigame(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();;

        if(!User.isCooldownDone(user.getMinigameEpoch(), 60, true)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " 
            + User.findTimeLeft(user.getMinigameEpoch(), 60, true));

        } else {
            user.resetMinigameEpoch();

            disp.resetGame();
            
            GameManager.sendDynamicEmbed(event, user, server, disp, -1);
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
            GameManager.sendMessage(event, IColors.red, "❌", "You haven't started a minigame yet!");

        } else {
            if(disp.isGuessCorrect(rarity.getAsString())) {
                String msg = "";

                disp.endGame(true);

                if(user.hasPremiumRole(event)) {
                    msg += GameManager.formatName(event) + " won the minigame!";
                    msg += user.updateTokens(3, true);
                    msg += user.updateCredits(GameManager.randRange(72, 90), false);
                    msg += user.updateStars(1, false);

                } else {
                    msg += GameManager.formatName(event) + " won the minigame!";
                    msg += user.updateTokens(3, true);
                    msg += user.updateCredits(GameManager.randRange(72, 90), false);
                }

                GameManager.sendMessage(event, user.getGameColor(), "🏆", msg);
                try { User.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    disp.endGame(false);

                    if(user.hasPremiumRole(event)) {
                        msg += GameManager.formatName(event) + " lost the minigame... But there's always next time!";
                        msg += user.updateTokens(1, true);
                        msg += user.updateCredits(GameManager.randRange(24, 30), true);
                        msg += user.updateStars(1, false);

                    } else {
                        msg += GameManager.formatName(event) + " lost the minigame... But there's always next time!";
                        msg += user.updateTokens(1, true);
                        msg += user.updateCredits(GameManager.randRange(24, 30), true);
                    }

                    GameManager.sendMessage(event, user.getGameColor(), "😭", msg);
                    try { User.saveUsers(); } catch(Exception e) {}

                } else {
                    String msg = "";

                    msg += "Whoops, that's not the one... You have **" + disp.getTries() + "** ";
                    if(disp.getTries() == 1) {
                        msg += "try left!";
                    } else {
                        msg += "tries left!";
                    }
                    GameManager.sendMessage(event, user.getGameColor(), IEmotes.clefairy, msg);
                }
            }
        }
    }
}
