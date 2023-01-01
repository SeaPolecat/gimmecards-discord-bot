package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.EmbedBuilder;

public class PrivacyCmds extends Cmds {
    
    public static void deleteAccount(SlashCommandInteractionEvent event) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Account Status** ┇ ✅ Active\n\n";

        desc += "After confirming, your user info will disappear from *Gimme Cards*, along with any cards and items.\n\n";
        desc += "*We're sad to see you go... but all stories must come to an end.*";

        embed.setTitle(logo_ + " Are You Sure? " + logo_);
        embed.setDescription(desc);
        embed.setImage("https://c.tenor.com/J6lraJXFl4IAAAAC/pokemon-pikachu.gif");
        embed.setFooter("the end of " + ui.getUserName() + "'s journey", ui.getUserIcon());
        embed.setColor(blue_);

        event.replyEmbeds(embed.build())
        .addActionRow(
            Button.danger("deleteaccount_yes", "Yes"),
            Button.secondary("deleteaccount_no", "No")
        ).queue();
    }

    public static void confirmDeletion(ButtonInteractionEvent event) {
        User user = User.findUser(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Account Status** ┇ ❌ Deleted\n\n";

        desc += "`Your Gimme Cards account has been deleted. "
        + "To ensure that your user data does not get recorded again, "
        + "do not use any commands from this point onwards.`";

        embed.setTitle("We'll Miss You");
        embed.setDescription(desc);

        for(int i = 0; i < User.users.size(); i++) {
            User u = User.users.get(i);

            if(u.getUserId().equals(user.getUserId())) {
                User.users.remove(i);

                event.editMessageEmbeds(embed.build())
                .setActionRow(
                    Button.danger("deleteaccount_yes", "Yes").asDisabled(),
                    Button.secondary("deleteaccount_no", "No").asDisabled()
                ).queue();

                try { User.saveUsers(); } catch(Exception e) {}
                return;
            }
        }
    }

    public static void denyDeletion(ButtonInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Account Status** ┇ ✅ Active\n\n";

        desc += "We're glad you're not going away... *welcome back!*";

        embed.setTitle(logo_ + " Welcome Back " + logo_);
        embed.setDescription(desc);
        embed.setImage("https://media.tenor.com/qObvHG4rT28AAAAC/pikachu-pokemon.gif");
        embed.setColor(blue_);

        event.editMessageEmbeds(embed.build())
        .setActionRow(
            Button.danger("deleteaccount_yes", "Yes").asDisabled(),
            Button.secondary("deleteaccount_no", "No").asDisabled()
        ).queue();
    }
}
