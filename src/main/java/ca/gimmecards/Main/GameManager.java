package ca.gimmecards.Main;
import ca.gimmecards.Display.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.Nullable;
import java.text.NumberFormat;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Random;

public class GameManager extends ListenerAdapter {

    //==============================================[ SAVE/LOAD PATHS ]=================================================================

    /**
     * the address of this game's save files; please change this accordingly (remember the 2 backslashes at the end)
     */
    protected static String address = "C:\\Users\\wangw\\Documents\\DocumentsV2\\GimmeCards\\src\\main\\java\\ca\\gimmecards\\Storage\\";

    protected static String setsPath = "CardSets.json";
    protected static String oldSetsPath = "OldCardSets.json";
    protected static String rareSetsPath = "RareCardSets.json";
    protected static String promoSetsPath = "PromoCardSets.json";
    protected static String userPath = "Users.json";
    protected static String serverPath = "Servers.json";

    /**
     * determines whether or not to prefix a path with the address
     * @param path the path in question
     * @return the path with an address if the file is on your local computer; no address is added if the file is being hosted on the VPS
     */
    protected static String findSavePath(String path) {
        if(Paths.get(path).toFile().length() > 0) {
            return path;
        } else {
            return address + path;
        }
    }

    //==============================================[ GLOBAL CHECKER ]===============================================================
    
    public static long globalDeletionEpoch = (long)(0);
    /*
     * Requries: 
     * Modifies: 
     * Effects: 
     */

    public static boolean globalCDChecker() {
        return User.isCooldownDone(globalDeletionEpoch, 720, true);
    }

    public static void globalTimeStampEpoch() { globalDeletionEpoch = Calendar.getInstance().getTimeInMillis() / 60000; } //set time to current timestamp in minutes

    //==============================================[ FORMAT FUNCTIONS ]===============================================================

    /**
     * picks a random int between 2 ints (because Java version 11 doesn't have a built-in function to do this)
     * @param min the lower range int
     * @param max the upper range int
     * @return a random int between the two ints
     */
    public static int randRange(int min, int max) {
        int diff = max - min;

        return new Random().nextInt(diff + 1) + min;
    }
    
    /**
     * inserts commas in a large number to make it more readable
     * @param number the large number
     * @return a formatted number with commas
     */
    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    /**
     * formats a command name by slapping a slash in front of it
     * @param cmd the command name to format
     * @return the formatted command name
     */
    public static String formatCmd(String cmd) {
        return "`/" + cmd + "`";
    }

    /**
     * formats the player's username by turning it into a ping
     * @param event a slash event, used to get the username
     * @return the formatted ping username
     */
    public static String formatName(SlashCommandInteractionEvent event) {
        return event.getUser().getAsMention();
    }

    /**
     * formats a mentioned player's username by turning it into a ping
     * @param mention the mentioned player
     * @param event a slash event, used to get the username
     * @return the formatted ping username
     */
    public static String formatName(User mention, SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.User user = event.getJDA().getUserById(mention.getUserId()+"");

        if(user == null) { return ""; }

        return user.getAsMention();
    }

    /**
     * creates a message notifying the player that they have earned a badge
     * @param event a slash event, used to call formatName()
     * @param badgeEmote the Discord emote of the badge
     * @param badgeName the name of the badge
     * @return a string message that notifies the player of their new badge
     */
    public static String formatBadge(SlashCommandInteractionEvent event, String badgeEmote, String badgeName) {
        return formatName(event) + " has been awarded the " + badgeEmote + " **" + badgeName + "** badge!";
    }

    //===================================[ DISCORD JDA HANDLER FUNCTIONS ]=============================================================

    /**
     * creates a button ID that's used to handle page-flipping
     * @param userId the Discord ID of the player
     * @param slashId the ID of a specific slash command
     * @param buttonType the type of button (left arrow, right arrow, or refresh)
     * @return a button ID
     */
    public static String createButtonId(SlashCommandInteractionEvent event, String buttonType) {
        String userId = event.getUser().getId();
        String slashId = event.getInteraction().getId();

        return userId + ";" + slashId + "_" + buttonType;
    }

    /**
     * sends a formatted embed message in the server based on a message event
     * @param event the message event
     * @param color the color that the embed should appear in
     * @param emote the emote that should come before the message
     * @param msg the string message you want to send
     */
    public static void sendMessage(MessageReceivedEvent event, int color, String emote, String msg) {
        try {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setDescription(emote + " " + msg);
            embed.setColor(color);
            sendEmbed(event, embed);
        } catch(InsufficientPermissionException e) {}
    }

    /**
     * sends a formatted embed message in the server based on a slash event
     * @param event the slash event
     * @param color the color that the embed should appear in
     * @param emote the emote that should come before the message
     * @param msg the string message you want to send
     */
    public static void sendMessage(SlashCommandInteractionEvent event, int color, String emote, String msg) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setDescription(emote + " " + msg);
        embed.setColor(color);
        sendEmbed(event, embed);
    }

     /**
      * sends a formatted embed message in the server based on a slash event, with a 'how to get Premium' button attached below
      * @param event the slash event
      * @param color the color that the embed should appear in
      * @param emote the emote that should come before the message
      * @param msg the string message you want to send
      * @param adCard a sample premium card to show in /redeem messages; can be null if you don't want this to show
      */
    public static void sendPremiumMessage(SlashCommandInteractionEvent event, int color, String emote, String msg, @Nullable Card adCard) {
        EmbedBuilder embed = new EmbedBuilder();
        Emoji kofiEmote = event.getJDA().getEmojiById("1140389615379959860");

        embed.setDescription(emote + " " + msg);

        if(adCard != null) {
            embed.setImage(adCard.getCardImage());
        }
        embed.setColor(color);
        event.getHook().editOriginalEmbeds(embed.build())
        .setActionRow(
            Button.primary(createButtonId(event, "premium"), "How to get Premium").withEmoji(kofiEmote)
        ).queue();
    }

    /**
     * sends a plain embed in the server based on a message event
     * @param event the message event
     * @param embed the embed to send
     */
    public static void sendEmbed(MessageReceivedEvent event, EmbedBuilder embed) {
        try {
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } catch(InsufficientPermissionException e) {}
    }

    /**
     * sends a plain embed in the server based on a slash event
     * @param event the slash event
     * @param embed the embed to send
     */
    public static void sendEmbed(SlashCommandInteractionEvent event, EmbedBuilder embed) {
        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }

    /**
     * sends an embed in the server that can be page-flipped or refreshed - based on a slash event
     * @param event the slash event
     * @param user the player
     * @param server the server
     * @param disp the type of Display that this embed should show
     * @param page the page number that this embed should initially show
     */
    public static void sendDynamicEmbed(SlashCommandInteractionEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);
        String slashId = event.getInteraction().getId();

        if(page == -1) {
            event.getHook().editOriginalEmbeds(embed.build())
            .setActionRow(
                Button.secondary(createButtonId(event, "refresh"), "Refresh")
            ).queue();

        } else {
            event.getHook().editOriginalEmbeds(embed.build())
            .setActionRow(
                Button.primary(createButtonId(event, "left"), "◀"),
                Button.primary(createButtonId(event, "right"), "▶"),
                Button.secondary(createButtonId(event, "refresh"), "Refresh")
            ).queue();
        }
        disp.setSlashId(slashId);
        disp.setPage(page);
    }

    /**
     * edits a dynamic embed (one that can be page-flipped or refreshed) - based on a button event
     * @param event the button event
     * @param user the player
     * @param server the server
     * @param disp the type of Display that this embed should show
     * @param page the page number that this embed should show after being edited
     */
    public static void editEmbed(ButtonInteractionEvent event, User user, Server server, Display disp, int page) {
        UserInfo ui = new UserInfo(event);
        EmbedBuilder embed = disp.buildEmbed(user, ui, server, page);
        
        event.getHook().editOriginalEmbeds(embed.build()).queue();
    }

    //==============================================[ EVENT FUNCTIONS ]================================================================

    /**
     * an event function that's called whenever the bot leaves a server; used to delete a server's data within Servers.json if the bot leaves that server
     * @param event the GuildLeave event
     */
    public void onGuildLeave(GuildLeaveEvent event) {
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
     * an event function that's called whenever the bot joins a server; used to perform necessary actions (i.e., data loading) when the bots starts running
     * @param event the GuildReady event
     */
    public void onGuildReady(GuildReadyEvent event) {

        //============================================[ DATA LOADING ]=================================================================

        try { CardSet.loadSets(); } catch(Exception e) {}
        try { CardSet.loadOldSets(); } catch(Exception e) {}
        try { CardSet.loadRareSets(); } catch(Exception e) {}
        try { CardSet.loadPromoSets(); } catch(Exception e) {}
        try { User.loadUsers(); } catch(Exception e) {}
        try { Server.loadServers(); } catch(Exception e) {}

        //==================================[ UPDATING GLOBAL SLASH COMMANDS ]=========================================================

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
        ).queue();*/

        //===========================================[ ADDING SET CODES ]===============================================================

        //sets
        /*CardSet.setCodes.put(1, "BLW");
        CardSet.setCodes.put(2, "EPO");
        CardSet.setCodes.put(3, "NVI");
        CardSet.setCodes.put(4, "NXD");
        CardSet.setCodes.put(5, "DEX");
        CardSet.setCodes.put(6, "DRX");
        CardSet.setCodes.put(7, "BCR");
        CardSet.setCodes.put(8, "PLS");
        CardSet.setCodes.put(9, "PLF");
        CardSet.setCodes.put(10, "PLB");
        CardSet.setCodes.put(11, "LTR");
        CardSet.setCodes.put(12, "XY");
        CardSet.setCodes.put(13, "FLF");
        CardSet.setCodes.put(14, "FFI");
        CardSet.setCodes.put(15, "PHF");
        CardSet.setCodes.put(16, "PRC");
        CardSet.setCodes.put(17, "ROS");
        CardSet.setCodes.put(18, "AOR");
        CardSet.setCodes.put(19, "BKT");
        CardSet.setCodes.put(20, "BKP");
        CardSet.setCodes.put(21, "FCO");
        CardSet.setCodes.put(22, "STS");
        CardSet.setCodes.put(23, "EVO");
        CardSet.setCodes.put(24, "SUM");
        CardSet.setCodes.put(25, "GRI");
        CardSet.setCodes.put(26, "BUS");
        CardSet.setCodes.put(27, "CIN");
        CardSet.setCodes.put(28, "UPR");
        CardSet.setCodes.put(29, "FLI");
        CardSet.setCodes.put(30, "CES");
        CardSet.setCodes.put(31, "LOT");
        CardSet.setCodes.put(32, "TEU");
        CardSet.setCodes.put(33, "UNB");
        CardSet.setCodes.put(34, "UNM");
        CardSet.setCodes.put(35, "CEC");
        CardSet.setCodes.put(36, "SSH");
        CardSet.setCodes.put(37, "RCL");
        CardSet.setCodes.put(38, "DAA");
        CardSet.setCodes.put(39, "VIV");
        CardSet.setCodes.put(40, "BST");
        CardSet.setCodes.put(41, "CRE");
        CardSet.setCodes.put(42, "EVS");
        CardSet.setCodes.put(43, "FST");
        CardSet.setCodes.put(44, "BRS");
        CardSet.setCodes.put(45, "ASR");
        CardSet.setCodes.put(46, "LOR");
        CardSet.setCodes.put(47, "SIT");

        //oldSets (FL doesn't exist)
        CardSet.oldSetCodes.put(1, "BS");
        CardSet.oldSetCodes.put(2, "JU");
        CardSet.oldSetCodes.put(3, "FO");
        CardSet.oldSetCodes.put(4, "B2");
        CardSet.oldSetCodes.put(5, "TR");
        CardSet.oldSetCodes.put(6, "G1");
        CardSet.oldSetCodes.put(7, "G2");
        CardSet.oldSetCodes.put(8, "N1");
        CardSet.oldSetCodes.put(9, "N2");
        CardSet.oldSetCodes.put(10, "N3");
        CardSet.oldSetCodes.put(11, "N4");
        CardSet.oldSetCodes.put(12, "LC");
        CardSet.oldSetCodes.put(13, "EX");
        CardSet.oldSetCodes.put(14, "AQ");
        CardSet.oldSetCodes.put(15, "SK");
        CardSet.oldSetCodes.put(16, "RS");
        CardSet.oldSetCodes.put(17, "SS");
        CardSet.oldSetCodes.put(18, "DR");
        CardSet.oldSetCodes.put(19, "MA");
        CardSet.oldSetCodes.put(20, "HL");
        CardSet.oldSetCodes.put(21, "TRR");
        CardSet.oldSetCodes.put(22, "DX");
        CardSet.oldSetCodes.put(23, "EM");
        CardSet.oldSetCodes.put(24, "UF");
        CardSet.oldSetCodes.put(25, "DS");
        CardSet.oldSetCodes.put(26, "LM");
        CardSet.oldSetCodes.put(27, "HP");
        CardSet.oldSetCodes.put(28, "CG");
        CardSet.oldSetCodes.put(29, "DF");
        CardSet.oldSetCodes.put(30, "PK");
        CardSet.oldSetCodes.put(31, "DP");
        CardSet.oldSetCodes.put(32, "MT");
        CardSet.oldSetCodes.put(33, "SW");
        CardSet.oldSetCodes.put(34, "GE");
        CardSet.oldSetCodes.put(35, "MD");
        CardSet.oldSetCodes.put(36, "LA");
        CardSet.oldSetCodes.put(37, "SF");
        CardSet.oldSetCodes.put(38, "PL");
        CardSet.oldSetCodes.put(39, "RR");
        CardSet.oldSetCodes.put(40, "SV");
        CardSet.oldSetCodes.put(41, "AR");
        CardSet.oldSetCodes.put(42, "HS");
        CardSet.oldSetCodes.put(43, "UL");
        CardSet.oldSetCodes.put(44, "UD");
        CardSet.oldSetCodes.put(45, "TM");
        CardSet.oldSetCodes.put(46, "CL");

        //rareSets
        CardSet.rareSetCodes.put(1, "DRV");
        CardSet.rareSetCodes.put(2, "DCR");
        CardSet.rareSetCodes.put(3, "GEN");
        CardSet.rareSetCodes.put(4, "SLG");
        CardSet.rareSetCodes.put(5, "DRM");
        CardSet.rareSetCodes.put(6, "HIF");
        CardSet.rareSetCodes.put(7, "CPA");
        CardSet.rareSetCodes.put(8, "SHF");
        CardSet.rareSetCodes.put(9, "CEL");

        //promoSets
        CardSet.promoSetCodes.put(1, "NP");
        CardSet.promoSetCodes.put(2, "DPP");
        CardSet.promoSetCodes.put(3, "HS");
        CardSet.promoSetCodes.put(4, "BLW");
        CardSet.promoSetCodes.put(5, "XY");
        CardSet.promoSetCodes.put(6, "SW");*/
    }
}
