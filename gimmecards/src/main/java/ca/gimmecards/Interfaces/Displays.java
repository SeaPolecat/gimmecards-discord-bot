package ca.gimmecards.Interfaces;
import ca.gimmecards.Display.*;
import ca.gimmecards.Display_.*;
import java.util.ArrayList;

public interface Displays {
    
    //singleplayer
    public ArrayList<BackpackDisplay> backpackDisplays = new ArrayList<BackpackDisplay>();
    public ArrayList<CardDisplay> cardDisplays = new ArrayList<CardDisplay>();
    public ArrayList<HelpDisplay> helpDisplays = new ArrayList<HelpDisplay>();
    public ArrayList<LeaderboardDisplay> leaderboardDisplays = new ArrayList<LeaderboardDisplay>();
    public ArrayList<MarketDisplay> marketDisplays = new ArrayList<MarketDisplay>();
    public ArrayList<MinigameDisplay> minigameDisplays = new ArrayList<MinigameDisplay>();
    public ArrayList<OldShopDisplay> oldShopDisplays = new ArrayList<OldShopDisplay>();
    public ArrayList<PrivacyDisplay> privacyDisplays = new ArrayList<PrivacyDisplay>();
    public ArrayList<SearchDisplay> searchDisplays = new ArrayList<SearchDisplay>();
    public ArrayList<ShopDisplay> shopDisplays = new ArrayList<ShopDisplay>();
    public ArrayList<TradeDisplay> tradeDisplays = new ArrayList<TradeDisplay>();
    public ArrayList<ViewDisplay> viewDisplays = new ArrayList<ViewDisplay>();

    //multiplayer
    public ArrayList<BackpackDisplay_> backpackDisplays_ = new ArrayList<BackpackDisplay_>();
    public ArrayList<CardDisplay_> cardDisplays_ = new ArrayList<CardDisplay_>();
    public ArrayList<ViewDisplay_> viewDisplays_ = new ArrayList<ViewDisplay_>();
}
