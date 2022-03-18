package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
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

        desc += "┅┅\n";
        desc += "**Users** ┇ " + User.users.size();
        desc += "**Servers** ┇ " + event.getJDA().getGuilds().size();
        desc += "┅┅\n";

        embed.setTitle(pikameme_ + " Current Stats " + pikameme_);
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

    public static void countSetContent(GuildMessageReceivedEvent event, String[] args) {
        try {
            String setCode = args[1];
            int page = 1, count = 0;
    
            while(true) {
                URL url = new URL("https://api.pokemontcg.io/v2/cards?q=set.ptcgoCode:" + setCode + "%20&page=" + page + "/key=1bfc1133-79a4-46f6-93d6-6a4dab4b7335");
                String jsonStr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine();
                JsonArray jsonArr = JsonParser.parseString(jsonStr).getAsJsonObject().getAsJsonArray("data");
    
                if(jsonArr.size() < 1) {
                    break;
                }
                for(int i = 0; i < jsonArr.size(); i++) {
                    count++;
                }
                page++;
            }
            Rest.sendMessage(event, count + "");
        } catch(IOException e) {
            Rest.sendMessage(event, "`Rate limit reached. Wait for a bit`");
        }
    }

    public static void addCardSet(GuildMessageReceivedEvent event, String[] args, String dataType) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = "";

            if(dataType.equalsIgnoreCase("new")) {
                setCode = Data.setCodes.get(setNum);
            } else if(dataType.equalsIgnoreCase("old")) {
                setCode = Data.oldSetCodes.get(setNum);
            } else if(dataType.equalsIgnoreCase("promo")) {
                setCode = Data.rareSetCodes.get(setNum);
            }
            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                String setEmote = "";
                
                Rest.sendMessage(event, "`Adding card data for set " + setNum + "...`");

                if(dataType.equalsIgnoreCase("promo")) {
                    setEmote = event.getJDA().getEmotesByName("PROMO", true).get(0).getAsMention();
                    //Rest.sendMessage(event, countPromos(setEmote, setCode) + "");
                } else {
                    setEmote = event.getJDA().getEmotesByName(setCode, true).get(0).getAsMention();
                }
                ArrayList<Data> commons = Data.findSetContents(setEmote, setCode, "common");
                ArrayList<Data> uncommons = Data.findSetContents(setEmote, setCode, "uncommon");
                ArrayList<Data> rares = Data.findSetContents(setEmote, setCode, "rare");
                ArrayList<Data> shinies = Data.findSetContents(setEmote, setCode, "shiny");
                String setName = commons.get(0).getSetName();

                if(dataType.equalsIgnoreCase("new")) {
                    Data.sets[setNum - 1] = new Data(setEmote, setName, commons, uncommons, rares, shinies);
                } else if(dataType.equalsIgnoreCase("old")) {
                    Data.oldSets[setNum - 1] = new Data(setEmote, setName, commons, uncommons, rares, shinies);
                } else if(dataType.equalsIgnoreCase("promo")) {
                    //Rest.sendMessage(event, countSetContent(setEmote, setCode) + "");
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

                if(dataType.equalsIgnoreCase("new")) {
                    try { Data.saveData(); } catch(Exception e) {}
                } else if(dataType.equalsIgnoreCase("old")) {
                    try { Data.saveOldData(); } catch(Exception e) {}
                } else if(dataType.equalsIgnoreCase("promo")) {
                    try { Data.saveRareData(); } catch(Exception e) {}
                }
            }
        } catch(NumberFormatException | IndexOutOfBoundsException | IOException e) {
            if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                Rest.sendMessage(event, "`The specified card set does not exist`");

            } else if(e.toString().startsWith("java.lang.IndexOutOfBoundsException:")) {
                Rest.sendMessage(event, "`Could not locate any cards for this set`");

            } else if(e.toString().startsWith("java.io.IOException:")) {
                Rest.sendMessage(event, "`Rate limit reached. Wait for a bit`");
            }
        }
    }
}
