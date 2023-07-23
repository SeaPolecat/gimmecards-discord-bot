package ca.gimmecards.Cmds;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

        /*for(User u : User.users) {
            for(int i = 0; i < u.getCardContainers().size(); i++) {
                CardContainer cc = u.getCardContainers().get(i);
                CardContainer container = new CardContainer(
                    cc.getCard(),
                    cc.getCardNum(),
                    cc.getCardQuantity(),
                    cc.getIsSellable(),
                    cc.getIsFav()
                );
                u.getCardContainers().set(i, container);
            }
        }
        for(User u : User.users) {
            for(CardContainer cc : u.getCardContainers()) {
                System.out.println(cc.getCard().getCardName());
            }
        }
        GameObject.sendMessage(event, IColors.blue_, "", "done testing!");
        //try { User.saveUsers(); } catch(Exception e) {}
        
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
                u.getCardContainers()
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            for(CardContainer cc : u.getCardContainers()) {
                System.out.println(cc.getIsFav());
            }
        }
        GameObject.sendMessage(event, IColors.blue_, "", "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
}
