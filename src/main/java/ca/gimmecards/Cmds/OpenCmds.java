package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class OpenCmds {
    
    public static void openPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        OpenDisplay disp = new OpenDisplay();
        //
        OptionMapping packName = event.getOption("pack-name");
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.openCooldown, user.getOpenEpoch());

        user.addDisplay(disp);

        if(packName == null) { return; }

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.red, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 1) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you're out of " + EmoteConsts.star + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName() + "'s exclusive card";
                        Card item = Card.pickCard(CustomCardConsts.customs);

                        msg += EmoteConsts.charmander + " ";
                        msg += FormatUtils.formatName(event) + " drew a card from " + EmoteConsts.mascot + " **Gimme Cards**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.openCredits_min, RewardConsts.openCredits_max), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);
                        
                        item.displayCard(event, ui, msg, footer, false);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else if(set.isRareSet() || set.isPromoSet()) {
                    if(user.getStars() < 1) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you're out of " + EmoteConsts.star + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = ui.getUserName();
                        Card item = Card.pickCard(set.getSpecials());

                        if(set.isRareSet()) {
                            msg += EmoteConsts.charmander + " ";
                            footer += "'s exclusive card";
                        } else {
                            msg += EmoteConsts.bulbasaur + " ";
                            footer += "'s promo card";
                        }
                        msg += FormatUtils.formatName(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.openCredits_min, RewardConsts.openCredits_max), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);

                        item.displayCard(event, ui, msg, footer, false);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you're out of " + EmoteConsts.token + " **Tokens**");
        
                    } else {
                        String msg = "";

                        if(set.isOldSet()) {
                            msg += EmoteConsts.squirtle + " ";
                        } else {
                            msg += EmoteConsts.pikachu + " ";
                        }
                        msg += FormatUtils.formatName(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.openCredits_min, RewardConsts.openCredits_max), false);
                        
                        disp.setNewCards(user.addNewCards(set));
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that pack...");
            }
        }
    }

    public static void openTenPacks(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        OpenBoxDisplay disp = new OpenBoxDisplay();
        //
        OptionMapping packName = event.getOption("pack-name");
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.openCooldown, user.getOpenEpoch());

        user.addDisplay(disp);

        if(packName == null) { return; }

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.red, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 10) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you need ***10*** " + EmoteConsts.star + " **Stars**");

                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        msg += EmoteConsts.charmander + " ";
                        msg += FormatUtils.formatName(event) + " drew ***10*** cards from " + EmoteConsts.mascot + " **Gimme Cards**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.openboxCredits_min, RewardConsts.openboxCredits_max), true);
                        msg += user.updateStars(-10, false);

                        newPacks.add(new ArrayList<Card>());
                        for(int i = 0; i < 10; i++) {
                            Card item = Card.pickCard(CustomCardConsts.customs);

                            user.addSingleCard(item, false);
                            newPacks.get(0).add(item);
                        }
                        disp.setNewPacks(newPacks);
                        disp.setMessage(msg);

                        user.resetOpenEpoch();
                        
                        JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else if(set.isRareSet() || set.isPromoSet()) {
                    if(user.getStars() < 10) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you need ***10*** " + EmoteConsts.star + " **Stars**");

                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        if(set.isRareSet()) {
                            msg += EmoteConsts.charmander + " ";
                        } else {
                            msg += EmoteConsts.bulbasaur + " ";
                        }
                        msg += FormatUtils.formatName(event) + " drew ***10*** cards from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.openboxCredits_min, RewardConsts.openboxCredits_max), true);
                        msg += user.updateStars(-10, false);

                        newPacks.add(new ArrayList<Card>());
                        for(int i = 0; i < 10; i++) {
                            Card item = Card.pickCard(set.getSpecials());

                            user.addSingleCard(item, false);
                            newPacks.get(0).add(item);
                        }
                        disp.setNewPacks(newPacks);
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 10) {
                        JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Sorry, you need ***10*** " + EmoteConsts.token + " **Tokens**");
        
                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        if(set.isOldSet()) {
                            msg += EmoteConsts.squirtle + " ";
                        } else {
                            msg += EmoteConsts.pikachu + " ";
                        }
                        msg += FormatUtils.formatName(event) + " opened ***10*** packs of " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-10, true);
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.openboxCredits_min, RewardConsts.openboxCredits_max), false);

                        for(int i = 0; i < 10; i++) {
                            newPacks.add(user.addNewCards(set));
                        }
                        disp.setNewPacks(newPacks);
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        JDAUtils.sendDynamicEmbed(event, user, null, disp, 1);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                JDAUtils.sendMessage(event, ColorConsts.red, "❌", "Whoops, I couldn't find that pack...");
                e.printStackTrace();
            }
        }
    }
}
