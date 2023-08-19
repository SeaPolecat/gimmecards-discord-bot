package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.EmbedBuilder;

public class MarketCmds {
    
    public static void viewMarket(SlashCommandInteractionEvent event) {
        Server server = Server.findServer(event);
        EmbedBuilder embed = new EmbedBuilder();
        Guild guild = event.getGuild();
        String desc = "";
        int count = 1;

        if(guild != null) {
            if(User.isCooldownDone(server.getMarketEpoch(), 1440, true)) {
                server.refreshMarket();
            }
            desc += "Next refresh in " + User.findTimeLeft(server.getMarketEpoch(), 1440, true) + "\n\n";
            desc += "`/mview (card #)` to view a card\n";
            desc += "┅┅\n";

            for(Card item : server.getMarket()) {
                
                desc += "`#" + count + "` " + item.findCardTitle(false)
                + " ┇ " + item.findRarityEmote() 
                + " ┇ " + item.getSetEmote()
                + " ┇ " + item.formatCredits() + "\n";
                count++;
            }
            desc += "┅┅\n";
            embed.setTitle(IEmotes.mew + " Daily Market " + IEmotes.mew);
            embed.setDescription(desc);
            embed.setFooter(guild.getName(), guild.getIconUrl());
            embed.setColor(IColors.marketColor);
            GameManager.sendEmbed(event, embed);
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

            GameManager.sendDynamicEmbed(event, user, server, disp, page);

        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void purchaseItem(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        Server server = Server.findServer(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        if(!User.isCooldownDone(user.getMarketEpoch(), 15, true)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " 
            + User.findTimeLeft(user.getMarketEpoch(), 15, true));

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                Card item = server.getMarket().get(index);
    
                if(user.getCredits() < item.getCardPrice()) {
                    GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you don't have enough " + IEmotes.credits + " **Credits**");
    
                } else {
                    String msg = "";
                    String footer = ui.getUserName() + "'s purchase";
    
                    msg += IEmotes.mew + " ";
                    msg += GameManager.formatName(event) + " bought " + item.findCardTitle(false) + " from the market!";
                    msg += user.updateCredits(-item.getCardPrice(), true);

                    user.resetMarketEpoch();
    
                    user.addSingleCard(item, false);
                    
                    item.displayCard(event, ui, msg, footer, false);
                    try { User.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
