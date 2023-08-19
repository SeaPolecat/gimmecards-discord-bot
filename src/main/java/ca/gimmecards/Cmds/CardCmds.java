package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class CardCmds {

    public static void viewBotStats(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "┅┅\n";
        desc += "**Users** ┇ " + User.users.size() + "\n";
        desc += "**Servers** ┇ " + event.getJDA().getGuilds().size() + "\n";
        desc += "┅┅\n";

        embed.setTitle(IEmotes.mascot + " Current Stats " + IEmotes.mascot);
        embed.setDescription(desc);
        embed.setColor(IColors.blue);
        GameManager.sendEmbed(event, embed);
    }

    public static void viewCardSets(MessageReceivedEvent event, String type) {
        EmbedBuilder embed = new EmbedBuilder();
        CardSet[] sets = null;
        String title = "";
        String desc = "";
        int count = 0;

        if(type.equalsIgnoreCase("new")) {
            sets = CardSet.sets;
            title = IEmotes.mascot + " **Card Sets** " + IEmotes.mascot;
        } else if(type.equalsIgnoreCase("old")) {
            sets = CardSet.oldSets;
            title = IEmotes.mascot + " **Old Card Sets** " + IEmotes.mascot;
        } else if(type.equalsIgnoreCase("rare")) {
            sets = CardSet.rareSets;
            title = IEmotes.mascot + " **Rare Card Sets** " + IEmotes.mascot;
        } else if(type.equalsIgnoreCase("promo")) {
            sets = CardSet.promoSets;
            title = IEmotes.mascot + " **Promo Card Sets** " + IEmotes.mascot;
        }
        for(int i = 0; i < sets.length; i++) {
            if(sets[i] == null) {
                desc += "**" + (i + 1) + ":** ❌ ";
            } else {
                desc += "**" + (i + 1) + ":** ✅ ";
            }
            count++;
            if(count == 8) {
                desc += "\n";
                count = 0;
            }
        }
        embed.setTitle(title);
        embed.setDescription(desc);
        embed.setColor(IColors.blue);
        GameManager.sendEmbed(event, embed);
    }

    public static void wipeCardSets(MessageReceivedEvent event) {

        for(int i = 0; i < CardSet.sets.length; i++) {
            CardSet.sets[i] = null;
        }
        for(int i = 0; i < CardSet.oldSets.length; i++) {
            CardSet.oldSets[i] = null;
        }
        for(int i = 0; i < CardSet.rareSets.length; i++) {
            CardSet.rareSets[i] = null;
        }
        for(int i = 0; i < CardSet.promoSets.length; i++) {
            CardSet.promoSets[i] = null;
        }
        GameManager.sendMessage(event, IColors.blue, "", "`Successfully wiped all card sets.`");
        try { CardSet.saveSets(); } catch(Exception e) {}
        try { CardSet.saveOldSets(); } catch(Exception e) {}
        try { CardSet.saveRareSets(); } catch(Exception e) {}
        try { CardSet.savePromoSets(); } catch(Exception e) {}
    }

    public static void refreshCardSets(MessageReceivedEvent event, String[] args) {
        try {
            int lengthChange = Integer.parseInt(args[1]);
            CardSet[] newSets = new CardSet[CardSet.sets.length + lengthChange];
    
            for(int i = 0; i < CardSet.sets.length; i++) {
                newSets[i] = CardSet.sets[i];

                if(i >= newSets.length - 1) {
                    break;
                }
            }
            CardSet.sets = newSets;
    
            GameManager.sendMessage(event, IColors.blue, "", "`Card set length changed by " + lengthChange + " unit(s).`");
            try { CardSet.saveSets(); } catch(Exception e) {}
        } catch(NumberFormatException e) {
            GameManager.sendMessage(event, IColors.red, "", "`Invalid length. Please use any integer.`");
        }
    }

    public static void countCardSet(MessageReceivedEvent event, String[] args) {
        try {
            String setCode = args[1].toUpperCase();
            ArrayList<JsonElement> promos = new ArrayList<JsonElement>();
            JsonArray rawContents;
            String msg = "";
    
            GameManager.sendMessage(event, IColors.blue, "", "`Counting the specified card set...`");
            rawContents = CardSet.crawlPokemonAPI(setCode);

            for(JsonElement j : rawContents) {
                try {
                    String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

                    if(cardRarity.equalsIgnoreCase("promo")) {
                        promos.add(j);
                    }
                } catch(NullPointerException e) {}
            }
            msg += "`...and counted!`\n"
            + "```\n";

            if(promos.size() > 0) {
                String setName = promos.get(0).getAsJsonObject().get("set").getAsJsonObject().get("name").getAsString();

                msg += setName + "\n";
            } else {
                msg += "N/A\n";
            }
            msg += "-----\n"
            + "Regulars: " + (int)(rawContents.size() - promos.size()) + "\n"
            + "Promos: " + promos.size() + "\n"
            + "-----\n"
            + "Total: " + rawContents.size() + "\n"
            + "```";

            GameManager.sendMessage(event, IColors.blue, "", msg);
        } catch(IOException e) {
            GameManager.sendMessage(event, IColors.red, "", "`Rate limit reached. Please wait for a bit.`");
        }
    }

    public static void addContents(MessageReceivedEvent event, String[] args, boolean isNew) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = "";

            if(isNew) {
                setCode = CardSet.setCodes.get(setNum);
            } else {
                setCode = CardSet.oldSetCodes.get(setNum);
            }
            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                GameManager.sendMessage(event, IColors.blue, "", "`Adding cards for set " + setNum + "...`");

                String setEmote = event.getJDA().getEmojisByName(setCode, true).get(0).getAsMention();
                CardSet contents = CardSet.findContents(setEmote, setCode);
                ArrayList<Card> commons = contents.getCommons();
                ArrayList<Card> uncommons = contents.getUncommons();
                ArrayList<Card> rares = contents.getRares();
                ArrayList<Card> shinies = contents.getShinies();

                if(isNew) {
                    CardSet.sets[setNum - 1] = contents;
                } else {
                    CardSet.oldSets[setNum - 1] = contents;
                }
                GameManager.sendMessage(event, IColors.blue, "", "`...and successful!`\n"
                + "```\n"
                + contents.getSetName() + "\n"
                + "-----\n"
                + "Commons: " + commons.size() + "\n"
                + "Uncommons: " + uncommons.size() + "\n"
                + "Rares: " + rares.size() + "\n"
                + "Shinies: " + shinies.size() + "\n"
                + "-----\n"
                + "Total: " + (int)(commons.size() + uncommons.size() + rares.size() + shinies.size())
                + "```");

                if(isNew) {
                    try { CardSet.saveSets(); } catch(Exception e) {}
                } else {
                    try { CardSet.saveOldSets(); } catch(Exception e) {}
                }
            }
        } catch(NumberFormatException | IOException e) {
            if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                GameManager.sendMessage(event, IColors.red, "", "`The specified card set does not exist.`");

            } else if(e.toString().startsWith("java.io.IOException:")) {
                GameManager.sendMessage(event, IColors.red, "", "`Rate limit reached. Please wait for a bit.`");
            }
        }
    }

    public static void addSpecialContents(MessageReceivedEvent event, String[] args, boolean isRare) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = "";

            if(isRare) {
                setCode = CardSet.rareSetCodes.get(setNum);
            } else {
                setCode = CardSet.promoSetCodes.get(setNum);
            }
            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                GameManager.sendMessage(event, IColors.blue, "", "`Adding cards for set " + setNum + "...`");

                String setEmote;
                CardSet contents;
                if(isRare) {
                    setEmote = event.getJDA().getEmojisByName(setCode, true).get(0).getAsMention();
                    contents = CardSet.findRareContents(setEmote, setCode);
                } else {
                    setEmote = IEmotes.promostar;
                    contents = CardSet.findPromoContents(setEmote, setCode);
                }
                ArrayList<Card> specs = contents.getSpecials();

                if(isRare) {
                    CardSet.rareSets[setNum - 1] = contents;
                } else {
                    CardSet.promoSets[setNum - 1] = contents;
                }
                GameManager.sendMessage(event, IColors.blue, "", "`...and successful!`\n"
                + "```\n"
                + contents.getSetName() + "\n"
                + "-----\n"
                + "Added " + specs.size() + " cards!\n"
                + "```");

                if(isRare) {
                    try { CardSet.saveRareSets(); } catch(Exception e) {}
                } else {
                    try { CardSet.savePromoSets(); } catch(Exception e) {}
                }
            }
        }  catch(NumberFormatException | IOException e) {
            if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                GameManager.sendMessage(event, IColors.red, "", "`The specified card set does not exist.`");
                
            } else if(e.toString().startsWith("java.io.IOException:")) {
                GameManager.sendMessage(event, IColors.red, "", "`Rate limit reached. Please wait for a bit.`");
            }
        }
    }
}
