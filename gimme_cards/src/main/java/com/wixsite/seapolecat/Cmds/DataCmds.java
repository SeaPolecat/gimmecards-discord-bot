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
        desc += "**Users** ┇ " + User.users.size() + "\n";
        desc += "**Servers** ┇ " + event.getJDA().getGuilds().size() + "\n";
        desc += "┅┅\n";

        embed.setTitle(pikameme_ + " Current Stats " + pikameme_);
        embed.setDescription(desc);
        embed.setColor(0xE8AE34);
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public static void viewData(GuildMessageReceivedEvent event, String dataType) {
        EmbedBuilder embed = new EmbedBuilder();
        Data[] dataList = new Data[0];
        String title = "";
        String desc = "";
        int count = 0;

        if(dataType.equalsIgnoreCase("new")) {
            dataList = Data.sets;
            title = pikameme_ + " **Card Sets** " + pikameme_;
        } else if(dataType.equalsIgnoreCase("old")) {
            dataList = Data.oldSets;
            title = pikameme_ + " **Old Card Sets** " + pikameme_;
        } else if(dataType.equalsIgnoreCase("spec")) {
            dataList = Data.specSets;
            title = pikameme_ + " **Special Card Sets** " + pikameme_;
        }
        for(int i = 0; i < dataList.length; i++) {
            if(dataList[i] == null) {
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
    
            Rest.sendMessage(event, "`Data length changed by " + lengthChange + " unit(s)`");
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
            Rest.sendMessage(event, "`Found " + count + " cards for the specified card set`");
        } catch(IOException e) {
            Rest.sendMessage(event, "`Rate limit reached. Please wait for a bit`");
        }
    }

    public static void addSetContent(GuildMessageReceivedEvent event, String[] args, boolean isNew) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = "";

            if(isNew) {
                setCode = Data.setCodes.get(setNum);
            } else {
                setCode = Data.oldSetCodes.get(setNum);
            }
            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                Rest.sendMessage(event, "`Adding card data for set " + setNum + "...`");

                String setEmote = event.getJDA().getEmotesByName(setCode, true).get(0).getAsMention();
                ArrayList<Data> commons = Data.crawlSetContent(setEmote, setCode, "common");
                ArrayList<Data> uncommons = Data.crawlSetContent(setEmote, setCode, "uncommon");
                ArrayList<Data> rares = Data.crawlSetContent(setEmote, setCode, "rare");
                ArrayList<Data> shinies = Data.crawlSetContent(setEmote, setCode, "shiny");
                String setName = commons.get(0).getSetName();

                if(isNew) {
                    Data.sets[setNum - 1] = new Data(setEmote, setName, commons, uncommons, rares, shinies);
                } else {
                    Data.oldSets[setNum - 1] = new Data(setEmote, setName, commons, uncommons, rares, shinies);
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

                if(isNew) {
                    try { Data.saveData(); } catch(Exception e) {}
                } else {
                    try { Data.saveOldData(); } catch(Exception e) {}
                }
            }
        } catch(NumberFormatException | IOException e) {
            if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                Rest.sendMessage(event, "`The specified card set does not exist`");

            } else if(e.toString().startsWith("java.io.IOException:")) {
                Rest.sendMessage(event, "`Rate limit reached. Please wait for a bit`");
            }
        }
    }

    public static void addSpecSetContent(GuildMessageReceivedEvent event, String[] args) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = Data.specSetCodes.get(setNum);

            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                Rest.sendMessage(event, "`Adding card data for set " + setNum + "...`");

                String setEmote = event.getJDA().getEmotesByName(setCode, true).get(0).getAsMention();
                ArrayList<Data> specs = Data.crawlSpecSetContent(setEmote, setCode);
                String setName = specs.get(0).getSetName();

                Data.specSets[setNum - 1] = new Data(setEmote, setName, specs);

                Rest.sendMessage(event, "`...and successful!`\n"
                + "```\n"
                + setName + "\n"
                + "-----\n"
                + "Added " + specs.size() + " cards!\n"
                + "```");

                try { Data.saveSpecData(); } catch(Exception e) {}
            }
        }  catch(NumberFormatException | IOException e) {
            if(e.toString().startsWith("java.lang.NumberFormatException:")) {
                Rest.sendMessage(event, "`The specified card set does not exist`");
                
            } else if(e.toString().startsWith("java.io.IOException:")) {
                Rest.sendMessage(event, "`Rate limit reached. Please wait for a bit`");
            }
        }
    }
}
