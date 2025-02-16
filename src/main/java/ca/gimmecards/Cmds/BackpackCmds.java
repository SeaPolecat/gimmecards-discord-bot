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
        BackpackDisplay disp = (BackpackDisplay) user.addDisplay(new BackpackDisplay(event));

        JDAUtils.sendDynamicEmbed(event, disp, user, null, false);
    }

    public static void redeemToken(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.REDEEM_CD, user.getRedeemEpoch());

        //if(cooldownLeft > 0) {
            //JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            //+ FormatUtils.formatCooldown(cooldownLeft));

        //} else {
            String msg = "";

            user.resetRedeemEpoch();

            msg += ui.getUserPing() + " redeemed a token!";
            msg += user.updateTokens(RewardConsts.REDEEM_TOKENS, true);
            msg += user.updateCredits(NumberUtils.randRange(RewardConsts.REDEEM_CREDITS_MIN, RewardConsts.REDEEM_CREDITS_MAX), false);

            msg += "\n┅┅\n";
            msg += ChangelogConsts.UPDATE_MESSAGE + "\n\n";

            JDAUtils.sendMessage(event, user.getGameColor(), "🎒", msg);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        //}
    }

    public static void receiveDailyReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.DAILY_CD, user.getDailyEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            Card item = Card.pickRandomCard("shiny");
            String msg = "";

            msg += "🎴 ";
            msg += ui.getUserPing() + " claimed their daily shiny card!";
            msg += user.updateCredits(NumberUtils.randRange(RewardConsts.DAILY_CREDITS_MIN, RewardConsts.DAILY_CREDITS_MAX), true);
            msg += user.updateStars(RewardConsts.DAILY_STARS, false);

            user.resetDailyEpoch();

            user.addSingleCard(item, false);

            String footer = ui.getUserName() + "'s shiny card";
            
            item.displayCard(event, ui, msg, footer, false);
            try { DataUtils.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void assignGameColor(SlashCommandInteractionEvent event) {
        OptionMapping hexCode = event.getOption("hex-code");
        //
        User user = User.findUser(event);

        try {
            int color = Integer.parseInt(hexCode.getAsString(), 16);

            user.setGameColor(color);

            JDAUtils.sendMessage(event, user.getGameColor(), EmoteConsts.EEVEE, 
            "Set your game's theme color to **" + hexCode.getAsString().toUpperCase() + "**");
            try { DataUtils.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException | ArithmeticException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "That's not a valid hex code!");
        }
    }

    public static void pinCard(SlashCommandInteractionEvent event) {
        OptionMapping cardNum = event.getOption("card-number");
        //
        User user = User.findUser(event);

        try {
            int index = cardNum.getAsInt() - 1;
            CardContainer cc = user.getCardContainers().get(index);
            String cardTitle = cc.getCard().findCardTitle(false);
            String cardImage = cc.getCard().getCardImage();

            user.setPinnedCard(cardImage);

            JDAUtils.sendMessage(event, user.getGameColor(), "🎒", "**" + cardTitle + "** has been pinned to your backpack!");
            try { DataUtils.saveUsers(); } catch(Exception e) {}
            
        } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void viewCooldowns(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        int redeemCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.REDEEM_CD, user.getRedeemEpoch());
        int minigameCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.MINIGAME_CD, user.getMinigameEpoch());
        int voteCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.VOTE_CD, user.getVoteEpoch());
        int dailyCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.DAILY_CD, user.getDailyEpoch());
        int buyCDLeft = TimeUtils.findCooldownLeft(CooldownConsts.BUY_CD, user.getMarketEpoch());

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
