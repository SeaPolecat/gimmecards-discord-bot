package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
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

        setMaxPage(CardSet.sets.length / 8);

        if(CardSet.sets.length % 8 != 0) {
            addMaxPage();
        }
        desc += "`" + user.countOwnedPacks(false) + "/" + CardSet.sets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            CardSet set = CardSet.sets[i];

            desc += token_ + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
            if(user.isPackUnlocked(set.getSetName())) {
                desc += "✅\n";
            } else {
                desc += "🔒\n";
            }
            if(i >= CardSet.sets.length - 1) {
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
