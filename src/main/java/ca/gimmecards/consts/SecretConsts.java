package ca.gimmecards.consts;
import io.github.cdimascio.dotenv.Dotenv;

public class SecretConsts {
    
    private static Dotenv dotenv = Dotenv.load();

    public static final String BOT_TOKEN = dotenv.get("BOT_TOKEN");

    public static final String TEST_TOKEN = dotenv.get("TEST_TOKEN");

    public static final String DBL_TOKEN = dotenv.get("DBL_TOKEN");

    public static final String ENCRYPTOR_PASSWORD = dotenv.get("ENCRYPTOR_PASSWORD");
}
