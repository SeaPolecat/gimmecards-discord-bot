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
        OptionMapping packName = event.getOption("pack-name");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        OpenDisplay disp = (OpenDisplay) Display.addDisplay(user, new OpenDisplay(event));
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.OPEN_CD, user.getOpenEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 1) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you're out of " + EmoteConsts.STAR + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName() + "'s exclusive card";
                        Card item = Card.pickCard(CustomCardConsts.CUSTOM_CARDS);

                        msg += EmoteConsts.CHARMANDER + " ";
                        msg += ui.getUserPing() + " drew a card from " + EmoteConsts.MASCOT + " **Gimme Cards**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.OPEN_CREDITS_MIN, RewardConsts.OPEN_CREDITS_MAX), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);
                        
                        item.displayCard(event, ui, msg, footer, false);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else if(set.isRareSet() || set.isPromoSet()) {
                    if(user.getStars() < 1) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you're out of " + EmoteConsts.STAR + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = ui.getUserName();
                        Card item = Card.pickCard(set.getSpecials());

                        if(set.isRareSet()) {
                            msg += EmoteConsts.CHARMANDER + " ";
                            footer += "'s exclusive card";
                        } else {
                            msg += EmoteConsts.BULBASAUR + " ";
                            footer += "'s promo card";
                        }
                        msg += ui.getUserPing() + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.OPEN_CREDITS_MIN, RewardConsts.OPEN_CREDITS_MAX), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);

                        item.displayCard(event, ui, msg, footer, false);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you're out of " + EmoteConsts.TOKEN + " **Tokens**");
        
                    } else {
                        String msg = "";

                        if(set.isOldSet()) {
                            msg += EmoteConsts.SQUIRTLE + " ";
                        } else {
                            msg += EmoteConsts.PIKACHU + " ";
                        }
                        msg += ui.getUserPing() + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.OPEN_CREDITS_MIN, RewardConsts.OPEN_CREDITS_MAX), false);
                        
                        disp.setNewCards(user.addNewCards(set));
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that pack...");
            }
        }
    }

    public static void openTenPacks(SlashCommandInteractionEvent event) {
        OptionMapping packName = event.getOption("pack-name");
        //
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        OpenBoxDisplay disp = (OpenBoxDisplay) Display.addDisplay(user, new OpenBoxDisplay(event));
        int cooldownLeft = TimeUtils.findCooldownLeft(CooldownConsts.OPEN_CD, user.getOpenEpoch());

        if(cooldownLeft > 0) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "⏰", "Please wait another " 
            + FormatUtils.formatCooldown(cooldownLeft));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(user.getStars() < 10) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you need ***10*** " + EmoteConsts.STAR + " **Stars**");

                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        msg += EmoteConsts.CHARMANDER + " ";
                        msg += ui.getUserPing() + " drew ***10*** cards from " + EmoteConsts.MASCOT + " **Gimme Cards**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.OPENBOX_CREDITS_MIN, RewardConsts.OPENBOX_CREDITS_MAX), true);
                        msg += user.updateStars(-10, false);

                        newPacks.add(new ArrayList<Card>());
                        for(int i = 0; i < 10; i++) {
                            Card item = Card.pickCard(CustomCardConsts.CUSTOM_CARDS);

                            user.addSingleCard(item, false);
                            newPacks.get(0).add(item);
                        }
                        disp.setNewPacks(newPacks);
                        disp.setMessage(msg);

                        user.resetOpenEpoch();
                        
                        JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else if(set.isRareSet() || set.isPromoSet()) {
                    if(user.getStars() < 10) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you need ***10*** " + EmoteConsts.STAR + " **Stars**");

                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        if(set.isRareSet()) {
                            msg += EmoteConsts.CHARMANDER + " ";
                        } else {
                            msg += EmoteConsts.BULBASAUR + " ";
                        }
                        msg += ui.getUserPing() + " drew ***10*** cards from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.OPENBOX_CREDITS_MIN, RewardConsts.OPENBOX_CREDITS_MAX), true);
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

                        JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 10) {
                        JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Sorry, you need ***10*** " + EmoteConsts.TOKEN + " **Tokens**");
        
                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        if(set.isOldSet()) {
                            msg += EmoteConsts.SQUIRTLE + " ";
                        } else {
                            msg += EmoteConsts.PIKACHU + " ";
                        }
                        msg += ui.getUserPing() + " opened ***10*** packs of " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-10, true);
                        msg += user.updateCredits(NumberUtils.randRange(RewardConsts.OPENBOX_CREDITS_MIN, RewardConsts.OPENBOX_CREDITS_MAX), false);

                        for(int i = 0; i < 10; i++) {
                            newPacks.add(user.addNewCards(set));
                        }
                        disp.setNewPacks(newPacks);
                        disp.setMessage(msg);

                        user.resetOpenEpoch();

                        JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
                        try { DataUtils.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that pack...");
                e.printStackTrace();
            }
        }
    }
}
