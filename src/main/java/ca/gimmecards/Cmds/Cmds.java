package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds_MP.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

public class Cmds extends ListenerAdapter {

    //==============================================[ EVENT FUNCTIONS ]================================================================
    
    /**
     * an event function that's called whenever a user sends a regular message in a server; used to listen to admin commands
     * @param event the MessageReceived event
     */
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot() == true) { return; }
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        String authorId = event.getAuthor().getId();

        if(authorId.equals("454773340163538955") || authorId.equals("967695872689315890")) {

            //===========================================[ TESTING ]===================================================================

            if(isValidCommand(event, args, new String[]{"test"}, null)) {
                TestingCmds.testSomething(event);
            }

            //=============================================[ CARD ]====================================================================

            if(isValidCommand(event, args, new String[]{"stats"}, null)) {
                CardCmds.viewBotStats(event);
            }
            if(isValidCommand(event, args, new String[]{"sets"}, null)) {
                CardCmds.viewCardSets(event, "new");
            }
            if(isValidCommand(event, args, new String[]{"oldsets"}, null)) {
                CardCmds.viewCardSets(event, "old");
            }
            if(isValidCommand(event, args, new String[]{"raresets"}, null)) {
                CardCmds.viewCardSets(event, "rare");
            }
            if(isValidCommand(event, args, new String[]{"promosets"}, null)) {
                CardCmds.viewCardSets(event, "promo");
            }
            if(isValidCommand(event, args, new String[]{"wipe"}, null)) {
                CardCmds.wipeCardSets(event);
            }
            if(isValidCommand(event, args, new String[]{"refresh"}, new String[]{"length change"})) {
                CardCmds.refreshCardSets(event, args);
            }
            if(isValidCommand(event, args, new String[]{"count"}, new String[]{"set code"})) {
                CardCmds.countCardSet(event, args);
            }
            if(isValidCommand(event, args, new String[]{"add"}, new String[]{"set #"})) {
                CardCmds.addContents(event, args, true);
            }
            if(isValidCommand(event, args, new String[]{"addold"}, new String[]{"set #"})) {
                CardCmds.addContents(event, args, false);
            }
            if(isValidCommand(event, args, new String[]{"addrare"}, new String[]{"set #"})) {
                CardCmds.addSpecialContents(event, args, true);
            }
            if(isValidCommand(event, args, new String[]{"addpromo"}, new String[]{"set #"})) {
                CardCmds.addSpecialContents(event, args, false);
            }
            if(isValidCommand(event, args, new String[]{"clear"}, null)) {
                CardCmds.clearingIDs(event);
            }
            if(isValidCommand(event, args, new String[]{"arraysize"}, null)) {
                CardCmds.checkSizeOfArray(event);
            }

            //========================================[ GUILD SLASH UPDATE ]===========================================================

            // please use ?update whenever you edit the slash commands

            if(isValidCommand(event, args, new String[]{"update"}, null)) {
                Guild guild = event.getGuild();

                guild.updateCommands().addCommands(

                    //LOCK
                    Commands.slash("qazxsw", "[REDACTED]"),
                    
                    Commands.slash("plmnko", "[REDACTED]"),

                    //PRIVACY
                    Commands.slash("deleteaccount", "Delete your Gimme Cards account (this action is irreversible!)"),
    
                    //GIFT
                    Commands.slash("gifttoken", "Gift someone tokens")
                    .addOption(OptionType.USER, "user", "mention a user", true)
                    .addOption(OptionType.INTEGER, "amount", "enter an amount", true),

                    Commands.slash("giftcredits", "Gift someone credits")
                    .addOption(OptionType.USER, "user", "mention a user", true)
                    .addOption(OptionType.INTEGER, "amount", "enter an amount", true),
    
                    Commands.slash("giftstar", "Gift someone stars")
                    .addOption(OptionType.USER, "user", "mention a user", true)
                    .addOption(OptionType.INTEGER, "amount", "enter an amount", true),
    
                    Commands.slash("giftcard", "Gift someone any card")
                    .addOption(OptionType.USER, "user", "mention a user", true)
                    .addOption(OptionType.STRING, "card-id", "enter a card ID", true),
    
                    //HELP
                    Commands.slash("help", "Get access to the website and other resources"),
    
                    Commands.slash("rarities", "Show every possible card rarity in the game"),
    
                    Commands.slash("badges", "Show every possible badge in the game"),

                    Commands.slash("premium", "See the instructions for getting Gimme Cards Premium"),
    
                    Commands.slash("changelog", "See the latest updates to the game"),
    
                    //LEADERBOARD
                    Commands.slash("ranks", "See the top collectors in your current server"),
    
                    Commands.slash("leaderboard", "See the top collectors in the world"),
    
                    //BACKPACK
                    Commands.slash("backpack", "See your current level, items, and badges")
                    .addOption(OptionType.USER, "user", "mention a user", false),
    
                    Commands.slash("redeem", "Redeem a single token (30 min cooldown)"),
    
                    Commands.slash("daily", "Get a free shiny card (24 hr cooldown)"),
    
                    Commands.slash("setcolor", "Change your game's theme color")
                    .addOption(OptionType.STRING, "hex-code", "enter a hex code", true),
    
                    Commands.slash("pin", "Display one of your cards on your backpack")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    Commands.slash("cooldowns", "See all your command cooldowns"),
    
                    //SHOP
                    Commands.slash("shop", "Visit the Pokémon packs shop"),
    
                    Commands.slash("oldshop", "Visit the legacy Pokémon packs shop"),
    
                    Commands.slash("rareshop", "Visit the exclusive Pokémon packs shop"),
    
                    Commands.slash("promoshop", "Visit the promo Pokémon packs shop"),
    
                    Commands.slash("unlock", "Use a key to unlock a pack")
                    .addOption(OptionType.STRING, "pack-name", "enter a pack name", true),
    
                    //COLLECTION
                    Commands.slash("collection", "See your current card collection")
                    .addOption(OptionType.INTEGER, "page", "enter a page", false)
                    .addOption(OptionType.USER, "user", "mention a user", false),
    
                    Commands.slash("fav", "Add a card to your favorites")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    Commands.slash("unfav", "Remove a card from your favorites")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    Commands.slash("favall", "Automatically favorite all your shiny cards"),
    
                    Commands.slash("sort", "Sort your cards a certain way")
                    .addOptions(
                        new OptionData(OptionType.STRING, "option", "select an option", true)
                        .addChoice("alphabetical", "alphabetical")
                        .addChoice("xp", "xp")
                        .addChoice("quantity", "quantity")
                        .addChoice("newest", "newest"),
    
                        new OptionData(OptionType.STRING, "order", "select an order", true)
                        .addChoice("increasing", "increasing")
                        .addChoice("decreasing", "decreasing")
                    ),
    
                    //VIEW
                    Commands.slash("open", "Use a token to open a pack")
                    .addOption(OptionType.STRING, "pack-name", "enter a pack name", true),
    
                    Commands.slash("view", "Show the details of a card you own")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true)
                    .addOption(OptionType.USER, "user", "mention a user", false),
    
                    //SELL
                    Commands.slash("sell", "Sell one of your cards for XP")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    Commands.slash("selldupes", "Sell your duplicate cards for XP"),
    
                    Commands.slash("sellall", "Sell all your non-favorite cards for XP"),
    
                    //MINIGAME
                    Commands.slash("minigame", "Play a guessing game (1 hr cooldown)"),
    
                    Commands.slash("guess", "Make a guess during the minigame")
                    .addOption(OptionType.STRING, "rarity", "enter a rarity", true),
    
                    //MARKET
                    Commands.slash("market", "View the daily card market"),
    
                    Commands.slash("mview", "Show the details of a market card")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    Commands.slash("buy", "Buy a card from the market")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    //SEARCH
                    Commands.slash("search", "Search for cards from the Pokémon card database")
                    .addOptions(
                        new OptionData(OptionType.STRING, "option", "select an option", true)
                        .addChoice("card", "card")
                        .addChoice("pack", "pack")
                        .addChoice("rarity", "rarity"),
    
                        new OptionData(OptionType.STRING, "keywords", "enter some keywords", true)
                    ),
    
                    Commands.slash("sview", "View any card from the TCG database")
                    .addOption(OptionType.STRING, "card-id", "enter a card ID", true),
    
                    //VOTE
                    Commands.slash("vote", "Vote for Gimme Cards on Top.gg (12 hr cooldown)"),
    
                    Commands.slash("claim", "Claim a special gift for voting"),
    
                    //TRADE
                    Commands.slash("trade", "Request a trade to another user")
                    .addOption(OptionType.USER, "user", "mention a user", true),
    
                    Commands.slash("offer", "Offer a card from your collection to the trade")
                    .addOption(OptionType.INTEGER, "card-number", "enter a card number", true),
    
                    Commands.slash("unoffer", "Unoffer a card from your trade")
                    .addOption(OptionType.INTEGER, "trade-number", "enter a trade number", true),
    
                    Commands.slash("accept", "Confirm your trade"),
    
                    Commands.slash("unaccept", "Cancel your trade offer to add more cards or reconsider"),
    
                    Commands.slash("reject", "End a trade instantly")
                ).queue();

                GameManager.sendMessage(event, IColors.blue, "", "`Successfully updated this guild's slash commands.`");
            }
        }
    }

    /**
     * an event function that's called whenever a user sends a slash command in a server; used to listen to slash commands
     * @param event the SlashCommandInteraction event
     */
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        event.deferReply().queue();

        //LOCK
        if(event.getUser().getId().equals("454773340163538955") && event.getName().equals("plmnko")) {
            Main.isLocked = false;
            GameManager.sendMessage(event, IColors.blue, "", "`Unlocked Gimme Cards.`");
        }

        if(Main.isLocked) {
            GameManager.sendMessage(event, IColors.red, "⏰", 
            "The developer has locked *Gimme Cards*, either to fix a bug or prepare for an update; "
            + "please wait until it comes back online!");
            return;
        }

        if(event.getUser().getId().equals("454773340163538955") && event.getName().equals("qazxsw")) {
            Main.isLocked = true;
            GameManager.sendMessage(event, IColors.blue, "", "`Locked Gimme Cards.`");
        }
        
        //PRIVACY
        if(event.getName().equals("deleteaccount")) {
            PrivacyCmds.deleteAccount(event);
        }

        //GIFT
        if(event.getName().equals("gifttoken")) {
            GiftCmds.giftToken(event);
        }
        if(event.getName().equals("giftcredits")) {
            GiftCmds.giftCredits(event);
        }
        if(event.getName().equals("giftstar")) {
            GiftCmds.giftStar(event);
        }
        if(event.getName().equals("giftcard")) {
            GiftCmds.giftCard(event);
        }

        //HELP
        if(event.getName().equals("help")) {
            HelpCmds.viewHelp(event);
        }
        if(event.getName().equals("rarities")) {
            HelpCmds.viewRarities(event);
        }
        if(event.getName().equals("badges")) {
            HelpCmds.viewBadges(event);
        }
        if(event.getName().equals("premium")) {
            HelpCmds.viewPremium(event);
        }
        if(event.getName().equals("changelog")) {
            HelpCmds.viewChangelog(event);
        }

        //LEADERBOARD
        if(event.getName().equals("ranks")) {
            LeaderboardCmds.viewRanks(event);
        }
        if(event.getName().equals("leaderboard")) {
            LeaderboardCmds.viewLeaderboard(event);
        }

        //BACKPACK
        if(event.getName().equals("backpack")) {
            if(event.getOption("user") == null) {
                BackpackCmds.viewBackpack(event);
            } else {
                BackpackCmds_MP.viewBackpack_(event);
            }
        }
        if(event.getName().equals("redeem")) {
            BackpackCmds.redeemToken(event);
        }
        if(event.getName().equals("daily")) {
            BackpackCmds.receiveDailyReward(event);
        }
        if(event.getName().equals("setcolor")) {
            BackpackCmds.assignGameColor(event);
        }
        if(event.getName().equals("pin")) {
            BackpackCmds.pinCard(event);
        }
        if(event.getName().equals("cooldowns")) {
            BackpackCmds.viewCooldowns(event);
        }

        //SHOP
        if(event.getName().equals("shop")) {
            ShopCmds.viewShop(event);
        }
        if(event.getName().equals("oldshop")) {
            ShopCmds.viewOldShop(event);
        }
        if(event.getName().equals("rareshop")) {
            ShopCmds.viewRareShop(event);
        }
        if(event.getName().equals("promoshop")) {
            ShopCmds.viewPromoShop(event);
        }
        if(event.getName().equals("unlock")) {
            ShopCmds.unlockPack(event);
        }

        //COLLECTION
        if(event.getName().equals("collection")) {
            if(event.getOption("user") == null) {
                CollectionCmds.viewCards(event);
            } else {
                CollectionCmds_MP.viewCards_(event);
            }
        }
        if(event.getName().equals("fav")) {
            CollectionCmds.favouriteCard(event);
        }
        if(event.getName().equals("unfav")) {
            CollectionCmds.unfavouriteCard(event);
        }
        if(event.getName().equals("favall")) {
            CollectionCmds.favouriteAll(event);
        }
        if(event.getName().equals("sort")) {
            CollectionCmds.sortCards(event);
        }

        //VIEW
        if(event.getName().equals("open")) {
            ViewCmds.openPack(event);
        }
        if(event.getName().equals("view")) {
            if(event.getOption("user") == null) {
                ViewCmds.viewCard(event);
            } else {
                ViewCmds_MP.viewCard_(event);
            }
        }

        //SELL
        if(event.getName().equals("sell")) {
            SellCmds.sellSingle(event);
        }
        if(event.getName().equals("selldupes")) {
            SellCmds.sellDuplicates(event);
        }
        if(event.getName().equals("sellall")) {
            SellCmds.sellAll(event);
        }

        //MINIGAME
        if(event.getName().equals("minigame")) {
            MinigameCmds.startMinigame(event);
        }
        if(event.getName().equals("guess")) {
            MinigameCmds.makeGuess(event);
        }

        //MARKET
        if(event.getName().equals("market")) {
            MarketCmds.viewMarket(event);
        }
        if(event.getName().equals("mview")) {
            MarketCmds.viewItem(event);
        }
        if(event.getName().equals("buy")) {
            MarketCmds.purchaseItem(event);
        }

        //SEARCH
        if(event.getName().equals("search")) {
            SearchCmds.searchCards(event);
        }
        if(event.getName().equals("sview")) {
            SearchCmds.viewAnyCard(event);
        }

        //VOTE
        if(event.getName().equals("vote")) {
            VoteCmds.voteBot(event);
        }
        if(event.getName().equals("claim")) {
            VoteCmds.claimReward(event);
        }

        //TRADE
        if(event.getName().equals("trade")) {
            TradeCmds.sendTrade(event);
        }
        if(event.getName().equals("offer")) {
            TradeCmds.offerCard(event);
        }
        if(event.getName().equals("unoffer")) {
            TradeCmds.unofferCard(event);
        }
        if(event.getName().equals("accept")) {
            TradeCmds.acceptOffer(event);
        }
        if(event.getName().equals("unaccept")) {
            TradeCmds.unacceptOffer(event);
        }
        if(event.getName().equals("reject")) {
            TradeCmds.rejectOffer(event);
        }

    }

    //===============================================[ PRIVATE FUNCTIONS ]=============================================================

    /**
     * checks if a player's message command is valid (is spelled correctly and has all the required arguments)
     * @param event the message event
     * @param args an array containing every separated string in the player's input
     * @param cmds a list of the command's aliases; typing any of them will work
     * @param params additional required command arguments; can be null if there aren't any additional requirements
     * @return whether or not the player's message command is valid
     */
    private static boolean isValidCommand(MessageReceivedEvent event, String[] args, String[] cmds, @Nullable String[] params) {
        for(String cmd : cmds) {
            if(args[0].equalsIgnoreCase("?" + cmd)) {
                if(params == null) {
                    return true;
    
                } else if(args.length >= params.length + 1) {
                    return true;
    
                } else {
                    String guidance = "";
                    for(int i = 0; i < params.length; i++) {
                        guidance += "(" + params[i] + ") ";
                    }
                    guidance = guidance.trim();
    
                    GameManager.sendMessage(event, IColors.red, "❌", "Please follow the format: `" + args[0] + " " + guidance + "`");
                }
            }
        }
        return false;
    }
}
