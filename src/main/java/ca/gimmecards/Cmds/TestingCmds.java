package ca.gimmecards.cmds;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/*embed.addField("**Rule 1**", "Please follow Discord's [Terms of Service](https://discord.com/terms)", false);
embed.addField("**Rule 2**", "Be respectful to the staff and other server members at all times", false);
embed.addField("**Rule 3**", "NSFW or explicit content/behavior is prohibited, as this is a family-friendly server", false);
embed.addField("**Rule 4**", "Advertising of any kind is not allowed, either in this server or in others' DMs", false);
embed.addField("**Rule 5**", "Please do not spam the chat, unless you're using the bot. This includes unneccesary mentions/pings", false);
embed.addField("**Rule 6**", "Try to post in the correct channels; they are separated for a reason", false);*/

public class TestingCmds {

    public static void testSomething(MessageReceivedEvent event) {

        /*int count = 0;

        for(User u : User.users) {
            for(CardContainer cc : u.getCardContainers()) {
                Card newCard = null;

                for(CardSet cs : CardSet.sets) {
                    for(Card c : cs.getCommons()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                    for(Card c : cs.getUncommons()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                    for(Card c : cs.getRares()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                    for(Card c : cs.getShinies()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                }
                for(CardSet cs : CardSet.oldSets) {
                    for(Card c : cs.getCommons()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                    for(Card c : cs.getUncommons()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                    for(Card c : cs.getRares()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                    for(Card c : cs.getShinies()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                }
                for(CardSet cs : CardSet.rareSets) {
                    for(Card c : cs.getSpecials()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                }
                for(CardSet cs : CardSet.promoSets) {
                    for(Card c : cs.getSpecials()) {
                        if(cc.getCard().getCardId().equals(c.getCardId())) {
                            newCard = c;
                        }
                    }
                }

                try {
                    cc.getCard().setCardPrice(newCard.getCardPrice());
                    count++;
                } catch(NullPointerException e) {}
            }
        }
        GameManager.sendMessage(event, IColors.blue, "", "done testing! " + count + " cards updated");
        try { DataHandler.saveUsers(); } catch(Exception e) {}*/

        /*int dupes = 0;

        for(int i = 0; i < User.users.size(); i++) {
            User u = User.users.get(i);

            for(int k = 0; k < User.users.size(); k++) {
                User u2 = User.users.get(k);

                if(u.getUserId().equals(u2.getUserId()) && i != k) {
                    System.out.println("duplicate found!");
                    System.out.println(u.getCardCount());
                    dupes++;
                }
            }
        }
        System.out.println(dupes + " duplicates found!");*/

        /*for(User u : User.users) {
            try {
                net.dv8tion.jda.api.entities.User user = event.getJDA().getUserById(u.getUserId());

                // user not in the same server as GC
                if(user.getEffectiveName().contains("\u1D0B\u026A\u0274\u0262")) {
                    System.out.println(u.getUserId());
                }

            } catch(NullPointerException e) {}
        }*/

        // ====================================================================================================

        /*if (IDisplays.backpackDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.backpackDisplays.size() + "\n");
        }
        if (IDisplays.collectionDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.collectionDisplays.size() + "\n");
        }
        if (IDisplays.helpDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.helpDisplays.size() + "\n");
        }
        if (IDisplays.leaderboardDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.leaderboardDisplays.size() + "\n");
        }
        if (IDisplays.marketDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.marketDisplays.size() + "\n");
        }
        if (IDisplays.minigameDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.minigameDisplays.size() + "\n");
        }
        if (IDisplays.oldShopDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.oldShopDisplays.size() + "\n");
        }
        if (IDisplays.searchDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.searchDisplays.size() + "\n");
        }
        if (IDisplays.shopDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.shopDisplays.size() + "\n");
        }
        if (IDisplays.tradeDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.tradeDisplays.size() + "\n");
        }
        if (IDisplays.viewDisplays.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.viewDisplays.size() + "\n");
        }
        if (IDisplays.backpackDisplays_MP.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.backpackDisplays_MP.size() + "\n");
        }
        if (IDisplays.cardDisplays_MP.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.cardDisplays_MP.size() + "\n");
        }
        if (IDisplays.viewDisplays_MP.size() != 0) {
            GameManager.sendMessage(event, IColors.blue, "", "ERROR: size of backpackDisplays is " + IDisplays.viewDisplays_MP.size() + "\n");
        }*/

        /*for(User u : User.users) {
            u.addTokens(50);
            u.addStars(10);
            u.getBadges().add("original");

            Data merch = new Data(
                IEmotes.logo_,
                "Gimme Cards",
                "merch-0",
                "Pikachu Cosplay Charizard X",
                "Merch",
                "https://i.ibb.co/LdQwZjX/2023-Pikachu-Cosplay-Charizard-X.png",
                "Pokémon",
                new String[]{"Lightning", "Fire"},
                2023
            );
            Card.addSingleCard(u, merch, true);
        }
        GameObject.sendMessage(event, IColors.blue_, "", "done testing!");
        try { DataHandler.saveUsers(); } catch(Exception e) {}*/

        //User.users.clear();
        //try { DataHandler.saveUsers(); } catch(Exception e) {}


        // clearing half of users so i can actually see the storage file
        /*int OGsize = User.users.size() / 2;

        for(int i = 0; i < OGsize; i++) {
            User.users.remove(0);
        }*/
        
        for(int i = 0; i < User.users.size(); i++) {
            User u = User.users.get(i);
            User user = new User(
                u.getUserIdUnencrypted(),
                u.getGameColor(),
                u.getCardCount(),
                u.getLevel(),
                u.getXP(),
                u.getMaxXP(),
                u.getTokens(),
                u.getCredits(),
                u.getStars(),
                u.getKeys(),
                u.getOpenEpoch(),
                u.getVoteEpoch(),
                u.getDailyEpoch(),
                u.getRedeemEpoch(),
                u.getMinigameEpoch(),
                u.getMarketEpoch(),
                u.getSortMethod(),
                u.getIsSortIncreasing(),
                u.getPinnedCard(),
                u.getBadges(),
                u.getPacks(),
                u.getCardContainers(),
                new LinkedList<Display>()
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getDisplays());
        }

        Collections.sort(User.users);

        for(int i = 1; i < User.users.size(); i++) {
            User u = User.users.get(i);
            User prevU = User.users.get(i - 1);
            long diff = Long.parseLong(u.getUserId()) - Long.parseLong(prevU.getUserId());

            if(diff < 0)
                System.out.println("found negative!");
        }

        User.users.get(0).getCardContainers().clear();

        //try { DataHandler.saveUsers(); } catch(Exception e) {}
        JDAUtils.sendMessage(event, ColorConsts.blue, "", "done testing!");
    }

    // EVENT REDEEM CMD
    /*public static void redeemToken(SlashCommandInteractionEvent event) {
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
    }*/
}
