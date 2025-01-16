package ca.gimmecards.cmds_mp;
import ca.gimmecards.display_mp.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class BackpackCmds_MP {
    
    public static void viewBackpack_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        BackpackDisplay_MP disp = new BackpackDisplay_MP();
        //
        OptionMapping user_ = event.getOption("user");

        user.addDisplay(disp);

        if(user_ == null) { return; }

        User mention = User.findOtherUser(event, user_.getAsUser().getId());
        String mentionIcon = user_.getAsUser().getAvatarUrl();

        if(mentionIcon == null) {
            mentionIcon = user_.getAsUser().getDefaultAvatarUrl();
        }
        disp.setUser(user);
        disp.setMention(mention);
        disp.setMentionInfo(new UserInfo(mention, event));

        JDAUtils.sendDynamicEmbed(event, user, null, disp, -1);
    }
}
