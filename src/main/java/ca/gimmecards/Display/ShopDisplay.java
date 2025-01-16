package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopDisplay extends Display {

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

            desc += EmoteConsts.token + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
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
        embed.setTitle(EmoteConsts.pikachu + " Poké Packs Shop " + EmoteConsts.pikachu);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(ColorConsts.shopColor);
        return embed;
    }
}
