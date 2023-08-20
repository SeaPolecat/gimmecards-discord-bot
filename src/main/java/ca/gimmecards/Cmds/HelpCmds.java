package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class HelpCmds {
    
    public static void viewHelp(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "*[Terms of Service & Privacy Policy](https://docs.google.com/document/d/1agd1vDsueTEL2khcavynOSptHKTLy0c2jymmX3xW5mc/edit?usp=sharing)*\n";
        desc += "┅┅\n";

        desc += "**How to Play**\n";
        desc += "[Click here](https://www.gimmecards.ca/) to visit our website, "
        + "where you'll find a quick tutorial and list of commands.\n\n";

        desc += "**Community Server**\n";
        desc += "[Click here](https://discord.gg/wmVvK2cyzM) to join our community server, "
        + "where you can ask questions, give ideas, or simply hang out with other collectors.\n\n";

        desc += "**Invite Me!**\n";
        desc += "[Click here](https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=0&scope=bot%20applications.commands) "
        //https://discord.com/api/oauth2/authorize?client_id=814025499381727232&permissions=354368&scope=bot
        + "to invite me to your own server!";

        embed.setTitle(IEmotes.eevee + " Getting Started " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
    }

    public static void viewRarities(SlashCommandInteractionEvent event) {
        String[] shinies = new String[]{"Amazing Rare","Classic Collection","LEGEND","Radiant Rare","Rare ACE","Rare BREAK","Rare Holo","Rare Holo EX","Rare Holo GX","Rare Holo LV.X","Rare Holo Star","Rare Holo V","Rare Holo VMAX","Rare Holo VSTAR","Rare Prime","Rare Prism Star","Rare Rainbow","Rare Secret","Rare Shining","Rare Shiny","Rare Shiny GX","Rare Ultra","V","VM"};
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Normal Cards**\n";
        desc += "┅┅\n";
        desc += "⚪ Common\n";
        desc += "🔷 Uncommon\n";
        desc += "⭐ Rare\n\n";

        desc += "**Shiny Cards**\n";
        desc += "┅┅\n";
        desc += "🎁 Promo\n";
        for(String rarity : shinies) {
            desc += "🌟 " + rarity + "\n";
        }
        desc += "\n**Special Cards**\n";
        desc += "┅┅\n";
        desc += "✨ Custom\n";
        desc += "🛍️ Merch\n";

        embed.setTitle(IEmotes.eevee + " All Rarities " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
    }

    public static void viewBadges(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += IEmotes.devBadge + " **Gimme Cards Developer**\n";
        desc += "Given to the developer of Gimme Cards, *SeaPolecat*\n\n";

        desc += IEmotes.staffBadge + " **Gimme Cards Staff**\n";
        desc += "Given to the staff members in our official [support server](https://discord.gg/urtHCGcE7y)\n\n";

        desc += IEmotes.veteranBadge + " **Veteran Collector**\n";
        desc += "Given to collectors who are level 50+\n\n";

        desc += IEmotes.masterBadge + " **Master Collector**\n";
        desc += "Given to collectors who are level 100+\n\n";

        desc += IEmotes.bdayBadge + " **1 Year Anniversary**\n";
        desc += "Given to collectors who participated in the Gimme Cards 1 Year Anniversary event\n\n";

        desc += IEmotes.originalBagde + " **Original Collector**\n";
        desc += "Given to collectors who created their account before Gimme Cards was verified (2023)";

        embed.setTitle(IEmotes.eevee + " All Badges " + IEmotes.eevee);
        embed.setDescription(desc);
        embed.setColor(IColors.helpColor);
        GameManager.sendEmbed(event, embed);
    }

    public static void viewChangelog(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        Server server = Server.findServer(event);
        HelpDisplay disp = new HelpDisplay(user.getUserId()).findDisplay();

        GameManager.sendDynamicEmbed(event, user, server, disp, IChangelog.changelog.length);
    }

    public static void viewPremium(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = buildPremiumEmbed();

        GameManager.sendEmbed(event, embed);
    }

    public static void viewPremium(ButtonInteractionEvent event) {
        EmbedBuilder embed = buildPremiumEmbed();
        Emoji kofiEmote = event.getJDA().getEmojiById("1140389615379959860");

        event.getHook().editOriginalEmbeds(embed.build())
        .setActionRow(
            Button.primary("temp", "How to get Premium").withEmoji(kofiEmote).asDisabled()
        ).queue();
    }

    private static EmbedBuilder buildPremiumEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**1:** [Click here](https://discord.gg/wmVvK2cyzM) to join the *Gimme Cards* community server\n\n";

        desc += "**2:** [Click here](https://ko-fi.com/gimmecards) to purchase the premium membership on Kofi "
        + IEmotes.kofi + "\n\n";

        desc += "**3:** Keep playing to earn " 
        + IEmotes.star + " **Stars**, and use them to draw exclusive cards from `/rareshop` and `/promoshop`";

        embed.setTitle(IEmotes.kofi + " How to get Premium " + IEmotes.kofi);
        embed.setDescription(desc);
        embed.setImage("https://storage.ko-fi.com/cdn/useruploads/post/7b76ada0-2b12-40c9-be94-b337eba13e20_kofitier.png");
        embed.setColor(IColors.kofiColor);
        embed.setFooter("For premium features to work, you need to stay in our community server");

        return embed;
    }
}
