/*package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EventCmds {
    
    public static void seeEvent(MessageReceivedEvent event) {
        User user = User.findUser(event);
        EventDisplay disp = EventDisplay.findEventDisplay(user.getUserId());

        Rest.sendDynamicEmbed(event, user, null, disp, -1);
    }

    public static void checkTasksComplete(MessageReceivedEvent event) {
        User user = User.findUser(event);
        UserInfo ui = new UserInfo(event);

        if(!user.getGiftClaimed() && user.getTasks() >= 25) {
            Card c;
            Data item = null, gift = null;
            String msg = "";
            String footer = event.getAuthor().getName() + "'s event gift";

            for(Data d : Data.rareSets[8].getSpecs()) {
                if(d.getCardId().equals("cel25c-24_A")) {
                    item = d;
                    break;
                }
            }
            gift = new Data(
                item.getSetEmote(),
                item.getSetName(),
                item.getCardId(),
                ui.getUserName() + "'s Pikachu",
                item.getCardRarity(),
                item.getCardImage(),
                item.getCardSupertype(),
                item.getCardSubtypes(),
                item.getCardPrice()
            );
            user.setGiftClaimed(true);
            user.getBadges().add("bday");
            c = Card.addSingleCard(user, gift, true);

            msg += GameObject.formatNick(event) + " received their event gift!\n";
            msg += "+ " + bdayBadge_ + " **1 Year Anniversary Badge**";

            State.updateBackpackDisplay(event, user);
            State.updateCardDisplay(event, user);
            State.updateEventDisplay(event, user);

            Rest.sendMessage(event, msg);
            Display.displayCard(event, user, gift, c, footer);
        }
    }
}*/
