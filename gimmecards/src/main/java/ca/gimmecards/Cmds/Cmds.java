package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Cmds extends ListenerAdapter implements Emotes {
    
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot() == true) { return; }
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        //STAFF
        if(isValidCommand(event, args, "giftbadge", new String[]{"user"})) {
            GiftCmds.giftBadge(event, args);
        }

        //DEVELOPER
        if(event.getAuthor().getId().equals("454773340163538955")) {
            //TESTING
            if(isValidCommand(event, args, "test", null)) {
                TestingCmds.testSomething(event);
            }
            if(isValidCommand(event, args, "stats", null)) {
                DataCmds.viewStats(event);
            }

            //GIFT
            if(isValidCommand(event, args, "gifttoken", new String[]{"user", "amount"})) {
                GiftCmds.giftToken(event, args);
            }
            if(isValidCommand(event, args, "giftstar", new String[]{"user", "amount"})) {
                GiftCmds.giftStar(event, args);
            }

            //DATA
            if(isValidCommand(event, args, "data", null)) {
                DataCmds.viewData(event, "new");
            }
            if(isValidCommand(event, args, "olddata", null)) {
                DataCmds.viewData(event, "old");
            }
            if(isValidCommand(event, args, "raredata", null)) {
                DataCmds.viewData(event, "rare");
            }
            if(isValidCommand(event, args, "promodata", null)) {
                DataCmds.viewData(event, "promo");
            }
            if(isValidCommand(event, args, "wipe", null)) {
                DataCmds.wipeData(event);
            }
            if(isValidCommand(event, args, "refresh", new String[]{"length change"})) {
                DataCmds.refreshData(event, args);
            }
            if(isValidCommand(event, args, "count", new String[]{"set code"})) {
                DataCmds.countSetContent(event, args);
            }
            if(isValidCommand(event, args, "add", new String[]{"set number"})) {
                DataCmds.addContents(event, args, true);
            }
            if(isValidCommand(event, args, "addold", new String[]{"set number"})) {
                DataCmds.addContents(event, args, false);
            }
            if(isValidCommand(event, args, "addrare", new String[]{"set number"})) {
                DataCmds.addSpecContents(event, args, true);
            }
            if(isValidCommand(event, args, "addpromo", new String[]{"set number"})) {
                DataCmds.addSpecContents(event, args, false);
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
        if(isValidCommand(event, args, "badges", null)) {
            HelpCmds.viewBadges(event);
        }
        if(isValidCommand(event, args, "changelog", null)) {
            HelpCmds.viewChangelog(event);
        }
        if(isValidCommand(event, args, "ranks", null)) {
            HelpCmds.viewRanks(event);
        }
        if(isValidCommand(event, args, "leaderboard", null)) {
            HelpCmds.viewLeaderboard(event);
        }

        //BACKPACK
        if(isValidCommand(event, args, "backpack", null)) {
            if(args.length < 2) {
                BackpackCmds.viewBackpack(event);
            } else {
                BackpackCmds_.viewBackpack_(event, args);
            }
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
        if(isValidCommand(event, args, "pin", new String[]{"card number"})) {
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
        if(isValidCommand(event, args, "promoshop", null)) {
            ShopCmds.viewPromoShop(event);
        }
        if(isValidCommand(event, args, "unlock", new String[]{"pack name"})) {
            ShopCmds.unlockPack(event, args);
        }

        //CARD
        if(isValidCommand(event, args, "cards", null)) {
            if(args.length < 2) {
                CardCmds.viewCards(event, args);
            } else {
                CardCmds_.viewCards_(event, args);
            }
        }
        if(isValidCommand(event, args, "fav", new String[]{"card number"})) {
            CardCmds.favouriteCard(event, args);
        }
        if(isValidCommand(event, args, "unfav", new String[]{"card number"})) {
            CardCmds.unfavouriteCard(event, args);
        }
        if(isValidCommand(event, args, "favall", null)) {
            CardCmds.favouriteAll(event);
        }
        if(isValidCommand(event, args, "sort", null)) {
            CardCmds.sortCards(event, args);
        }

        //INSPECTION
        if(isValidCommand(event, args, "open", new String[]{"pack name"})) {
            InspectionCmds.openPack(event, args);
        }
        if(isValidCommand(event, args, "view", new String[]{"card number/id"})) {
            if(args.length < 3) {
                InspectionCmds.viewCard(event, args);
            } else {
                InspectionCmds_.viewCard_(event, args);
            }
        }

        //SELL
        if(isValidCommand(event, args, "sell", new String[]{"card number"})) {
            SellCmds.sellSingle(event, args);
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
        if(isValidCommand(event, args, "vote", null)) {
            VoteCmds.voteBot(event);
        }
        if(isValidCommand(event, args, "claim", null)) {
            VoteCmds.claimReward(event);
        }

        //TRADE
        /*if(isValidCommand(event, args, "trade", new String[]{"mention"})) {
            TradeCmds.sendTrade(event, args);
        }
        if(isValidCommand(event, args, "offer", new String[]{"card number"})) {
            TradeCmds.makeOffer(event, args);
        }*/
    }

    private static boolean isValidCommand(MessageReceivedEvent event, String[] args, String cmd, String[] params) {
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
            }
        }
        return false;
    }
}
