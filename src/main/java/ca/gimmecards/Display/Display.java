package ca.gimmecards.Display;
import ca.gimmecards.Cmds.*;
import ca.gimmecards.Display_.*;
import ca.gimmecards.Helpers.*;
import ca.gimmecards.Interfaces.*;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;
import javax.annotation.Nonnull;

public class Display extends ListenerAdapter implements Displays, Emotes, Colors {

    private String userId;
    private String slashId;
    private Integer page;
    private Integer maxPage;

    public Display() {}

    public Display(String ui) {
        userId = ui;
        slashId = "";
        page = 1;
        maxPage = 1;
    }

    public String getUserId() { return userId; }
    public String getSlashId() { return slashId; }
    public int getPage() { return page; }
    public int getMaxPage() { return maxPage; }
    //
    public void setSlashId(String sId) { slashId = sId; }
    public void setPage(int p) { page = p; }
    public void nextPage() { page++; }
    public void prevPage() { page--; }
    public void setMaxPage(int mp) { maxPage = mp; }
    public void addMaxPage() { maxPage++; }

    public Display findDisplay() {
        return null;
    }

    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        return null;
    }

    public static void displayCard(SlashCommandInteractionEvent event, User user, UserInfo ui, Data data, String message, String footer, boolean isFav) {
        String cardTitle = UX.findCardTitle(data, isFav);
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        if(!message.isEmpty()) {
            desc += message;
            desc += "\n┅┅\n";
        }
        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXP(data, Check.isSellable(data)) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(cardTitle);
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter(footer, ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        JDA.sendEmbed(event, embed);
        embed.clear();
    }

    private void flipPage(ButtonInteractionEvent event, User user, Server server, Display disp) {
        String buttonId = event.getComponentId();
        int underscoreIndex = buttonId.indexOf("_");
        String choice = buttonId.substring(underscoreIndex + 1);

        if(disp instanceof CardDisplay) {
            if(user.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(user.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                while(user.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, server, disp, disp.getPage());
                return;
            }

        } else if(disp instanceof CardDisplay_) {
            CardDisplay_ disp_ = new CardDisplay_(user.getUserId()).findDisplay();
            User mention = disp_.getMention();

            if(mention.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(mention.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                while(mention.getCardContainers().size() <= (disp.getPage() * 15) - 15) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, server, disp, disp.getPage());
                return; 
            }

        } else if(disp instanceof ViewDisplay) {
            if(user.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(user.getCardContainers().size() < disp.getPage()) {
                while(user.getCardContainers().size() < disp.getPage()) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, server, disp, disp.getPage());
                return;
            }

        } else if(disp instanceof ViewDisplay_) {
            ViewDisplay_ disp_ = new ViewDisplay_(user.getUserId()).findDisplay();
            User mention = disp_.getMention();

            if(mention.getCardContainers().size() < 1) {
                disp.setPage(1);
                event.getMessage().delete().queue();
                return;

            } else if(mention.getCardContainers().size() < disp.getPage()) {
                while(mention.getCardContainers().size() < disp.getPage()) {
                    disp.prevPage();
                }
                JDA.editEmbed(event, user, server, disp, disp.getPage());
                return;
            }
        }

        if(choice.equals("left")) {
            if(disp.getPage() <= 1) {
                disp.setPage(disp.getMaxPage());
            } else {
                disp.prevPage();
            }

        } else if(choice.equals("right")) {
            if(disp.getPage() >= disp.getMaxPage()) {
                disp.setPage(1);
            } else {
                disp.nextPage();
            }
        }
        JDA.editEmbed(event, user, server, disp, disp.getPage());
    }

    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        if(event.getComponentId().equalsIgnoreCase("deleteaccount_yes")) {
            PrivacyCmds.confirmDeletion(event);

        } else if(event.getComponentId().equalsIgnoreCase("deleteaccount_no")) {
            PrivacyCmds.denyDeletion(event);

        } else {
            User user = User.findUser(event);
            Server server = Server.findServer(event);
            ArrayList<Display> displays = new ArrayList<Display>();

            displays.add(new BackpackDisplay(user.getUserId()).findDisplay());
            displays.add(new CardDisplay(user.getUserId()).findDisplay());
            displays.add(new HelpDisplay(user.getUserId()).findDisplay());
            displays.add(new LeaderboardDisplay(user.getUserId()).findDisplay());
            displays.add(new MarketDisplay(user.getUserId()).findDisplay());
            displays.add(new MinigameDisplay(user.getUserId()).findDisplay());
            displays.add(new OldShopDisplay(user.getUserId()).findDisplay());
            displays.add(new SearchDisplay(user.getUserId()).findDisplay());
            displays.add(new ShopDisplay(user.getUserId()).findDisplay());
            displays.add(new TradeDisplay(user.getUserId()).findDisplay());
            displays.add(new ViewDisplay(user.getUserId()).findDisplay());
            //
            displays.add(new BackpackDisplay_(user.getUserId()).findDisplay());
            displays.add(new CardDisplay_(user.getUserId()).findDisplay());
            displays.add(new ViewDisplay_(user.getUserId()).findDisplay());
            //
            String buttonId = event.getComponentId();
            int semiColIndex = buttonId.indexOf(";");
            int underscoreIndex = buttonId.indexOf("_");
            String userId = buttonId.substring(0, semiColIndex);
            String slashId = buttonId.substring(semiColIndex + 1, underscoreIndex);

            for(Display disp : displays) {
                if(slashId.equals(disp.getSlashId())) {
                    flipPage(event, user, server, disp);
                    return;
                }
            }
            if(!user.getUserId().equals(userId)) {
                event.reply("This is not your button!").setEphemeral(true).queue();
            } else {
                event.reply("This button is outdated. Please send a new command!").setEphemeral(true).queue();
            }
        }
    }
}
