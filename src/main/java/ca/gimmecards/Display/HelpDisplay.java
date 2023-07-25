package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpDisplay extends Display {

    public HelpDisplay(String ui) {
        super(ui);
    }

    @Override
    public HelpDisplay findDisplay() {
        String userId = getUserId();

        for(HelpDisplay c : IDisplays.helpDisplays) {
            if(c.getUserId().equals(userId)) {
                return c;
            }
        }
        IDisplays.helpDisplays.add(0, new HelpDisplay(userId));
        return IDisplays.helpDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page - 1;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(IChangelog.changelog.length);
        desc = IChangelog.changelog[startIndex].replace("[", "`/").replace("]", "`");
        
        embed.setTitle(IEmotes.eevee + " Le Changelog " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(IColors.helpColor);
        return embed;
    }
}
