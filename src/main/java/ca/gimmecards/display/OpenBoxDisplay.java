package ca.gimmecards.display;
import ca.gimmecards.main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.ArrayList;

public class OpenBoxDisplay extends Display {

    private ArrayList<ArrayList<Card>> newPacks;
    private String message;
    
    public OpenBoxDisplay(SlashCommandInteractionEvent event) {
        super(event);
    }

    public ArrayList<ArrayList<Card>> getNewPacks() { return this.newPacks; }
    public String getMessage() { return this.message; }
    //
    public void setNewPacks(ArrayList<ArrayList<Card>> newPacks) { this.newPacks = newPacks; }
    public void setMessage(String message) { this.message = message; }

    private void getValidPage() {
        setMaxPage(newPacks.size());
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        getValidPage();

        desc += message;
        desc += "\n┅┅\n";
        for(Card newCard : this.newPacks.get(getPage() - 1)) {
            desc += newCard.findCardTitle(false)
            + " ┇ " + newCard.findRarityEmote()
            + " ┇ " + newCard.getSetEmote()
            + " ┇ " + newCard.formatXP(newCard.isCardSellable()) + "\n";
        }
        desc += "┅┅\n";
        
        embed.setTitle(ui.getUserName() + "'s New Cards");
        embed.setDescription(desc);
        embed.setFooter("Page " + getPage() + " of " + getMaxPage(), ui.getUserIcon());
        embed.setColor(user.getGameColor());
        return embed;
    }
}
