package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class VoteCmds extends Cmds {

    public static void voteBot(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!State.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getVoteEpoch(), 720, true));

        } else {
            EmbedBuilder embed = new EmbedBuilder();
            String desc = "";
    
            desc += "Use the link below to vote, then type " + UX.formatCmd(server, "claim") + " to claim your reward!\n\n";
            desc += "[Vote on Top.gg](https://top.gg/bot/814025499381727232/vote)";
    
            embed.setTitle(gift_ + " Voting Reward " + gift_);
            embed.setDescription(desc);
            embed.setColor(0xBD2D2D);
            Rest.sendEmbed(event, embed);
            embed.clear();
        }
    }
    
    public static void claimReward(MessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!State.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getVoteEpoch(), 720, true));

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
                    Rest.sendMessage(event, jigglypuff_ + " You haven't voted for *Gimme Cards* yet! "
                    + "Please use " + UX.formatCmd(server, "vote"));

                } else {
                    EmbedBuilder embed = new EmbedBuilder();
                    String msg = "";

                    user.resetVoteEpoch();

                    msg += UX.formatNick(event) + " claimed their gift! Thank you for voting 😊";
                    msg += UX.updateTokens(user, 5);
                    msg += UX.updateEnergy(user, UX.randRange(120, 150));
                    msg += UX.updateStars(user, 1);
    
                    State.updateBackpackDisplay(event, user);

                    embed.setDescription(msg);
                    embed.setColor(0x408CFF);
                    Rest.sendEmbed(event, embed);
                    embed.clear();
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(IOException | InterruptedException e) {}
        }
    }
}
