package ca.gimmecards.Cmds_MP;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_MP.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BackpackCmds_MP extends Cmds {
    
    public static void viewBackpack_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        BackpackDisplay_MP disp = new BackpackDisplay_MP(user.getUserId()).findDisplay();
        //
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        User mention = User.findOtherUser(event, user_.getAsUser().getId());
        String mentionIcon = user_.getAsUser().getAvatarUrl();

        if(mentionIcon == null) {
            mentionIcon = user_.getAsUser().getDefaultAvatarUrl();
        }
        disp.setUser(user);
        disp.setMention(mention);
        disp.setMentionInfo(new UserInfo(mention, event));

        GameManager.sendDynamicEmbed(event, user, null, disp, -1);
    }
}
