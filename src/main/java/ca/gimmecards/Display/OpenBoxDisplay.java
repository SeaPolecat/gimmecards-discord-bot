package ca.gimmecards.display;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

import ca.gimmecards.main.*;

public class OpenBoxDisplay extends Display {

    private ArrayList<ArrayList<Card>> newPacks;
    private String message;
    
    public OpenBoxDisplay() {
        super();
        newPacks = new ArrayList<ArrayList<Card>>();
        message = "";
    }

    public ArrayList<ArrayList<Card>> getNewPacks() { return this.newPacks; }
    public String getMessage() { return this.message; }
    //
    public void setNewPacks(ArrayList<ArrayList<Card>> newPacks) { this.newPacks = newPacks; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        setMaxPage(newPacks.size());

        desc += message;
        desc += "\n┅┅\n";
        for(Card newCard : this.newPacks.get(page - 1)) {
            desc += newCard.findCardTitle(false)
            + " ┇ " + newCard.findRarityEmote()
            + " ┇ " + newCard.getSetEmote()
            + " ┇ " + newCard.formatXP(newCard.isCardSellable()) + "\n";
        }
        desc += "┅┅\n";
        
        embed.setTitle(ui.getUserName() + "'s New Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + page + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(user.getGameColor());
        return embed;
    }
}
