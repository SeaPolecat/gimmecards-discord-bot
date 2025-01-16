package ca.gimmecards.consts;

public class CooldownConsts {
    
    // all cooldown checking is calculated in SECONDS
    
    private static int minutesToSeconds(int mins) {
        return mins * 60;
    }

    private static int hoursToSeconds(int hrs) {
        return hrs * 3600;
    }

    public static final int redeemCooldown = minutesToSeconds(30);

    public static final int dailyCooldown = hoursToSeconds(24);

    public static final int marketResetCooldown = hoursToSeconds(24);

    public static final int buyCooldown = minutesToSeconds(15);

    public static final int minigameCooldown = minutesToSeconds(60);

    public static final int openCooldown = 5;

    public static final int voteCooldown = hoursToSeconds(12);
}
