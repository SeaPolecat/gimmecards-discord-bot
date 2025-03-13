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

        /* USERS =======================================================================*/

        // clearing half of users so i can actually see the storage file
        /*int OGsize = User.users.size() / 2;

        for(int i = 0; i < OGsize; i++) {
            User.users.remove(0);
        }*/

        // sorting for binary search
        /*Collections.sort(User.users);

        for(int i = 1; i < User.users.size(); i++) {
            User u = User.users.get(i);
            User prevU = User.users.get(i - 1);
            long diff = Long.parseLong(u.getUserId()) - Long.parseLong(prevU.getUserId());

            if(diff < 0)
                System.out.println("found negative!");
        }

        try { DataUtils.saveUsers(); } catch(Exception e) {}*/

        /* SERVERS =======================================================================*/

        /*Collections.sort(Server.servers);

        for(int i = 1; i < Server.servers.size(); i++) {
            Server s = Server.servers.get(i);
            Server prevS = Server.servers.get(i - 1);
            long diff = Long.parseLong(s.getServerId()) - Long.parseLong(prevS.getServerId());

            if(diff < 0)
                System.out.println("found negative!");
        }

        try { DataUtils.saveServers(); } catch(Exception e) {}*/
        
        JDAUtils.sendMessage(event, ColorConsts.BLUE, "", "done testing!");
    }
}
