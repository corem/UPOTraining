package stats;

public class SentimentCount {

    private int negative;
    private int neutral;
    private int positive;

    public SentimentCount() {
        this.negative = 0;
        this.neutral = 0;
        this.positive = 0;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public void incNegative(){
        this.negative++;
    }

    public void incNeutral(){
        this.neutral++;
    }

    public void incPositive(){
        this.positive++;
    }

    @Override
    public String toString() {
        return "SentimentCount{" +
                "negative=" + negative +
                ", neutral=" + neutral +
                ", positive=" + positive +
                '}';
    }
}
