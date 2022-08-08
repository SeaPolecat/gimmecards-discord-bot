package ca.gimmecards.Display;
import ca.gimmecards.Helpers.UX;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class PrivacyDisplay extends Display {

    boolean isSure;

    public PrivacyDisplay(String ui) {
        super(ui);
        isSure = false;
    }

    public boolean getIsSure() { return isSure; }
    //
    public void setIsSure(boolean is) { isSure = is; }

    @Override
    public PrivacyDisplay findDisplay() {
        String userId = getUserId();

        for(PrivacyDisplay p : privacyDisplays) {
            if(p.getUserId().equals(userId)) {
                return p;
            }
        }
        privacyDisplays.add(0, new PrivacyDisplay(userId));
        return privacyDisplays.get(0);
    }

    public static void removePrivacyDisplay(User user) {
        for(int i = 0; i < privacyDisplays.size(); i++) {
            if(privacyDisplays.get(i).getUserId().equals(user.getUserId())) {
                privacyDisplays.remove(i);
                break;
            }
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += UX.formatCmd(server, "confirm") + " to confirm account deletion\n";
        desc += UX.formatCmd(server, "deny") + " to go back\n\n";

        desc += "**Account Status** ┇ ";
        if(!isSure) {
            desc += "✅ Still Active\n\n";
        } else {
            desc += "❌ Deleted\n\n";
        }
        desc += "After confirming, your user info will disappear from *Gimme Cards*, along with any cards and items.\n\n";
        desc += "*We're sad to see you go... but all stories must come to an end.*";

        embed.setTitle(logo_ + " We'll Miss You " + logo_);
        embed.setDescription(desc);
        if(!isSure) {
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
