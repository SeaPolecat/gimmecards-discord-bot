package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HelpDisplay extends Display {

    public HelpDisplay(SlashCommandInteractionEvent event) {
        super(event);

        setPage(ChangelogConsts.CHANGELOG.length);
        setMaxPage(ChangelogConsts.CHANGELOG.length);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        int startIndex = getPage() - 1;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc = ChangelogConsts.CHANGELOG[startIndex].replace("[", "`/").replace("]", "`");
        
        embed.setTitle(EmoteConsts.EEVEE + " Le Changelog " + EmoteConsts.EEVEE);
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(ColorConsts.HELP_COLOR);
        return embed;
    }
}
