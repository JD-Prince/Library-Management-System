import java.time.LocalDate;

public class TimeData {
    private final LocalDate rtn_date;
    private final String bookName;
    private Double Amount=0.0;

    public TimeData(String n,String d){
        this.bookName=n;
        this.rtn_date=LocalDate.parse(d).plusDays(30);

    }

    public synchronized String getBookName(){
        return bookName;
    }
    public synchronized LocalDate getTime(){
        return rtn_date.minusDays(30);
    }
    public synchronized LocalDate getRtn_date(){
        return rtn_date;
    }
    public synchronized Double getAmount(){
        return Amount;
    }

}
