package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.Collections;

public class LeaderboardDisplay extends Display {

    public static final int MAX_PAGE = 3;
    
    public LeaderboardDisplay(SlashCommandInteractionEvent event) {
        super(event);

        setMaxPage(MAX_PAGE);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        synchronized(UserRanked.usersRanked) {
            Collections.sort(UserRanked.usersRanked);

            desc += ui.getUserPing() + " is `#";
            desc += findSelfRank(user.getUserId()) + "` in the world\n";
            desc += "┅┅\n";
    
            desc += findMiddle();
    
            desc += "┅┅\n";
            embed.setTitle(EmoteConsts.MASCOT + " World's Top Collectors " + EmoteConsts.MASCOT);
            embed.setColor(ColorConsts.BLUE);
            embed.setDescription(desc);
            embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        }
        return embed;
    }

    private static int findSelfRank(String userId) {
        synchronized(UserRanked.usersRanked) {
            for(int i = 0; i < UserRanked.usersRanked.size(); i++) {
                UserRanked userRanked = UserRanked.usersRanked.get(i);
    
                if(userRanked.getUser().getUserId().equals(userId)) {
                    return i + 1;
                }
            }
            return -1;
        }
    }

    private String findMiddle() {
        String middle = "";
        int startIndex = getPage() * 15 - 15;

        for(int i = startIndex; i < startIndex + 15; i++) {
            UserRanked userRanked = UserRanked.usersRanked.get(i);

            if(i == 0)
                middle += "🥇";
            else if(i == 1)
                middle += "🥈";
            else if(i == 2)
                middle += "🥉";
            else
                middle += "`#" + (i+1) + "`";
                
            middle += " ┇ **" + userRanked.getUserName() + "**"
            + " ┇ *" + "Lvl. " + userRanked.getUser().getLevel() + "*"
            + " ┇ " + EmoteConsts.XP + " `" + FormatUtils.formatNumber(userRanked.getUser().getXP()) 
            + " / " + FormatUtils.formatNumber(userRanked.getUser().getMaxXP()) + "`\n";
        }
        return middle;
    }
}
