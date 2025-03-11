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
        MinigameDisplay disp = (MinigameDisplay) Display.addDisplay(user, new MinigameDisplay(event));
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.MINIGAME_CD, user.getMinigameEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            user.resetMinigameEpoch();

            disp.resetGame();
            
            JDAUtils.sendDynamicEmbed(event, disp, user, null, false);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void makeGuess(SlashCommandInteractionEvent event) {
        OptionMapping rarity = event.getOption("rarity");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        MinigameDisplay disp = (MinigameDisplay) Display.findDisplay(user, MinigameDisplay.class);

        if(disp == null || disp.getHasCompleted()) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't started a minigame yet!");

        } else {
            if(disp.isGuessCorrect(rarity.getAsString())) {
                String msg = "";

                disp.endGame(true);

                msg += ui.getUserPing() + " won the minigame!";
                msg += user.updateTokens(RewardConsts.MINIGAME_TOKENS_WIN, true);
                msg += user.updateCredits(NumberUtils.randRange(RewardConsts.MINIGAME_CREDITS_WIN_MIN, RewardConsts.MINIGAME_CREDITS_WIN_MAX), false);

                JDAUtils.sendMessage(event, user.getGameColor(), "🏆", msg);
                try { DataUtils.saveUsers(); } catch(Exception e) {}

            } else {
                if(disp.getTries() < 1) {
                    String msg = "";

                    disp.endGame(false);

                    msg += ui.getUserPing() + " lost the minigame... But there's always next time!";
                    msg += user.updateTokens(RewardConsts.MINIGAME_TOKENS_LOSE, true);
                    msg += user.updateCredits(NumberUtils.randRange(RewardConsts.MINIGAME_CREDITS_LOSE_MIN, RewardConsts.MINIGAME_CREDITS_LOSE_MAX), true);

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
                    JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.CLEFAIRY, msg);
                }
            }
        }
    }
}
