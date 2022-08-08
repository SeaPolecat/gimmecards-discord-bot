package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.ArrayList;

public class SearchCmds extends Cmds {

    public static void searchCards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        SearchDisplay disp = new SearchDisplay(user.getUserId()).findDisplay();
        String key = "";
        ArrayList<Data> searchedCards = new ArrayList<Data>();
        
        for(int i = 1; i < args.length; i++) {
            key += args[i] + " ";
        }
        key = key.trim();
        searchedCards = Card.searchCards(user, key);

        if(searchedCards.size() < 1) {
            JDA.sendMessage(event, jigglypuff_ + " Sorry, your search had no results!");

        } else {
            disp.setKey(key);
            disp.setSearchedCards(searchedCards);
            JDA.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }
}
