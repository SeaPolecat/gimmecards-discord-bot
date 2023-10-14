package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
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

        if(!User.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " 
            + User.findTimeLeft(user.getVoteEpoch(), 720, true));

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            Emoji topggEmote = event.getJDA().getEmojiById("1158152227131772928");
            String desc = "";
    
            desc += "Click the button below to vote, then type " + GameManager.formatCmd("claim") + " to claim your reward!\n\n";
    
            embed.setTitle(IEmotes.lootbox + " Voting Reward " + IEmotes.lootbox);
            embed.setDescription(desc);
            embed.setColor(IColors.voteColor);

            event.getHook().editOriginalEmbeds(embed.build())
            .setActionRow(
                Button.link("https://top.gg/bot/814025499381727232/vote", "Vote on Top.gg").withEmoji(topggEmote)
            ).queue();
        }
    }
    
    public static void claimReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);

        if(!User.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " 
            + User.findTimeLeft(user.getVoteEpoch(), 720, true));

        } else {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://top.gg/api/bots/814025499381727232/check?userId=" + user.getUserId()))
                .GET()
                .header("Authorization", Main.dblToken)
                .build();
                String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body().toString();
                boolean hasVoted = response.contains("1");
    
                if(!hasVoted) {
                    GameManager.sendMessage(event, IColors.red, "❌", "You haven't voted for *Gimme Cards* yet! "
                    + "Please use " + GameManager.formatCmd("vote"));

                } else {
                    String msg = "";

                    user.resetVoteEpoch();

                    if(user.hasPremiumRole(event)) {
                        msg += GameManager.formatName(event) + " claimed their gift! Thank you for voting 😊";
                        msg += user.updateTokens(6, true);
                        msg += user.updateCredits(GameManager.randRange(144, 180), false);
                        msg += user.updateStars(1, false);

                    } else {
                        msg += GameManager.formatName(event) + " claimed their gift! Thank you for voting 😊";
                        msg += user.updateTokens(6, true);
                        msg += user.updateCredits(GameManager.randRange(144, 180), false);
                    }

                    GameManager.sendMessage(event, user.getGameColor(), IEmotes.lootbox, msg);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(IOException | InterruptedException e) {}
        }
    }
}
