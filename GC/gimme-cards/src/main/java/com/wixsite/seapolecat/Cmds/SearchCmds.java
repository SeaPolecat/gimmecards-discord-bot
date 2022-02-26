package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SearchCmds extends Cmds {

    public static void searchCards(GuildMessageReceivedEvent event, String[] args) {
        User user = User.findUser(event);
        SearchDisplay disp = SearchDisplay.findSearchDisplay(user.getUserId());

        try {
            String key = "";
            for(int i = 1; i < args.length; i++) {
                key += args[i] + " ";
            }
            key = key.trim();
            
            disp.setKey(key);
            Card.searchCards(user, key);

            Rest.sendDynamicEmbed(event, user, null, disp, 1);
            try { User.saveUsers(); } catch(Exception e) {}
        } catch(IndexOutOfBoundsException e) {
            State.updateSearchDisplay(event, disp);
            Rest.sendMessage(event, jigglypuff_ + " Sorry, your search had no results!");
        }
    }
}
