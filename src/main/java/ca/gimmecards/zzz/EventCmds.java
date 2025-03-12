/*package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EventCmds {
    
    public static void seeEvent(MessageReceivedEvent event) {
        User user = User.findUser(event);
        EventDisplay disp = EventDisplay.findEventDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void checkTasksComplete(MessageReceivedEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getGiftClaimed() && user.getTasks() >= 25) {
            Card c;
            Data item = null, gift = null;
            String msg = "";
            String footer = event.getAuthor().getName() + "'s event gift";

            for(Data d : Data.rareSets[8].getSpecs()) {
                if(d.getCardId().equals("cel25c-24_A")) {
                    item = d;
                    break;
                }
            }
            gift = new Data(
                item.getSetEmote(),
                item.getSetName(),
                item.getCardId(),
                ui.getUserName() + "'s Pikachu",
                item.getCardRarity(),
                item.getCardImage(),
                item.getCardSupertype(),
                item.getCardSubtypes(),
                item.getCardPrice()
            );
            user.setGiftClaimed(true);
            user.getBadges().add("bday");
            c = Card.addSingleCard(user, gift, true);

            msg += GameObject.formatNick(event) + " received their event gift!\n";
            msg += "+ " + bdayBadge_ + " **1 Year Anniversary Badge**";

            State.updateBackpackDisplay(event, user);
            State.updateCardDisplay(event, user);
            State.updateEventDisplay(event, user);

            Rest.sendMessage(event, msg);
            Display.displayCard(event, user, gift, c, footer);
        }
    }

    // EVENT REDEEM CMD
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
                event.getHook().editOriginalEmbeds(embed.build()).queue();

            } else {
                if(!user.hasPremiumRole(event) && adChance == 0) {
                    Card adCard = Card.pickRandomSpecialCard();

                    msg += IEmotes.kofi + " Get the premium membership for exclusive cards, like this one!";

                    GameManager.sendPremiumMessage(event, user.getGameColor(), "🎒", msg, adCard);

                } else {
                    GameManager.sendMessage(event, user.getGameColor(), "🎒", msg);
                }
            }
            try { DataHandler.saveUsers(); } catch(Exception e) {}
        }
    }
}*/
