package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ShopDisplay extends Display {

    public ShopDisplay(SlashCommandInteractionEvent event) {
        super(event);

        int numFullPages = CardSet.sets.length / 8;
        int remainder = CardSet.sets.length % 8;

        setMaxPage(numFullPages + (remainder / remainder));
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        int startIndex = getPage() * 8 - 8;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "`" + user.countOwnedPacks(false) + "/" + CardSet.sets.length + "` packs unlocked\n";
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 8; i++) {
            CardSet set = CardSet.sets[i];

            desc += EmoteConsts.TOKEN + " " + set.getSetEmote() + " " + set.getSetName() + " ┇ ";
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
        embed.setTitle(EmoteConsts.PIKACHU + " Poké Packs Shop " + EmoteConsts.PIKACHU);
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(ColorConsts.SHOP_COLOR);
        return embed;
    }
}
