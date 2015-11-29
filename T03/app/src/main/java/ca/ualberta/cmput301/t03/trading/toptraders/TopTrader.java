package ca.ualberta.cmput301.t03.trading.toptraders;

/**
 * Created by rishi on 15-11-28.
 */
public class TopTrader {
    private final String userName;
    private final Integer successfulTradeCount;

    public TopTrader(String userName, Integer successfulTradeCount) {
        this.userName = userName;
        this.successfulTradeCount = successfulTradeCount;
    }

    public Integer getSuccessfulTradeCount() {
        return successfulTradeCount;
    }

    public String getUserName() {
        return userName;
    }
}
