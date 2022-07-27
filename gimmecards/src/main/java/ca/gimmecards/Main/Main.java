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
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static Dotenv dotenv = Dotenv.load();
    public static JDA jda;
    public static final String botToken = dotenv.get("BOT_TOKEN");
    public static final String testToken = dotenv.get("TEST_TOKEN");
    public static final String dblToken = dotenv.get("DBL_TOKEN");

    public static void main(String[] args) throws LoginException {

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
        jda.getPresence().setActivity(Activity.playing("?help"));

        jda.addEventListener(new Ready());
        jda.addEventListener(new Cmds());
        jda.addEventListener(new Display());
    }
}
