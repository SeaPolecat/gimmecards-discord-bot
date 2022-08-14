package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
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
        for(Card card : user.getCards()) {
            String cardId = card.getData().getCardId();

            if(cardId.equals(data.getCardId()) && card.getCardQuantity() > 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean ownsFavCard(User user) {
        for(Card card : user.getCards()) {
            String cardImage = card.getData().getCardImage();

            if(user.getPinCard().equals(cardImage)) {
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

        for(Card card : user.getCards()) {
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

    public static void checkLevelUp(MessageReceivedEvent event, User user) {
        UserInfo ui = new UserInfo(event);

        while(user.getXP() >= user.getMaxXP()) {
            int energyReward = ((user.getLevel() + 9) / 10) * 100;
            EmbedBuilder embed = new EmbedBuilder();
            String msg = "";

            user.levelUp();

            msg += UX.formatNick(event) + " is now level **" + user.getLevel() + "** :tada:";
            msg += user.updateTokens(2, true);
            msg += user.updateEnergy(energyReward, false);
            msg += user.updateKeys(1, false);
            msg += user.updateStars(1, false);

            if(user.getLevel() == 50) {
                user.getBadges().add("veteran");
                JDA.sendMessage(event, user.getGameColor(), "🎒", UX.formatBadge(event, veteranBadge_, "Veteran Collector"));
            } else if(user.getLevel() == 100) {
                user.getBadges().add("master");
                JDA.sendMessage(event, user.getGameColor(), "🎒", UX.formatBadge(event, masterBadge_, "Master Collector"));
            }
            Update.updateBackpackDisplay(event, user);

            embed.setThumbnail(ui.getUserIcon());
            embed.setDescription(msg);
            embed.setColor(user.getGameColor());
            JDA.sendEmbed(event, embed);
            embed.clear();
        }
    }
}
