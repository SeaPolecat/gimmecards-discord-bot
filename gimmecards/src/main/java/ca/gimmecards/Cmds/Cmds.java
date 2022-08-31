package ca.gimmecards.Cmds;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Cmds_.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Cmds extends ListenerAdapter implements Emotes, Colors {
    
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot() == true) { return; }
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(event.getAuthor().getId().equals("454773340163538955")) {
            if(args[0].equalsIgnoreCase("?unlockbot")) {
                Main.isLocked = false;
                JDA.sendMessage(event, blue_, "", "`Unlocked Gimme Cards.`");
            }
        }

        if(Main.isLocked && isCommand(event, args)) {
            JDA.sendMessage(event, red_, "⏰", 
            "The developer has locked *Gimme Cards*, either to fix a bug or prepare for an update; "
            + "please wait until it comes back online!");
            return;
        }

        //DEVELOPER
        if(event.getAuthor().getId().equals("454773340163538955")) {
            if(args[0].equalsIgnoreCase("?lockbot")) {
                Main.isLocked = true;
                JDA.sendMessage(event, blue_, "", "`Locked Gimme Cards.`");
            }

            //TESTING
            if(isValidCommand(event, args, new String[]{"test"}, null)) {
                TestingCmds.testSomething(event);
            }
            if(isValidCommand(event, args, new String[]{"stats"}, null)) {
                DataCmds.viewStats(event);
            }

            //DATA
            if(isValidCommand(event, args, new String[]{"data"}, null)) {
                DataCmds.viewData(event, "new");
            }
            if(isValidCommand(event, args, new String[]{"olddata"}, null)) {
                DataCmds.viewData(event, "old");
            }
            if(isValidCommand(event, args, new String[]{"raredata"}, null)) {
                DataCmds.viewData(event, "rare");
            }
            if(isValidCommand(event, args, new String[]{"promodata"}, null)) {
                DataCmds.viewData(event, "promo");
            }
            if(isValidCommand(event, args, new String[]{"wipe"}, null)) {
                DataCmds.wipeData(event);
            }
            if(isValidCommand(event, args, new String[]{"refresh"}, new String[]{"length change"})) {
                DataCmds.refreshData(event, args);
            }
            if(isValidCommand(event, args, new String[]{"count"}, new String[]{"set code"})) {
                DataCmds.countSetContent(event, args);
            }
            if(isValidCommand(event, args, new String[]{"add"}, new String[]{"set #"})) {
                DataCmds.addContents(event, args, true);
            }
            if(isValidCommand(event, args, new String[]{"addold"}, new String[]{"set #"})) {
                DataCmds.addContents(event, args, false);
            }
            if(isValidCommand(event, args, new String[]{"addrare"}, new String[]{"set #"})) {
                DataCmds.addSpecContents(event, args, true);
            }
            if(isValidCommand(event, args, new String[]{"addpromo"}, new String[]{"set #"})) {
                DataCmds.addSpecContents(event, args, false);
            }
        }

        //PRIVACY
        if(isValidCommand(event, args, new String[]{"accdelete"}, null)) {
            PrivacyCmds.deleteAccount(event);
        }
        if(isValidCommand(event, args, new String[]{"confirm"}, null)) {
            PrivacyCmds.confirmDeletion(event);
        }
        if(isValidCommand(event, args, new String[]{"deny"}, null)) {
            PrivacyCmds.denyDeletion(event);
        }

        //GIFT
        if(isValidCommand(event, args, new String[]{"gifttoken"}, new String[]{"@user", "amount"})) {
            GiftCmds.giftToken(event, args);
        }
        if(isValidCommand(event, args, new String[]{"giftstar"}, new String[]{"@user", "amount"})) {
            GiftCmds.giftStar(event, args);
        }
        if(isValidCommand(event, args, new String[]{"giftcard"}, new String[]{"@user", "card ID"})) {
            GiftCmds.giftCard(event, args);
        }
        if(isValidCommand(event, args, new String[]{"tier1"}, new String[]{"@user"})) {
            GiftCmds.giftRare(event, args);
        }
        if(isValidCommand(event, args, new String[]{"tier2"}, new String[]{"@user"})) {
            GiftCmds.giftRadiantRare(event, args);
        }
        if(isValidCommand(event, args, new String[]{"untier"}, new String[]{"@user"})) {
            GiftCmds.removePatreonRewards(event, args);
        }
        if(isValidCommand(event, args, new String[]{"giftbadge"}, new String[]{"@user"})) {
            GiftCmds.giftHelperBadge(event, args);
        }
        if(isValidCommand(event, args, new String[]{"ungiftbadge"}, new String[]{"@user"})) {
            GiftCmds.removeHelperBadge(event, args);
        }

        //HELP
        if(isValidCommand(event, args, new String[]{"setprefix"}, new String[]{"prefix"})) {
            HelpCmds.changePrefix(event, args);
        }
        if(isValidCommand(event, args, new String[]{"help"}, null) || args[0].equalsIgnoreCase("?help")) {
            HelpCmds.viewHelp(event);
        }
        if(isValidCommand(event, args, new String[]{"rarities"}, null)) {
            HelpCmds.viewRarities(event);
        }
        if(isValidCommand(event, args, new String[]{"badges"}, null)) {
            HelpCmds.viewBadges(event);
        }
        if(isValidCommand(event, args, new String[]{"patreon"}, null)) {
            HelpCmds.viewPatreon(event);
        }
        if(isValidCommand(event, args, new String[]{"changelog", "log"}, null)) {
            HelpCmds.viewChangelog(event);
        }

        //LEADERBOARD
        if(isValidCommand(event, args, new String[]{"ranks"}, null)) {
            LeaderboardCmds.viewRanks(event);
        }
        if(isValidCommand(event, args, new String[]{"leaderboard", "lb"}, null)) {
            LeaderboardCmds.viewLeaderboard(event);
        }

        //BACKPACK
        if(isValidCommand(event, args, new String[]{"backpack", "bag"}, null)) {
            if(args.length < 2) {
                BackpackCmds.viewBackpack(event);
            } else {
                BackpackCmds_.viewBackpack_(event, args);
            }
        }
        if(isValidCommand(event, args, new String[]{"redeem", "rd"}, null)) {
            BackpackCmds.redeemToken(event);
        }
        if(isValidCommand(event, args, new String[]{"daily"}, null)) {
            BackpackCmds.receiveDailyReward(event);
        }
        if(isValidCommand(event, args, new String[]{"setcolor"}, new String[]{"hex code"})) {
            BackpackCmds.assignGameColor(event, args);
        }
        if(isValidCommand(event, args, new String[]{"pin"}, new String[]{"card #"})) {
            BackpackCmds.pinCard(event, args);
        }
        if(isValidCommand(event, args, new String[]{"cooldowns", "cd"}, null)) {
            BackpackCmds.viewCooldowns(event);
        }

        //SHOP
        if(isValidCommand(event, args, new String[]{"shop"}, null)) {
            ShopCmds.viewShop(event);
        }
        if(isValidCommand(event, args, new String[]{"oldshop"}, null)) {
            ShopCmds.viewOldShop(event);
        }
        if(isValidCommand(event, args, new String[]{"rareshop"}, null)) {
            ShopCmds.viewRareShop(event);
        }
        if(isValidCommand(event, args, new String[]{"promoshop"}, null)) {
            ShopCmds.viewPromoShop(event);
        }
        if(isValidCommand(event, args, new String[]{"unlock"}, new String[]{"pack name"})) {
            ShopCmds.unlockPack(event, args);
        }

        //CARD
        if(isValidCommand(event, args, new String[]{"cards"}, null)) {
            if(args.length < 2) {
                CardCmds.viewCards(event, args);
            } else {
                try {
                    Integer.parseInt(args[1]);
                    CardCmds.viewCards(event, args);
                } catch(NumberFormatException e) {
                    CardCmds_.viewCards_(event, args);
                }
            }
        }
        if(isValidCommand(event, args, new String[]{"fav"}, new String[]{"card #"})) {
            CardCmds.favouriteCard(event, args);
        }
        if(isValidCommand(event, args, new String[]{"unfav"}, new String[]{"card #"})) {
            CardCmds.unfavouriteCard(event, args);
        }
        if(isValidCommand(event, args, new String[]{"favall"}, null)) {
            CardCmds.favouriteAll(event);
        }
        if(isValidCommand(event, args, new String[]{"sort"}, null)) { //conditions in command
            CardCmds.sortCards(event, args);
        }

        //VIEW
        if(isValidCommand(event, args, new String[]{"open"}, new String[]{"pack name"})) {
            ViewCmds.openPack(event, args);
        }
        if(isValidCommand(event, args, new String[]{"view"}, new String[]{"card #"})) {
            if(args.length < 3) {
                ViewCmds.viewCard(event, args);
            } else {
                ViewCmds_.viewCard_(event, args);
            }
        }

        //SELL
        if(isValidCommand(event, args, new String[]{"sell"}, new String[]{"card #"})) {
            SellCmds.sellSingle(event, args);
        }
        if(isValidCommand(event, args, new String[]{"selldupes"}, null)) {
            SellCmds.sellDuplicates(event);
        }
        if(isValidCommand(event, args, new String[]{"sellall"}, null)) {
            SellCmds.sellAll(event);
        }

        //MINIGAME
        if(isValidCommand(event, args, new String[]{"minigame", "mini"}, null)) {
            MinigameCmds.startMinigame(event);
        }
        if(isValidCommand(event, args, new String[]{"guess"}, new String[]{"rarity"})) {
            MinigameCmds.makeGuess(event, args);
        }

        //MARKET
        if(isValidCommand(event, args, new String[]{"market"}, null)) {
            MarketCmds.viewMarket(event);
        }
        if(isValidCommand(event, args, new String[]{"mview"}, new String[]{"card #"})) {
            MarketCmds.viewItem(event, args);
        }
        if(isValidCommand(event, args, new String[]{"buy"}, new String[]{"card #"})) {
            MarketCmds.purchaseItem(event, args);
        }

        //SEARCH
        if(isValidCommand(event, args, new String[]{"search"}, null)) { //conditions in command
            SearchCmds.searchCards(event, args);
        }
        if(isValidCommand(event, args, new String[]{"sview"}, new String[]{"card ID"})) {
            SearchCmds.viewAnyCard(event, args);
        }

        //VOTE
        if(isValidCommand(event, args, new String[]{"vote"}, null)) {
            VoteCmds.voteBot(event);
        }
        if(isValidCommand(event, args, new String[]{"claim"}, null)) {
            VoteCmds.claimReward(event);
        }

        //TRADE
        if(isValidCommand(event, args, new String[]{"trade"}, new String[]{"@user"})) {
            TradeCmds.sendTrade(event, args);
        }
        if(isValidCommand(event, args, new String[]{"offer"}, new String[]{"card #"})) {
            TradeCmds.offerCard(event, args);
        }
        if(isValidCommand(event, args, new String[]{"unoffer"}, new String[]{"trade #"})) {
            TradeCmds.unofferCard(event, args);
        }
        if(isValidCommand(event, args, new String[]{"accept"}, null)) {
            TradeCmds.acceptOffer(event);
        }
        if(isValidCommand(event, args, new String[]{"unaccept"}, null)) {
            TradeCmds.unacceptOffer(event);
        }
        if(isValidCommand(event, args, new String[]{"reject"}, null)) {
            TradeCmds.rejectOffer(event);
        }
    }

    private static boolean isCommand(MessageReceivedEvent event, String[] args) {
        Server server = Server.findServer(event);

        if(args[0].toLowerCase().startsWith(server.getPrefix().toLowerCase())) {
            return true;
        }
        return false;
    }

    private static boolean isValidCommand(MessageReceivedEvent event, String[] args, String[] cmds, String[] params) {
        Server server = Server.findServer(event);

        for(String cmd : cmds) {
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
    
                    JDA.sendMessage(event, red_, "❌", "Please follow the format: `" + args[0] + " " + guidance + "`");
                }
            }
        }
        return false;
    }
}
