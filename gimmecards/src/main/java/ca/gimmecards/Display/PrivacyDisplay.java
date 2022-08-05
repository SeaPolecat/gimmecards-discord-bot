package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class PrivacyDisplay extends Display {
    
    public static ArrayList<PrivacyDisplay> displays = new ArrayList<PrivacyDisplay>();
    //
    boolean isSure;

    public PrivacyDisplay(String ui) {
        super(ui);
        isSure = false;
    }

    public boolean getIsSure() { return isSure; }
    //
    public void setIsSure(boolean is) { isSure = is; }

    public static PrivacyDisplay findPrivacyDisplay(String authorId) {
        for(PrivacyDisplay p : displays) {
            if(p.getUserId().equals(authorId)) {
                return p;
            }
        }
        return null;
    }

    public static void addPrivacyDisplay(User user) {
        removePrivacyDisplay(user);
        displays.add(new PrivacyDisplay(user.getUserId()));
    }

    public static void removePrivacyDisplay(User user) {
        for(int i = 0; i < displays.size(); i++) {
            if(displays.get(i).getUserId().equals(user.getUserId())) {
                displays.remove(i);
                break;
            }
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        PrivacyDisplay disp = findPrivacyDisplay(user.getUserId());
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "`?confirm` to confirm account deletion\n";
        desc += "`?deny` to go back\n\n";

        desc += "**Account Status** ┇ ";
        if(!disp.getIsSure()) {
            desc += "✅ Still Active\n\n";
        } else {
            desc += "❌ Deleted\n\n";
        }
        desc += "After confirming, your user info will disappear from *Gimme Cards*, along with any cards and items.\n\n";
        desc += "*We're sad to see you go... but all stories must come to an end.*";

        embed.setTitle(logo_ + " We'll Miss You " + logo_);
        embed.setDescription(desc);
        if(!disp.getIsSure()) {
            embed.setTitle(logo_ + " We'll Miss You " + logo_);
            embed.setImage("https://c.tenor.com/J6lraJXFl4IAAAAC/pokemon-pikachu.gif");
            embed.setFooter("the end of " + ui.getUserName() + "'s journey", ui.getUserIcon());
            embed.setColor(0x408CFF);
        } else {
            embed.setTitle("We'll Miss You");
        }
        return embed;
    }
}
