package com.wixsite.seapolecat.Interfaces;

public interface Changelog {
    
    String[] changelog = {

        //1
        "**Update 7/19/2021**\n"
        + "🔹Redeem cooldown is now shortened to 1 hour\n"
        + "🔹Fixed issue where [cards] didn't work sometimes\n"
        + "🔹Fixed a few other minor issues\n"
        + "🔹Removed the cards collected & cards sold stats in backpack\n"
        + "🔹Changed the error messages a bit",

        //2
        "**Update 7/20/2021**\n"
        + "🔹New command [setprefix] that lets you set a custom prefix\n"
        + "🔹New commands [fav] and [unfav]" 
        + " that locks/unlocks cards so you don't sell your favourite ones by accident\n"
        + "🔹Nerfed chances of getting shiny cards (meant to be more realistic)",

        //3
        "**Update 7/28/2021**\n"
        + "🔹Added the legacy card sets (46 new packs 🥳)\n"
        + "🔹Less XP is needed to level up now, to make it slightly easier",

        //4
        "**Update 7/30/2021**\n"
        + "🔹Redeem cooldown is now shortened to 30 mins\n"
        + "🔹Added a minigame! Use [minigame] to play 😛\n"
        + "🔹New command [rarities] allows you to see every rarity in the game",

        //5
        "**Update 7/31/2021**\n"
        + "🔹Slightly nerfed minigame rewards\n"
        + "🔹Legacy cards are now in a separate shop, and cost 2 Tokens"
        + " (this is because they are worth much more than the newer cards)."
        + " Use [shopold] to see them\n"
        + "🔹New command [sellall] lets you sell all your cards at once (except favs)\n"
        + "🔹New command [vote] lets you vote for this bot 😎\n"
        + "🔹Card XP values have been balanced a bit more\n"
        + "🔹Changelog now automatically sends after each update (if the bot has the perms)\n"
        + "🔹Slight adjustments in the visual design\n"
        + "🔹Other minor improvements/bug fixes",

        //6
        "**Update 8/2/2021**\n"
        + "🔹The XP needed to level up has been greatly increased. This is because the previous levelling system "
        + "was too fast, leading to near bot-abuse cases. However, in exchange, you will also get tokens for each "
        + "level-up! The higher your level, the more tokens you'll get 😎\n"
        + "🔹Minor bug fixes",

        //7
        "**Update 8/5/2021**\n"
        + "🔹Legacy cards have been removed, since their XP value is too high, making them easy to abuse\n"
        + "🔹The max level is now 40. Those with insanely high levels have been reset to 40. "
        + "You can still reach over 40 though, but I'll have to think about what to do with your extra keys... 🤔\n"
        + "🔹Those with a godly number of tokens have been reset to 20 tokens\n"
        + "🔹Card XP values have been further balanced\n"
        + "🔹Backpacks are now customizable! 😋 For now, [setcolor <hex code>]"
        + " lets you change your backpack colour\n"
        + "🔹Some bug fixes/overall improvements",

        //8
        "**Update 8/6/2021**\n"
        + "🔹Fixed a bug where you can't view the old shop cards\n"
        + "🔹Removed the [latest] command, as no one was using it\n"
        + "🔹New command [setfavcard <#>] that lets you display your favourite card on your backpack\n"
        + "🔹You can now view other people's cards! Do [cards] or [view <#>]"
        + " and mention someone\n"
        + "🔹Bot's speed has been slightly improved",

        //9
        "**Update 8/11/2021**\n"
        + "🔹New command [search <card name>] that lets you search for specific cards "
        + "from the entire Pokémon card database\n"
        + "🔹New command [view <id>] that lets you view cards you searched for\n"
        + "🔹Levelling has been slightly buffed: additional XP needed to level up reduced from 1,000 to 700 \n"
        + "🔹Minigame now also includes the rarities of normal cards (common, uncommon, rare)\n"
        + "🔹Bug fixes/general improvements\n"
        + "🔹Surprise update: you'll see it when you redeem tokens 🙃",

        //10
        "**Update 8/18/2021**\n"
        + "🔹Switched to slash commands 🥳 (will need to re-invite the bot)\n"
        + "🔹To view searched cards, you will now have to use the separate command `/viewsearch`\n"
        + "🔹Some small bug fixes",

        //11
        "**Update 8/20/2021**\n"
        + "🔹Legacy packs have returned! (but they're not sellable anymore) Use `/shopold` to see them\n"
        + "🔹You can now earn rewards by voting for the bot. Use `/claim` every 12 hours to claim them!\n"
        + "🔹Minigame XP rewards have been buffed\n"
        + "🔹Levelling has been made slightly easier\n"
        + "🔹A few bug fixes / UI changes",

        //12
        "**Update 8/22/2021**\n"
        + "🔹We've switched back to normal commands (sorry about that! 😅)\n"
        + "🔹Bot responses no longer ping the user\n"
        + "🔹Added a 5 second cooldown to opening packs, to prevent bot spam\n"
        + "🔹Redeem command now gives a link to the latest changelog\n"
        + "🔹Legacy shop command changed from [shopold] to [oldshop]\n"
        + "🔹Both minigame and card search now also include legacy cards\n"
        + "🔹Sorting cards by newest option has been removed",

        //13
        "**Update 9/19/2021**\n"
        + "🔸New pack *Chilling Reign*\n"
        + "🔹Shop now shows how many packs you've unlocked\n"
        + "🔹Now you must own a card to display it on your backpack",

        //14
        "**Update 1/1/2022**\n"
        + "🔸New pack *Evolving Skies*\n"
        + "🔸Happy New Year 🎆 Everyone gets a free flying pikachu! (check your cards)\n"
        + "🔹Added *Energy*, a new currency system! Gain energy by being active and using the bot\n"
        + "🔹Added a daily market! Each day, the market will refresh with 10 new cards, which you can buy "
        + "using energy. Every server's market is different, so make sure to explore them all!\n"
        + "🔹New command [market] that lets you view the market\n"
        + "🔹New command [mview (card number)] that lets you view items in the market\n"
        + "🔹New command [buy (card number)] that lets you buy items from the market\n"
        + "🔹Voting and claiming are now separate. Use [vote] to vote, and [claim] to claim your reward\n"
        + "🔹Using [changelog] now shows the entire history of the bot's changes\n"
        + "🔹Changed the designs of the bot's messages\n"
        + "🔹Changed the design of the welcome message that *Gimme Cards* sends when it joins a server\n"
        + "🔹Fixed a bug where [sell] sells the entire stack of cards, instead of only one",

        //15
        "**Update 3/20/2022**\n"
        + "🔸New pack *Fusion Strike*\n"
        + "🔸New pack *Brilliant Stars*\n"
        + "🔸Added 11 new _exclusive_ Pokémon TCG packs! Use [rareshop] to see them "
        + "and [open (pack name)] to draw a single card from any exclusive pack\n"
        + "🔹Added *Stars*, another new currency system! Use stars to get exclusive cards\n"
        + "🔹Game balancing: chances of shiny card drops have been decreased (20% to 10%), but "
        + "using commands now gives much more Energy, so be sure to check out the [market] often :D\n"
        + "🔹[vote] and [claim] have been temporarily disabled due to technical problems...\n"
        + "🔹In the meantime, enjoy a new command [daily] that gives a reward every 24 hours\n"
        + "🔹New command [favs] that shows all your favourite cards\n"
        + "🔹New command [ranks] that shows the top 10 collectors in the server, based on level\n"
        + "🔹Minor design changes",

        //16
        "**Update 5/27/2022**\n"
        + "🔸Did you see the new website?? 👀 You can go check it out with the [help] command!\n"
        + "🔹Voting has been fixed! 🥳 You can now use [vote] to vote for *Gimme Cards*, then [claim] "
        + "to claim a special gift! Let's all vote and make this server huge! 😄\n"
        + "🔹[daily] now gives a random shiny card instead of tokens and stars\n"
        + "🔹New command [favall] that favourites all your shiny cards automatically\n"
        + "🔹New command [leaderboard] that shows the highest level collectors in the world\n"
        + "🔹[ranks] has been re-designed, and now also shows everyone's XP progress\n"
        + "🔹Added \"🎁 Promo\" to [rarities]\n"
        + "🔹Levelling up now gives 1 star each time\n"
        + "🔹[guess] now accepts guesses that have no spaces, like \"rareholo\" or \"rareshiny\"\n"
        + "🔹Game balacing: the amount of energy you get has been decreased\n"
        + "🔹Market cards are now unsellable (due to a loophole where you could convert all your "
        + "energy into XP)\n"
        + "🔹Fixed a bug where sometimes you gain -0 energy\n"
        + "🔹There weren't enough card submissions for the custom booster pack, sorry! We have 5/10",

        //17
        "**Update 6/4/2022**\n"
        + "🔸Congrats to all giveaway winners! 🥳 Please check your backpack/cards to see your prizes\n"
        + "🔹Deleted all *Gimme Cards* accounts under level 2 (to save storage space)\n"
        + "🔹Exclusive cards now also show up in [search] results\n"
        + "🔹Removed the [sellgroup] command\n"
        + "🔹Improved the design of level-up messages\n"
        + "🔹Other minor design changes\n"
        + "🔹A few bug fixes"
    };
}
