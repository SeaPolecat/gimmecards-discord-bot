package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class MinigameDisplay extends Display {
    
    public static ArrayList<MinigameDisplay> displays = new ArrayList<MinigameDisplay>();
    //
    private int tries;
    private boolean hasWon;
    private Data card;

    public MinigameDisplay(String ui, Data d) {
        super(ui);
        tries = 3;
        hasWon = false;
        card = d;
    }

    public int getTries() { return tries; }
    public boolean getHasWon() { return hasWon; }
    public Data getCard() { return card; }
    //
    public void minusTries() { tries--; }
    public void winGame() { hasWon = true; }

    public static MinigameDisplay findMinigameDisplay(String authorId) {
        MinigameDisplay disp = null;

        for(MinigameDisplay m : displays) {
            if(m.getUserId().equals(authorId)) {
                disp = m;
                break;
            }
        }
        return disp;
    }

    public static void addMinigameDisplay(User user) {
        removeMinigameDisplay(user);
        displays.add(new MinigameDisplay(user.getUserId(), Card.pickRandomCard()));
    }

    public static void removeMinigameDisplay(User user) {
        for(int i = 0; i < displays.size(); i++) {
            if(displays.get(i).getUserId().equals(user.getUserId())) {
                displays.remove(i);
                break;
            }
        }
    }

    public static boolean isGuessCorrect(MessageReceivedEvent event, User user, String guess) {
        MinigameDisplay disp = findMinigameDisplay(user.getUserId());
        String cardRarity = disp.getCard().getCardRarity();

        disp.minusTries();
        if(cardRarity.equalsIgnoreCase(guess)) {
            disp.winGame();
            State.updateMinigameDisplay(event, user);
            return true;

        } else {
            State.updateMinigameDisplay(event, user);
            return false;
        }
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        MinigameDisplay disp = findMinigameDisplay(user.getUserId());
        String cardRarity = disp.getCard().getCardRarity();
        String rarityEmote = UX.findRarityEmote(disp.getCard());
        String cardImage = disp.getCard().getCardImage();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += UX.formatCmd(server, "guess (rarity)") + " to play\n";
        desc += UX.formatCmd(server, "rarities") + " for hints\n\n";

        desc += "**Rarity** ┇ ";
        if(!disp.getHasWon() && disp.getTries() > 0) {
            desc += "???\n";
        } else {
            desc += rarityEmote + " " + cardRarity + "\n";
        }
        desc += "**Game Status** ┇ ";
        if(disp.getHasWon()) {
            desc += "🏆 Won\n";
        } else if(!disp.getHasWon() && disp.getTries() < 1) {
            desc += "😭 Lost\n";
        } else {
            desc += "⏳ In Progress\n";
        }
        desc += "**Tries Left** ┇ " + disp.getTries() + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(clefairy_ + " Guess My Rarity " + clefairy_);
        embed.setDescription(desc);
        embed.setImage(cardImage);
        embed.setFooter(ui.getUserName() + "'s minigame", ui.getUserIcon());
        embed.setColor(0xEF9EC2);
        return embed;
    }
}
