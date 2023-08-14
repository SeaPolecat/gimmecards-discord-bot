package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.EmbedBuilder;

public class PrivacyCmds {
    
    public static void deleteAccount(SlashCommandInteractionEvent event) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Account Status** ┇ ✅ Active\n\n";

        desc += "After confirming, your user info will disappear from *Gimme Cards*, along with any cards and items.\n\n";
        desc += "*We're sad to see you go... but all stories must come to an end.*";

        embed.setTitle(IEmotes.logo + " Are You Sure? " + IEmotes.logo);
        embed.setDescription(desc);
        embed.setImage("https://c.tenor.com/J6lraJXFl4IAAAAC/pokemon-pikachu.gif");
        embed.setFooter("the end of " + ui.getUserName() + "'s journey", ui.getUserIcon());
        embed.setColor(IColors.blue);

        event.replyEmbeds(embed.build())
        .addActionRow(
            Button.danger(GameManager.createButtonId(event, "deleteaccount_yes"), "Yes"),
            Button.secondary(GameManager.createButtonId(event, "deleteaccount_no"), "No")
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
                    Button.danger("temp", "Yes").asDisabled(),
                    Button.secondary("temp2", "No").asDisabled()
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

        embed.setTitle(IEmotes.logo + " Welcome Back " + IEmotes.logo);
        embed.setDescription(desc);
        embed.setImage("https://media.tenor.com/qObvHG4rT28AAAAC/pikachu-pokemon.gif");
        embed.setColor(IColors.blue);

        event.editMessageEmbeds(embed.build())
        .setActionRow(
            Button.danger("temp", "Yes").asDisabled(),
            Button.secondary("temp2", "No").asDisabled()
        ).queue();
    }
}
