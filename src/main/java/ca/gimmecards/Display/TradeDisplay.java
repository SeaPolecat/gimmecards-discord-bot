package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.ArrayList;

public class TradeDisplay extends Display {

    private ArrayList<CardContainer> offers;
    private boolean hasAccepted;
    private boolean hasRejected;
    private boolean hasCompleted;
    private boolean isFirst;

    public TradeDisplay(SlashCommandInteractionEvent event, User user, User target, boolean isFirst) {
        super(event, user, target);
        
        this.offers = new ArrayList<CardContainer>();
        this.hasAccepted = false;
        this.hasRejected = false;
        this.hasCompleted = false;
        this.isFirst = isFirst;
    }

    public ArrayList<CardContainer> getOffers() { return offers; }
    public boolean getHasAccepted() { return hasAccepted; }
    public boolean getHasRejected() { return hasRejected; }
    public boolean getHasCompleted() { return hasCompleted; }

    public void setHasAccepted(boolean hasAccepted) { this.hasAccepted = hasAccepted; }
    public void setHasRejected(boolean hasRejected) { this.hasRejected = hasRejected; }
    public void setHasCompleted(boolean hasCompleted) { this.hasCompleted = hasCompleted; }

    @Override
    public EmbedBuilder buildEmbed(User target, Server server) {
        UserInfo userInfo = getUserInfo(), targetInfo = getTargetInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";
        TradeDisplay targetDisp = (TradeDisplay) target.findDisplay(TradeDisplay.class);

        String firstPing = "", secondPing = "";
        boolean firstHasRejected = false, secondHasRejected = false;
        boolean firstHasAccepted = false, secondHasAccepted = false;
        ArrayList<CardContainer> firstOffers = null, secondOffers = null;

        if(this.isFirst) {
            firstPing = userInfo.getUserPing();
            firstHasRejected = this.hasRejected;
            firstHasAccepted = this.hasAccepted;
            firstOffers = this.offers;

            secondPing = targetInfo.getUserPing();
            secondHasRejected = targetDisp.getHasRejected();
            secondHasAccepted = targetDisp.getHasAccepted();
            secondOffers = targetDisp.getOffers();
        } else {
            firstPing = targetInfo.getUserPing();
            firstHasRejected = targetDisp.hasRejected;
            firstHasAccepted = targetDisp.hasAccepted;
            firstOffers = targetDisp.offers;

            secondPing = userInfo.getUserPing();
            secondHasRejected = this.hasRejected;
            secondHasAccepted = this.hasAccepted;
            secondOffers = this.offers;
        }

        desc += FormatUtils.formatCmd("offer (card #)") + " to offer a card\n";
        desc += FormatUtils.formatCmd("unoffer (trade #)") + " remove an offer\n";
        desc += FormatUtils.formatCmd("accept") + " / " + FormatUtils.formatCmd("unaccept") + " to lock your offer\n";
        desc += FormatUtils.formatCmd("reject") + " to end trade\n\n";

        desc += firstPing + "\n";
        desc += "┅┅\n";
        desc += "**Offer Status** ┇ ";
        if(firstHasRejected) {
            desc += "🛑 Rejected\n";
        } else if(firstHasAccepted) {
            desc += "✅ Accepted\n";
        } else {
            desc += "⏳ Deciding\n";
        }
        desc += "┅┅\n";

        for(int i = 0; i < 5; i++) {
            try {
                CardContainer cc = firstOffers.get(i);
                Card card = cc.getCard();

                desc += "`#" + (i+1) + "` "  + card.findCardTitle(false)
                + " ┇ " + card.findRarityEmote() 
                + " ┇ " + card.getSetEmote()
                + " ┇ " + card.formatXP(cc.getIsSellable())
                + " ┇ *x" + cc.getCardQuantity() + "*\n";

            } catch(IndexOutOfBoundsException e) {
                desc += "`#" + (i+1) + "`\n";
            }
        }
        desc += "\n";

        desc += secondPing + "\n";
        desc += "┅┅\n";
        desc += "**Offer Status** ┇ ";
        if(secondHasRejected) {
            desc += "🛑 Rejected\n";
        } else if(secondHasAccepted) {
            desc += "✅ Accepted\n";
        } else {
            desc += "⏳ Deciding\n";
        }
        desc += "┅┅\n";

        for(int i = 0; i < 5; i++) {
            try {
                CardContainer cc = secondOffers.get(i);
                Card card = cc.getCard();

                desc += "`#" + (i+1) + "` "  + card.findCardTitle(false)
                + " ┇ " + card.findRarityEmote() 
                + " ┇ " + card.getSetEmote()
                + " ┇ " + card.formatXP(cc.getIsSellable())
                + " ┇ *x" + cc.getCardQuantity() + "*\n";

            } catch(IndexOutOfBoundsException e) {
                desc += "`#" + (i+1) + "`\n";
            }
        }
        embed.setTitle(EmoteConsts.DITTO + " Trading Center " + EmoteConsts.DITTO);
        embed.setDescription(desc);
        embed.setColor(ColorConsts.TRADE_COLOR);
        return embed;
    }
}
