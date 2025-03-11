package ca.gimmecards.cmds;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class SearchCmds {

    public static void searchCards(SlashCommandInteractionEvent event) {
        OptionMapping location = event.getOption("location");
        OptionMapping filter = event.getOption("filter");
        OptionMapping isExact = event.getOption("exact-match");
        OptionMapping keywords = event.getOption("keywords");
        //
        User user = User.findUser(event);
        SearchDisplay disp = (SearchDisplay) Display.addDisplay(user, new SearchDisplay(event));

        if(location.getAsString().equalsIgnoreCase("collection")) {
            if(user.getCardContainers().size() < 1) {
                JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "You don't have any cards yet!");

            } else {
                disp.modifyVariables(
                    "collection", 
                    filter.getAsString(),
                    isExact.getAsBoolean(),
                    keywords.getAsString()
                );
                JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
            }

        } else if(location.getAsString().equalsIgnoreCase("pokedex")) {
            disp.modifyVariables(
                "pokedex",
                filter.getAsString(),
                isExact.getAsBoolean(),
                keywords.getAsString()
            );
            JDAUtils.sendDynamicEmbed(event, disp, user, null, true);
        }
    }

    public static void viewAnyCard(SlashCommandInteractionEvent event) {
        OptionMapping cardId = event.getOption("card-id");
        //
        UserInfo ui = new UserInfo(event);

        try {
            Card card = Card.findCardById(cardId.getAsString());
            String footer = "Card ID: " + card.getCardId();

            card.displayCard(event, ui, "", footer, false);

        } catch(NullPointerException e) {
            JDAUtils.sendMessage(event, ColorConsts.RED, "❌", "Whoops, I couldn't find that card...");
        }
    }
}
