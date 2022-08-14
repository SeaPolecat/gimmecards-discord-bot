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
import org.jasypt.util.text.BasicTextEncryptor;
//import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    //public static Dotenv dotenv = Dotenv.load();
    public static JDA jda;
    public static BasicTextEncryptor encryptor = new BasicTextEncryptor();
    public static final String botToken = "ODE0MDI1NDk5MzgxNzI3MjMy.YDX2Ug.zM7q9Pv8aFYrqv0IBpWUpzNrScw";
    public static final String testToken = "ODY3MTA1NjEzNzIwNTg0MjIy.YPcRCA.Xtytp7Dh2-v3xb1ch1J92DPqUcw";
    public static final String dblToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjgxNDAyNTQ5OTM4MTcyNzIzMiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjUzMjA1MjI3fQ.cKoI_mWYtI6WeJ4boujB3zW6QVqe8Fl1YlnoAj57fMw";
    public static final String encryptorPass = "uxdfINYH8jS4QILwTLub6HZZz9r2GPstOI5Jh9cWTwsm09fQ7S";
    public static final String updateMsg = "🔴 New update on 8/14/2022 ┇ `?changelog`";

    public static void main(String[] args) throws LoginException {

        encryptor.setPassword(encryptorPass);

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

        jda.addEventListener(new Tracker());
        jda.addEventListener(new Cmds());
        jda.addEventListener(new Display());
    }
}
