package ca.gimmecards.OtherInterfaces;
import ca.gimmecards.Display.*;
import ca.gimmecards.Display_MP.*;
import java.util.ArrayList;

public interface IDisplays {
    
    //singleplayer
    public ArrayList<BackpackDisplay> backpackDisplays = new ArrayList<BackpackDisplay>();
    public ArrayList<CollectionDisplay> collectionDisplays = new ArrayList<CollectionDisplay>();
    public ArrayList<HelpDisplay> helpDisplays = new ArrayList<HelpDisplay>();
    public ArrayList<LeaderboardDisplay> leaderboardDisplays = new ArrayList<LeaderboardDisplay>();
    public ArrayList<MarketDisplay> marketDisplays = new ArrayList<MarketDisplay>();
    public ArrayList<MinigameDisplay> minigameDisplays = new ArrayList<MinigameDisplay>();
    public ArrayList<OldShopDisplay> oldShopDisplays = new ArrayList<OldShopDisplay>();
    public ArrayList<SearchDisplay> searchDisplays = new ArrayList<SearchDisplay>();
    public ArrayList<ShopDisplay> shopDisplays = new ArrayList<ShopDisplay>();
    public ArrayList<TradeDisplay> tradeDisplays = new ArrayList<TradeDisplay>();
    public ArrayList<ViewDisplay> viewDisplays = new ArrayList<ViewDisplay>();

    //multiplayer
    public ArrayList<BackpackDisplay_MP> backpackDisplays_MP = new ArrayList<BackpackDisplay_MP>();
    public ArrayList<CardDisplay_MP> cardDisplays_MP = new ArrayList<CardDisplay_MP>();
    public ArrayList<ViewDisplay_MP> viewDisplays_MP = new ArrayList<ViewDisplay_MP>();
}
