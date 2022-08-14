package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class MarketCmds extends Cmds {
    
    public static void viewMarket(MessageReceivedEvent event) {
        Server server = Server.findServer(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        int count = 1;

        if(Check.isCooldownDone(server.getMarketEpoch(), 1440, true)) {
            server.refreshMarket();
            JDA.sendMessage(event, market_, mew_, "The market has been refreshed!");
        }
        desc += "Next refresh in " + Check.findTimeLeft(server.getMarketEpoch(), 1440, true) + "\n";
        desc += "┅┅\n";
        for(Data item : server.getMarket()) {
            
            desc += "`#" + count + "` " + UX.findCardTitle(item, false)
            + " ┇ " + UX.findRarityEmote(item) 
            + " ┇ " + item.getSetEmote()
            + " ┇ " + UX.formatEnergy(item) + "\n";
            count++;
        }
        desc += "┅┅\n";
        embed.setTitle(mew_ + " Daily Market " + mew_);
        embed.setDescription(desc);
        embed.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
        embed.setColor(market_);
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewItem(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MarketDisplay disp = new MarketDisplay(user.getUserId()).findDisplay();

        try {
            int page = Integer.parseInt(args[1]);

            JDA.sendDynamicEmbed(event, user, server, disp, page);

        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void purchaseItem(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        if(!Check.isCooldownDone(user.getMarketEpoch(), 15, true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " + Check.findTimeLeft(user.getMarketEpoch(), 15, true));

        } else {
            try {
                int index = Integer.parseInt(args[1]) - 1;
                Data item = server.getMarket().get(index);
    
                if(user.getEnergy() < item.getCardPrice()) {
                    JDA.sendMessage(event, red_, "❌", "Sorry, you don't have enough " + energy_ + " **Energy**");
    
                } else {
                    String msg = "";
                    String footer = event.getAuthor().getName() + "'s purchase";

                    user.resetMarketEpoch();
    
                    msg += UX.formatNick(event) + " bought " + UX.findCardTitle(item, false) + " from the market!";
                    msg += user.updateEnergy(-item.getCardPrice(), true);
    
                    Card.addSingleCard(user, item);
    
                    Update.updateBackpackDisplay(event, user);
                    Update.updateCardDisplay(event, user);
                    Update.updateViewDisplay(event, user);
    
                    JDA.sendMessage(event, user.getGameColor(), mew_, msg);
                    Display.displayCard(event, user, item, footer);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
