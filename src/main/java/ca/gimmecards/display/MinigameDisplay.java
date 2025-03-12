package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MinigameDisplay extends Display {

    private int tries;
    private boolean hasWon;
    private Card card;
    private boolean hasCompleted;

    public MinigameDisplay(SlashCommandInteractionEvent event) {
        super(event);
        tries = 3;
        hasCompleted = false;
        hasWon = false;
        card = Card.pickRandomCard();
    }

    public int getTries() { return tries; }
    public boolean getHasWon() { return hasWon; }
    public Card getCard() { return card; }
    public boolean getHasCompleted() { return hasCompleted; }
    //
    public void minusTries() { tries--; }

    public void resetGame() {
        tries = 3;
        hasCompleted = false;
        hasWon = false;
        card = Card.pickRandomCard();
    }

    public void endGame(boolean win) { 
        hasCompleted = true;

        if(win) {
            hasWon = true;
        }
    }

    public boolean isGuessCorrect(String guess) {
        String cardRarity = card.getCardRarity();

        minusTries();
        if(cardRarity.replaceAll("\\s+", "").equalsIgnoreCase(guess.replaceAll("\\s+", ""))) {
            return true;

        } else {
            return false;
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        String cardRarity = card.getCardRarity();
        String rarityEmote = card.findRarityEmote();
        String cardImage = card.getCardImage();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += FormatUtils.formatCmd("guess (rarity)") + " to play\n";
        desc += FormatUtils.formatCmd("rarities") + " for hints\n";
        desc += "┅┅\n";

        desc += "**Rarity** ┇ ";
        if(!hasWon && tries > 0) {
            desc += "???\n";
        } else {
            desc += rarityEmote + " " + cardRarity + "\n";
        }
        desc += "**Game Status** ┇ ";
        if(hasWon) {
            desc += "🏆 Won\n";
        } else if(!hasWon && tries < 1) {
            desc += "😭 Lost\n";
        } else {
            desc += "⏳ In Progress\n";
        }
        desc += "**Tries Left** ┇ " + tries + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(EmoteConsts.CLEFAIRY + " Guess My Rarity " + EmoteConsts.CLEFAIRY);
        embed.setDescription(desc);
        embed.setImage(cardImage);
        embed.setFooter(ui.getUserName() + "'s minigame", ui.getUserIcon());
        embed.setColor(ColorConsts.MINIGAME_COLOR);
        return embed;
    }
}
