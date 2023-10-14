package ca.gimmecards.Main;
import ca.gimmecards.MainInterfaces.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.Random;

public class Card implements ICard {

    //==========================================[ INSTANCE VARIABLES ]===================================================================

    private String setEmote;            // the Discord emote of the card set this card belongs to
    private String setName;             // the name of the card set this card belongs to
    private String cardId;              // the Pokemon TCG Online ID of this card
    private String cardName;            // the name of this card
    private String cardRarity;          // the rarity of this card
    private String cardImage;           // this card's image link
    private String cardSupertype;       // the supertype of this card (Pokemon, Trainer, or Energy)
    private String[] cardSubtypes;      // the subtype of this card (fire, water, lightning, etc.)
    private Integer cardPrice;          // the in-game XP value of this card

    //=============================================[ CONSTRUCTORS ]====================================================================

    /**
     * creates a new Card
     * @param setEmote the Discord emote of the card set this card belongs to
     * @param cardPrice the in-game XP value of this card
     * @param j the JsonElement crawled from the Pokemon TCG API
     */
    public Card(String setEmote, Integer cardPrice, JsonElement j) {
        this.setEmote = setEmote;
        this.setName = j.getAsJsonObject().get("set").getAsJsonObject().get("name").getAsString().replace("—", " ");
        this.cardId = j.getAsJsonObject().get("id").getAsString();
        this.cardName = j.getAsJsonObject().get("name").getAsString();
        this.cardRarity = j.getAsJsonObject().get("rarity").getAsString();
        this.cardImage = j.getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString();
        this.cardSupertype = j.getAsJsonObject().get("supertype").getAsString();
        this.cardSubtypes = findCardSubtypes(j);
        this.cardPrice = cardPrice;
    }

    /**
     * creates a new custom Card (cards created by the community); params are literally all the instance variables
     * @param setEmote
     * @param setName
     * @param cardId
     * @param cardName
     * @param cardRarity
     * @param cardImage
     * @param cardSupertype
     * @param cardSubtypes
     * @param cardPrice
     */
    public Card(String setEmote, String setName, String cardId, String cardName, String cardRarity, String cardImage,
            String cardSupertype, String[] cardSubtypes, Integer cardPrice) {
        this.setEmote = setEmote;
        this.setName = setName;
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardRarity = cardRarity;
        this.cardImage = cardImage;
        this.cardSupertype = cardSupertype;
        this.cardSubtypes = cardSubtypes;
        this.cardPrice = cardPrice;
    }

    //===============================================[ GETTERS ] ======================================================================

    public String getSetEmote() { return this.setEmote; }
    public String getSetName() { return this.setName; }
    public String getCardId() { return this.cardId; }
    public String getCardName() { return this.cardName; }
    public String getCardRarity() { return this.cardRarity; }
    public String getCardImage() { return this.cardImage; }
    public String getCardSupertype() { return this.cardSupertype; }
    public String[] getCardSubtypes() { return this.cardSubtypes; }
    public int getCardPrice() { return this.cardPrice; }

    //=============================================[ PUBLIC STATIC FUNCTIONS ]==============================================================

    /**
     * picks a random card from an ArrayList of cards
     * @param cards the ArrayList of cards
     * @return a random card
     */
    public static Card pickCard(ArrayList<Card> cards) {
        Random rand = new Random();

        return cards.get(rand.nextInt(cards.size()));
    }

    /**
     * picks a random card from an array of cards
     * @param cards the array of cards
     * @return a random card
     */
    public static Card pickCard(Card[] cards) {
        Random rand = new Random();

        return cards[rand.nextInt(cards.length)];
    }

    /**
     * @return a random card from the game
     */
    public static Card pickRandomCard() {
        ArrayList<Card> cards = new ArrayList<Card>();
        Random rand = new Random();
        int setsChance = rand.nextInt(4);
        int rarityChance = rand.nextInt(4);
        CardSet set;

        if(setsChance == 0) {
            set = CardSet.sets[rand.nextInt(CardSet.sets.length)];

        } else if(setsChance == 1) {
            set = CardSet.oldSets[rand.nextInt(CardSet.oldSets.length)];

        } else if(setsChance == 2) {
            set = CardSet.rareSets[rand.nextInt(CardSet.rareSets.length)];
            return pickCard(set.getSpecials());

        } else { // setsChance == 3
            set = CardSet.promoSets[rand.nextInt(CardSet.promoSets.length)];
            return pickCard(set.getSpecials());
        }

        if(rarityChance == 0) {
            cards = set.getCommons();
        } else if(rarityChance == 1) {
            cards = set.getUncommons();
        } else if(rarityChance == 2) {
            cards = set.getRares();
        } else if(rarityChance == 3) {
            cards = set.getShinies();
        }
        return pickCard(cards);
    }

    /**
     * picks a random card from the game
     * @param rarity the rarity that this random card should be
     * @return the random card
     */
    public static Card pickRandomCard(String rarity) {
        ArrayList<Card> cards = new ArrayList<Card>();
        Random rand = new Random();
        int setsChance = rand.nextInt(2);
        CardSet set;

        if(setsChance == 0) {
            set = CardSet.sets[rand.nextInt(CardSet.sets.length)];
        } else {
            set = CardSet.oldSets[rand.nextInt(CardSet.oldSets.length)];
        }
        if(rarity.equalsIgnoreCase("common")) {
            cards = set.getCommons();
        } else if(rarity.equalsIgnoreCase("uncommon")) {
            cards = set.getUncommons();
        } else if(rarity.equalsIgnoreCase("rare")) {
            cards = set.getRares();
        } else if(rarity.equalsIgnoreCase("shiny")) {
            cards = set.getShinies();
        }
        return pickCard(cards);
    }

    /**
     * @return a random special card from the game
     */
    public static Card pickRandomSpecialCard() {
        Random rand = new Random();
        int setsChance = rand.nextInt(2);
        CardSet set;

        /*if(setsChance == 0) {
            return pickCard(ICustomCards.customs);
        }*/
        if(setsChance == 0) {
            set = CardSet.rareSets[rand.nextInt(CardSet.rareSets.length)];
            return pickCard(set.getSpecials());

        } else {
            set = CardSet.promoSets[rand.nextInt(CardSet.promoSets.length)];
            return pickCard(set.getSpecials());
        }
    }

    /**
     * finds a card from the game based on an ID
     * @param cardId the Pokemon TCG Online ID of the card
     * @return the card to be found; returns null if the card cannot be found
     */
    public static Card findCardById(String cardId) {
        for(CardSet set : CardSet.sets) {
            Card card = crawlCardSet(set, cardId, false);

            if(card != null) {
                return card;
            }
        }
        for(CardSet set : CardSet.oldSets) {
            Card card = crawlCardSet(set, cardId, false);

            if(card != null) {
                return card;
            }
        }
        for(CardSet set : CardSet.rareSets) {
            Card card = crawlCardSet(set, cardId, true);

            if(card != null) {
                return card;
            }
        }
        for(CardSet set : CardSet.promoSets) {
            Card card = crawlCardSet(set, cardId, true);

            if(card != null) {
                return card;
            }
        }
        /*for(Card card : ICustomCards.customs) {
            if(card.getCardId().equalsIgnoreCase(cardId)) {
                return card;
            }
        }*/
        return null;
    }

    /**
     * formats this card's credits amount
     * @param amount the amount of credits
     * @return the formatted credits amount
     */
    public static String formatCredits(int amount) {
        return IEmotes.credits + " **" + GameManager.formatNumber(amount) + "**";
    }

    //==============================================[ PUBLIC NON-STATIC FUNCTIONS ]=====================================================

    @Override
    public boolean isCardSellable() {
        if(this.setName.equalsIgnoreCase("gimme cards")) {
            return false;
            
        } else if(isOldCard() || isPromoCard()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isOldCard() {        
        for(CardSet set : CardSet.oldSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRareCard() {        
        for(CardSet set : CardSet.rareSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPromoCard() {        
        for(CardSet set : CardSet.promoSets) {
            if(set.getSetName().equalsIgnoreCase(this.setName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isShinyCard() {
        if(this.cardRarity.equalsIgnoreCase("common")
        || this.cardRarity.equalsIgnoreCase("uncommon")
        || this.cardRarity.equalsIgnoreCase("rare")) {
            return false;
        }
        return true;
    }

    @Override
    public String findCardTitle(boolean isFav) {
        String cardTitle = "";

        if(this.cardSubtypes == null) {
            if(this.cardSupertype.equalsIgnoreCase("trainer")) {
                cardTitle += "🔧";
            } else if(this.cardSupertype.equalsIgnoreCase("energy")) {
                cardTitle += "💡";
            }

        } else {
            for(int i = 0; i < this.cardSubtypes.length; i++) {
                String cardSubType = this.cardSubtypes[i];
    
                if(cardSubType.equalsIgnoreCase("water")) {
                    cardTitle += IEmotes.water;
                } else if(cardSubType.equalsIgnoreCase("psychic")) {
                    cardTitle += IEmotes.psychic;
                } else if(cardSubType.equalsIgnoreCase("metal")) {
                    cardTitle += IEmotes.metal;
                } else if(cardSubType.equalsIgnoreCase("lightning")) {
                    cardTitle += IEmotes.lightning;
                } else if(cardSubType.equalsIgnoreCase("grass")) {
                    cardTitle += IEmotes.grass;
                } else if(cardSubType.equalsIgnoreCase("fire")) {
                    cardTitle += IEmotes.fire;
                } else if(cardSubType.equalsIgnoreCase("fighting")) {
                    cardTitle += IEmotes.fighting;
                } else if(cardSubType.equalsIgnoreCase("fairy")) {
                    cardTitle += IEmotes.fairy;
                } else if(cardSubType.equalsIgnoreCase("dragon")) {
                    cardTitle += IEmotes.dragon;
                } else if(cardSubType.equalsIgnoreCase("darkness")) {
                    cardTitle += IEmotes.darkness;
                } else if(cardSubType.equalsIgnoreCase("colorless")) {
                    cardTitle += IEmotes.colorless;
                }
            }
        }
        cardTitle += " " + this.cardName;
        if(isFav) {
            return cardTitle + " ❤";
        }
        return cardTitle;
    }

    @Override
    public String findRarityEmote() {
        String rarityEmote = "";

        if(this.cardRarity.equalsIgnoreCase("common")) {
            rarityEmote = "⚪ ";
        } else if(this.cardRarity.equalsIgnoreCase("uncommon")) {
            rarityEmote = "🔷 ";
        } else if(this.cardRarity.equalsIgnoreCase("rare")) {
            rarityEmote = "⭐ ";
        } else if(this.cardRarity.equalsIgnoreCase("promo")) {
            rarityEmote = "🎁 ";
        } else if(this.cardRarity.equalsIgnoreCase("custom")) {
            rarityEmote = "✨ ";
        }else if(this.cardRarity.equalsIgnoreCase("merch")) {
            rarityEmote = "🛍️ ";
        } else {
            rarityEmote = "🌟 ";
        }
        return rarityEmote;
    }

    @Override
    public String formatXP(boolean isSellable) {
        String formattedXP = IEmotes.XP + " **" + GameManager.formatNumber(this.cardPrice) + "**";

        if(!isSellable) {
            return formattedXP + " 🚫";
        }
        return formattedXP;
    }

    @Override
    public String formatCredits() {
        String formattedCredits = IEmotes.credits + " **" + GameManager.formatNumber(this.cardPrice) + "**";

        if(!isCardSellable()) {
            return formattedCredits + " 🚫";
        }
        return formattedCredits;
    }

    @Override
    public int findEmbedColour() {
        int embedColour = 0;

        if(this.cardSubtypes == null) {
            if(this.cardSupertype.equalsIgnoreCase("trainer")) {
                embedColour = 0x768696;
            } else if(this.cardSupertype.equalsIgnoreCase("energy")) {
                embedColour = 0xFED171;
            }

        } else {
            String cardSubType = this.cardSubtypes[0];

            if(cardSubType.equalsIgnoreCase("water")) {
                embedColour = 0x1C9EDD;
            } else if(cardSubType.equalsIgnoreCase("psychic")) {
                embedColour = 0x862E88;
            } else if(cardSubType.equalsIgnoreCase("metal")) {
                embedColour = 0x788088;
            } else if(cardSubType.equalsIgnoreCase("lightning")) {
                embedColour = 0xF9C425;
            } else if(cardSubType.equalsIgnoreCase("grass")) {
                embedColour = 0x207733;
            } else if(cardSubType.equalsIgnoreCase("fire")) {
                embedColour = 0xCC0920;
            } else if(cardSubType.equalsIgnoreCase("fighting")) {
                embedColour = 0x97241B;
            } else if(cardSubType.equalsIgnoreCase("fairy")) {
                embedColour = 0xB9408D;
            } else if(cardSubType.equalsIgnoreCase("dragon")) {
                embedColour = 0x967925;
            } else if(cardSubType.equalsIgnoreCase("darkness")) {
                embedColour = 0x0F2541;
            } else if(cardSubType.equalsIgnoreCase("colorless")) {
                embedColour = 0xE8EBEE;
            }
        }
        return embedColour;
    }

    @Override
    public void displayCard(SlashCommandInteractionEvent event, UserInfo ui, String message, String footer, boolean isFav) {
        String cardTitle = findCardTitle(isFav);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(!message.isEmpty()) {
            desc += message;
            desc += "\n┅┅\n";
        }
        desc += "**Rarity** ┇ " + findRarityEmote() + " " + this.cardRarity + "\n";
        desc += "**Card Set** ┇ " + this.setEmote + " " + this.setName + "\n";
        desc += "**XP Value** ┇ " + formatXP(isCardSellable()) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(this.cardImage);
        embed.setFooter(footer, ui.getUserIcon());
        embed.setColor(findEmbedColour());
        GameManager.sendEmbed(event, embed);
    }

    //===============================================[ PRIVATE FUNCTIONS ]=============================================================

    /**
     * finds this card's subtypes (fire, water, lightning, etc.)
     * @param j the JsonElement crawled from the Pokemon TCG API
     * @return this card's subtypes; returns null if they don't exist (in that case, this card isn't added to the game)
     */
    private static String[] findCardSubtypes(JsonElement j) {
        String[] cardSubtypes;

        try {
            JsonArray jsonArrSubtypes = j.getAsJsonObject().get("types").getAsJsonArray();
            cardSubtypes = new String[jsonArrSubtypes.size()];

            for(int i = 0; i < jsonArrSubtypes.size(); i++) {
                cardSubtypes[i] = jsonArrSubtypes.get(i).toString().replaceAll("\"", "");
            }
            return cardSubtypes;
        } catch(NullPointerException e) {
            return null;
        }
    }

    /**
     * crawls through a card set to find a specific card, based on the card's ID
     * @param set the card set to crawl through
     * @param cardId the Pokemon TCG Online ID of the card
     * @param isSpecial whether or not the card is special (either from rareshop or promoshop)
     * @return the card to be found; returns null if the card cannot be found
     */
    private static Card crawlCardSet(CardSet set, String cardId, boolean isSpecial) {
        if(!isSpecial) {
            for(Card card : set.getCommons()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
            for(Card card : set.getUncommons()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
            for(Card card : set.getRares()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
            for(Card card : set.getShinies()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }

        } else {
            for(Card card : set.getSpecials()) {
                if(card.getCardId().equalsIgnoreCase(cardId)) {
                    return card;
                }
            }
        }
        return null;
    }
}
