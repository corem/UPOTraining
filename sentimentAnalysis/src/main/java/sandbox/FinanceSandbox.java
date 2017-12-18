package sandbox;

import model.Quote;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;

public class FinanceSandbox {

    public static void main(String args[]){

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.DAY_OF_YEAR, -30);

        try {
            Stock stock = YahooFinance.get("^IBEX", from, to, Interval.DAILY);
            System.out.println(stock.getHistory());
            for(HistoricalQuote quote : stock.getHistory()){
                Quote dailyQuote = new Quote(quote.getDate().getTime(), quote.getLow(), quote.getHigh(), quote.getOpen(), quote.getClose(), quote.getAdjClose());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }



}
