package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class FavDisplay extends Display {
    
    public static ArrayList<FavDisplay> displays = new ArrayList<FavDisplay>();

    public FavDisplay(String ui) {
        super(ui);
    }

    public static ArrayList<Card> findFavCards(User user) {
        ArrayList<Card> favCards = new ArrayList<Card>();

        for(Card c : user.getCards()) {
            if(c.getIsFav()) {
                favCards.add(c);
            }
        }
        return favCards;
    }

    public static FavDisplay findFavDisplay(String authorId) {
        for(FavDisplay f : displays) {
            if(f.getUserId().equals(authorId)) {
                return f;
            }
        }
        displays.add(0, new FavDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        ArrayList<Card> favCards = findFavCards(user);
        int startIndex = page * 15 - 15;
        int maxPage = favCards.size() / 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = user.getSortIncreasing() ? "increasing" : "decreasing";

        if(favCards.size() % 15 != 0) {
            maxPage++;
        }
        desc += "Sorted by `" + user.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card c = favCards.get(i);
            Data data = c.getData();
            int count = 0;

            for(int k = 0; k < user.getCards().size(); k++) {
                Data data2 = user.getCards().get(k).getData();

                if(data.getCardId().equalsIgnoreCase(data2.getCardId())) {
                    count = k + 1;
                }
            }
            desc += UX.findCardTitle(data, c.getIsFav())
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data)
            + " ┇ **x" + c.getCardQuantity() + "**\n";
            count++;
            if(i >= favCards.size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(redditto_ + " " + ui.getUserName() + "'s Favourite Cards " + redditto_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0xFB2932);
        return embed;
    }
}
