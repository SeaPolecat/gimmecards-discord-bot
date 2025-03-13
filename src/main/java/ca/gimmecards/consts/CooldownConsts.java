package ca.gimmecards.consts;

public class CooldownConsts {
    
    // all cooldown checking is calculated in SECONDS
    
    private static int minutesToSeconds(int mins) {
        return mins * 60;
    }

    private static int hoursToSeconds(int hrs) {
        return hrs * 3600;
    }

    public static final int REDEEM_CD = minutesToSeconds(30);

    public static final int DAILY_CD = hoursToSeconds(24);

    public static final int MARKET_RESET_CD = hoursToSeconds(24);

    public static final int BUY_CD = minutesToSeconds(15);

    public static final int MINIGAME_CD = minutesToSeconds(60);

    public static final int OPEN_CD = 5;

    public static final int VOTE_CD = hoursToSeconds(12);
}
