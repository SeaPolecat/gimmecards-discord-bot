package ca.gimmecards.Cmds;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TestingCmds extends Cmds {

    /*public User(String id, int cc, int l, int xp, int mxp, int t, int e, int k, int s, long oe, long ve, long de, long re, long me, String sm, boolean si, ArrayList<String> b, ArrayList<String> p, ArrayList<Card> c) {
        userId = id;
        gameColor = 0;
        cardCount = cc;
        level = l;
        XP = xp;
        maxXP = mxp;
        tokens = t;
        energy = e;
        keys = k;
        stars = s;
        openEpoch = oe;
        voteEpoch = ve;
        dailyEpoch = de;
        redeemEpoch = re;
        minigameEpoch = me;
        marketEpoch = (long)(0);
        sortMethod = sm;
        sortIncreasing = si;
        badges = b;
        pinCard = "";
        packs = p;
        cards = c;
        isRare = false;
        isRadiantRare = false;
    }*/

    public static void testSomething(MessageReceivedEvent event) {

        /*for(int i = 0; i < User.users.size(); i++) {
            User u = User.users.get(i);
            User user = new User(
                u.getUserId(),
                u.getCardCount(),
                u.getLevel(),
                u.getXP(),
                u.getMaxXP(),
                u.getTokens(),
                u.getEnergy(),
                u.getKeys(),
                u.getStars(),
                u.getOpenEpoch(),
                u.getVoteEpoch(),
                u.getDailyEpoch(),
                u.getRedeemEpoch(),
                u.getMinigameEpoch(),
                u.getSortMethod(),
                u.getSortIncreasing(),
                u.getBadges(),
                u.getPacks(),
                u.getCards()
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getIsRare());
            System.out.println(u.getIsRadiantRare());
        }
        JDA.sendMessage(event, blue_, "", "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
}
