package databasebuilding;

import dao.DAOStockMarket;
import model.Quote;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;

/**
 * This class is used to build a MongoDB database of IBEX35 index values.
 */
public class BuildStockMarket {

    public static void main(String args[]){
        buildStockMarket();
    }

    private static void buildStockMarket(){
        final DAOStockMarket daoStockMarket = new DAOStockMarket();

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.DAY_OF_YEAR, -30);

        try {
            Stock stock = YahooFinance.get("^IBEX", from, to, Interval.DAILY);
            System.out.println(stock.getHistory());
            for(HistoricalQuote quote : stock.getHistory()){
                Quote dailyQuote = new Quote(quote.getDate().getTime(), quote.getLow(), quote.getHigh(), quote.getOpen(), quote.getClose(), quote.getAdjClose());
                daoStockMarket.insertQuote(dailyQuote);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
