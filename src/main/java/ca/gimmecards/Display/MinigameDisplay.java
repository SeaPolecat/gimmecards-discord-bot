package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class MinigameDisplay extends Display {

    private int tries;
    private boolean isOver;
    private boolean hasWon;
    private Card card;

    public MinigameDisplay(String ui) {
        super(ui);
        tries = 3;
        isOver = false;
        hasWon = false;
        card = Card.pickRandomCard();
    }

    public int getTries() { return tries; }
    public boolean getIsOver() { return isOver; }
    public boolean getHasWon() { return hasWon; }
    public Card getCard() { return card; }
    //
    public void minusTries() { tries--; }

    public void resetGame() {
        tries = 3;
        isOver = false;
        hasWon = false;
        card = Card.pickRandomCard();
    }

    public void endGame(boolean win) { 
        isOver = true;

        if(win) {
            hasWon = true;
        }
    }

    @Override
    public MinigameDisplay findDisplay() {
        String userId = getUserId();

        for(MinigameDisplay m : IDisplays.minigameDisplays) {
            if(m.getUserId().equals(userId)) {
                return m;
            }
        }
        IDisplays.minigameDisplays.add(0, new MinigameDisplay(userId));
        return IDisplays.minigameDisplays.get(0);
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
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        String cardRarity = card.getCardRarity();
        String rarityEmote = card.findRarityEmote();
        String cardImage = card.getCardImage();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += server.formatCmd("guess (rarity)") + " to play\n";
        desc += server.formatCmd("rarities") + " for hints\n\n";

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

        embed.setTitle(IEmotes.clefairy + " Guess My Rarity " + IEmotes.clefairy);
        embed.setDescription(desc);
        embed.setImage(cardImage);
        embed.setFooter(ui.getUserName() + "'s minigame", ui.getUserIcon());
        embed.setColor(IColors.minigameColor);
        return embed;
    }
}
