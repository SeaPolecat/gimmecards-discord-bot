package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.ArrayList;

public class SearchCmds extends Cmds {

    public static void searchCards(MessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        SearchDisplay disp = SearchDisplay.findSearchDisplay(user.getUserId());
        String key = "";
        ArrayList<Data> searchedCards = new ArrayList<Data>();
        
        for(int i = 1; i < args.length; i++) {
            key += args[i] + " ";
        }
        key = key.trim();
        searchedCards = Card.searchCards(user, key);

        if(searchedCards.size() < 1) {
            Rest.sendMessage(event, jigglypuff_ + " Sorry, your search had no results!");

        } else {
            disp.setKey(key);
            disp.setSearchedCards(searchedCards);
            Rest.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }
        
    public static void viewFavCards(MessageReceivedEvent event) {
        User user = User.findUser(event);
        FavDisplay disp = FavDisplay.findFavDisplay(user.getUserId());

        if(user.getCards().size() < 1) {
            Rest.sendMessage(event, jigglypuff_ + " You don't have any cards yet!");

        } else if(FavDisplay.findFavCards(user).size() < 1) {
            Rest.sendMessage(event, jigglypuff_ + " You don't have any ❤ favourite cards yet!");

        } else {
            Rest.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }
}
