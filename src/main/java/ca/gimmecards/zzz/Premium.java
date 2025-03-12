/*package ca.gimmecards.zzz;

public class Premium {
    
    public static void viewPremium(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = buildPremiumEmbed();

        JDAUtils.sendEmbed(event, embed);
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
        + EmoteConsts.kofi + "\n\n";

        desc += "**3:** Keep playing to earn " 
        + EmoteConsts.star + " **Stars**, and use them to draw exclusive cards from `/rareshop` and `/promoshop`";

        embed.setTitle(EmoteConsts.kofi + " How to get Premium " + EmoteConsts.kofi);
        embed.setDescription(desc);
        embed.setImage("https://storage.ko-fi.com/cdn/useruploads/post/7b76ada0-2b12-40c9-be94-b337eba13e20_kofitier.png");
        embed.setColor(ColorConsts.kofiColor);
        embed.setFooter("For premium features to work, you need to stay in our community server");

        return embed;
    }
}*/
