package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class OldShopDisplay extends Display {

    public OldShopDisplay(SlashCommandInteractionEvent event) {
        super(event);

        int numFullPages = CardSet.oldSets.length / 8;
        int remainder = CardSet.oldSets.length % 8;

        setMaxPage(numFullPages + (remainder / remainder));
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        int startIndex = getPage() * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "`" + user.countOwnedPacks(true) + "/" + CardSet.oldSets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            CardSet set = CardSet.oldSets[i];

            desc += EmoteConsts.TOKEN + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
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
        embed.setTitle(EmoteConsts.SQUIRTLE + " Legacy Packs Shop " + EmoteConsts.SQUIRTLE + " 🚫");
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(ColorConsts.OLD_SHOP_COLOR);
        return embed;
    }
}
