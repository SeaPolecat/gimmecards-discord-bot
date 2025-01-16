package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class OldShopDisplay extends Display {

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(CardSet.oldSets.length / 8);

        if(CardSet.oldSets.length % 8 != 0) {
            addMaxPage();
        }
        desc += "`" + user.countOwnedPacks(true) + "/" + CardSet.oldSets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            CardSet set = CardSet.oldSets[i];

            desc += EmoteConsts.token + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
            if(user.isPackUnlocked(set.getSetName())) {
                desc += "✅\n";
            } else {
                desc += "🔒\n";
            }
            if(i >= CardSet.oldSets.length - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        embed.setTitle(EmoteConsts.squirtle + " Legacy Packs Shop " + EmoteConsts.squirtle + " 🚫");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(ColorConsts.oldshopColor);
        return embed;
    }
}
