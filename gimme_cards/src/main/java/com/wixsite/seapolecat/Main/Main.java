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
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

    public static Dotenv dotenv = Dotenv.load();
    public static JDA jda;
    public static final String botToken = "ODY3MTA1NjEzNzIwNTg0MjIy.YPcRCA.Xtytp7Dh2-v3xb1ch1J92DPqUcw";
    public static final String dblToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjgxNDAyNTQ5OTM4MTcyNzIzMiIsImJvdCI6dHJ1ZSwiaWF0IjoxNjUzMjA1MjI3fQ.cKoI_mWYtI6WeJ4boujB3zW6QVqe8Fl1YlnoAj57fMw";
    public static final String botId = "814025499381727232";

    public static void main(String[] args) throws LoginException {

        jda = JDABuilder
        .createDefault(botToken,
        GatewayIntent.GUILD_MESSAGES,
        GatewayIntent.GUILD_MESSAGE_REACTIONS,
        GatewayIntent.GUILD_MEMBERS)
        .setChunkingFilter(ChunkingFilter.ALL)
        .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE)
        .build();

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        jda.addEventListener(new Ready());
        jda.addEventListener(new Cmds());
        jda.addEventListener(new Display());
    }
}
