package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.text.NumberFormat;
import java.util.Random;

public class UX implements Emotes {
    
    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String formatCmd(Server server, String cmd) {
        return "`/" + cmd + "`";
    }

    public static String formatNick(SlashCommandInteractionEvent event) {
        return event.getUser().getAsMention();
    }

    public static String formatNick(User mention, SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.User user = event.getJDA().getUserById(mention.getUserId()+"");

        if(user == null) { return ""; }

        return user.getAsMention();
    }

    public static String formatBadge(SlashCommandInteractionEvent event, String badgeEmote, String badgeName) {
        return UX.formatNick(event) + " has been awarded the " + badgeEmote + " **" + badgeName + "** badge!";
    }

    public static String formatBadge(User user, SlashCommandInteractionEvent event, String badgeEmote, String badgeName) {
        return UX.formatNick(user, event) + " has been awarded the " + badgeEmote + " **" + badgeName + "** badge!";
    }

    public static int randRange(int min, int max) {
        int diff = max - min;

        return new Random().nextInt(diff + 1) + min;
    }
    
    public static String findCardTitle(Data data, boolean isFav) {
        String cardTitle = "";

        if(data.getCardSubtypes() == null) {
            String supertype = data.getCardSupertype();

            if(supertype.equalsIgnoreCase("trainer")) {
                cardTitle += "🔧";
            } else if(supertype.equalsIgnoreCase("energy")) {
                cardTitle += "💡";
            }

        } else {
            for(int i = 0; i < data.getCardSubtypes().length; i++) {
                String cardType = data.getCardSubtypes()[i];
    
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
        } else if(cardRarity.equalsIgnoreCase("custom")) {
            rarityEmote = "✨ ";
        }else if(cardRarity.equalsIgnoreCase("merch")) {
            rarityEmote = "🛍️ ";
        } else {
            rarityEmote = "🌟 ";
        }
        return rarityEmote;
    }

    public static String formatXP(Data data, Boolean sellable) {
        String formattedXP = XP_ + " **" + formatNumber(data.getCardPrice()) + "**";

        if(sellable == null) {
            if(!Check.isSellable(data)) {
                return formattedXP + " 🚫";
            }

        } else if(!sellable) {
            return formattedXP + " 🚫";
        }
        return formattedXP;
    }

    public static String formatCredits(Data data) {
        String formattedCredits = credits_ + " **" + UX.formatNumber(data.getCardPrice()) + "**";

        if(!Check.isSellable(data)) {
            return formattedCredits + " 🚫";
        }
        return formattedCredits;
    }

    public static String formatCredits(int amount) {
        return credits_ + " **" + UX.formatNumber(amount) + "**";
    }

    public static int findEmbedColour(Data data) {
        int embedColour = 0;

        if(data.getCardSubtypes() == null) {
            String supertype = data.getCardSupertype();

            if(supertype.equalsIgnoreCase("trainer")) {
                embedColour = 0x768696;
            } else if(supertype.equalsIgnoreCase("energy")) {
                embedColour = 0xFED171;
            }

        } else {
            String cardType = data.getCardSubtypes()[0];

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
        }
        return embedColour;
    }
}
