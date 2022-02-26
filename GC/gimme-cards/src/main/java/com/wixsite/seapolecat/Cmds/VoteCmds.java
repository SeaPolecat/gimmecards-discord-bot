package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.Random;

public class VoteCmds extends Cmds {

    public static void voteBot(GuildMessageReceivedEvent event) {
        Server server = Server.findServer(event);
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
    
    public static void claimReward(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!State.isCooldownDone(user.getDailyEpoch(), 720, true)) {
            Rest.sendMessage(event, jigglypuff_ + " Please wait another " + State.findTimeLeft(user.getDailyEpoch(), 720, true));

        } else {
            Main.dbl.hasVoted(user.getUserId()).whenComplete((hasVoted, failure) -> {
                if(!hasVoted) {
                    Rest.sendMessage(event, jigglypuff_ + " You haven't voted for *Gimme Cards* yet! "
                    + "Please use " + UX.formatCmd(server, "vote"));
                    
                } else {
                    int energyReward = (new Random().nextInt(10) + 1) + 40;
                    Data reward = Card.pickCard(Data.promos);
                    String msg = "";
                    String footer = event.getAuthor().getName() + "'s reward";

                    Card.addSingleCard(user, reward);
                    user.resetDailyEpoch();

                    msg += UX.formatNick(event) + " claimed their reward... Thank you for voting!";
                    msg += UX.updateTokens(user, 5);
                    msg += UX.updateEnergy(user, energyReward);

                    State.updateBackpackDisplay(event, user);
                    State.updateCardDisplay(event, user);

                    Rest.sendMessage(event, msg);
                    Display.displayCard(event, user, reward, footer);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            });
        }
    }
}
