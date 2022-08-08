package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class OldShopDisplay extends Display {

    public OldShopDisplay(String ui) {
        super(ui);
    }

    @Override
    public OldShopDisplay findDisplay() {
        String userId = getUserId();

        for(OldShopDisplay o : oldShopDisplays) {
            if(o.getUserId().equals(userId)) {
                return o;
            }
        }
        oldShopDisplays.add(0, new OldShopDisplay(userId));
        return oldShopDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        disp.setMaxPage(Data.oldSets.length / 8);

        if(Data.oldSets.length % 8 != 0) {
            disp.addMaxPage();
        }
        desc += "`" + Check.countOwnedPacks(user, true) + "/" + Data.oldSets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            Data set = Data.oldSets[i];

            desc += set.getSetEmote() + " " + set.getSetName() + " ┇ ";
            if(Check.isPackUnlocked(user, set.getSetName())) {
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
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(0x7CBAC5);
        return embed;
    }
}
