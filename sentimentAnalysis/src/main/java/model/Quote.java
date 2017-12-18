package model;

import java.math.BigDecimal;
import java.util.Date;

public class Quote {

    private String id;
    private Date date;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal adjClose;

    public Quote(String id, Date date, BigDecimal low, BigDecimal high, BigDecimal open, BigDecimal close, BigDecimal adjClose) {
        this(date, low, high, open, close, adjClose);
        this.id = id;
    }

    public Quote(Date date, BigDecimal low, BigDecimal high, BigDecimal open, BigDecimal close, BigDecimal adjClose) {
        this.date = date;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.adjClose = adjClose;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(BigDecimal adjClose) {
        this.adjClose = adjClose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
