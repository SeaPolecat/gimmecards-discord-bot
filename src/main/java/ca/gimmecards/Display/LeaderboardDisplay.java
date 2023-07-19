package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class LeaderboardDisplay extends Display {
    
    private String dispType;
    private ArrayList<User> players;
    private ArrayList<UserInfo> playerInfos;
    
    public LeaderboardDisplay(String ui) {
        super(ui);
        dispType = "";
        players = new ArrayList<User>();
        playerInfos = new ArrayList<UserInfo>();
    }

    public String getDispType() { return dispType; }
    public ArrayList<User> getPlayers() { return players; }
    public ArrayList<UserInfo> getPlayerInfos() { return playerInfos; }
    //
    public void setDispType(String dt) { dispType = dt; }
    public void setPlayers(ArrayList<User> p) { players = p; }
    public void setPlayerInfos(ArrayList<UserInfo> pi) { playerInfos = pi; }

    @Override
    public LeaderboardDisplay findDisplay() {
        String userId = getUserId();

        for(LeaderboardDisplay l : leaderboardDisplays) {
            if(l.getUserId().equals(userId)) {
                return l;
            }
        }
        leaderboardDisplays.add(0, new LeaderboardDisplay(userId));
        return leaderboardDisplays.get(0);
    }

    private int findSelfRank(String userId) {
        for(int i = 0; i < players.size(); i++) {
            User player = players.get(i);

            if(player.getUserId().equals(userId)) {
                return i+1;
            }
        }
        return -1;
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        int startIndex = page * 15 - 15;
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(players.size() / 15);

        if(players.size() % 15 != 0) {
            addMaxPage();
        }
        desc += ui.getUserPing() + " is `#";
        if(dispType.equalsIgnoreCase("ranks")) {
            desc += findSelfRank(user.getUserId()) + "` in this server\n";
        } else if(dispType.equalsIgnoreCase("leaderboard")) {
            desc += findSelfRank(user.getUserId()) + "` in the world\n";
        }
        desc += "┅┅\n";
        for(int i = startIndex; i < startIndex + 15; i++) {
            User player = players.get(i);
            UserInfo pi = playerInfos.get(i);

            if(i == 0) {
                desc += "🥇";
            } else if(i == 1) {
                desc += "🥈";
            } else if(i == 2) {
                desc += "🥉";
            } else {
                desc += "`#" + (i+1) + "`";
            }
            desc += " ┇ **" + pi.getUserName() + "**"
            + " ┇ *" + "Lvl. " + player.getLevel() + "*"
            + " ┇ " + XP_ + " `" + GameObject.formatNumber(player.getXP()) 
            + " / " + GameObject.formatNumber(player.getMaxXP()) + "`\n";

            if(i >= players.size() - 1) {
                break;
            }
        }
        desc += "┅┅\n";
        if(dispType.equalsIgnoreCase("ranks")) {
            embed.setTitle(trainer_ + " Top Collectors Here " + trainer_);
            embed.setColor(ranks_);
        } else if(dispType.equalsIgnoreCase("leaderboard")) {
            embed.setTitle(logo_ + " World's Top Collectors " + logo_);
            embed.setColor(blue_);
        }
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        return embed;
    }
}
