import java.util.ArrayList;

public class Book {
    private final String BookName,Author,Description,Publisher,Price, Genre;
    private final Double Rating;
    private int Total;
    public Book(String n, String Ath,String info,String pub,String Rs,String Gen,Double Star,int count){
        this.BookName=n;
        this.Author=Ath;
        this.Description=info;
        this.Publisher=pub;
        this.Price=Rs;
        this.Genre =Gen;
        this.Rating=Star;
        this.Total=count;

    }
    private Record r = new Record();
    private int count=Total;

    public synchronized String getBookName(){
        return BookName;
    }
    public synchronized String getAuthor(){
        return Author;
    }
    public synchronized String getPublisher(){
        return Publisher;
    }
    public synchronized String getDescription(){
        return Description;
    }
    public synchronized String getPrice(){
        return Price;
    }
    public synchronized Double getRating(){
        return Rating;
    }
    public synchronized String getGenre(){
        return Genre;
    }

    public synchronized int getCount(){
        return count;
    }
    public synchronized void Borrow(String x,String Id,String name){
        //count--;
        r.BookcountDecre(x);
        setCount(x);
        AddID(Id,name);

    }

    private ArrayList<String> UserList = new ArrayList<>();
    public synchronized void newUserlist(){
        UserList.clear();
    }
    public synchronized void getlist(){
        if(Total!=count) {
            for (String i : UserList) System.out.println(i);
        }
        else{
            System.out.println("All books were Here");
        }
    }
    public synchronized void Rtn(String x,String y){
        //count ++;
        RmvID(x);
        r.BookcountIncre(y);
        setCount(y);

    }
    public synchronized void AddID(String Id,String name){
        UserList.add(Id+" - "+name);
    }
    public synchronized void RmvID(String x){
        UserList.remove(x);
    }

    public synchronized void setCount(String X){
        count=r.GetCount(X);
    }
    public synchronized int getTotal() {
        return Total;
    }

}
