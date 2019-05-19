/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture;

import java.io.Console;
import java.util.Scanner;

/**
 *
 * @author yup
 */
public class ex {
    
public static void main(String[] args) {
        try{
            Scanner ip = new Scanner(System.in);
            
            Console c = System.console();
            System.out.print("Enter password: ");
            char[] ch = c.readPassword();
//            convert char array into string
            String pass = String.valueOf(ch);
            System.out.println("Password is: " + pass);
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        
    }    
    
    
}
    
    
    
    
    
    
/*    
    
        ArrayList arr = new ArrayList();
        char [] name = {'H','e','l','l','o'};
        String empName = new String(name);
        System.out.println("empName: " + empName);

public static void main01(String[] args) {
        Date date = new Date();
        LocalDate f = LocalDate.now();
        System.out.println("f: " + f.toString());
        System.out.println("date: " + date);
        
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        //-------------------------------
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        simpleDateFormat.format(now, stringBuffer, new FieldPosition(0));
        String date1 = simpleDateFormat.format(date).toString();
        
        System.out.println("-----------------------------");
        System.out.println("simpleDateFormat: " + simpleDateFormat.format(date));
        System.out.println("simpleDateFormat.format(date).toString(): " + simpleDateFormat.format(date).toString());
        
        //------------------------------------------------
        String date2;
        date2 = simpleDateFormat.format(new Date());
        System.out.println("date2: " +date2);
        
        
        
        
        /*
        
        int y = f.getYear();
        Month m = f.getMonth();
        int d = f.getDayOfMonth();
        Long t = date.getTime();
        String id = ""+y+m+d+t;
        
        System.out.println("year: " + y);
        System.out.println("month: " + m);
        System.out.println("day: " +d);
        
        System.out.println("id: " + id);
        System.out.println("date.toString(): " + date.getTime());
        
        System.out.println("date: " + date);
    */    
        
        // 1557652147300
        //1557652181510
/*        
    }
 */   
    
