package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Interfaces.*;
import com.wixsite.seapolecat.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class HelpDisplay extends Display {
    
    public static ArrayList<HelpDisplay> displays = new ArrayList<HelpDisplay>();

    public HelpDisplay(String ui) {
        super(ui);
    }

    public static HelpDisplay findHelpDisplay(String authorId) {
        for(HelpDisplay c : displays) {
            if(c.getUserId().equals(authorId)) {
                return c;
            }
        }
        displays.add(0, new HelpDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int maxPage = Changelog.changelog.length;
        int startIndex = page - 1;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc = Changelog.changelog[startIndex].replace("[", "`" + server.getPrefix()).replace("]", "`");
        
        embed.setTitle(eevee_ + " Le Changelog " + eevee_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0xE9BB7A);
        return embed;
    }
}
