package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class MinigameDisplay extends Display {

    private int tries;
    private boolean hasWon;
    private Data data;

    public MinigameDisplay(String ui) {
        super(ui);
        tries = 3;
        hasWon = false;
        data = Card.pickRandomCard();
    }

    public int getTries() { return tries; }
    public boolean getHasWon() { return hasWon; }
    public Data getData() { return data; }
    //
    public void minusTries() { tries--; }
    public void winGame() { hasWon = true; }

    @Override
    public MinigameDisplay findDisplay() {
        String userId = getUserId();

        for(MinigameDisplay m : minigameDisplays) {
            if(m.getUserId().equals(userId)) {
                return m;
            }
        }
        minigameDisplays.add(0, new MinigameDisplay(userId));
        return minigameDisplays.get(0);
    }

    public static void removeMinigameDisplay(User user) {
        for(int i = 0; i < minigameDisplays.size(); i++) {
            if(minigameDisplays.get(i).getUserId().equals(user.getUserId())) {
                minigameDisplays.remove(i);
                break;
            }
        }
    }

    public static boolean isGuessCorrect(MessageReceivedEvent event, User user, String guess) {
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();
        String cardRarity = disp.getData().getCardRarity();

        disp.minusTries();
        if(cardRarity.replaceAll("\\s+", "").equalsIgnoreCase(guess.replaceAll("\\s+", ""))) {
            disp.winGame();
            Update.updateMinigameDisplay(event, user);
            return true;

        } else {
            Update.updateMinigameDisplay(event, user);
            return false;
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, Display disp, int page) {
        String cardRarity = ((MinigameDisplay) disp).getData().getCardRarity();
        String rarityEmote = UX.findRarityEmote(data);
        String cardImage = data.getCardImage();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += UX.formatCmd(server, "guess (rarity)") + " to play\n";
        desc += UX.formatCmd(server, "rarities") + " for hints\n\n";

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

        embed.setTitle(clefairy_ + " Guess My Rarity " + clefairy_);
        embed.setDescription(desc);
        embed.setImage(cardImage);
        embed.setFooter(ui.getUserName() + "'s minigame", ui.getUserIcon());
        embed.setColor(0xEF9EC2);
        return embed;
    }
}
