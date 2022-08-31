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
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static Dotenv dotenv = Dotenv.load();
    public static JDA jda;
    public static DiscordBotListAPI dbl;
    public static BasicTextEncryptor encryptor = new BasicTextEncryptor();
    public static final String botToken = dotenv.get("BOT_TOKEN");
    public static final String testToken = dotenv.get("TEST_TOKEN");
    public static final String dblToken = dotenv.get("DBL_TOKEN");
    public static final String encryptorPass = dotenv.get("ENCRYPTOR_PASS");
    public static final String updateMsg = "🟡 New update on 8/26/2022 ┇ `?changelog`";
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
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
        GatewayIntent.GUILD_MEMBERS)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableCache(CacheFlag.EMOJI)
        .disableCache(CacheFlag.VOICE_STATE)
        .build();

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("type ?help"));

        jda.addEventListener(new Ready());
        jda.addEventListener(new Cmds());
        jda.addEventListener(new Display());
    }
}
