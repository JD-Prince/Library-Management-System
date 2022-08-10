import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Record {
    static private Lock lk = new ReentrantLock(true);
    static private int r;
    synchronized void GenerateReport(String i,String x){
        lk.lock();
        try {
            try {
                FileWriter Fw = new FileWriter("Report.txt", true);

                Fw.write(i);
                Fw.write("\n");


                Fw.close();
            } catch (IOException e) {

                e.printStackTrace();

            }
            if (!"0".equals(x))
                try {
                    File F = new File("Report.txt");
                    FileWriter FR = new FileWriter("Rpt.txt");
                    Scanner sc = new Scanner(F);

                    while (sc.hasNextLine()) {
                        String s = sc.nextLine();
                        if (s.contains(x)) {
                            FileWriter fw = new FileWriter("Rpt.txt", true);
                            fw.write(s);
                            fw.write("\n");
                            fw.close();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }finally {
            lk.unlock();
        }


    }
    synchronized void BookcountDecre(String x){
        lk.lock();
        try {
            String f = x + ".txt";
            try {
                File Book = new File(f);
                Scanner sc = new Scanner(Book);
                String str = sc.next();
                FileWriter Fw = new FileWriter(f);
                int i = Integer.parseInt(str) - 1;
                Fw.write(Integer.toString(i));
                Fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            lk.unlock();
        }
    }
    synchronized int GetCount(String x){
        lk.lock();
        try {
            String f = x + ".txt";
            File Book = new File(f);
            Scanner sc;
            try {
                sc = new Scanner(Book);
                String str = sc.next();
                r = Integer.parseInt(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            lk.unlock();
        }
        return r;

    }
    synchronized void BookcountIncre(String x){
        lk.lock();
        try {
            String f = x + ".txt";
            try {
                File Book = new File(f);
                Scanner sc = new Scanner(Book);
                String str = sc.next();
                FileWriter Fw = new FileWriter(Book);
                int i = Integer.parseInt(str) + 1;
                Fw.write(Integer.toString(i));
                Fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            lk.unlock();
        }
    }
    synchronized void Add(String UserId, String BookId, LocalDate d,int status){
        lk.lock();

        try {
            FileWriter Fw=new FileWriter("Stdrpt.txt",true);
            Fw.write(UserId);
            Fw.write("\n");
            Fw.write(BookId);
            Fw.write("\n");
            Fw.write(String.valueOf(d));
            Fw.write("\n");
            Fw.write(Integer.toString(status)); //1 add book ---- 0 return book
            Fw.write("\n");
            Fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            lk.unlock();
        }
    }
}
