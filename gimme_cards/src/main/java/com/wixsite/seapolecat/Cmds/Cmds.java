package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Interfaces.*;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Cmds extends ListenerAdapter implements Emotes {
    
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot() == true) { return; }
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //ADMIN
        if(event.getAuthor().getId().equals("454773340163538955")) {
            if(isValidCommand(event, args, "test", null)) {
                TesterCmds.testSomething(event);
            }
            if(isValidCommand(event, args, "stats", null)) {
                DataCmds.viewStats(event);
            }
            if(isValidCommand(event, args, "data", null)) {
                DataCmds.viewData(event, "new");
            }
            if(isValidCommand(event, args, "olddata", null)) {
                DataCmds.viewData(event, "old");
            }
            if(isValidCommand(event, args, "specdata", null)) {
                DataCmds.viewData(event, "spec");
            }
            if(isValidCommand(event, args, "refresh", new String[]{"length change"})) {
                DataCmds.refreshData(event, args);
            }
            if(isValidCommand(event, args, "count", new String[]{"set code"})) {
                DataCmds.countSetContent(event, args);
            }
            if(isValidCommand(event, args, "addset", new String[]{"set number"})) {
                DataCmds.addSetContent(event, args, true);
            }
            if(isValidCommand(event, args, "addoldset", new String[]{"set number"})) {
                DataCmds.addSetContent(event, args, false);
            }
            if(isValidCommand(event, args, "addspecset", new String[]{"set number"})) {
                DataCmds.addSpecSetContent(event, args);
            }
        }

        //HELP
        if(isValidCommand(event, args, "setprefix", new String[]{"prefix"})) {
            HelpCmds.changePrefix(event, args);
        }
        if(isValidCommand(event, args, "help", null) || args[0].equalsIgnoreCase("?help")) {
            HelpCmds.viewHelp(event);
        }
        if(isValidCommand(event, args, "rarities", null)) {
            HelpCmds.viewRarities(event);
        }
        if(isValidCommand(event, args, "changelog", null)) {
            HelpCmds.viewChangelog(event);
        }
        if(isValidCommand(event, args, "leaderboard", null)) {
            HelpCmds.viewLeaderboard(event);
        }

        //BACKPACK
        if(isValidCommand(event, args, "backpack", null)) {
            BackpackCmds.viewBackpack(event);
        }
        if(isValidCommand(event, args, "redeem", null)) {
            BackpackCmds.redeemToken(event);
        }
        if(isValidCommand(event, args, "daily", null)) {
            BackpackCmds.receiveDailyReward(event);
        }
        if(isValidCommand(event, args, "setcolor", new String[]{"hex code"})) {
            BackpackCmds.assignBackpackColor(event, args);
        }
        if(isValidCommand(event, args, "setfavcard", new String[]{"card number"})) {
            BackpackCmds.assignBackpackCard(event, args);
        }

        //SHOP
        if(isValidCommand(event, args, "shop", null)) {
            ShopCmds.viewShop(event);
        }
        if(isValidCommand(event, args, "oldshop", null)) {
            ShopCmds.viewOldShop(event);
        }
        if(isValidCommand(event, args, "rareshop", null)) {
            ShopCmds.viewRareShop(event);
        }
        if(isValidCommand(event, args, "unlock", new String[]{"pack name"})) {
            ShopCmds.unlockPack(event, args);
        }

        //CARD
        if(isValidCommand(event, args, "cards", null)) {
            CardCmds.viewCards(event, args);
            CardCmds_.viewCards_(event, args);
        }
        if(isValidCommand(event, args, "fav", new String[]{"card number"})) {
            CardCmds.favouriteCard(event, args);
        }
        if(isValidCommand(event, args, "unfav", new String[]{"card number"})) {
            CardCmds.unfavouriteCard(event, args);
        }
        if(isValidCommand(event, args, "sort", null)) {
            CardCmds.sortCards(event, args);
        }

        //INSPECTION
        if(isValidCommand(event, args, "open", new String[]{"pack name"})) {
            InspectionCmds.openPack(event, args);
        }
        if(isValidCommand(event, args, "view", new String[]{"card number/id"})) {
            InspectionCmds.viewCard(event, args);
            InspectionCmds_.viewCard_(event, args);
        }

        //SELL
        if(isValidCommand(event, args, "sell", new String[]{"card number"})) {
            SellCmds.sellSingle(event, args);
        }
        if(isValidCommand(event, args, "sellgroup", new String[]{"card number", "card number"})) {
            SellCmds.sellGroup(event, args);
        }
        if(isValidCommand(event, args, "selldupes", null)) {
            SellCmds.sellDuplicates(event);
        }
        if(isValidCommand(event, args, "sellall", null)) {
            SellCmds.sellAll(event);
        }

        //MINIGAME
        if(isValidCommand(event, args, "minigame", null)) {
            MinigameCmds.startMinigame(event);
        }
        if(isValidCommand(event, args, "guess", new String[]{"rarity"})) {
            MinigameCmds.makeGuess(event, args);
        }

        //MARKET
        if(isValidCommand(event, args, "market", null)) {
            MarketCmds.viewMarket(event);
        }
        if(isValidCommand(event, args, "mview", new String[]{"card number"})) {
            MarketCmds.viewItem(event, args);
        }
        if(isValidCommand(event, args, "buy", new String[]{"card number"})) {
            MarketCmds.purchaseItem(event, args);
        }

        //SEARCH
        if(isValidCommand(event, args, "search", new String[]{"card name"})) {
            SearchCmds.searchCards(event, args);
        }
        if(isValidCommand(event, args, "favs", null)) {
            SearchCmds.viewFavCards(event);
        }

        //VOTE
        /*
        if(isValidCommand(event, args, "vote", null)) {
            VoteCmds.voteBot(event);
        }
        if(isValidCommand(event, args, "claim", null)) {
            VoteCmds.claimReward(event);
        }
        */
    }

    private static boolean isValidCommand(GuildMessageReceivedEvent event, String[] args, String cmd, String[] params) {
        Server server = Server.findServer(event);

        if(args[0].equalsIgnoreCase(server.getPrefix() + cmd)) {
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

                Rest.sendMessage(event, jigglypuff_ + " Please follow the format: `" + args[0] + " " + guidance + "`");
                return false;
            }
        }
        return false;
    }
}
