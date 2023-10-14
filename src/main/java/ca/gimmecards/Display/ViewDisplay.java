package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class ViewDisplay extends Display {

    public ViewDisplay(String ui) {
        super(ui);
    }

    @Override
    public ViewDisplay findDisplay() {
        String userId = getUserId();

        for(ViewDisplay v : IDisplays.viewDisplays) {
            if(v.getUserId().equals(userId)) {
                return v;
            }
        }
        IDisplays.viewDisplays.add(0, new ViewDisplay(userId));
        return IDisplays.viewDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        Card card = user.getCardContainers().get(page - 1).getCard();
        CardContainer cc = user.getCardContainers().get(page - 1);
        String cardTitle = card.findCardTitle(cc.getIsFav());
        Boolean isSellable = cc.getIsSellable();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(user.getCardContainers().size());

        desc += "**Rarity** ┇ " + card.findRarityEmote() + " " + card.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + card.getSetEmote() + " " + card.getSetName() + "\n";
        desc += "**XP Value** ┇ " + card.formatXP(isSellable) + "\n\n";
        desc += "*Click on image for zoomed view*";
        
        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(card.getCardImage());
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(card.findEmbedColour());
        return embed;
    }
}
