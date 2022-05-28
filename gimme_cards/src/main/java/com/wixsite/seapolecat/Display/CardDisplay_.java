package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class CardDisplay_ extends Display {
    
    public static ArrayList<CardDisplay_> displays = new ArrayList<CardDisplay_>();
    //
    private User user;
    private User mention;
    private String mentionName;

    public CardDisplay_(String ui) {
        super(ui);
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public String getMentionName() { return mentionName; }
    //
    public void setUser(User u) { user = u; }
    public void setMention(User m) { mention = m; }
    public void setMentionName(String mn) { mentionName = mn; }

    public static CardDisplay_ findCardDisplay_(String authorId) {
        for(CardDisplay_ c : displays) {
            if(c.getUserId().equals(authorId)) {
                return c;
            }
        }
        displays.add(0, new CardDisplay_(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        CardDisplay_ disp = findCardDisplay_(user.getUserId());
        User mention = disp.getMention();
        int startIndex = page * 15 - 15;
        int maxPage = mention.getCards().size() / 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        String sortOrder = mention.getSortIncreasing() ? "increasing" : "decreasing";
        int count = startIndex + 1;

        if(mention.getCards().size() % 15 != 0) {
            maxPage++;
        }
        desc += "Sorted by `" + mention.getSortMethod() + "` `" + sortOrder + "`\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            Card c = mention.getCards().get(i);
            Data data = c.getData();

            desc += UX.findCardTitle(data, c.getIsFav())
            + " ┇ `#" + count + "`"
            + " ┇ " + UX.findRarityEmote(data) 
            + " ┇ " + data.getSetEmote()
            + " ┇ " + UX.formatXPPrice(data, c.getSellable())
            + " ┇ **x" + c.getCardQuantity() + "**\n";
            count++;
            if(i >= mention.getCards().size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(greenditto_ + " " + ui.getUserName() + " ➤ " + disp.getMentionName() + "'s cards " + greenditto_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0x33A75E);
        return embed;
    }
}
