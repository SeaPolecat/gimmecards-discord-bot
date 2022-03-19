package com.wixsite.seapolecat.Helpers;
import com.wixsite.seapolecat.Interfaces.*;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.Calendar;

public class State implements Emotes {

    public static boolean isOldSet(Data data) {        
        for(Data d : Data.oldSets) {
            if(d.getSetName().equalsIgnoreCase(data.getSetName())) {
                return true;
            }
        }
        return false;
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

    public static void checkLevelUp(GuildMessageReceivedEvent event, User user) {
        while(user.getXP() >= user.getMaxXP()) {
            int tokenReward = ((user.getLevel() + 9) / 10) * 5;
            int energyReward = (user.getLevel() + 1) * 100;
            int starReward = (user.getLevel() + 9) / 10;
            String msg = "";

            user.levelUp();

            msg += UX.formatNick(event) + " is now level **" + user.getLevel() + "**!";
            msg += UX.updateTokens(user, tokenReward);
            msg += UX.updateEnergy(user, energyReward);
            msg += UX.updateKeys(user, 1);
            msg += UX.updateStars(user, starReward);

            State.updateBackpackDisplay(event, user);
            Rest.sendMessage(event, msg);
        }
    }

    public static void updateBackpackDisplay(GuildMessageReceivedEvent event, User user) {
        BackpackDisplay disp = BackpackDisplay.findBackpackDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            Rest.editEmbed(event, user, null, disp, -1);
        }
    }

    public static void updateShopDisplay(GuildMessageReceivedEvent event, User user) {
        ShopDisplay disp = ShopDisplay.findShopDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            Rest.editEmbed(event, user, null, disp, disp.getPage());
        }
    }

    public static void updateOldShopDisplay(GuildMessageReceivedEvent event, User user) {
        OldShopDisplay disp = OldShopDisplay.findOldShopDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            Rest.editEmbed(event, user, null, disp, disp.getPage());
        }
    }

    public static void updateCardDisplay(GuildMessageReceivedEvent event, User user) {
        CardDisplay disp = CardDisplay.findCardDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            disp.setPage(1);

            if(user.getCards().size() < 1) {
                Rest.deleteMessage(event, disp);
                
            } else {
                Rest.editEmbed(event, user, null, disp, 1);
            }
        }
    }

    public static void updateInspectionDisplay(GuildMessageReceivedEvent event, User user) {
        InspectionDisplay disp = InspectionDisplay.findInspectionDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            disp.setPage(1);

            if(user.getCards().size() < 1) {
                Rest.deleteMessage(event, disp);
                
            } else {
                Rest.editEmbed(event, user, null, disp, 1);
            }
        }
    }

    public static void updateMinigameDisplay(GuildMessageReceivedEvent event, User user) {
        Server server = Server.findServer(event);
        MinigameDisplay disp = MinigameDisplay.findMinigameDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            Rest.editEmbed(event, user, server, disp, -1);
        }
    }

    public static void updateSearchDisplay(GuildMessageReceivedEvent event, SearchDisplay disp) {
        Rest.removeReaction(event, disp, "◀");
        Rest.removeReaction(event, disp, "▶");
    }
}
