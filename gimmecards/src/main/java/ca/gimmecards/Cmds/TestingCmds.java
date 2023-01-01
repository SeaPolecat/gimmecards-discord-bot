package ca.gimmecards.Cmds;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TestingCmds extends Cmds {

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

        /*for(int i = 0; i < Server.servers.size(); i++) {
            Server s = Server.servers.get(i);
            Server server = new Server(
                s.getServerId(),
                s.getMarket(),
                s.getMarketEpoch()
            );
            Server.servers.set(i, server);
        }
        for(Server s : Server.servers) {
            System.out.println(s.getServerId());
        }
        JDA.sendMessage(event, blue_, "", "done testing!");
        try { Server.saveServers(); } catch(Exception e) {}*/
        
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
                u.getKeys(),
                u.getStars(),
                u.getOpenEpoch(),
                u.getVoteEpoch(),
                u.getDailyEpoch(),
                u.getRedeemEpoch(),
                u.getMinigameEpoch(),
                u.getMarketEpoch(),
                u.getSortMethod(),
                u.getSortIncreasing(),
                u.getBadges(),
                u.getPinCard(),
                u.getPacks(),
                u.getCards(),
                u.getIsRare(),
                u.getIsRadiantRare()
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getCredits());
        }
        JDA.sendMessage(event, blue_, "", "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
}
