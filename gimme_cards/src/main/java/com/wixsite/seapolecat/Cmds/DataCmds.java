package com.wixsite.seapolecat.Cmds;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class DataCmds extends Cmds {

    public static void viewStats(MessageReceivedEvent event) {
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

    public static void viewData(MessageReceivedEvent event, String dataType) {
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
        } else if(dataType.equalsIgnoreCase("rare")) {
            dataList = Data.rareSets;
            title = pikameme_ + " **Rare Card Sets** " + pikameme_;
        } else if(dataType.equalsIgnoreCase("promo")) {
            dataList = Data.promoSets;
            title = pikameme_ + " **Promo Card Sets** " + pikameme_;
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

    public static void wipeData(MessageReceivedEvent event) {
        for(int i = 0; i < Data.sets.length; i++) {
            Data.sets[i] = null;
        }
        for(int i = 0; i < Data.oldSets.length; i++) {
            Data.oldSets[i] = null;
        }
        for(int i = 0; i < Data.rareSets.length; i++) {
            Data.rareSets[i] = null;
        }
        for(int i = 0; i < Data.promoSets.length; i++) {
            Data.promoSets[i] = null;
        }
        Rest.sendMessage(event, "`Successfully wiped all card data`");
        try { Data.saveData(); } catch(Exception e) {}
        try { Data.saveOldData(); } catch(Exception e) {}
        try { Data.saveRareData(); } catch(Exception e) {}
        try { Data.savePromoData(); } catch(Exception e) {}
    }

    public static void refreshData(MessageReceivedEvent event, String[] args) {
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

    public static void countSetContent(MessageReceivedEvent event, String[] args) {
        try {
            String setCode = args[1].toUpperCase();
            ArrayList<JsonElement> promos = new ArrayList<JsonElement>();
            JsonArray rawContents;
            String msg = "";
    
            Rest.sendMessage(event, "`Counting the specified card set...`");

            rawContents = Data.crawlDatabase(setCode);
            for(JsonElement j : rawContents) {
                try {
                    String cardRarity = j.getAsJsonObject().get("rarity").getAsString();

                    if(cardRarity.equalsIgnoreCase("promo")) {
                        promos.add(j);
                    }
                } catch(NullPointerException e) {
                    System.out.println(j + "\n");
                }
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

            Rest.sendMessage(event, msg);
        } catch(IOException e) {
            Rest.sendMessage(event, "`Rate limit reached. Please wait for a bit`");
        }
    }

    public static void addContents(MessageReceivedEvent event, String[] args, boolean isNew) {
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

                String setEmote = event.getJDA().getEmojisByName(setCode, true).get(0).getAsMention();
                Data contents = Data.findContents(setEmote, setCode);
                ArrayList<Data> commons = contents.getCommons();
                ArrayList<Data> uncommons = contents.getUncommons();
                ArrayList<Data> rares = contents.getRares();
                ArrayList<Data> shinies = contents.getShinies();

                if(isNew) {
                    Data.sets[setNum - 1] = contents;
                } else {
                    Data.oldSets[setNum - 1] = contents;
                }
                Rest.sendMessage(event, "`...and successful!`\n"
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

    public static void addSpecContents(MessageReceivedEvent event, String[] args, boolean isRare) {
        try {
            int setNum = Integer.parseInt(args[1]);
            String setCode = "";

            if(isRare) {
                setCode = Data.rareSetCodes.get(setNum);
            } else {
                setCode = Data.promoSetCodes.get(setNum);
            }
            if(setCode == null) {
                Integer.parseInt("$");

            } else {
                Rest.sendMessage(event, "`Adding card data for set " + setNum + "...`");

                String setEmote;
                Data contents;
                if(isRare) {
                    setEmote = event.getJDA().getEmojisByName(setCode, true).get(0).getAsMention();
                    contents = Data.findRareContents(setEmote, setCode);
                } else {
                    setEmote = promostar_;
                    contents = Data.findPromoContents(setEmote, setCode);
                }
                ArrayList<Data> specs = contents.getSpecs();

                if(isRare) {
                    Data.rareSets[setNum - 1] = contents;
                } else {
                    Data.promoSets[setNum - 1] = contents;
                }
                Rest.sendMessage(event, "`...and successful!`\n"
                + "```\n"
                + contents.getSetName() + "\n"
                + "-----\n"
                + "Added " + specs.size() + " cards!\n"
                + "```");

                if(isRare) {
                    try { Data.saveRareData(); } catch(Exception e) {}
                } else {
                    try { Data.savePromoData(); } catch(Exception e) {}
                }
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
