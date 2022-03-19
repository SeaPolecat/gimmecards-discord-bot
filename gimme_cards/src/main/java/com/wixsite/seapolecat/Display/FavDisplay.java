package com.wixsite.seapolecat.Display;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class FavDisplay extends Display {
    
    public static ArrayList<FavDisplay> displays = new ArrayList<FavDisplay>();
    //
    private ArrayList<Data> favCards;

    public FavDisplay(String ui) {
        super(ui);
        favCards = new ArrayList<Data>();
    }

    public ArrayList<Data> getFavCards() { return favCards; }
    //
    public void setFavCards(ArrayList<Data> f) { favCards = f; }

    public static FavDisplay findFavDisplay(String authorId) {
        for(FavDisplay f : displays) {
            if(f.getUserId().equals(authorId)) {
                return f;
            }
        }
        displays.add(0, new FavDisplay(authorId));
        return displays.get(0);
    }

    
}
