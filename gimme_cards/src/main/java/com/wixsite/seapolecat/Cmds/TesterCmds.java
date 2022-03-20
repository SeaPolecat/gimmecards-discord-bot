package com.wixsite.seapolecat.Cmds;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class TesterCmds extends Cmds {

    /*embed.addField("Rule 1", "Please follow Discord's [Terms of Service](https://discord.com/terms)", false);
    embed.addField("Rule 2", "Be respectful to the staff and other server members at all times", false);
    embed.addField("Rule 3", "NSFW or explicit content/behavior is prohibited, as this is a family-friendly server", false);
    embed.addField("Rule 4", "Advertising of any kind is not allowed, either in this server or in others' DMs", false);
    embed.addField("Rule 5", "Please do not spam the chat, unless you're using the bot. This includes unneccesary mentions/pings", false);
    embed.addField("Rule 6", "Try to post in the correct channels; they are separated for a reason", false);*/

    /*public static void saitamaBday(GuildMessageReceivedEvent event) {
        User user = User.findUser(event);

        if(Cmds.bdayGift) {
            Rest.sendMessage(event, jigglypuff_ + " You've gotten your birthday gift already!");

        } else {
            JsonElement reward = null;
            EmbedBuilder embed = new EmbedBuilder();
            String desc = "";
    
            for(Data d : Data.oldSets) {
                for(JsonElement card : d.getShinies()) {
                    String cardId = card.getAsJsonObject().get("id").getAsString();
    
                    if(cardId.equalsIgnoreCase("ex15-100")) {
                        reward = card;
                        break;
                    }
                }
            }
            desc += "*A large shadow suddenly engulfs you, and as you look up, you realize there is something... something huge looming above...*\n\n";
            desc += "It's the legendary dark charizard! Happy birthday saitama, and thanks for being a legendary friend!\n\n";
            desc += "(Psst, check your backpack too haha)\n\n";
            desc += "-Sea\n";
    
            embed.setTitle("🥳 Happy Birthday Saitama 🥳");
            embed.setDescription(desc);
            embed.setImage("https://pbs.twimg.com/media/EH02mRdWsAEMVOc.jpg:large");
            embed.setColor(0x8c03fc);
    
            user.addTokens(50);
            Display.displayCard(event, user, Card.addSingleCard(user, reward), "Happy Birthday " + user.getUserNick() + "!!");
            Rest.sendEmbed(event, embed);
            try { User.saveUsers(); } catch(Exception e) {}
        }
    }*/

    /*public User (String id, int cc, int l, int xp, int mxp, int t, int e, int k, int s, long oe, long de, long re, long me, String sm, boolean si, int bc, String bcard, ArrayList<String> p, ArrayList<Card> c) {
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
        dailyEpoch = de;
        redeemEpoch = re;
        minigameEpoch = me;
        sortMethod = sm;
        sortIncreasing = si;
        backpackColor = bc;
        backpackCard = bcard;
        packs = p;
        cards = c;
    }*/

    /*public Server(String si, String p, ArrayList<Data> m, long me) {
        serverId = si;
        prefix = p;
        market = m;
        marketEpoch = me;
    }*/

    public static void testSomething(GuildMessageReceivedEvent event) {

        /*for(Data d : Data.oldSets) {
            if(d.getSetName().equalsIgnoreCase("Rising Rivals")) {

                for(Data data : d.getShinies()) {
                    if(data.getCardName().equalsIgnoreCase("Flying Pikachu")) {
                        
                        for(User u : User.users) {
                            Card.addSingleCard(u, data);
                        }
                    }
                }
            }
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/

        /*for(int i = 0; i < Server.servers.size(); i++) {
            Server s = Server.servers.get(i);
            
            Server server = new Server(
                s.getServerId(),
                s.getPrefix(),
                new ArrayList<Data>(),
                (long)(0)
            );
            Server.servers.set(i, server);
        }
        for(Server s : Server.servers) {
            System.out.println(s.getMarket().size());
        }
        Rest.sendMessage(event, "done testing!");*/
        //try { Server.saveServers(); } catch(Exception e) {}

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
                0,
                u.getOpenEpoch(),
                u.getDailyEpoch(),
                u.getRedeemEpoch(),
                u.getMinigameEpoch(),
                u.getSortMethod(),
                u.getSortIncreasing(),
                u.getBackpackColor(),
                u.getBackpackCard(),
                u.getPacks(),
                u.getCards()
            );
            User.users.set(i, user);
        }
        for(User u : User.users) {
            System.out.println(u.getStars());
        }
        Rest.sendMessage(event, "done testing!");
        try { User.saveUsers(); } catch(Exception e) {}*/
    }
}
