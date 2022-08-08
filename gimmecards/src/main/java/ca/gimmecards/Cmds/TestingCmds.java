package ca.gimmecards.Cmds;
import ca.gimmecards.Helpers.JDA;
import ca.gimmecards.Main.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TestingCmds extends Cmds {

    /*public User(String id, int cc, int l, int xp, int mxp, int t, int e, int k, int s, long oe, long ve, long de, long re, long me, String sm, boolean si, ArrayList<String> b, int bc, String bcard, ArrayList<String> p, ArrayList<Card> c) {
        userId = id;
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
        sortMethod = sm;
        sortIncreasing = si;
        badges = b;
        backpackColor = bc;
        backpackCard = bcard;
        packs = p;
        cards = c;
    }*/

    public static void testSomething(MessageReceivedEvent event) {

        for(User u : User.users) {
            if(u.getUserId().equals("631888244883062794")) {
                u.addTokens(1000000);
            }
        }
        JDA.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}

        /*for(Server s : Server.servers) {
            s.setServerId(Main.encryptor.encrypt(s.getServerId()));
            System.out.println(s.getServerId());
        }
        Rest.sendMessage(event, "done testing!");
        try { Server.saveServers(); } catch(Exception e) {}

        for(User u : User.users) {
            u.setUserId(Main.encryptor.encrypt(u.getUserId()));
            System.out.println(u.getUserId());
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/

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
                u.getBackpackColor(),
                u.getBackpackCard(),
                u.getPacks(),
                u.getCards()
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getBadges());
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
}
