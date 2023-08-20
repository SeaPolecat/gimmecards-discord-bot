package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.IColors;
import java.util.ArrayList;
import ca.gimmecards.Display.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import ca.gimmecards.Display_MP.BackpackDisplay_MP;
import ca.gimmecards.Display_MP.CardDisplay_MP;
import ca.gimmecards.Display_MP.ViewDisplay_MP;
import ca.gimmecards.OtherInterfaces.IDisplays;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/*embed.addField("**Rule 1**", "Please follow Discord's [Terms of Service](https://discord.com/terms)", false);
embed.addField("**Rule 2**", "Be respectful to the staff and other server members at all times", false);
embed.addField("**Rule 3**", "NSFW or explicit content/behavior is prohibited, as this is a family-friendly server", false);
embed.addField("**Rule 4**", "Advertising of any kind is not allowed, either in this server or in others' DMs", false);
embed.addField("**Rule 5**", "Please do not spam the chat, unless you're using the bot. This includes unneccesary mentions/pings", false);
embed.addField("**Rule 6**", "Try to post in the correct channels; they are separated for a reason", false);*/

public class TestingCmds {

    /*public User(String id, int gc, int cc, int l, int xp, int mxp, int t, int creds, int k, int s, long oe, long ve, long de, long re, long me, long marketE, String sm, boolean si, ArrayList<String> b, String pc, ArrayList<String> p, ArrayList<Card> c, boolean ir, boolean irr) {
        userId = id;
        gameColor = gc;
        cardCount = cc;
        level = l;
        XP = xp;
        maxXP = mxp;
        tokens = t;
        credits = creds;
        keys = k;
        stars = s;
        openEpoch = oe;
        voteEpoch = ve;
        dailyEpoch = de;
        redeemEpoch = re;
        minigameEpoch = me;
        marketEpoch = marketE;
        sortMethod = sm;
        sortIncreasing = si;
        badges = b;
        pinCard = pc;
        packs = p;
        cards = c;
        isRare = ir;
        isRadiantRare = irr;
    }*/

    public static void testSomething(MessageReceivedEvent event) {

        int count = 0;

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
        //try { User.saveUsers(); } catch(Exception e) {}

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

        if (IDisplays.backpackDisplays.size() != 0) {
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
        }


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
        try { User.saveUsers(); } catch(Exception e) {}*/
        
        /*for(int i = 0; i < User.users.size(); i++) {
            User u = User.users.get(i);
            User user = new User(
                u.getUserId(),
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
                10,
                false
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getQuestRedeems());
            System.out.println(u.getIsQuestComplete());
        }
        GameManager.sendMessage(event, IColors.blue, "", "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
    
}
