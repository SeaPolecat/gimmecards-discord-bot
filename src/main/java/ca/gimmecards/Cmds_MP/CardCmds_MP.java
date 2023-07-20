package ca.gimmecards.Cmds_MP;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_MP.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CardCmds_MP extends Cmds {
    
    public static void viewCards_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        CardDisplay_MP disp = new CardDisplay_MP(user.getUserId()).findDisplay();
        //
        OptionMapping page = event.getOption("page");
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        User mention = User.findOtherUser(event, user_.getAsUser().getId());

        if(mention.getCardContainers().size() < 1) {
            GameManager.sendMessage(event, IColors.red, "❌", "That user doesn't have any cards yet!");

        } else {
            disp.setUser(user);
            disp.setMention(mention);
            disp.setMentionInfo(new UserInfo(mention, event));

            if(page != null) {
                try {
                    GameManager.sendDynamicEmbed(event, user, null, disp, page.getAsInt());

                } catch(NumberFormatException | IndexOutOfBoundsException e) {
                    GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that page...");
                }
            } else {
                GameManager.sendDynamicEmbed(event, user, null, disp, 1);
            }
        }
    }
}
