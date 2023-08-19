package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackCmds {

    public static void viewBackpack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        BackpackDisplay disp = new BackpackDisplay(user.getUserId()).findDisplay();

        GameManager.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void redeemToken(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);

        if(!User.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " 
            + User.findTimeLeft(user.getRedeemEpoch(), 30, true));

        } else {
            String msg = "";
            int adChance = GameManager.randRange(0, 1);

            user.resetRedeemEpoch();
            user.minusQuestRedeem();

            if(user.hasPremiumRole(event)) {
                msg += GameManager.formatName(event) + " redeemed a token and star!";

                if(user.getQuestRedeems() > 0) {
                    msg += "\n\n**REVIVAL QUEST**\nRedeem `" + user.getQuestRedeems() + "` more times for a special gift!";
                }
                msg += user.updateTokens(1, true);
                msg += user.updateCredits(GameManager.randRange(24, 30), false);
                msg += user.updateStars(1, false);

            } else {
                msg += GameManager.formatName(event) + " redeemed a token!";

                if(user.getQuestRedeems() > 0) {
                    msg += "\n\n**REVIVAL QUEST**\nRedeem `" + user.getQuestRedeems() + "` more times for a special gift!";
                }
                msg += user.updateTokens(1, true);
                msg += user.updateCredits(GameManager.randRange(24, 30), false);
            }
            msg += "\n┅┅\n";
            msg += Main.updateMsg + "\n\n";

            if(user.getQuestRedeems() < 1 && !user.getIsQuestComplete()) {
                EmbedBuilder embed = new EmbedBuilder();
                Card gift = new Card(
                    IEmotes.mascot,
                    "Gimme Cards",
                    "merch-1",
                    "Vibing Scatterbug",
                    "Merch",
                    "https://i.ibb.co/W3YbM7X/Vibing-Scatterbug.png",
                    "Pokémon",
                    new String[]{"Grass"},
                    81423
                );

                user.completeQuest();
                user.addSingleCard(gift, true);

                msg += "🎉 **QUEST COMPLETE** 🎉\n"
                + "Thank you for continuing to support *Gimme Cards*, and here's a plush of our new mascot, just for you!";

                embed.setDescription("🎒 " + msg);
                embed.setImage(gift.getCardImage());
                embed.setColor(user.getGameColor());
                event.replyEmbeds(embed.build()).queue();

            } else {
                if(!user.hasPremiumRole(event) && adChance == 0) {
                    Card adCard = Card.pickRandomSpecialCard();

                    msg += IEmotes.kofi + " Get the premium membership for exclusive cards, like this one!";

                    GameManager.sendPremiumMessage(event, user.getGameColor(), "🎒", msg, adCard);

                } else {
                    GameManager.sendMessage(event, user.getGameColor(), "🎒", msg);
                }
            }
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }

    public static void receiveDailyReward(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!User.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " 
            + User.findTimeLeft(user.getDailyEpoch(), 1440, true));

        } else {
            Card item = Card.pickRandomCard("shiny");
            String msg = "";
            String footer = ui.getUserName() + "'s shiny card";

            msg += "🎴 ";
            msg += GameManager.formatName(event) + " claimed their daily shiny card!";
            msg += user.updateCredits(GameManager.randRange(240, 300), true);

            user.resetDailyEpoch();

            user.addSingleCard(item, false);
            
            item.displayCard(event, ui, msg, footer, false);
            try { User.saveUsers(); } catch(Exception e) {}
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

            GameManager.sendMessage(event, user.getGameColor(), IEmotes.eevee, 
            "Set your game's theme color to **" + hexCode.getAsString().toUpperCase() + "**");
            try { User.saveUsers(); } catch(Exception e) {}

        } catch(NumberFormatException e) {
            e.printStackTrace();
            GameManager.sendMessage(event, IColors.red, "❌", "That's not a valid hex code!");
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

            GameManager.sendMessage(event, user.getGameColor(), "🎒", "**" + cardTitle + "** has been pinned to your backpack!");
            try { User.saveUsers(); } catch(Exception e) {}
            
        } catch(NumberFormatException | IndexOutOfBoundsException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }

    public static void viewCooldowns(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += GameManager.formatCmd("redeem") + " ┇ ";
        if(User.isCooldownDone(user.getRedeemEpoch(), 30, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + User.findTimeLeft(user.getRedeemEpoch(), 30, true) + "\n\n";
        }
        desc += GameManager.formatCmd("minigame") + " ┇ ";
        if(User.isCooldownDone(user.getMinigameEpoch(), 60, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + User.findTimeLeft(user.getMinigameEpoch(), 60, true) + "\n\n";
        }
        desc += GameManager.formatCmd("vote") + " ┇ ";
        if(User.isCooldownDone(user.getVoteEpoch(), 720, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + User.findTimeLeft(user.getVoteEpoch(), 720, true) + "\n\n";
        }
        desc += GameManager.formatCmd("daily") + " ┇ ";
        if(User.isCooldownDone(user.getDailyEpoch(), 1440, true)) {
            desc += "✅\n\n";
        } else {
            desc += "⏰ " + User.findTimeLeft(user.getDailyEpoch(), 1440, true) + "\n\n";
        }
        desc += GameManager.formatCmd("buy") + " ┇ ";
        if(User.isCooldownDone(user.getMarketEpoch(), 15, true)) {
            desc += "✅";
        } else {
            desc += "⏰ " + User.findTimeLeft(user.getMarketEpoch(), 15, true);
        }
        embed.setTitle(ui.getUserName() + "'s Cooldowns");
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getGameColor());
        GameManager.sendEmbed(event, embed);
    }
}
