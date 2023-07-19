package ca.gimmecards.Cmds_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CardCmds_ extends Cmds {
    
    public static void viewCards_(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        CardDisplay_ disp = new CardDisplay_(user.getUserId()).findDisplay();
        //
        OptionMapping page = event.getOption("page");
        OptionMapping user_ = event.getOption("user");

        if(user_ == null) { return; }

        User mention = User.findOtherUser(event, user_.getAsUser().getId());

        if(mention.getCardContainers().size() < 1) {
            JDA.sendMessage(event, red_, "❌", "That user doesn't have any cards yet!");

        } else {
            disp.setUser(user);
            disp.setMention(mention);
            disp.setMentionInfo(new UserInfo(mention, event));

            if(page != null) {
                try {
                    JDA.sendDynamicEmbed(event, user, null, disp, page.getAsInt());

                } catch(NumberFormatException | IndexOutOfBoundsException e) {
                    JDA.sendMessage(event, red_, "❌", "Whoops, I couldn't find that page...");
                }
            } else {
                JDA.sendDynamicEmbed(event, user, null, disp, 1);
            }
        }
    }
}
