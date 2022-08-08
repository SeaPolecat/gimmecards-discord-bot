package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopDisplay extends Display {

    public ShopDisplay(String ui) {
        super(ui);
    }

    @Override
    public ShopDisplay findDisplay() {
        String userId = getUserId();

        for(ShopDisplay s : shopDisplays) {
            if(s.getUserId().equals(userId)) {
                return s;
            }
        }
        shopDisplays.add(0, new ShopDisplay(userId));
        return shopDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        disp.setMaxPage(Data.sets.length / 8);

        if(Data.sets.length % 8 != 0) {
            disp.addMaxPage();
        }
        desc += "`" + Check.countOwnedPacks(user, false) + "/" + Data.sets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            Data set = Data.sets[i];

            desc += set.getSetEmote() + " " + set.getSetName() + " ┇ ";
            if(Check.isPackUnlocked(user, set.getSetName())) {
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
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(0xF2E442);
        return embed;
    }
}
