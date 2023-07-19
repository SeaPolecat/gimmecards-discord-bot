package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class VoteCmds extends Cmds {

    public static void voteBot(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!GameObject.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            GameObject.sendMessage(event, red_, "⏰", "Please wait another " 
            + GameObject.findTimeLeft(user.getVoteEpoch(), 720, true));

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            String desc = "";
    
            desc += "Use the link below to vote, then type " + server.formatCmd("claim") + " to claim your reward!\n\n";
            desc += "[Vote on Top.gg](https://top.gg/bot/814025499381727232/vote)";
    
            embed.setTitle(lootbox_ + " Voting Reward " + lootbox_);
            embed.setDescription(desc);
            embed.setColor(vote_);
            GameObject.sendEmbed(event, embed);
            embed.clear();
        }
    }
    
    public static void claimReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!GameObject.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            GameObject.sendMessage(event, red_, "⏰", "Please wait another " 
            + GameObject.findTimeLeft(user.getVoteEpoch(), 720, true));

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
                    GameObject.sendMessage(event, red_, "❌", "You haven't voted for *Gimme Cards* yet! "
                    + "Please use " + server.formatCmd("vote"));

                } else {
                    String msg = "";

                    user.resetVoteEpoch();

                    msg += GameObject.formatNick(event) + " claimed their gift! Thank you for voting 😊";
                    msg += user.updateTokens(5, true);
                    msg += user.updateCredits(GameObject.randRange(120, 150), false);
                    msg += user.updateStars(1, false);

                    GameObject.sendMessage(event, user.getGameColor(), lootbox_, msg);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(IOException | InterruptedException e) {}
        }
    }
}
