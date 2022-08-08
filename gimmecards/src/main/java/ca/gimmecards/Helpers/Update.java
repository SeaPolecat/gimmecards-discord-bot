package ca.gimmecards.Helpers;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Display_.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Update implements Displays, Emotes {

    public static void updateBackpackDisplay(MessageReceivedEvent event, User user) {
        BackpackDisplay disp = new BackpackDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            JDA.editEmbed(event, user, null, disp, -1);
        }
    }

    public static void updateCardDisplay(MessageReceivedEvent event, User user) {
        CardDisplay disp = new CardDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            if(user.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(user.getCards().size() <= (disp.getPage() * 15) - 15) {
                while(user.getCards().size() <= (disp.getPage() * 15) - 15) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, null, disp, disp.getPage());
                
            } else {
                JDA.editEmbed(event, user, null, disp, disp.getPage());
            }
        }
        for(CardDisplay_ disp_ : cardDisplays_) {
            if(!disp_.getMessageId().isEmpty() 
            && disp_.getMention().getUserId().equals(user.getUserId())) {
                updateCardDisplay_(event, disp_.getUserId());
            }
        }
    }

    private static void updateCardDisplay_(MessageReceivedEvent event, String userId) {
        User user = User.findOtherUser(event, userId);
        CardDisplay_ disp = new CardDisplay_(userId).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            if(user.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(user.getCards().size() <= (disp.getPage() * 15) - 15) {
                while(user.getCards().size() <= (disp.getPage() * 15) - 15) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, null, disp, disp.getPage());
                
            } else {
                JDA.editEmbed(event, user, null, disp, disp.getPage());
            }
        }
    }

    public static void updateMinigameDisplay(MessageReceivedEvent event, User user) {
        Server server = Server.findServer(event);
        MinigameDisplay disp = new MinigameDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            JDA.editEmbed(event, user, server, disp, -1);
        }
    }

    public static void updateOldShopDisplay(MessageReceivedEvent event, User user) {
        OldShopDisplay disp = new OldShopDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            JDA.editEmbed(event, user, null, disp, disp.getPage());
        }
    }

    public static void updatePrivacyDisplay(MessageReceivedEvent event, User user, boolean delete) {
        Server server = Server.findServer(event);
        PrivacyDisplay disp = new PrivacyDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            if(delete) {
                JDA.deleteMessage(event, disp);
            } else {
                JDA.editEmbed(event, user, server, disp, -1);
            }
        }
    }

    public static void updateShopDisplay(MessageReceivedEvent event, User user) {
        ShopDisplay disp = new ShopDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            JDA.editEmbed(event, user, null, disp, disp.getPage());
        }
    }

    public static void updateTradeDisplay(MessageReceivedEvent event, User user) {
        Server server = Server.findServer(event);
        TradeDisplay disp = TradeDisplay.findTradeDisplay(user.getUserId());

        if(!disp.getMessageId().isEmpty()) {
            JDA.editEmbed(event, user, server, disp, -1);
        }
    }

    public static void updateViewDisplay(MessageReceivedEvent event, User user) {
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            if(user.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(user.getCards().size() < disp.getPage()) {
                disp.prevPage();
                JDA.editEmbed(event, user, null, disp, disp.getPage());
                
            } else {
                JDA.editEmbed(event, user, null, disp, disp.getPage());
            }
        }
        for(ViewDisplay_ disp_ : viewDisplays_) {
            if(!disp_.getMessageId().isEmpty() 
            && disp_.getMention().getUserId().equals(user.getUserId())) {
                updateViewDisplay_(event, disp_.getUserId());
            }
        }
    }

    private static void updateViewDisplay_(MessageReceivedEvent event, String userId) {
        User user = User.findOtherUser(event, userId);
        ViewDisplay_ disp = new ViewDisplay_(userId).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            if(user.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(user.getCards().size() < disp.getPage()) {
                disp.prevPage();
                JDA.editEmbed(event, user, null, disp, disp.getPage());
                
            } else {
                JDA.editEmbed(event, user, null, disp, disp.getPage());
            }
        }
    }
}
