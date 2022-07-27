package ca.gimmecards.Cmds;
import ca.gimmecards.Helpers.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TestingCmds extends Cmds {

    /*public User(String id, int cc, int l, int xp, int mxp, int t, int e, int k, int s, long oe, long ve, long de, long re, long me, String sm, boolean si, int bc, String bcard, ArrayList<String> p, ArrayList<Card> c) {
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
        badges = new ArrayList<String>();
        backpackColor = bc;
        backpackCard = bcard;
        packs = p;
        cards = c;
        //
        tasks = 0;
        giftClaimed = false;
    }*/

    public static void testSomething(MessageReceivedEvent event) {

        for(User u : User.users) {
            u.getBadges().clear();
        }

        for(User u : User.users) {
            if(u.getLevel() >= 50) {
                u.getBadges().add("veteran");
            }
        }
        for(User u : User.users) {
            if(u.getLevel() >= 100) {
                u.getBadges().add("master");
            }
        }
        for(User u : User.users) {
            if(u.getUserId().equals("454773340163538955")) {
                u.getBadges().add("dev");
            }
        }
        for(User u : User.users) {
            if(u.getUserId().equals("454773340163538955")
            || u.getUserId().equals("762805188737237003")
            || u.getUserId().equals("812361136933699615")
            || u.getUserId().equals("907415225945452594")
            || u.getUserId().equals("818314670284210196")) {
                u.getBadges().add("staff");
            }
        }
        for(User u : User.users) {
            if(u.getGiftClaimed()) {
                u.getBadges().add("bday");
            }
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}

        /*Data mew = null;
        Data gardevoir = null;
        //ecard1-19
        //g1-RC30
        for(Data set : Data.oldSets) {
            for(Data data : set.getShinies()) {
                if(data.getCardId().equals("ecard1-19")) {
                    System.out.println("mew found!");
                    mew = data;
                }
            }
        }
        for(Data set : Data.specSets) {
            for(Data data : set.getSpecs()) {
                if(data.getCardId().equals("g1-RC30")) {
                    System.out.println("gardevoir found!");
                    gardevoir = data;
                }
            }
        }

        for(User u : User.users) {
            //gallard
            if(u.getUserId().equals("765490431348834344")) {
                u.addTokens(50);
                System.out.println(u.getTokens());
            }
            //gawr gura
            if(u.getUserId().equals("835854670005272616")) {
                u.addTokens(50);
                System.out.println(u.getTokens());
            }
            //sora
            if(u.getUserId().equals("973492494413217802")) {
                u.getCards().add(0, new Card(mew, u.getCardCount() + 1, true));
                Rest.sendMessage(event, u.getCards().get(0).getData().getCardImage());
            }
            //jinny
            if(u.getUserId().equals("727304307464077413")) {
                u.getCards().add(0, new Card(gardevoir, u.getCardCount() + 1, true));
                Rest.sendMessage(event, u.getCards().get(0).getData().getCardImage());
            }
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/

        /*for(int i = 0; i < User.users.size(); i++) {
            User u = User.users.get(i);
            User user;
            try {
                user = new User(
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
                    u.getBackpackColor(),
                    u.getBackpackCard(),
                    u.getPacks(),
                    u.getCards(),
                    u.getTasks(),
                    u.getGiftClaimed()
                );
            } catch(NullPointerException e) {
                user = new User(
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
                    u.getBackpackColor(),
                    u.getBackpackCard(),
                    u.getPacks(),
                    u.getCards(),
                    0,
                    false
                );
            }
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getTasks());
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
}
