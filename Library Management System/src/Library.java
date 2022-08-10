
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Library {
    static private int ck;
    static private int choice;
    static private Boolean permit=false,Dtack=true;
    static private String UserId, AdSID,AdBID;
    static private final Record Whole_Rec=new Record();
    static private final Scanner sc=new Scanner(System.in);
    static Lock Userlk = new ReentrantLock(true);
    static Lock Booklk = new ReentrantLock(true);
    static Lock Brwlk = new ReentrantLock(true);
    static Lock Rcdlk = new ReentrantLock(true);
    //static private final ReentrantReadWriteLock l1 = new ReentrantReadWriteLock(true);
    static private LocalDateTime DT =  LocalDateTime.now();
    static private final ArrayList<String> Admin=new ArrayList<>();
    static private final ArrayList<String> Student=new ArrayList<>();
    //  Input Reference
    private synchronized static int InputCheck(String x){
        if(x.charAt(0)>='0'&&x.charAt(0)<='9') return x.charAt(0)-'0';
        else {
            System.out.println("Invalid Input Format");
            return -1;
        }
    }

    //  User Id Verification
    private synchronized static int Check(String x){
        if(Admin.contains(x)) return 1;
        else if(Student.contains(x)) return 2;
        return 0;

    }

    private synchronized static void getData(HashMap<String,UserData> pswd, HashMap<String,Book> Lib) {



        for(Map.Entry<String,Book> i:Lib.entrySet()){
            String title=i.getKey()+".txt";
            try {
                FileWriter Fw = new FileWriter(title);
                Fw.write(Integer.toString(Lib.get(i.getKey()).getTotal()));
                Fw.close();
            }catch(IOException e){e.printStackTrace();}
            Lib.get(i.getKey()).newUserlist();

        }

        for(Map.Entry<String,UserData> i : pswd.entrySet()){
            pswd.get(i.getKey()).newBooklist();
        }

        try {
            String Filename="Stdrpt.txt";
            File Rpt = new File(Filename);
            Scanner sf = new Scanner(Rpt);
            while(sf.hasNextLine()){
                String SID = sf.nextLine();
                String BID = sf.nextLine();
                String Date=sf.nextLine();
                String status=sf.nextLine();


                if(status.charAt(0)-'0'==1) {
                    pswd.get(SID).Add(BID, Lib.get(BID).getBookName(), Date);


                    Lib.get(BID).Borrow(BID,SID,pswd.get(SID).getName());
                }
                else if(status.charAt(0)-'0'==0){
                    pswd.get(SID).RTNUpdate(BID);
                    Lib.get(BID).Rtn(SID+" - "+pswd.get(SID).getName(),BID);
                }


            }


        }catch(IOException e){
            e.printStackTrace();
        }

        for(Map.Entry<String,Book> i:Lib.entrySet()){
            String ID=i.getKey();
            Book obj=i.getValue();
            obj.setCount(ID);
        }

    }
    private static void AddBook(){


        System.out.println("Enter the Name of the Book");
        sc.nextLine();
        String bkname=sc.nextLine();
        System.out.println("Enter the name of the Author");
        String Athr=sc.nextLine();
        System.out.println("Type the Description of the Book in a Single line");
        String Dscrp=sc.nextLine();
        System.out.println("Enter the name of the publisher");
        String pbs=sc.nextLine();
        System.out.println("Enter the Cost of the Books");
        String cst=sc.nextLine();
        System.out.println("Enter the Genre of the Book");
        String Genr=sc.nextLine();
        System.out.println("Enter the Book Rating");
        String Rating=sc.nextLine();
        System.out.println("Enter the Total number of Books");
        String Tot=sc.nextLine();
        Booklk.lock();
        try {
            try {
                File Flib = new File("Lib.txt");
                Scanner Slib = new Scanner(Flib);
                while (Slib.hasNextLine()) {
                    String id = Slib.nextLine();
                    if (id.charAt(0) == Genr.charAt(0)) {
                        AdBID = id;
                    }
                    Slib.nextLine();//name
                    Slib.nextLine();//ath
                    Slib.nextLine();//des
                    Slib.nextLine();//pub
                    Slib.nextLine();//cost
                    Slib.nextLine();//genr
                    Slib.nextLine();//rat
                    Slib.nextLine();//cnt
                }
                String s = String.valueOf(Genr.charAt(0));
                String tep = AdBID.replaceAll(s, "0");
                AdBID = s + (Integer.parseInt(tep) + 1);
                System.out.println("The Generated BOOK ID is " + AdBID);
                FileWriter Frl = new FileWriter(AdBID + ".txt");
                FileWriter Fwrt = new FileWriter(Flib, true);
                Fwrt.write(AdBID);
                Fwrt.write("\n");
                Fwrt.write(bkname);
                Fwrt.write("\n");
                Fwrt.write(Athr);
                Fwrt.write("\n");
                Fwrt.write(Dscrp);
                Fwrt.write("\n");
                Fwrt.write(pbs);
                Fwrt.write("\n");
                Fwrt.write(cst);
                Fwrt.write("\n");
                Fwrt.write(Genr);
                Fwrt.write("\n");
                Fwrt.write(Rating);
                Fwrt.write("\n");
                Fwrt.write(Tot);
                Fwrt.write("\n");
                Fwrt.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            Booklk.unlock();
        }


    }


    private static void AddUser(){

        String Stu="LIBS",Adm="LIBA",newID;
        System.out.println("1.Student \n2.Admin\n3.Back");
        choice=InputCheck(sc.next());
        if(choice==-1){
            AddUser();
        }
        else if(choice==1 || choice == 2){

            Userlk.lock();
            try {
                try {
                    File fu = new File("Users.txt");
                    Scanner sa = new Scanner(fu);
                    while (sa.hasNextLine()) {
                        String id = sa.nextLine();
                        if (id.contains(Stu) && choice == 1) AdSID = id;
                        else if (id.contains(Adm) && choice == 2) AdSID = id;
                    }
                    int temp;
                    if (choice == 1) {
                        temp = Integer.parseInt(AdSID.replaceAll(Stu, "0")) + 1;
                        newID = ("LIBS0" + temp);

                    } else {
                        temp = Integer.parseInt(AdSID.replaceAll(Adm, "0")) + 1;
                        newID = ("LIBA0" + temp);

                    }
                    FileWriter Fwrt = new FileWriter(fu, true);
                    Fwrt.write(newID);
                    Fwrt.write("\n");
                    System.out.println("Enter the Name ");
                    Fwrt.write(sc.next());
                    Fwrt.write("\n");
                    System.out.println("Enter the pswd");
                    Fwrt.write(sc.next());
                    Fwrt.write("\n");
                    Fwrt.close();
                    System.out.println("Generated ID for the user is " + newID);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }finally {
                Userlk.unlock();
            }

        }else System.out.println("Invalid Input try again");


    }

    // Password verification
    private synchronized static Boolean User(HashMap<String, UserData> pswd, HashMap<String, Book> Lib){

            System.out.println("\n1.Login\n2.Exit");
            getData(pswd,Lib);
            int Ac=InputCheck(sc.next());
            if(Ac==-1) {
                permit=User(pswd, Lib);
                if(permit){
                    Stage2(Lib,pswd);
                }
            } else if(Ac==2) {
                Dtack=false;
                return false;
            } else if(Ac==1) {
                System.out.print("User name : ");
                UserId = sc.next();
                ck = Check(UserId);
                if (ck != 0) {
                    System.out.print("Password : ");
                    String psd = sc.next();
                    if (!pswd.get(UserId).Checkpswd(psd)) {
                        System.out.println("ACCESS DENIED - Incorrect Password\nTry Again");
                        permit = User(pswd, Lib);
                        if (permit) {
                            Stage2(Lib, pswd);
                        }
                    }
                    else {
                        System.out.println("ACCESS GRANTED");
                        if (ck == 1) System.out.println("User : "+pswd.get(UserId).getName()+"(Admin)");
                        else System.out.println("User : "+pswd.get(UserId).getName()+"(Student)");
                        DT= LocalDateTime.now();
                        Runnable Userlogin1=()->{
                            if(ck==1) {

                                Whole_Rec.GenerateReport(DT + "\tSign in by------>" + UserId + "->" + pswd.get(UserId).getName()+"\tAdmin","0");
                            }
                            else{
                                Whole_Rec.GenerateReport(DT + "\tSign in by------>" + UserId + "->" + pswd.get(UserId).getName()+"\tStudent","0");
                            }
                        };
                        Thread UL1 = new Thread(Userlogin1);
                        UL1.start();


                        return true;
                    }
                } else {
                    System.out.println("INVALID USER\nTry Again");
                    permit=User(pswd, Lib);
                    if(permit){
                        Stage2(Lib,pswd);
                    }
                }
            }
            else {
                System.out.println("Invalid Option \nTry again");
                permit=User(pswd, Lib);
                if(permit){
                    Stage2(Lib,pswd);
                }
            }


            return false;

    }
    //Details option fuction for Admin
    private synchronized static void Details(HashMap<String, Book> Lib, HashMap<String, UserData> pswd){
        System.out.println("\n\n1.Generate Report\n2.Student Details\n3.Book details\n4.Back");
        getData(pswd,Lib);
        choice=InputCheck(sc.next());
        if(choice==-1) Details(Lib,pswd);
        else if(choice==1){

            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\tYour Report has generated Succesfully");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            Details(Lib,pswd);
        }
        else if(choice==2){
            StudentList(Lib, pswd);
        }
        else if(choice==3){
            BookList(Lib,pswd);
        }
        else if(choice==4) Stage2(Lib,pswd);
        else {
            System.out.println("Invalid Option \nTry Again");
            Details(Lib,pswd);
        }
    }
    //Booklist for Admin
    private synchronized static void BookList(HashMap<String,Book> lib, HashMap<String,UserData> pswd) {
        Catlogue(lib);
        System.out.println("\nEnter the Book ID     or enter 0 to go back   ");
        getData(pswd,lib);
        String BID=sc.next();
        if("0".equals(BID)) Stage2(lib,pswd);

        else if(lib.containsKey(BID)){
            lib.get(BID).getlist();
            System.out.println("Enter R to generate Report  or enter any other key to go back");
            String s=sc.next();
            if("R".equals(s)||"r".equals(s)){
                    Whole_Rec.GenerateReport("",BID);
                    System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\tYour Report has generated Succesfully");
                    System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    Details(lib,pswd);
            }
            else {
                Details(lib, pswd);
            }
        }
        else{
            System.out.println("Invalid Option\nTry Again");
            BookList(lib,pswd);
        }
    }

    // Catlogue
    private synchronized static void Catlogue(HashMap<String, Book> Lib){
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Book_ID-BookName-Genre\n");
        for(Map.Entry<String,Book> i : Lib.entrySet()){

            String key=i.getKey();
            Book obj=i.getValue();
            System.out.println(key+"-"+obj.getBookName()+"--Genre: "+obj.getGenre());
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
    // get book for students
    private synchronized static void Order(HashMap<String, Book> lib, HashMap<String, UserData> pswd) {
        //Lock l3 = new ReentrantLock();
        System.out.println("\nEnter the Book_ID");
        getData(pswd,lib);
        String ID=sc.next();
        if(!lib.containsKey(ID) ){
            System.out.println("INVALID ID... \n 1.Back to Main menu 2.Exit");
            choice=InputCheck(sc.next());
            if(choice == 2 || choice == -1) Order(lib, pswd);
            else if(choice==1) Stage2(lib, pswd);
            else {
                System.out.println("Invalid Option \nTry Again");
                Order(lib,pswd);
            }

        }
        else if(lib.get(ID).getCount()==0){
            System.out.println("Sorry , The Book is currently Unavailable. Try Other Books");
            Stage2(lib, pswd);
        }
        else{
            Book obj=lib.get(ID);
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\nBook name : "+obj.getBookName()+"\nby\t"+obj.getAuthor()+"\n\nDescription:\n"+obj.getDescription()+"\n\nGenre : "+obj.getGenre()+"\n\nPublished by : "+obj.getPublisher()+"\n\nRating : "+obj.getRating()+" Star\n\nNo of Books Available : "+obj.getCount()+"\n\nPrice: "+obj.getPrice());
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\n1.Confrim \n2.Back\n");
            getData(pswd,lib);
            choice=InputCheck(sc.next());
            if(choice == 2 || choice == -1) Order(lib, pswd);
            else if(lib.get(ID).getCount()==0){
                System.out.println("Sorry , The Book is currently Unavailable. Try Other Books");
                Stage2(lib, pswd);}
            else if(choice==1){
                Brwlk.lock();
                //obj.Borrow(ID,UserId,pswd.get(UserId).getName());
                try {
                    Whole_Rec.Add(UserId, ID, LocalDate.now(), 1);
                    DT = LocalDateTime.now();
                    pswd.get(UserId).Add(ID, lib.get(ID).getBookName(), String.valueOf(LocalDate.now()));

                    Whole_Rec.GenerateReport(DT + "\t" + UserId + " Borrowed the book " + obj.getBookName() + " (" + ID + ")", "0");
                }finally {
                    Brwlk.unlock();
                }

                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tYour request has been submitted");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t          thank you\n");
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                getData(pswd,lib);
                Stage2(lib, pswd);
            }
            else {
                System.out.println("Invalid Option \n Try again");
                Order(lib,pswd);
            }
        }
    }
    //Search in description
    private synchronized static void Search(HashMap<String, Book> lib, HashMap<String, UserData> pswd) {
        System.out.println("Enter the keyword       or Enter 0 to go back\n");
        String Kwd=sc.next();
        if("0".equals(Kwd)) Stage2(lib,pswd);
        else {
            for (Map.Entry<String, Book> i : lib.entrySet()) {
                String j = i.getValue().getDescription();
                if (j.contains(Kwd)) {
                    System.out.println(i.getKey() + " -> " + i.getValue().getBookName());

                }
            }
            System.out.println("1.GetBook\n2.Back to Search");
            choice = InputCheck(sc.next());
            if (choice == -1 || choice == 2) Search(lib,pswd);
            else if(choice==1){
                Order(lib, pswd);
            }

        }
    }

    // Book returning
    private synchronized static void Rtnbook(HashMap<String,Book> lib, HashMap<String,UserData> pswd) {
        System.out.println("Enter the BookId    or enter 0 to go back");
        getData(pswd,lib);
        String BID=sc.next();
        if("0".equals(BID)) Stage2(lib,pswd);
        else if(pswd.get(UserId).checklist(BID)){

            Runnable Rtbk = ()->{
                DT=LocalDateTime.now();
                LocalDate d = LocalDate.now();
                pswd.get(UserId).Rtn(BID,d);
                lib.get(BID).Rtn(UserId+" - "+pswd.get(UserId).getName(),BID);
                Whole_Rec.Add(UserId,BID,d,0);
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tYou Book has Returned Successfully");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t          Thank you\n");

                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                DT= LocalDateTime.now();
                Whole_Rec.GenerateReport(DT+"\t"+UserId+" Returned the book "+lib.get(BID).getBookName()+"("+BID+")","0");

            };

            Thread rtbk = new Thread(Rtbk);
            rtbk.start();
            try {
                rtbk.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getData(pswd,lib);



            Stage2(lib,pswd);
        }
        else{
            System.out.println("Wrong Id");
            Rtnbook(lib,pswd);
        }
    }
    //Student list for Admin
    private synchronized static void StudentList(HashMap<String, Book> Lib, HashMap<String, UserData> pswd) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for(String i:Student){
            System.out.print(i+" - ");
            System.out.println(pswd.get(i).getName());
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\nEnter the Student ID      or enter 0 to go back");
        getData(pswd,Lib);
        String SID=sc.next();
        int chk=Check(SID);
        if("0".equals(SID)) Stage2(Lib,pswd);

        else if(chk==2){
            System.out.println("Books taken by "+pswd.get(SID).getName());
            pswd.get(SID).getBooklist();
            System.out.println("Enter R to generate the Report  or enter any other key to go back");
            String s=sc.next();
            if("R".equals(s) ||"r".equals(s)){
                Whole_Rec.GenerateReport("",SID);
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\tYour Report has generated Succesfully");
                System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                Details(Lib,pswd);}
            else Details(Lib,pswd);
        }
        else{
            System.out.println("Invalid Option \nTry Again");
            StudentList(Lib,pswd);
        }
    }


    // Options after login
    private synchronized static void Stage2(HashMap<String, Book> Lib, HashMap<String, UserData> pswd){
        System.out.println("\n\n1.Catlogue");
        if(ck ==1) System.out.println("2.Details\n3.Add Books\n4.Add User\n5.Exit");
        else System.out.println("2.Return Books\n3.Exit");
        //System.out.println("3.Exit");
        choice=InputCheck(sc.next());
        if(choice==-1) Stage2(Lib,pswd);
        else if(choice==1){
            Catlogue(Lib);
            if(ck!=1) {
                System.out.println("\n1.Get Book\n2.Search\n3.Back");
                getData(pswd,Lib);
                choice = InputCheck(sc.next());
                if (choice == 3 || choice==-1) Stage2(Lib, pswd);
                else if(choice == 1){
                    Order(Lib, pswd);
                }
                else if(choice == 2){
                    Search(Lib, pswd);
                }
                else{
                    System.out.println("Invalid Option \nTry Again");
                    Stage2(Lib,pswd);
                }
            }
            else{
                Stage2(Lib,pswd);
            }
        }
        else if(choice==2&&ck==1){
            //Admin
            Details(Lib, pswd);
        }
        else if(choice==2&&ck==2){
            //Student
            Rtnbook(Lib,pswd);
        }
        else if((choice==3&&ck==2)||(choice==5&&ck==1) ){
            Runnable UserLogin2 = ()->{
                DT= LocalDateTime.now();
                Whole_Rec.GenerateReport( DT+"\t"+"Sign out by "+UserId + "->" + pswd.get(UserId).getName()+"\n","0");

            };
            Thread UL2 = new Thread(UserLogin2);
            UL2.start();
            permit=User(pswd, Lib);
            if(permit){
                Stage2(Lib,pswd);
            }

        }
        else if(choice==3&&ck==1){
            AddBook();
            Stage2(Lib,pswd);
        }
        else if(choice==4 && ck==1){
            AddUser();
            Stage2(Lib,pswd);

        }
        else{
            System.out.println("Invalid Option \nTry Again");
            Stage2(Lib,pswd);
        }
    }


    public static void main(String[] args){

        HashMap<String,UserData> pswd=new HashMap<>();
        HashMap<String, Book> Lib = new HashMap<>();

        try {
            File User = new File("Users.txt");
            Scanner su = new Scanner(User);
            while (su.hasNextLine()) {
                String SID = su.nextLine();
                String Name = su.nextLine();
                String pssd = su.nextLine();
                if (SID.charAt(3) == 'A') Admin.add(SID);
                else if (SID.charAt(3) == 'S') Student.add(SID);
                UserData Obj = new UserData(Name, pssd);
                pswd.put(SID, Obj);
            }
        }catch(IOException e){e.printStackTrace();}

        try{
            File Bk = new File("Lib.txt");
            Scanner sb = new Scanner(Bk);
            while(sb.hasNextLine()){
                String BID = sb.nextLine();
                String Bookname=sb.nextLine();
                String Athr=sb.nextLine();
                String dscrp=sb.nextLine();
                String pb = sb.nextLine();
                String cost=sb.nextLine();
                String Gen=sb.nextLine();
                String Rat=sb.nextLine();
                String cnt=sb.nextLine();
                Book obj = new Book(Bookname,Athr,dscrp,pb,cost,Gen,Double.parseDouble(Rat),Integer.parseInt(cnt));
                Lib.put(BID,obj);
            }

        }catch(IOException e){e.printStackTrace();}



        getData(pswd,Lib);


        permit=(User(pswd, Lib));
        if(permit){
            Stage2(Lib,pswd);
        }



    }


}
