package ca.gimmecards.Interfaces;
import ca.gimmecards.Display.*;
import ca.gimmecards.Display_.*;
import java.util.ArrayList;

public interface Displays {
    
    //singleplayer
    public static ArrayList<BackpackDisplay> backpackDisplays = new ArrayList<BackpackDisplay>();
    public static ArrayList<CardDisplay> cardDisplays = new ArrayList<CardDisplay>();
    public static ArrayList<HelpDisplay> helpDisplays = new ArrayList<HelpDisplay>();
    public static ArrayList<MarketDisplay> marketDisplays = new ArrayList<MarketDisplay>();
    public static ArrayList<MinigameDisplay> minigameDisplays = new ArrayList<MinigameDisplay>();
    public static ArrayList<OldShopDisplay> oldShopDisplays = new ArrayList<OldShopDisplay>();
    public static ArrayList<PrivacyDisplay> privacyDisplays = new ArrayList<PrivacyDisplay>();
    public static ArrayList<SearchDisplay> searchDisplays = new ArrayList<SearchDisplay>();
    public static ArrayList<ShopDisplay> shopDisplays = new ArrayList<ShopDisplay>();
    public static ArrayList<TradeDisplay> tradeDisplays = new ArrayList<TradeDisplay>();
    public static ArrayList<ViewDisplay> viewDisplays = new ArrayList<ViewDisplay>();

    //multiplayer
    public static ArrayList<BackpackDisplay_> backpackDisplays_ = new ArrayList<BackpackDisplay_>();
    public static ArrayList<CardDisplay_> cardDisplays_ = new ArrayList<CardDisplay_>();
    public static ArrayList<ViewDisplay_> viewDisplays_ = new ArrayList<ViewDisplay_>();
}
