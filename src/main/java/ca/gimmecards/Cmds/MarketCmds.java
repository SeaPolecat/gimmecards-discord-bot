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
            int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.marketResetCooldown, server.getMarketEpoch());

            if(cooldownLeft == 0) {
                server.refreshMarket();
            }
            desc += "Next refresh in " + FormatUtils.formatCooldown(cooldownLeft) + "\n\n";
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
            embed.setTitle(EmoteConsts.mew + " Daily Market " + EmoteConsts.mew);
            embed.setDescription(desc);
            embed.setFooter(guild.getName(), guild.getIconUrl());
            embed.setColor(ColorConsts.marketColor);
            JDAUtils.sendEmbed(event, embed);
        }
    }

    public static void viewItem(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        MarketDisplay disp = new MarketDisplay();
        //
        OptionMapping cardNum = event.getOption("card-number");

        user.addDisplay(disp);

        if(cardNum == null) { return; }

        try {
            int page = cardNum.getAsInt();

            JDAUtils.sendDynamicEmbed(event, user, server, disp, page);

        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void purchaseItem(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        Server server = Server.findServer(event);
        //
        OptionMapping cardNum = event.getOption("card-number");
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.buyCooldown, user.getMarketEpoch());

        if(cardNum == null) { return; }

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.red, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                int index = cardNum.getAsInt() - 1;
                Card item = server.getMarket().get(index);
    
                if(user.getCredits() < item.getCardPrice()) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you don't have enough " + EmoteConsts.credits + " **Credits**");
    
                } else {
                    String msg = "";
                    String footer = ui.getUserName() + "'s purchase";
    
                    msg += EmoteConsts.mew + " ";
                    msg += FormatUtils.formatName(event) + " bought " + item.findCardTitle(false) + " from the market!";
                    msg += user.updateCredits(-item.getCardPrice(), true);

                    user.resetMarketEpoch();
    
                    user.addSingleCard(item, false);
                    
                    item.displayCard(event, ui, msg, footer, false);
                    try { DataUtils.saveUsers(); } catch(Exception e) {}
                }
            } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
            }
        }
    }
}
