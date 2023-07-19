package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.EmbedBuilder;

public class MarketCmds extends Cmds {
    
    public static void viewMarket(SlashCommandInteractionEvent event) {
        Server server = Server.findServer(event);
        EmbedBuilder embed = new EmbedBuilder();
        Guild guild = event.getGuild();
        String desc = "";
        int count = 1;

        if(guild != null) {
            if(Check.isCooldownDone(server.getMarketEpoch(), 1440, true)) {
                server.refreshMarket();
            }
            desc += "Next refresh in " + Check.findTimeLeft(server.getMarketEpoch(), 1440, true) + "\n";
            desc += "┅┅\n";
            for(Data item : server.getMarket()) {
                
                desc += "`#" + count + "` " + UX.findCardTitle(item, false)
                + " ┇ " + UX.findRarityEmote(item) 
                + " ┇ " + item.getSetEmote()
                + " ┇ " + UX.formatCredits(item) + "\n";
                count++;
            }
            desc += "┅┅\n";
            embed.setTitle(mew_ + " Daily Market " + mew_);
            embed.setDescription(desc);
            embed.setFooter(guild.getName(), guild.getIconUrl());
            embed.setColor(market_);
            JDA.sendEmbed(event, embed);
            embed.clear();
        }
    }

    public static void viewItem(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MarketDisplay disp = new MarketDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            int page = cardNum.getAsInt();

            JDA.sendDynamicEmbed(event, user, server, disp, page);

        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void purchaseItem(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        Server server = Server.findServer(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        if(!Check.isCooldownDone(user.getMarketEpoch(), 15, true)) {
            JDA.sendMessage(event, red_, "⏰", "Please wait another " 
            + Check.findTimeLeft(user.getMarketEpoch(), 15, true));

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                Data item = server.getMarket().get(index);
    
                if(user.getCredits() < item.getCardPrice()) {
                    JDA.sendMessage(event, red_, "❌", "Sorry, you don't have enough " + credits_ + " **Credits**");
    
                } else {
                    String msg = "";
                    String footer = ui.getUserName() + "'s purchase";
    
                    msg += mew_ + " ";
                    msg += UX.formatNick(event) + " bought " + UX.findCardTitle(item, false) + " from the market!";
                    msg += user.updateCredits(-item.getCardPrice(), true);

                    user.resetMarketEpoch();
    
                    Card.addSingleCard(user, item, false);
                    
                    Display.displayCard(event, user, ui, item, msg, footer, false);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
