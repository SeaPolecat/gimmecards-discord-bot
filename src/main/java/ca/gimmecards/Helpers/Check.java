package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.Calendar;

public class Check implements Emotes {

    public static boolean isSellable(Data data) {
        if(data.getSetName().equalsIgnoreCase("gimme cards")) {
            return false;
            
        } else if(isOldSet(data) || isPromoSet(data)) {
            return false;
        }
        return true;
    }
    
    public static boolean isOldSet(Data data) {        
        for(Data d : Data.oldSets) {
            if(d.getSetName().equalsIgnoreCase(data.getSetName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRareSet(Data data) {        
        for(Data d : Data.rareSets) {
            if(d.getSetName().equalsIgnoreCase(data.getSetName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPromoSet(Data data) {        
        for(Data d : Data.promoSets) {
            if(d.getSetName().equalsIgnoreCase(data.getSetName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isShinyCard(Data data) {
        String cardRarity = data.getCardRarity();
        if(cardRarity.equalsIgnoreCase("common")
        || cardRarity.equalsIgnoreCase("uncommon")
        || cardRarity.equalsIgnoreCase("rare")) {
            return false;
        }
        return true;
    }

    public static boolean ownsCard(User user, Data data) {
        for(CardContainer card : user.getCardContainers()) {
            String cardId = card.getData().getCardId();

            if(cardId.equals(data.getCardId()) && card.getCardQuantity() > 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean ownsFavCard(User user) {
        for(CardContainer card : user.getCardContainers()) {
            String cardImage = card.getData().getCardImage();

            if(user.getPinnedCard().equals(cardImage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ownsShopPack(User user) {
        for(String pack : user.getPacks()) {
            for(Data data : Data.sets) {
                if(pack.equalsIgnoreCase(data.getSetName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean ownsBadge(User user, String badge) {
        for(String b : user.getBadges()) {
            if(b.equalsIgnoreCase(badge)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPackUnlocked(User user, String setName) {
        for(String pack : user.getPacks()) {
            if(pack.equalsIgnoreCase(setName)) {
                return true;
            }
        }
        return false;
    }

    public static int countOwnedPacks(User user, boolean isOld) {
        int count = 0;
        Data[] sets = isOld ? Data.oldSets : Data.sets;

        for(String name : user.getPacks()) {
            for(Data set : sets) {
                if(name.equalsIgnoreCase(set.getSetName())) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int countOwnedCards(User user) {
        int count = 0;

        for(CardContainer card : user.getCardContainers()) {
            count += card.getCardQuantity();
        }
        return count;
    }

    public static boolean isCooldownDone(Long epoch, int cooldown, boolean isMins) {
        long current = isMins ? Calendar.getInstance().getTimeInMillis() / 60000 : Calendar.getInstance().getTimeInMillis() / 1000;

        if(current - epoch >= cooldown) {
            return true;
        }
        return false;
    }

    public static String findTimeLeft(Long epoch, int cooldown, boolean isMins) {
        long current;

        if(isMins) {
            current = Calendar.getInstance().getTimeInMillis() / 60000;
            long minutes = cooldown - (current - epoch);

            if(minutes > 60) {
                int hours = (int)(minutes / 60);
    
                if(hours < 2) {
                    return "**" + hours + " hour " + (minutes % 60) + " minutes**";
                } else {
                    return "**" + hours + " hours " + (minutes % 60) + " minutes**";
                }
            } else {
                return "**" + minutes + " minutes**";
            }

        } else {
            current = Calendar.getInstance().getTimeInMillis() / 1000;
            long seconds = cooldown - (current - epoch);

            if(seconds > 60) {
                int minutes = (int)(seconds / 60);
    
                if(minutes < 2) {
                    return "**" + minutes + " minute " + (seconds % 60) + " seconds**";
                } else {
                    return "**" + minutes + " minutes " + (seconds % 60) + " seconds**";
                }
            } else {
                return "**" + seconds + " seconds**";
            }
        }
    }

    public static String checkLevelUp(SlashCommandInteractionEvent event, User user) {
        int prevLvl = user.getLevel();
        int tokenReward = 0;
        int creditsReward = 0;
        int keyReward = 0;
        int starReward = 0;
        String msg = "";

        if(user.getXP() >= user.getMaxXP()) {
            msg += "\n┅┅";
            msg += "\n" + UX.formatNick(event) + "** LEVELED UP :tada:**";

            while(user.getXP() >= user.getMaxXP()) {
                user.levelUp();
    
                tokenReward += 2;
                creditsReward += ((user.getLevel() + 9) / 10) * 100;
                keyReward++;
                starReward++;
    
                if(user.getLevel() == 50) {
                    user.getBadges().add("veteran");
                } else if(user.getLevel() == 100) {
                    user.getBadges().add("master");
                }
            }
            msg += "\nLevel **" + prevLvl + "** ➜ **" + user.getLevel() + "**";
            msg += user.updateTokens(tokenReward, true);
            msg += user.updateCredits(creditsReward, false);
            msg += user.updateKeys(keyReward, false);
            msg += user.updateStars(starReward, false);
        }
        return msg;
    }
}
