package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class OldShopDisplay extends Display {

    public OldShopDisplay(String ui) {
        super(ui);
    }

    @Override
    public OldShopDisplay findDisplay() {
        String userId = getUserId();

        for(OldShopDisplay o : IDisplays.oldShopDisplays) {
            if(o.getUserId().equals(userId)) {
                return o;
            }
        }
        IDisplays.oldShopDisplays.add(0, new OldShopDisplay(userId));
        return IDisplays.oldShopDisplays.get(0);
    }

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

            desc += IEmotes.token + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
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
        embed.setTitle(IEmotes.squirtle + " Legacy Packs Shop " + IEmotes.squirtle + " 🚫");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(IColors.oldshopColor);
        return embed;
    }
}
