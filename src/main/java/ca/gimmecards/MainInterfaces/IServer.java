package ca.gimmecards.MainInterfaces;

public interface IServer {

    /**
     * setter that refreshes the server-specific card market
     */
    public void refreshMarket();

    public String formatCmd(String cmd);
}
