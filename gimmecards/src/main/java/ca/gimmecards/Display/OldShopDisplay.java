package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class OldShopDisplay extends Display {
    
    public static ArrayList<OldShopDisplay> displays = new ArrayList<OldShopDisplay>();

    public OldShopDisplay(String ui) {
        super(ui);
    }

    public static OldShopDisplay findOldShopDisplay(String authorId) {
        for(OldShopDisplay o : displays) {
            if(o.getUserId().equals(authorId)) {
                return o;
            }
        }
        displays.add(0, new OldShopDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int maxPage = Data.oldSets.length / 8;
        int startIndex = page * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(Data.oldSets.length % 8 != 0) {
            maxPage++;
        }
        desc += "`" + State.countOwnedPacks(user, true) + "/" + Data.oldSets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            Data set = Data.oldSets[i];

            desc += set.getSetEmote() + " " + set.getSetName() + " ┇ ";
            if(State.isPackUnlocked(user, set.getSetName())) {
                desc += "✅\n";
            } else {
                desc += "🔒\n";
            }
            if(i >= Data.oldSets.length - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(squirtle_ + " Legacy Packs Shop " + squirtle_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(0x7CBAC5);
        return embed;
    }
}
