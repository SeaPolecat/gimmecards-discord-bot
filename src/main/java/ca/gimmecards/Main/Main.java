package ca.gimmecards.main;
import ca.gimmecards.cmds.*;
import ca.gimmecards.consts.*;
import ca.gimmecards.utils.*;
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
import javax.security.auth.login.LoginException;

public class Main {

    //=========================================[ API INSTANCES ]=======================================================================

    public static JDA jda;
    public static DiscordBotListAPI dbl;
    public static BasicTextEncryptor encryptor = new BasicTextEncryptor();

    //============================================[ MAIN METHOD ]======================================================================

    public static void main(String[] args) throws LoginException {

        encryptor.setPassword(PasswordConsts.encryptorPassword);

        dbl = new DiscordBotListAPI.Builder()
        .token(PasswordConsts.dblToken)
        .botId("814025499381727232")
        .build();

        jda = JDABuilder
        .createDefault(PasswordConsts.testToken, // change this token accordingly
        GatewayIntent.MESSAGE_CONTENT, // comment this line out before releasing an update (the actual bot isn't allowed to have this)
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

        jda.addEventListener(new Cmds());
        jda.addEventListener(new Server());

        // load up game data on startup
        try { 
            DataUtils.loadSets();
            DataUtils.loadOldSets();
            DataUtils.loadRareSets();
            DataUtils.loadPromoSets();
            DataUtils.loadUsers();
            DataUtils.loadServers();
        } catch(Exception e) {}
    }
}
