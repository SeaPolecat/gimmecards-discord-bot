package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LeaderboardDisplay extends Display {
    
    public LeaderboardDisplay(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += ui.getUserPing() + " is `#";
        desc += findSelfRank(user) + "` in the world\n";
        desc += "┅┅\n";

        desc += findMiddle();

        desc += "┅┅\n";
        embed.setTitle(EmoteConsts.MASCOT + " World's Top Collectors " + EmoteConsts.MASCOT);
        embed.setColor(ColorConsts.BLUE);
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());

        return embed;
    }

    private int findSelfRank(User user) {
        int i = 0;

        while(i < User.usersRanked.size()) {
            User userRanked = User.usersRanked.get(i);

            if(userRanked.getUserId().equals(user.getUserId()))
                break;

            i++;
        }
        return i + 1;
    }

    private String findMiddle() {
        String middle = "";

        for(int i = 0; i < 25; i++) {
            User userRanked = User.usersRanked.get(i);

            if(i == 0)
                middle += "🥇";
            else if(i == 1)
                middle += "🥈";
            else if(i == 2)
                middle += "🥉";
            else
                middle += "`#" + (i+1) + "`";
                
            middle += " ┇ **" + /*Main.jda.getUserById(userRanked.getUserId()).getEffectiveName() +*/ "**"
            + " ┇ *" + "Lvl. " + userRanked.getLevel() + "*"
            + " ┇ " + EmoteConsts.XP + " `" + FormatUtils.formatNumber(userRanked.getXP()) 
            + " / " + FormatUtils.formatNumber(userRanked.getMaxXP()) + "`\n";
        }
        return middle;
    }
}
