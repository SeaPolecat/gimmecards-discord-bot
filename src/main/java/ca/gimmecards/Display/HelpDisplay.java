package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpDisplay extends Display {

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(ChangelogConsts.changelog.length);
        desc = ChangelogConsts.changelog[startIndex].replace("[", "`/").replace("]", "`");
        
        embed.setTitle(EmoteConsts.eevee + " Le Changelog " + EmoteConsts.eevee);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(ColorConsts.helpColor);
        return embed;
    }
}
