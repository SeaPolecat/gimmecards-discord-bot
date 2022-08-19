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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(Data.sets.length / 8);

        if(Data.sets.length % 8 != 0) {
            addMaxPage();
        }
        desc += "`" + Check.countOwnedPacks(user, false) + "/" + Data.sets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            Data set = Data.sets[i];

            desc += token_ + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
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
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(shop_);
        return embed;
    }
}
