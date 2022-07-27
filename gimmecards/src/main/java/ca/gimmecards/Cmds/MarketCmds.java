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

        if(State.isCooldownDone(server.getMarketEpoch(), 1440, true)) {
            server.refreshMarket();
            Rest.sendMessage(event, mew_ + " The market has been refreshed!");
        }
        desc += "Next refresh in " + State.findTimeLeft(server.getMarketEpoch(), 1440, true) + "\n";
        desc += "┅┅\n";
        desc += "🥳 50% off Sale - Happy Bday Gimme Cards!\n";
        desc += "┅┅\n";
        for(Data data : server.getMarket()) {
            
            desc += UX.findCardTitle(data, false)
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ ~~" + UX.formatEnergyPrice(data) + "~~ " + UX.formatEventEnergyPrice(data) + "\n";
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

    public static void viewItem(MessageReceivedEvent event, String[] args) {
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

    public static void purchaseItem(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);

        try {
            int index = Integer.parseInt(args[1]) - 1;
            Data item = server.getMarket().get(index);

            if(user.getEnergy() < (int)(item.getCardPrice() / 2)) {
                Rest.sendMessage(event, jigglypuff_ + " Sorry, you don't have enough " + energy_ + " **Energy**");

            } else {
                Card c;
                String msg = "";
                String footer = event.getAuthor().getName() + "'s purchase";

                msg += UX.formatNick(event) + " bought " + UX.findCardTitle(item, false) + " from the market!";
                msg += UX.updateEnergy(user, -(int)(item.getCardPrice() / 2));

                c = Card.addSingleCard(user, item, false);

                State.updateBackpackDisplay(event, user);
                State.updateCardDisplay(event, user);

                Rest.sendMessage(event, msg);
                Display.displayCard(event, user, item, c, footer);
                try { User.saveUsers(); } catch(Exception e) {}
            }
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            Rest.sendMessage(event, jigglypuff_ + " Whoops, I couldn't find that card...");
        }
    }
}
