package ca.gimmecards.Main;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class Ready extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("✨ Thanks for inviting me!");
        embed.setThumbnail("https://i.ibb.co/GcC31Sr/logo-rounded.png");
        embed.setDescription("Please use `?help` to get started");
        embed.setColor(0x408CFF);
        
        Rest.sendEmbed(event, embed);
        embed.clear();
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        for(int i = 0; i < Server.servers.size(); i++) {
            Server s = Server.servers.get(i);

            if(s.getServerId().equals(event.getGuild().getId())) {
                Server.servers.remove(i);
                try { Server.saveServers(); } catch(Exception e) {}
                break;
            }
        }
    }
    
    public void onGuildReady(GuildReadyEvent event) {

        try { Data.loadData(); } catch(Exception e) {}
        try { Data.loadOldData(); } catch(Exception e) {}
        try { Data.loadRareData(); } catch(Exception e) {}
        try { Data.loadPromoData(); } catch(Exception e) {}
        try { User.loadUsers(); } catch(Exception e) {}
        try { Server.loadServers(); } catch(Exception e) {}

        //sets
        /*
        Data.setCodes.put(1, "BLW");
        Data.setCodes.put(2, "EPO");
        Data.setCodes.put(3, "NVI");
        Data.setCodes.put(4, "NXD");
        Data.setCodes.put(5, "DEX");
        Data.setCodes.put(6, "DRX");
        Data.setCodes.put(7, "BCR");
        Data.setCodes.put(8, "PLS");
        Data.setCodes.put(9, "PLF");
        Data.setCodes.put(10, "PLB");
        Data.setCodes.put(11, "LTR");
        Data.setCodes.put(12, "XY");
        Data.setCodes.put(13, "FLF");
        Data.setCodes.put(14, "FFI");
        Data.setCodes.put(15, "PHF");
        Data.setCodes.put(16, "PRC");
        Data.setCodes.put(17, "ROS");
        Data.setCodes.put(18, "AOR");
        Data.setCodes.put(19, "BKT");
        Data.setCodes.put(20, "BKP");
        Data.setCodes.put(21, "FCO");
        Data.setCodes.put(22, "STS");
        Data.setCodes.put(23, "EVO");
        Data.setCodes.put(24, "SUM");
        Data.setCodes.put(25, "GRI");
        Data.setCodes.put(26, "BUS");
        Data.setCodes.put(27, "CIN");
        Data.setCodes.put(28, "UPR");
        Data.setCodes.put(29, "FLI");
        Data.setCodes.put(30, "CES");
        Data.setCodes.put(31, "LOT");
        Data.setCodes.put(32, "TEU");
        Data.setCodes.put(33, "UNB");
        Data.setCodes.put(34, "UNM");
        Data.setCodes.put(35, "CEC");
        Data.setCodes.put(36, "SSH");
        Data.setCodes.put(37, "RCL");
        Data.setCodes.put(38, "DAA");
        Data.setCodes.put(39, "VIV");
        Data.setCodes.put(40, "BST");
        Data.setCodes.put(41, "CRE");
        Data.setCodes.put(42, "EVS");
        Data.setCodes.put(43, "FST");
        Data.setCodes.put(44, "BRS");
        Data.setCodes.put(45, "ASR");
        */

        //oldSets (FL gone)
        /*
        Data.oldSetCodes.put(1, "BS");
        Data.oldSetCodes.put(2, "JU");
        Data.oldSetCodes.put(3, "FO");
        Data.oldSetCodes.put(4, "B2");
        Data.oldSetCodes.put(5, "TR");
        Data.oldSetCodes.put(6, "G1");
        Data.oldSetCodes.put(7, "G2");
        Data.oldSetCodes.put(8, "N1");
        Data.oldSetCodes.put(9, "N2");
        Data.oldSetCodes.put(10, "N3");
        Data.oldSetCodes.put(11, "N4");
        Data.oldSetCodes.put(12, "LC");
        Data.oldSetCodes.put(13, "EX");
        Data.oldSetCodes.put(14, "AQ");
        Data.oldSetCodes.put(15, "SK");
        Data.oldSetCodes.put(16, "RS");
        Data.oldSetCodes.put(17, "SS");
        Data.oldSetCodes.put(18, "DR");
        Data.oldSetCodes.put(19, "MA");
        Data.oldSetCodes.put(20, "HL");
        Data.oldSetCodes.put(21, "TRR");
        Data.oldSetCodes.put(22, "DX");
        Data.oldSetCodes.put(23, "EM");
        Data.oldSetCodes.put(24, "UF");
        Data.oldSetCodes.put(25, "DS");
        Data.oldSetCodes.put(26, "LM");
        Data.oldSetCodes.put(27, "HP");
        Data.oldSetCodes.put(28, "CG");
        Data.oldSetCodes.put(29, "DF");
        Data.oldSetCodes.put(30, "PK");
        Data.oldSetCodes.put(31, "DP");
        Data.oldSetCodes.put(32, "MT");
        Data.oldSetCodes.put(33, "SW");
        Data.oldSetCodes.put(34, "GE");
        Data.oldSetCodes.put(35, "MD");
        Data.oldSetCodes.put(36, "LA");
        Data.oldSetCodes.put(37, "SF");
        Data.oldSetCodes.put(38, "PL");
        Data.oldSetCodes.put(39, "RR");
        Data.oldSetCodes.put(40, "SV");
        Data.oldSetCodes.put(41, "AR");
        Data.oldSetCodes.put(42, "HS");
        Data.oldSetCodes.put(43, "UL");
        Data.oldSetCodes.put(44, "UD");
        Data.oldSetCodes.put(45, "TM");
        Data.oldSetCodes.put(46, "CL");
        */

        //rareSets
        /*
        Data.rareSetCodes.put(1, "DRV");
        Data.rareSetCodes.put(2, "DCR");
        Data.rareSetCodes.put(3, "GEN");
        Data.rareSetCodes.put(4, "SLG");
        Data.rareSetCodes.put(5, "DRM");
        Data.rareSetCodes.put(6, "HIF");
        Data.rareSetCodes.put(7, "CPA");
        Data.rareSetCodes.put(8, "SHF");
        Data.rareSetCodes.put(9, "CEL");
        */

        //promSets
        /*
        Data.promoSetCodes.put(1, "NP");
        Data.promoSetCodes.put(2, "DPP");
        Data.promoSetCodes.put(3, "HS");
        Data.promoSetCodes.put(4, "BLW");
        Data.promoSetCodes.put(5, "XY");
        Data.promoSetCodes.put(6, "SW");
        */
    }
}
