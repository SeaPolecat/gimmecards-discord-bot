package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackCmds {

    public static void viewBackpack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = new BackpackDisplay();
        
        user.addDisplay(disp);

        JDAUtils.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.redeemCooldown, user.getRedeemEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.red, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            String msg = "";

            user.resetRedeemEpoch();

            msg += FormatUtils.formatName(event) + " redeemed a token!";
            msg += user.updateTokens(RewardConsts.redeemTokens, true);
            msg += user.updateCredits(NumberUtils.randRange(RewardConsts.redeemCredits_min, RewardConsts.redeemCredits_max), false);

            msg += "\n┅┅\n";
            msg += ChangelogConsts.updateMsg + "\n\n";

            JDAUtils.sendMessage(event, user.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.dailyCooldown, user.getDailyEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.red, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            Card item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = ui.getUserName() + "'s shiny card";

            msg += "🎴 ";
            msg += FormatUtils.formatName(event) + " claimed their daily shiny card!";
            msg += user.updateCredits(NumberUtils.randRange(RewardConsts.dailyCredits_min, RewardConsts.dailyCredits_max), true);
            msg += user.updateStars(RewardConsts.dailyStars, false);

            user.resetDailyEpoch();

            user.addSingleCard(item, false);
            
            item.displayCard(event, ui, msg, footer, false);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignGameColor(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping hexCode = event.getOption("hex-code");

        if(hexCode == null) { return; }

        try {
            int color = Integer.parseInt(hexCode.getAsString(), 16);

            user.setGameColor(color);

            JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.eevee, 
            "Set your game's theme color to **" + hexCode.getAsString().toUpperCase() + "**");
            try { DataUtils.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException | ArithmeticException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "That's not a valid hex code!");
        }
    }

    public static void pinCard(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        //
        OptionMapping cardNum = event.getOption("card-number");

        if(cardNum == null) { return; }

        try {
            int index = cardNum.getAsInt() - 1;
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);
            String cardImage = cc.getCard().getCardImage();

            user.setPinnedCard(cardImage);

            JDAUtils.sendMessage(event, user.getGameColor(), "🎒", "**" + cardTitle + "** has been pinned to your backpack!");
            try { DataUtils.saveUsers(); } catch(Exception e) {}
            
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void viewCooldowns(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        int redeemCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.redeemCooldown, user.getRedeemEpoch());
        int minigameCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.minigameCooldown, user.getMinigameEpoch());
        int voteCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.voteCooldown, user.getVoteEpoch());
        int dailyCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.dailyCooldown, user.getDailyEpoch());
        int buyCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.buyCooldown, user.getMarketEpoch());

        desc += FormatUtils.formatCmd("redeem") + " ┇ ";
        if(redeemCDLeft == 0) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + FormatUtils.formatCooldown(redeemCDLeft) + "\n\n";
        }
        desc += FormatUtils.formatCmd("minigame") + " ┇ ";
        if(minigameCDLeft == 0) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + FormatUtils.formatCooldown(minigameCDLeft) + "\n\n";
        }
        desc += FormatUtils.formatCmd("vote") + " ┇ ";
        if(voteCDLeft == 0) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + FormatUtils.formatCooldown(voteCDLeft) + "\n\n";
        }
        desc += FormatUtils.formatCmd("daily") + " ┇ ";
        if(dailyCDLeft == 0) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + FormatUtils.formatCooldown(dailyCDLeft) + "\n\n";
        }
        desc += FormatUtils.formatCmd("buy") + " ┇ ";
        if(buyCDLeft == 0) {
            desc += "✅";
        } else {
            desc += "⏰ " + FormatUtils.formatCooldown(buyCDLeft);
        }
        embed.setTitle(ui.getUserName() + "'s Cooldowns");
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getGameColor());
        JDAUtils.sendEmbed(event, embed);
    }
}
