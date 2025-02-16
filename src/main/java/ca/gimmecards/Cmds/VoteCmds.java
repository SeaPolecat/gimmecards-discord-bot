package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.EmbedBuilder;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class VoteCmds {

    public static void voteBot(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.VOTE_CD, user.getVoteEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            Emoji topggEmote = event.getJDA().getEmojiById("1158152227131772928");
            String desc = "";
    
            desc += "Click the button below to vote, then type " + FormatUtils.formatCmd("claim") + " to claim your reward!\n\n";
    
            embed.setTitle(EmoteConsts.LOOTBOX + " Voting Reward " + EmoteConsts.LOOTBOX);
            embed.setDescription(desc);
            embed.setColor(ColorConsts.VOTE_COLOR);

            event.getHook().editOriginalEmbeds(embed.build())
            .setActionRow(
                Button.link("https://top.gg/bot/814025499381727232/vote", "Vote on Top.gg").withEmoji(topggEmote)
            ).queue();
        }
    }
    
    public static void claimReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.VOTE_CD, user.getVoteEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://top.gg/api/bots/814025499381727232/check?userId=" + user.getUserId()))
                .GET()
                .header("Authorization", PasswordConsts.DBL_TOKEN)
                .build();
                String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body().toString();
                boolean hasVoted = response.contains("1");
    
                if(!hasVoted) {
                    JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You haven't voted for *Gimme Cards* yet! "
                    + "Please use " + FormatUtils.formatCmd("vote"));

                } else {
                    String msg = "";

                    user.resetVoteEpoch();

                    msg += ui.getUserPing() + " claimed their gift! Thank you for voting 😊";
                    msg += user.updateTokens(RewardConsts.CLAIM_TOKENS, true);
                    msg += user.updateCredits(NumberUtils.randRange(RewardConsts.CLAIM_CREDITS_MIN, RewardConsts.CLAIM_CREDITS_MAX), false);
                    msg += user.updateStars(RewardConsts.CLAIM_STARS, false);

                    JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.LOOTBOX, msg);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            } catch(IOException | InterruptedException e) {}
        }
    }
}
