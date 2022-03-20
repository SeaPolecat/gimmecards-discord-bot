package com.wixsite.seapolecat.Main;
import com.wixsite.seapolecat.Cmds.*;
import com.wixsite.seapolecat.Display.*;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.discordbots.api.client.DiscordBotListAPI;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static JDA jda;
    public static DiscordBotListAPI dbl;

    public static void main(String[] args) throws LoginException {

        Dotenv dotenv = Dotenv.load();
        String botToken = dotenv.get("BOT_TOKEN");
        String dblToken = dotenv.get("BDL_TOKEN");
        String botId = dotenv.get("BOT_ID");

        jda = JDABuilder
        .createDefault(botToken,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.GUILD_EMOJIS,
        GatewayIntent.GUILD_MEMBERS)
        .setChunkingFilter(ChunkingFilter.ALL)
        .enableCache(CacheFlag.EMOTE)
        .disableCache(CacheFlag.VOICE_STATE)
        .build();

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        jda.addEventListener(new Ready());
        jda.addEventListener(new Cmds());
        jda.addEventListener(new Display());

        dbl = new DiscordBotListAPI.Builder()
        .token(dblToken)
        .botId(botId)
        .build();
    }
}
