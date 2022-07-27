package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class ShopDisplay extends Display {

    public static ArrayList<ShopDisplay> displays = new ArrayList<ShopDisplay>();

    public ShopDisplay(String ui) {
        super(ui);
    }

    public static ShopDisplay findShopDisplay(String authorId) {
        for(ShopDisplay s : displays) {
            if(s.getUserId().equals(authorId)) {
                return s;
            }
        }
        displays.add(0, new ShopDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int maxPage = Data.sets.length / 8;
        int startIndex = page * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(Data.sets.length % 8 != 0) {
            maxPage++;
        }
        desc += "`" + State.countOwnedPacks(user, false) + "/" + Data.sets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            Data set = Data.sets[i];

            desc += set.getSetEmote() + " " + set.getSetName() + " ┇ ";
            if(State.isPackUnlocked(user, set.getSetName())) {
                desc += "✅\n";
            } else {
                desc += "🔒\n";
            }
            if(i >= Data.sets.length - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(pikachu_ + " Poké Packs Shop " + pikachu_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0xF2E442);
        return embed;
    }
}
