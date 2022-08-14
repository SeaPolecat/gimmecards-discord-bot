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
        for(BackpackDisplay_ disp_ : backpackDisplays_) {
            if(!disp_.getMessageId().isEmpty()) {
                updateBackpackDisplay_(event, disp_.getUser());
            }
        }
    }

    public static void updateBackpackDisplay_(MessageReceivedEvent event, User user) {
        BackpackDisplay_ disp = new BackpackDisplay_(user.getUserId()).findDisplay();

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
            if(!disp_.getMessageId().isEmpty()) {
                updateCardDisplay_(event, disp_.getUser(), disp_.getMention());
            }
        }
    }

    public static void updateCardDisplay_(MessageReceivedEvent event, User user, User mention) {
        CardDisplay_ disp = new CardDisplay_(user.getUserId()).findDisplay();
        
        if(!disp.getMessageId().isEmpty()) {
            if(mention.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(mention.getCards().size() <= (disp.getPage() * 15) - 15) {
                while(mention.getCards().size() <= (disp.getPage() * 15) - 15) {
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
        TradeDisplay disp = new TradeDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            JDA.editEmbed(event, user, server, disp, -1);
        }
    }

    public static void updateViewDisplay(MessageReceivedEvent event, User user) {
        ViewDisplay disp = new ViewDisplay(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty() && disp.getDispType().equals("old")) {
            if(user.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(user.getCards().size() < disp.getPage()) {
                while(user.getCards().size() < disp.getPage()) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, null, disp, disp.getPage());
                
            } else {
                JDA.editEmbed(event, user, null, disp, disp.getPage());
            }
        }
        for(ViewDisplay_ disp_ : viewDisplays_) {
            if(!disp_.getMessageId().isEmpty()) {
                updateViewDisplay_(event, disp_.getUser(), disp_.getMention());
            }
        }
    }

    public static void updateViewDisplay_(MessageReceivedEvent event, User user, User mention) {
        ViewDisplay_ disp = new ViewDisplay_(user.getUserId()).findDisplay();

        if(!disp.getMessageId().isEmpty()) {
            if(mention.getCards().size() < 1) {
                disp.setPage(1);
                JDA.deleteMessage(event, disp);

            } else if(mention.getCards().size() < disp.getPage()) {
                while(mention.getCards().size() < disp.getPage()) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, null, disp, disp.getPage());
                
            } else {
                JDA.editEmbed(event, user, null, disp, disp.getPage());
            }
        }
    }
}
