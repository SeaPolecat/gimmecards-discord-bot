package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import java.util.ArrayList;

public class OpenCmds {
    
    public static void openPack(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);
        OpenDisplay disp = new OpenDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        if(!User.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " + User.findTimeLeft(user.getOpenEpoch(), 5, false));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                /*if(packName.getAsString().equalsIgnoreCase("gimme cards")) {
                    if(!user.hasPremiumRole(event)) {
                        GameManager.sendPremiumMessage(event, IColors.kofiColor, IEmotes.kofi, "Sorry, this is a premium feature!", null);

                    } else if(user.getStars() < 1) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.star + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = event.getUser().getName() + "'s exclusive card";
                        Card item = Card.pickCard(ICustomCards.customs);

                        msg += IEmotes.charmander + " ";
                        msg += GameManager.formatName(event) + " drew a card from " + IEmotes.logo + " **Gimme Cards**";
                        msg += user.updateCredits(GameManager.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);
                        
                        item.displayCard(event, ui, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                }*/
                if(set.isRareSet() || set.isPromoSet()) {
                    if(!user.hasPremiumRole(event)) {
                        GameManager.sendPremiumMessage(event, IColors.kofiColor, IEmotes.kofi, "Sorry, this is a premium feature!", null);

                    } else if(user.getStars() < 1) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.star + " **Stars**");

                    } else {
                        String msg = "";
                        String footer = ui.getUserName();
                        Card item = Card.pickCard(set.getSpecials());

                        if(set.isRareSet()) {
                            msg += IEmotes.charmander + " ";
                            footer += "'s exclusive card";
                        } else {
                            msg += IEmotes.bulbasaur + " ";
                            footer += "'s promo card";
                        }
                        msg += GameManager.formatName(event) + " drew a card from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(GameManager.randRange(8, 10), true);
                        msg += user.updateStars(-1, false);

                        user.resetOpenEpoch();
                        user.addSingleCard(item, false);
                        user.sortCards();

                        item.displayCard(event, ui, msg, footer, false);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        GameManager.sendMessage(event, IColors.red, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 1) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you're out of " + IEmotes.token + " **Tokens**");
        
                    } else {
                        String msg = "";

                        if(set.isOldSet()) {
                            msg += IEmotes.squirtle + " ";
                        } else {
                            msg += IEmotes.pikachu + " ";
                        }
                        msg += GameManager.formatName(event) + " opened " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-1, true);
                        msg += user.updateCredits(GameManager.randRange(8, 10), false);
                        
                        disp.setNewCards(user.addNewCards(set));
                        disp.setMessage(msg);

                        user.resetOpenEpoch();
                        user.sortCards();

                        GameManager.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that pack...");
            }
        }
    }

    public static void openTenPacks(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        OpenBoxDisplay disp = new OpenBoxDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping packName = event.getOption("pack-name");

        if(packName == null) { return; }

        if(!User.isCooldownDone(user.getOpenEpoch(), 5, false)) {
            GameManager.sendMessage(event, IColors.red, "⏰", "Please wait another " + User.findTimeLeft(user.getOpenEpoch(), 5, false));

        } else {
            try {
                CardSet set = CardSet.findCardSet(packName.getAsString());

                if(set.isRareSet() || set.isPromoSet()) {
                    if(!user.hasPremiumRole(event)) {
                        GameManager.sendPremiumMessage(event, IColors.kofiColor, IEmotes.kofi, "Sorry, this is a premium feature!", null);

                    } else if(user.getStars() < 10) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you need ***10*** " + IEmotes.star + " **Stars**");

                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        if(set.isRareSet()) {
                            msg += IEmotes.charmander + " ";
                        } else {
                            msg += IEmotes.bulbasaur + " ";
                        }
                        msg += GameManager.formatName(event) + " drew ***10*** cards from " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateCredits(GameManager.randRange(80, 100), true);
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
                        user.sortCards();

                        GameManager.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }

                } else {
                    if(!user.isPackUnlocked(set.getSetName())) {
                        GameManager.sendMessage(event, IColors.red, "❌", "This pack is locked!");
        
                    } else if(user.getTokens() < 10) {
                        GameManager.sendMessage(event, IColors.red, "❌", "Sorry, you need ***10*** " + IEmotes.token + " **Tokens**");
        
                    } else {
                        String msg = "";
                        ArrayList<ArrayList<Card>> newPacks = new ArrayList<ArrayList<Card>>();

                        if(set.isOldSet()) {
                            msg += IEmotes.squirtle + " ";
                        } else {
                            msg += IEmotes.pikachu + " ";
                        }
                        msg += GameManager.formatName(event) + " opened ***10*** packs of " + set.getSetEmote() + " **" + set.getSetName() + "**";
                        msg += user.updateTokens(-10, true);
                        msg += user.updateCredits(GameManager.randRange(80, 100), false);

                        for(int i = 0; i < 10; i++) {
                            newPacks.add(user.addNewCards(set));
                        }
                        disp.setNewPacks(newPacks);
                        disp.setMessage(msg);

                        user.resetOpenEpoch();
                        user.sortCards();

                        GameManager.sendDynamicEmbed(event, user, null, disp, 1);
                        try { User.saveUsers(); } catch(Exception e) {}
                    }
                }
            } catch(NullPointerException e) {
                GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that pack...");
            }
        }
    }
}
