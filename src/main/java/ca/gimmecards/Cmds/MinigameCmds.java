package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MinigameCmds {
    
    public static void startMinigame(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MinigameDisplay disp = new MinigameDisplay();
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.minigameCooldown, user.getMinigameEpoch());

        user.addDisplay(disp);

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.red, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            user.resetMinigameEpoch();

            disp.resetGame();
            
            JDAUtils.sendDynamicEmbed(event, user, server, disp, -1);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void makeGuess(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        MinigameDisplay disp = (MinigameDisplay) user.findDisplay(new MinigameDisplay());
        //
        OptionMapping rarity = event.getOption("rarity");

        if(rarity == null) { return; }

        if(disp == null || disp.getIsOver()) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "You haven't started a minigame yet!");

        } else {
            if(disp.isGuessCorrect(rarity.getAsString())) {
                String msg = "";

                disp.endGame(true);

                msg += FormatUtils.formatName(event) + " won the minigame!";
                msg += user.updateTokens(RewardConsts.minigameTokens_win, true);
                msg += user.updateCredits(NumberUtils.randRange(RewardConsts.minigameCredits_win_min, RewardConsts.minigameCredits_win_max), false);

                JDAUtils.sendMessage(event, user.getGameColor(), "🏆", msg);
                try { DataUtils.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    disp.endGame(false);

                    msg += FormatUtils.formatName(event) + " lost the minigame... But there's always next time!";
                    msg += user.updateTokens(RewardConsts.minigameTokens_lose, true);
                    msg += user.updateCredits(NumberUtils.randRange(RewardConsts.minigameCredits_lose_min, RewardConsts.minigameCredits_lose_max), true);

                    JDAUtils.sendMessage(event, user.getGameColor(), "😭", msg);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}

                } else {
                    String msg = "";

                    msg += "Whoops, that's not the one... You have **" + disp.getTries() + "** ";
                    if(disp.getTries() == 1) {
                        msg += "try left!";
                    } else {
                        msg += "tries left!";
                    }
                    JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.clefairy, msg);
                }
            }
        }
    }
}
