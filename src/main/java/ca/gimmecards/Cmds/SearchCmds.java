package ca.gimmecards.Cmds;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SearchCmds {

    public static void searchCards(SlashCommandInteractionEvent event) {
        User user = User.findUser(event);
        SearchDisplay disp = new SearchDisplay(user.getUserId()).findDisplay();
        //
        OptionMapping location = event.getOption("location");
        OptionMapping filter = event.getOption("filter");
        OptionMapping isExact = event.getOption("exact-match");
        OptionMapping keywords = event.getOption("keywords");

        if(location == null || filter == null || isExact == null || keywords == null) { return; }

        if(location.getAsString().equalsIgnoreCase("collection")) {
            if(user.getCardContainers().size() < 1) {
                GameManager.sendMessage(event, IColors.red, "❌", "You don't have any cards yet!");

            } else {
                disp.modifyVariables(
                    "collection", 
                    filter.getAsString(),
                    isExact.getAsBoolean(),
                    keywords.getAsString()
                );
                GameManager.sendDynamicEmbed(event, user, null, disp, 1);
            }

        } else if(location.getAsString().equalsIgnoreCase("pokedex")) {
            disp.modifyVariables(
                "pokedex",
                filter.getAsString(),
                isExact.getAsBoolean(),
                keywords.getAsString()
            );
            GameManager.sendDynamicEmbed(event, user, null, disp, 1);
        }
    }

    public static void viewAnyCard(SlashCommandInteractionEvent event) {
        UserInfo ui = new UserInfo(event);
        //
        OptionMapping cardId = event.getOption("card-id");

        if(cardId == null) { return; }

        try {
            Card card = Card.findCardById(cardId.getAsString());
            String footer = "Card ID: " + card.getCardId();

            card.displayCard(event, ui, "", footer, false);

        } catch(NullPointerException e) {
            GameManager.sendMessage(event, IColors.red, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
