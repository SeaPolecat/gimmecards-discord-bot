package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.Collections;

public class LeaderboardDisplay extends Display {

    private static final int NUM_ON_TOP = 25;
    
    public LeaderboardDisplay(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        EmbedBuilder embed = new EmbedBuilder();

        synchronized(User.usersRanked) {
            Collections.sort(User.usersRanked, User.BY_LEVEL_DESCENDING);
        }

        embed.setTitle(EmoteConsts.MASCOT + " World's Top Collectors " + EmoteConsts.MASCOT);
        embed.setColor(ColorConsts.BLUE);
        embed.setDescription(findDescription(user, ui));
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());

        return embed;
    }

    private String findDescription(User user, UserInfo ui) {
        String desc = "";
        int rankNum = 1, loopEnd = NUM_ON_TOP;
        boolean foundSelfRankWithinTop = false;
        int selfRank = 0;

        for(int i = 0; i < loopEnd; i++) {
            try {
                User userRanked = User.usersRanked.get(i);
                String userName = Main.jda.getUserById(userRanked.getUserId()).getEffectiveName();

                if(i == 0)
                    desc += "🥇";
                else if(i == 1)
                    desc += "🥈";
                else if(i == 2)
                    desc += "🥉";
                else
                    desc += "`#" + rankNum + "`";
                    
                desc += " ┇ **" + userName + "**"
                + " ┇ *" + "Lvl. " + userRanked.getLevel() + "*"
                + " ┇ " + EmoteConsts.XP + " `" + FormatUtils.formatNumber(userRanked.getXP()) 
                + " / " + FormatUtils.formatNumber(userRanked.getMaxXP()) + "`\n";

                if(userRanked.getUserId().equals(user.getUserId())) {
                    foundSelfRankWithinTop = true;
                    selfRank = rankNum;
                }
                rankNum++;

            } catch(NullPointerException e) {
                loopEnd++;
            }
        }
        
        if(!foundSelfRankWithinTop)
            selfRank = findSelfRankBelowTop(user);

        desc = "┅┅\n" + desc;
        desc = selfRank + "` in the world\n" + desc;
        desc = ui.getUserPing() + " is `#" + desc;

        desc += "┅┅\n";

        return desc;
    }

    private int findSelfRankBelowTop(User user) {
        int i = 0;

        while(i < User.usersRanked.size()) {
            User userRanked = User.usersRanked.get(i);

            if(userRanked.getUserId().equals(user.getUserId()))
                break;

            i++;
        }
        return i + 1;
    }
}
