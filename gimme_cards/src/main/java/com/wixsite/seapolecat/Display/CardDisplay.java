package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class CardDisplay extends Display {

    public static ArrayList<CardDisplay> displays = new ArrayList<CardDisplay>();

    public CardDisplay(String ui) {
        super(ui);
    }

    public static CardDisplay findCardDisplay(String authorId) {
        for(CardDisplay c : displays) {
            if(c.getUserId().equals(authorId)) {
                return c;
            }
        }
        displays.add(0, new CardDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        int maxPage = user.getCards().size() / 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = user.getSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        if(user.getCards().size() % 15 != 0) {
            maxPage++;
        }
        desc += "Sorted by `" + user.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card c = user.getCards().get(i);
            Data data = c.getData();

            desc += UX.findCardTitle(data, c.getIsFav())
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data)
            + " ┇ **x" + c.getCardQuantity() + "**\n";
            count++;
            if(i >= user.getCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(ditto_ + " " + ui.getUserName() + "'s Cards " + ditto_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0xA742D8);
        return embed;
    }
}
