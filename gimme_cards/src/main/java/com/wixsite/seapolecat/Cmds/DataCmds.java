package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;
import java.net.URL;

public class DataCmds extends Cmds {

    public static void viewStats(GuildMessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        int redeem = 0, minigame = 0, claim = 0;

        for(User u : User.users) {
            if(!State.isCooldownDone(u.getRedeemEpoch(), 30, true)) {
                redeem++;
            }
            if(!State.isCooldownDone(u.getMinigameEpoch(), 60, true)) {
                minigame++;
            }
            if(!State.isCooldownDone(u.getDailyEpoch(), 720, true)) {
                claim++;
            }
        }
        desc += "*Gimme Cards* is now in **" + event.getJDA().getGuilds().size() + "** servers!\n";
        desc += "┅┅\n";
        desc += "**" + redeem + "** users just redeemed a Token\n";
        desc += "**" + minigame + "** users just played the minigame\n";
        desc += "**" + claim + "** users have voted today\n";
        desc += "┅┅\n";

        embed.setTitle(pikameme_ + " Today's Stats " + pikameme_);
        embed.setDescription(desc);
        embed.setColor(0xE8AE34);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewCardSets(GuildMessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        int count = 0;
        
        desc += pikameme_ + " **Sets** " + pikameme_ + "\n";
        for(int i = 0; i < Data.sets.length; i++) {
            if(Data.sets[i] == null) {
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
        count = 0;

        desc += "\n\n" + pikameme_ + " **Legacy Sets** " + pikameme_ + "\n";
        for(int i = 0; i < Data.oldSets.length; i++) {
            if(Data.oldSets[i] == null) {
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
        embed.setDescription(desc);
        embed.setColor(0xE8AE34);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void refreshData(GuildMessageReceivedEvent event, String[] args) {
        try {
            int lengthChange = Integer.parseInt(args[1]);
            Data[] newSets = new Data[Data.sets.length + lengthChange];
    
            for(int i = 0; i < Data.sets.length; i++) {
                newSets[i] = Data.sets[i];

                if(i >= newSets.length - 1) {
                    break;
                }
            }
            Data.sets = newSets;
    
            Rest.sendMessage(event, "`Data successfully refreshed`");
            try { Data.saveData(); } catch(Exception e) {}
        } catch(NumberFormatException e) {
            Rest.sendMessage(event, "`Invalid length. Please use any integer`");
        }
    }

    public static void addCardSet(GuildMessageReceivedEvent event, String[] args, boolean isOld) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = isOld ? Data.oldSetCodes.get(setNum) : Data.setCodes.get(setNum);

            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                Rest.sendMessage(event, "`Adding card data for set " + setNum + "...`");

                String setEmote = event.getJDA().getEmotesByName(setCode, true).get(0).getAsMention();
                ArrayList<Data> commons = findSetContents(setEmote, setCode, "common");
                ArrayList<Data> uncommons = findSetContents(setEmote, setCode, "uncommon");
                ArrayList<Data> rares = findSetContents(setEmote, setCode, "rare");
                ArrayList<Data> shinies = findSetContents(setEmote, setCode, "shiny");
                String setName = commons.get(0).getSetName();

                if(isOld) {
                    Data.oldSets[setNum - 1] = new Data(setEmote, setName, commons, uncommons, rares, shinies);
                } else {
                    Data.sets[setNum - 1] = new Data(setEmote, setName, commons, uncommons, rares, shinies);
                }
                Rest.sendMessage(event, "`...and successful!`\n"
                + "```\n"
                + setName + "\n"
                + "-----\n"
                + "Commons: " + commons.size() + "\n"
                + "Uncommons: " + uncommons.size() + "\n"
                + "Rares: " + rares.size() + "\n"
                + "Shinies: " + shinies.size() + "\n"
                + "```");
                if(isOld) {
                    try { Data.saveOldData(); } catch(Exception e) {}
                } else {
                    try { Data.saveData(); } catch(Exception e) {}
                }
            }
        } catch(NumberFormatException | IOException e) {
            if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                Rest.sendMessage(event, "`The specified card set does not exist`");

            } else if(e.toString().startsWith("java.io.IOException:")) {
                Rest.sendMessage(event, "`Rate limit reached. Wait for a bit`");
            }
        }
    }

    public static void addPromoCards(GuildMessageReceivedEvent event) {
        int count = 0;

        try {
            Rest.sendMessage(event, "`Adding the promo cards...`");

            Data.promos.clear();
            while(count < Data.setCodes.size()) {
                String setCode = Data.setCodes.get(count);
                ArrayList<Data> promos = findSetContents(event.getJDA().getEmotesByName("promo", true).get(0).getAsMention(), 
                setCode, "promo");

                for(Data data : promos) {
                    Data.promos.add(data);
                }
                System.out.println(count + 1);
                count++;
                try { Thread.sleep(5000); } catch(Exception e) {}
            }
            Rest.sendMessage(event, "`...and successful! " + Data.promos.size() + " cards in total`");
            try { Data.savePromos(); } catch(Exception e) {}
        } catch(IOException e) {
            Rest.sendMessage(event, "`Rate limit reached. Wait for a bit`");
        }
    }

    private static ArrayList<Data> findSetContents(String setEmote, String setCode, String rarity) throws IOException {
        ArrayList<Data> contents = new ArrayList<Data>();
        int page = 1;

        while(true) {
            URL url = new URL("https://api.pokemontcg.io/v2/cards?q=set.ptcgoCode:" + setCode + "%20&page=" + page + "/key=1bfc1133-79a4-46f6-93d6-6a4dab4b7335");
            String jsonStr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine();
            JsonArray jsonArr = JsonParser.parseString(jsonStr).getAsJsonObject().getAsJsonArray("data");

            if(jsonArr.size() < 1) {
                break;
            }
            for(JsonElement j : jsonArr) {
                try {
                    String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

                    if(rarity.equalsIgnoreCase("shiny")) {
                        if(cardRarity.toLowerCase().contains("rare") && cardRarity.length() > 4 
                        || cardRarity.equalsIgnoreCase("legend")) {
                            contents.add(new Data(setEmote, j));
                        }
        
                    } else if(rarity.equalsIgnoreCase("promo")) {
                        try {
                            if(cardRarity.equalsIgnoreCase(rarity)) {
                                contents.add(new Data(setEmote, j));
                            }
                        } catch(UnsupportedOperationException e) {}

                    } else {
                        if(cardRarity.equalsIgnoreCase(rarity)) {
                            contents.add(new Data(setEmote, j));
                        }
                    }
                } catch(NullPointerException e) {}
            }
            page++;
        }
        return contents;
    }
}
