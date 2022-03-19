package com.wixsite.seapolecat.Helpers;
import com.wixsite.seapolecat.Interfaces.*;
import com.wixsite.seapolecat.Main.*;
import com.google.gson.JsonArray;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.text.NumberFormat;
import java.util.Random;

public class UX implements Emotes {
    
    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatCmd(Server server, String cmd) {
        return "`" + server.getPrefix() + cmd + "`";
    }

    public static String formatNick(GuildMessageReceivedEvent event) {
        return "__" + event.getAuthor().getName() + "__";
    }

    public static String findUserIcon(GuildMessageReceivedEvent event) {
        String userIcon = event.getAuthor().getAvatarUrl();

        if(userIcon == null) {
            userIcon = event.getAuthor().getDefaultAvatarUrl();
        }
        return userIcon;
    }

    public static int randRange(int min, int max) {
        int diff = max - min;

        return new Random().nextInt(diff + 1) + min;
    }

    public static String updateXP(User user, int quantity) {
        String msg = "\n";

        user.addXP(quantity);
        msg += "+ " + UX.formatNumber(quantity);
        msg += " " + XP_ + " **XP**";

        return msg;
    }

    public static String updateTokens(User user, int quantity) {
        String msg = "\n";

        user.addTokens(quantity);
        if(quantity > 0) {
            msg += "+ " + UX.formatNumber(quantity);
        } else {
            msg += "- " + UX.formatNumber(Math.abs(quantity));
        }
        msg += " " + token_ + " **Token**";

        return msg;
    }

    public static String updateEnergy(User user, int quantity) {
        String msg = "\n";

        user.addEnergy(quantity);
        if(quantity > 0) {
            msg += "+ " + UX.formatNumber(quantity);
        } else {
            msg += "- " + UX.formatNumber(Math.abs(quantity));
        }
        msg += " " + energy_ + " **Energy**";

        return msg;
    }

    public static String updateKeys(User user, int quantity) {
        String msg = "\n";

        user.addKeys(quantity);
        if(quantity > 0) {
            msg += "+ " + UX.formatNumber(quantity);
        } else {
            msg += "- " + UX.formatNumber(Math.abs(quantity));
        }
        msg += " " + key_ + " **Key**";

        return msg;
    }

    public static String updateStars(User user, int quantity) {
        String msg = "\n";

        user.addStars(quantity);
        if(quantity > 0) {
            msg += "+ " + UX.formatNumber(quantity);
        } else {
            msg += "- " + UX.formatNumber(Math.abs(quantity));
        }
        msg += " " + star_ + " **Star**";

        return msg;
    }
    
    public static String findCardTitle(Data data, boolean isFav) {
        String cardTitle = "";

        try {
            JsonArray cardTypes = data.getCardSubtypes();

            for(int i = 0; i < cardTypes.size(); i++) {
                String cardType = cardTypes.get(i).getAsString();
    
                if(cardType.equalsIgnoreCase("water")) {
                    cardTitle += water_;
                } else if(cardType.equalsIgnoreCase("psychic")) {
                    cardTitle += psychic_;
                } else if(cardType.equalsIgnoreCase("metal")) {
                    cardTitle += metal_;
                } else if(cardType.equalsIgnoreCase("lightning")) {
                    cardTitle += lightning_;
                } else if(cardType.equalsIgnoreCase("grass")) {
                    cardTitle += grass_;
                } else if(cardType.equalsIgnoreCase("fire")) {
                    cardTitle += fire_;
                } else if(cardType.equalsIgnoreCase("fighting")) {
                    cardTitle += fighting_;
                } else if(cardType.equalsIgnoreCase("fairy")) {
                    cardTitle += fairy_;
                } else if(cardType.equalsIgnoreCase("dragon")) {
                    cardTitle += dragon_;
                } else if(cardType.equalsIgnoreCase("darkness")) {
                    cardTitle += darkness_;
                } else if(cardType.equalsIgnoreCase("colorless")) {
                    cardTitle += colorless_;
                }
            }
        } catch(NullPointerException e) {
            String supertype = data.getCardSupertype();

            if(supertype.equalsIgnoreCase("trainer")) {
                cardTitle += "🔧";
            } else if(supertype.equalsIgnoreCase("energy")) {
                cardTitle += "💡";
            }
        }
        cardTitle += " " + data.getCardName();
        if(isFav) {
            return cardTitle + " ❤";
        }
        return cardTitle;
    }

    public static String findRarityEmote(Data data) {
        String cardRarity = data.getCardRarity();
        String rarityEmote = "";

        if(cardRarity.equalsIgnoreCase("common")) {
            rarityEmote = "⚪ ";
        } else if(cardRarity.equalsIgnoreCase("uncommon")) {
            rarityEmote = "🔷 ";
        } else if(cardRarity.equalsIgnoreCase("rare")) {
            rarityEmote = "⭐ ";
        } else if(cardRarity.equalsIgnoreCase("promo")) {
            rarityEmote = "🎁 ";
        } else {
            rarityEmote = "🌟 ";
        }
        return rarityEmote;
    }

    public static String formatXPPrice(Data data) {
        String XPPrice = XP_ + " **" + UX.formatNumber(data.getCardPrice()) + "**";

        if(State.isOldSet(data)) {
            return XPPrice + " 🚫";
        }
        return XPPrice;
    }

    public static String formatEnergyPrice(Data data) {
        return energy_ + " **" + UX.formatNumber(data.getCardPrice()) + "**";
    }

    public static int findEmbedColour(Data data) {
        int embedColour = 0;

        try {
            JsonArray cardTypes = data.getCardSubtypes();
            String cardType = cardTypes.get(0).getAsString();

            if(cardType.equalsIgnoreCase("water")) {
                embedColour = 0x1C9EDD;
            } else if(cardType.equalsIgnoreCase("psychic")) {
                embedColour = 0x862E88;
            } else if(cardType.equalsIgnoreCase("metal")) {
                embedColour = 0x788088;
            } else if(cardType.equalsIgnoreCase("lightning")) {
                embedColour = 0xF9C425;
            } else if(cardType.equalsIgnoreCase("grass")) {
                embedColour = 0x207733;
            } else if(cardType.equalsIgnoreCase("fire")) {
                embedColour = 0xCC0920;
            } else if(cardType.equalsIgnoreCase("fighting")) {
                embedColour = 0x97241B;
            } else if(cardType.equalsIgnoreCase("fairy")) {
                embedColour = 0xB9408D;
            } else if(cardType.equalsIgnoreCase("dragon")) {
                embedColour = 0x967925;
            } else if(cardType.equalsIgnoreCase("darkness")) {
                embedColour = 0x0F2541;
            } else if(cardType.equalsIgnoreCase("colorless")) {
                embedColour = 0xE8EBEE;
            }
        } catch(NullPointerException e) {
            String supertype = data.getCardSupertype();

            if(supertype.equalsIgnoreCase("trainer")) {
                embedColour = 0x768696;
            } else if(supertype.equalsIgnoreCase("energy")) {
                embedColour = 0xFED171;
            }
        }
        return embedColour;
    }
}
