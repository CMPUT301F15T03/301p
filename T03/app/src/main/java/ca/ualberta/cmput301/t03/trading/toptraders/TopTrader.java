package ca.ualberta.cmput301.t03.trading.toptraders;

/**
 * The model modelling a Top Trader (or just a simple trader) containing the trader's username
 * and his successful trade count.
 * Created by rishi on 15-11-28.
 */
public class TopTrader {
    private final String userName;
    private final Integer successfulTradeCount;

    /**
     * Creates a new instance of TopTrader with a given username and his successful trade count.
     * @param userName The username of the trader.
     * @param successfulTradeCount The successful trade count of this username.
     */
    public TopTrader(String userName, Integer successfulTradeCount) {
        this.userName = userName;
        this.successfulTradeCount = successfulTradeCount;
    }

    /**
     * Returns the top trader's successful trade count.
     * @return The top trader's successful trade count.
     */
    public Integer getSuccessfulTradeCount() {
        return successfulTradeCount;
    }

    /**
     * Gets the top trader's user name.
     * @return The top trader's user name.
     */
    public String getUserName() {
        return userName;
    }
}
