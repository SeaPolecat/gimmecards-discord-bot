package ca.gimmecards.cmds_mp;
import ca.gimmecards.consts.*;
import ca.gimmecards.display_mp.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CollectionCmds_MP {
    
    public static void viewCards_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        CollectionDisplay_MP disp = new CollectionDisplay_MP();
        //
        OptionMapping page = event.getOption("page");
        OptionMapping user_ = event.getOption("user");

        user.addDisplay(disp);

        if(user_ == null) { return; }

        User mention = User.findOtherUser(event, user_.getAsUser().getId());

        if(mention.getCardContainers().size() < 1) {
            JDAUtils.sendMessage(event, ColorConsts.red, "❌", "That user doesn't have any cards yet!");

        } else {
            disp.setUser(user);
            disp.setMention(mention);
            disp.setMentionInfo(new UserInfo(mention, event));

            if(page != null) {
                try {
                    JDAUtils.sendDynamicEmbed(event, user, null, disp, page.getAsInt());

                } catch(NumberFormatException | ArithmeticException | IndexOutOfBoundsException e) {
                    JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that page...");
                }
            } else {
                JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
            }
        }
    }
}
