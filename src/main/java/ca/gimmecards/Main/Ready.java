package ca.gimmecards.Main;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
//import net.dv8tion.jda.api.interactions.commands.OptionType;
//import net.dv8tion.jda.api.interactions.commands.build.Commands;
//import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import javax.annotation.Nonnull;

public class Ready extends ListenerAdapter {

    /**
     * deletes the Server data whenever the bot leaves that server
     * @param event a leave event
     */
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        for(int i = 0; i < Server.servers.size(); i++) {
            Server s = Server.servers.get(i);

            if(s.getServerId().equals(event.getGuild().getId())) {
                Server.servers.remove(i);
                try { Server.saveServers(); } catch(Exception e) {}
                break;
            }
        }
    }
    
    /**
     * loads all data into their respective lists,
     * and updates global slash commands when the bot is ready
     * @param event a ready event
     */
    public void onGuildReady(@Nonnull GuildReadyEvent event) {

        try { Data.loadData(); } catch(Exception e) {}
        try { Data.loadOldData(); } catch(Exception e) {}
        try { Data.loadRareData(); } catch(Exception e) {}
        try { Data.loadPromoData(); } catch(Exception e) {}
        try { User.loadUsers(); } catch(Exception e) {}
        try { Server.loadServers(); } catch(Exception e) {}

        /*Main.jda.updateCommands().addCommands(
            
            //LOCK
            Commands.slash("qazxsw", "[REDACTED]"),
            
            Commands.slash("plmnko", "[REDACTED]"),

            //PRIVACY
            Commands.slash("deleteaccount", "Delete your Gimme Cards account (this action is irreversible!)"),

            //GIFT
            Commands.slash("gifttoken", "Gift someone tokens")
            .addOption(OptionType.USER, "user", "mention a user", true)
            .addOption(OptionType.INTEGER, "amount", "enter an amount", true),

            Commands.slash("giftstar", "Gift someone stars")
            .addOption(OptionType.USER, "user", "mention a user", true)
            .addOption(OptionType.INTEGER, "amount", "enter an amount", true),

            Commands.slash("giftcard", "Gift someone any card")
            .addOption(OptionType.USER, "user", "mention a user", true)
            .addOption(OptionType.STRING, "card-id", "enter a card ID", true),

            Commands.slash("tier1", "Gift someone the Rare Patreon perk")
            .addOption(OptionType.USER, "user", "mention a user", true),

            Commands.slash("tier2", "Gift someone the Radiant Rare Patreon perk")
            .addOption(OptionType.USER, "user", "mention a user", true),

            Commands.slash("untier", "Remove all Patreon perks from someone")
            .addOption(OptionType.USER, "user", "mention a user", true),

            Commands.slash("giftbadge", "Reward someone with the Helper Badge")
            .addOption(OptionType.USER, "user", "mention a user", true),

            Commands.slash("ungiftbadge", "Remove the Helper Badge from someone")
            .addOption(OptionType.USER, "user", "mention a user", true),

            //HELP
            Commands.slash("help", "Get access to the website and other resources"),

            Commands.slash("rarities", "Show every possible card rarity in the game"),

            Commands.slash("badges", "Show every possible badge in the game"),

            Commands.slash("patreon", "Show the Gimme Cards premium rewards"),

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

            //CARD
            Commands.slash("cards", "See your current card collection")
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
        ).queue();*/

        //sets
        /*
        Data.setCodes.put(1, "BLW");
        Data.setCodes.put(2, "EPO");
        Data.setCodes.put(3, "NVI");
        Data.setCodes.put(4, "NXD");
        Data.setCodes.put(5, "DEX");
        Data.setCodes.put(6, "DRX");
        Data.setCodes.put(7, "BCR");
        Data.setCodes.put(8, "PLS");
        Data.setCodes.put(9, "PLF");
        Data.setCodes.put(10, "PLB");
        Data.setCodes.put(11, "LTR");
        Data.setCodes.put(12, "XY");
        Data.setCodes.put(13, "FLF");
        Data.setCodes.put(14, "FFI");
        Data.setCodes.put(15, "PHF");
        Data.setCodes.put(16, "PRC");
        Data.setCodes.put(17, "ROS");
        Data.setCodes.put(18, "AOR");
        Data.setCodes.put(19, "BKT");
        Data.setCodes.put(20, "BKP");
        Data.setCodes.put(21, "FCO");
        Data.setCodes.put(22, "STS");
        Data.setCodes.put(23, "EVO");
        Data.setCodes.put(24, "SUM");
        Data.setCodes.put(25, "GRI");
        Data.setCodes.put(26, "BUS");
        Data.setCodes.put(27, "CIN");
        Data.setCodes.put(28, "UPR");
        Data.setCodes.put(29, "FLI");
        Data.setCodes.put(30, "CES");
        Data.setCodes.put(31, "LOT");
        Data.setCodes.put(32, "TEU");
        Data.setCodes.put(33, "UNB");
        Data.setCodes.put(34, "UNM");
        Data.setCodes.put(35, "CEC");
        Data.setCodes.put(36, "SSH");
        Data.setCodes.put(37, "RCL");
        Data.setCodes.put(38, "DAA");
        Data.setCodes.put(39, "VIV");
        Data.setCodes.put(40, "BST");
        Data.setCodes.put(41, "CRE");
        Data.setCodes.put(42, "EVS");
        Data.setCodes.put(43, "FST");
        Data.setCodes.put(44, "BRS");
        Data.setCodes.put(45, "ASR");
        Data.setCodes.put(46, "LOR");
        Data.setCodes.put(47, "SIT");
        */

        //oldSets (FL doesn't exist)
        /*
        Data.oldSetCodes.put(1, "BS");
        Data.oldSetCodes.put(2, "JU");
        Data.oldSetCodes.put(3, "FO");
        Data.oldSetCodes.put(4, "B2");
        Data.oldSetCodes.put(5, "TR");
        Data.oldSetCodes.put(6, "G1");
        Data.oldSetCodes.put(7, "G2");
        Data.oldSetCodes.put(8, "N1");
        Data.oldSetCodes.put(9, "N2");
        Data.oldSetCodes.put(10, "N3");
        Data.oldSetCodes.put(11, "N4");
        Data.oldSetCodes.put(12, "LC");
        Data.oldSetCodes.put(13, "EX");
        Data.oldSetCodes.put(14, "AQ");
        Data.oldSetCodes.put(15, "SK");
        Data.oldSetCodes.put(16, "RS");
        Data.oldSetCodes.put(17, "SS");
        Data.oldSetCodes.put(18, "DR");
        Data.oldSetCodes.put(19, "MA");
        Data.oldSetCodes.put(20, "HL");
        Data.oldSetCodes.put(21, "TRR");
        Data.oldSetCodes.put(22, "DX");
        Data.oldSetCodes.put(23, "EM");
        Data.oldSetCodes.put(24, "UF");
        Data.oldSetCodes.put(25, "DS");
        Data.oldSetCodes.put(26, "LM");
        Data.oldSetCodes.put(27, "HP");
        Data.oldSetCodes.put(28, "CG");
        Data.oldSetCodes.put(29, "DF");
        Data.oldSetCodes.put(30, "PK");
        Data.oldSetCodes.put(31, "DP");
        Data.oldSetCodes.put(32, "MT");
        Data.oldSetCodes.put(33, "SW");
        Data.oldSetCodes.put(34, "GE");
        Data.oldSetCodes.put(35, "MD");
        Data.oldSetCodes.put(36, "LA");
        Data.oldSetCodes.put(37, "SF");
        Data.oldSetCodes.put(38, "PL");
        Data.oldSetCodes.put(39, "RR");
        Data.oldSetCodes.put(40, "SV");
        Data.oldSetCodes.put(41, "AR");
        Data.oldSetCodes.put(42, "HS");
        Data.oldSetCodes.put(43, "UL");
        Data.oldSetCodes.put(44, "UD");
        Data.oldSetCodes.put(45, "TM");
        Data.oldSetCodes.put(46, "CL");
        */

        //rareSets
        /*
        Data.rareSetCodes.put(1, "DRV");
        Data.rareSetCodes.put(2, "DCR");
        Data.rareSetCodes.put(3, "GEN");
        Data.rareSetCodes.put(4, "SLG");
        Data.rareSetCodes.put(5, "DRM");
        Data.rareSetCodes.put(6, "HIF");
        Data.rareSetCodes.put(7, "CPA");
        Data.rareSetCodes.put(8, "SHF");
        Data.rareSetCodes.put(9, "CEL");
        */

        //promoSets
        /*
        Data.promoSetCodes.put(1, "NP");
        Data.promoSetCodes.put(2, "DPP");
        Data.promoSetCodes.put(3, "HS");
        Data.promoSetCodes.put(4, "BLW");
        Data.promoSetCodes.put(5, "XY");
        Data.promoSetCodes.put(6, "SW");
        */
    }
}
