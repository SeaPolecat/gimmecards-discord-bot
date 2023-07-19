package ca.gimmecards.Main;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display.*;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jasypt.util.text.BasicTextEncryptor;

public class Main {

    public static JDA jda;
    public static DiscordBotListAPI dbl;
    public static BasicTextEncryptor encryptor = new BasicTextEncryptor();
    public static final String botToken = "ODE0MDI1NDk5MzgxNzI3MjMy.GuoICs.Hf2yW37yztgjcDa6Ov4mJy6kn2lcN0DhEgG4Dg";
    public static final String testToken = "ODY3MTA1NjEzNzIwNTg0MjIy.G6YPjJ.ne63D-J9CF_swKCP4N6kj9wRLQ6bqC1K_8rtPg";
    public static final String dblToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjgxNDAyNTQ5OTM4MTcyNzIzMiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjUzMjA1MjI3fQ.cKoI_mWYtI6WeJ4boujB3zW6QVqe8Fl1YlnoAj57fMw";
    public static final String encryptorPass = "uxdfINYH8jS4QILwTLub6HZZz9r2GPstOI5Jh9cWTwsm09fQ7S";
    public static final String updateMsg = "🟢 New update on 1/1/2023 ┇ `/changelog`";
    public static boolean isLocked = false;

    public static void main(String[] args) throws LoginException {

        encryptor.setPassword(encryptorPass);

        dbl = new DiscordBotListAPI.Builder()
        .token(dblToken)
        .botId("814025499381727232")
        .build();

        jda = JDABuilder
        .createDefault(testToken,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_MEMBERS,
        GatewayIntent.GUILD_EMOJIS_AND_STICKERS)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableCache(CacheFlag.EMOJI)
        .disableCache(CacheFlag.VOICE_STATE)
        .build();

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("type /help"));

        jda.addEventListener(new Ready());
        jda.addEventListener(new Cmds());
        jda.addEventListener(new Display());
    }
}
