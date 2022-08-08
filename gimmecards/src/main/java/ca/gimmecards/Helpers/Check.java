package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.Calendar;

public class Check implements Emotes {
    
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

    public static boolean ownsFavCard(User user) {
        for(Card c : user.getCards()) {
            String cardImage = c.getData().getCardImage();

            if(user.getBackpackCard().equals(cardImage)) {
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

    public static boolean isCooldownDone(Long epoch, int cooldown, boolean isMins) {
        long current = isMins ? Calendar.getInstance().getTimeInMillis() / 60000 : Calendar.getInstance().getTimeInMillis() / 1000;

        if(current - epoch >= cooldown) {
            return true;
        }
        return false;
    }

    public static int countOwnedPacks(User user, boolean isOld) {
        int ownedPacks = 0;
        Data[] sets = isOld ? Data.oldSets : Data.sets;

        for(String name : user.getPacks()) {
            for(Data d : sets) {
                if(name.equalsIgnoreCase(d.getSetName())) {
                    ownedPacks++;
                }
            }
        }
        return ownedPacks;
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
            msg += UX.updateTokens(user, 2);
            msg += UX.updateEnergy(user, energyReward);
            msg += UX.updateKeys(user, 1);
            msg += UX.updateStars(user, 1);

            if(user.getLevel() == 50) {
                user.getBadges().add("veteran");
                JDA.sendMessage(event, UX.formatBadge(event, veteranBadge_, "Veteran Collector"));
            } else if(user.getLevel() == 100) {
                user.getBadges().add("master");
                JDA.sendMessage(event, UX.formatBadge(event, masterBadge_, "Master Collector"));
            }
            Update.updateBackpackDisplay(event, user);

            embed.setThumbnail(ui.getUserIcon());
            embed.setDescription(msg);
            embed.setColor(0x408CFF);
            JDA.sendEmbed(event, embed);
            embed.clear();
        }
    }
}
