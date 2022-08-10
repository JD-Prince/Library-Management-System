
import java.time.LocalDate;


import java.util.HashMap;

import java.util.Map;

public class UserData {
    private final String pswd, Name;


    public UserData(String name,String x){
        this.pswd=x;
        this.Name=name;
    }




    public synchronized Boolean Checkpswd(String x){
        return x.equals(pswd);
    }
    public synchronized String getName(){
        return Name;
    }


    //public void Rtn(String x){
     //   Booklist.remove(x);
      //  DecreCount();
   // }
    private HashMap<String, TimeData> Booklist = new HashMap<String,TimeData>();


    public synchronized void getBooklist(){
        if(Booklist.isEmpty()){
            System.out.println("There is no pending books");
        }
        else{
            for(Map.Entry<String,TimeData> i:Booklist.entrySet()){
                String ID = i.getKey();
                TimeData obj = i.getValue();
                System.out.println(ID+" -> "+obj.getBookName()+" Borrowed on "+obj.getTime()+ " Should be returned befor "+obj.getRtn_date());
            }
        }
    }
    public synchronized void Add(String ID,String name,String d){

        TimeData obj = new TimeData(name,d);

        Booklist.put(ID,obj);
    }
    public synchronized Boolean checklist(String x){
        return Booklist.containsKey(x);
    }
    public synchronized void newBooklist(){
        Booklist.clear();
    }
    public synchronized void RTNUpdate(String BID){
        Booklist.remove(BID);
    }
    public synchronized void Rtn(String x, LocalDate d){
        int c=Booklist.get(x).getRtn_date().compareTo(d);
        if(c<0){
            System.out.println("You delayed "+c+" days to return your book. So, You would be charged "+c*5+"$ for late Submission");
            System.out.println("Your total Fine is "+getAmount()+"$");
        }
        else System.out.println("You Submitted your Book on time , Thank you");
        Booklist.remove(x);
    }
    public synchronized double getAmount(){
        double Totalfine=0.0;
        for(Map.Entry<String,TimeData> i : Booklist.entrySet()){
            TimeData obj=i.getValue();
            Totalfine=Double.sum(Totalfine,obj.getAmount());

        }
        return Totalfine;
    }

}
