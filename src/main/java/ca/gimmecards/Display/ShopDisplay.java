package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopDisplay extends Display {

    public ShopDisplay(String ui) {
        super(ui);
    }

    @Override
    public ShopDisplay findDisplay() {
        String userId = getUserId();

        for(ShopDisplay s : IDisplays.shopDisplays) {
            if(s.getUserId().equals(userId)) {
                return s;
            }
        }
        IDisplays.shopDisplays.add(0, new ShopDisplay(userId));
        return IDisplays.shopDisplays.get(0);
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

            desc += IEmotes.token + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
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
        embed.setTitle(IEmotes.pikachu + " Poké Packs Shop " + IEmotes.pikachu);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(IColors.shopColor);
        return embed;
    }
}
