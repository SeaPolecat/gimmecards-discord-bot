package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class MarketCmds extends Cmds {
    
    public static void viewMarket(GuildMessageReceivedEvent event) {
        Server server = Server.findServer(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        int count = 1;

        if(State.isCooldownDone(server.getMarketEpoch(), 1440, true)) {
            server.refreshMarket();
            Rest.sendMessage(event, mew_ + " The market has been refreshed!");
        }
        desc += "Next refresh in " + State.findTimeLeft(server.getMarketEpoch(), 1440, true) + "\n";
        desc += "┅┅\n";
        for(Data data : server.getMarket()) {
            
            desc += UX.findCardTitle(data, false)
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatEnergyPrice(data) + "\n";
            count++;
        }
        desc += "┅┅\n";
        embed.setTitle(mew_ + " Daily Market " + mew_);
        embed.setDescription(desc);
        embed.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
        embed.setColor(0x76B1EC);
        
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewItem(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MarketDisplay disp = MarketDisplay.findMarketDisplay(user.getUserId());

        try {
            int page = Integer.parseInt(args[1]);

            Rest.sendDynamicEmbed(event, user, server, disp, page);
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }

    public static void purchaseItem(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        try {
            int page = Integer.parseInt(args[1]);
            int index = page - 1;

            if(user.getEnergy() < server.getMarket().get(index).getCardPrice()) {
                Rest.sendMessage(event, jigglypuff_ + " Sorry, you don't have enough " + energy_ + " **Energy**");

            } else {
                Data item = server.getMarket().get(index);
                String msg = "";
                String footer = event.getAuthor().getName() + "'s purchase";

                msg += UX.formatNick(event) + " bought " + UX.findCardTitle(item, false) + " from the market!";
                msg += UX.updateEnergy(user, -item.getCardPrice());

                Card.addSingleCard(user, item);

                State.updateBackpackDisplay(event, user);
                State.updateCardDisplay(event, user);
                State.checkLevelUp(event, user);

                Rest.sendMessage(event, msg);
                Display.displayCard(event, user, item, footer);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }
}
