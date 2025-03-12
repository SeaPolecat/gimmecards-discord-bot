package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
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
        int itemNum = 1;

        if(guild != null) {
            int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.MARKET_RESET_CD, server.getMarketEpoch());

            if(cooldownLeft == 0) {
                server.refreshMarket();

                desc += "✨ **The market has refreshed** ✨\n\n";

            } else {
                desc += "Next refresh in " + FormatUtils.formatCooldown(cooldownLeft) + "\n\n";
            }
            desc += "`/mview (card #)` to view a card\n";
            desc += "┅┅\n";

            for(Card item : server.getMarket()) {
                
                desc += "`#" + itemNum + "` " + item.findCardTitle(false)
                + " ┇ " + item.findRarityEmote() 
                + " ┇ " + item.getSetEmote()
                + " ┇ " + item.formatCredits() + "\n";
                itemNum++;
            }
            desc += "┅┅\n";
            embed.setTitle(EmoteConsts.MEW + " Daily Market " + EmoteConsts.MEW);
            embed.setDescription(desc);
            embed.setFooter(guild.getName(), guild.getIconUrl());
            embed.setColor(ColorConsts.MARKET_COLOR);
            JDAUtils.sendEmbed(event, embed);
        }
    }

    public static void viewItem(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MarketDisplay disp = (MarketDisplay) Display.addDisplay(user, new MarketDisplay(event, server, cardNum.getAsInt()));

        try {
            JDAUtils.sendDynamicEmbed(event, disp, user, server, true);

        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void purchaseItem(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        Server server = Server.findServer(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.BUY_CD, user.getMarketEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                Card item = server.getMarket().get(index);
    
                if(user.getCredits() < item.getCardPrice()) {
                    JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you don't have enough " + EmoteConsts.CREDITS + " **Credits**");
    
                } else {
                    String msg = "";
                    String footer = ui.getUserName() + "'s purchase";
    
                    msg += EmoteConsts.MEW + " ";
                    msg += ui.getUserPing() + " bought " + item.findCardTitle(false) + " from the market!";
                    msg += user.updateCredits(-item.getCardPrice(), true);

                    user.resetMarketEpoch();
    
                    user.addSingleCard(item, false);
                    
                    item.displayCard(event, ui, msg, footer, false);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
