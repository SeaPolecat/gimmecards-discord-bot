package ca.gimmecards.Display;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpDisplay extends Display {

    public HelpDisplay(String ui) {
        super(ui);
    }

    @Override
    public HelpDisplay findDisplay() {
        String userId = getUserId();

        for(HelpDisplay c : helpDisplays) {
            if(c.getUserId().equals(userId)) {
                return c;
            }
        }
        helpDisplays.add(0, new HelpDisplay(userId));
        return helpDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        int startIndex = page - 1;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        disp.setMaxPage(Changelog.changelog.length);
        desc = Changelog.changelog[startIndex].replace("[", "`" + server.getPrefix()).replace("]", "`");
        
        embed.setTitle(eevee_ + " Le Changelog " + eevee_);
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + disp.getMaxPage(), ui.getUserIcon());
        embed.setColor(0xE9BB7A);
        return embed;
    }
}
