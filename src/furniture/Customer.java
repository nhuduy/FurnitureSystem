/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yup
 */
public class Customer extends Product{
    public String userCust;
    public String pwCust;
    public String statusCust;
    
        public void clrscr() {
        try{    
            if( System.getProperty( "os.name" ).startsWith( "Window" ) )
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("clear");
        }catch(Exception ex){
            for (int j=0; j<300; j++){
                System.out.println();
            }
        }
    }
        
    public void custMain(){    
        Scanner ip = new Scanner(System.in);
        boolean flagM = true;
        while(flagM){
            try{
                System.out.println("\nFurniture System - Customer form");
                System.out.println("----------------------------------");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter choice number: ");

                int num = ip.nextInt();
                switch (num) {
                    case 1:  // Login
                        flagM = false;
                        custLogin();
                        break;
                    case 2: // Register
                        flagM = false;
                        custRegister();
                        break;
                    case 3: // Exit
                        flagM = false;
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Sorry your choice number is invalid!\n");
                }
            }catch(InputMismatchException ex){
                clrscr();
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }
        System.out.println("Program finished!");
    }    

    public void custLogin(){
        boolean flagAc = true;
        while(flagAc){
            try {
                Scanner ip = new Scanner(System.in);
                System.out.println("\nCustomer Login Form");
                System.out.println("------------------------");
                System.out.print("Enter Username (type e to exit): ");
                String us = ip.nextLine();
                
                Statement st = conn.createStatement();
                ResultSet rs1 = st.executeQuery("select * from Customer where CustId = '" + us +"'");
                
                if(rs1.next()){
                    String id = rs1.getString("CustId");
                    String pw = rs1.getString("Pass");
                    String name = rs1.getString("CustName");
                    String statusC = rs1.getString("CustStatus");
                    
                    boolean flagPw =  false;
                    int times = 0;
                    do{
                        String pwa = null;
                        if(times==0){
                            System.out.print("Enter Password: ");
                            pwa = ip.nextLine();
                        }else if(times<5){
                            System.out.printf("Enter Password the %d times (maximum 5 times): \n", times+1);
                            pwa = ip.nextLine();
                        }else{
                            System.out.println("Sorry, please login agian!");
                        }
                        
                        if(id.trim().equals(us) && pw.trim().equals(pwa)){
                            this.userCust = us;
                            this.statusCust = statusC;
                            flagAc = false;
                            flagPw = false;
                            System.out.println("\nHi: " + name.trim());
                            if(statusC.trim().equals("Enable")){
                                custManagement();
                            }else{
                                System.out.println("Your account is not active");
                                System.out.println("Please call our Customer service for this problem - No: 0901040714");
                            }
                            
                        }else{
                            times++;
                            if(times<5){
                                System.out.println("\nIncorrect Password.");
                                flagPw = true;
                            }else{
                                flagPw = false;
                                System.out.println("\nSorry, you typed incorrect password many times. Login agian please!");
                            }
                        }
                    }while(flagPw); 
                }else{
                    if(us.equals("e")){
                        flagAc = false;
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }else{
                        System.out.println("Account does not exist!");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Can not login account");
            }
        }
    }
    
    public void signOutAcc(){
        this.userCust = null;
        this.pwCust = null;
    }
    
    public void custRegister(){
        try{
            Scanner ip = new Scanner(System.in);
            String[] ac = new String[6];
            
            boolean condition = false;
            do{
                System.out.println("Customer Register Form ");
                System.out.println("-------------------------");
                System.out.print("Username, 10 characters of maximum: ");
                ac[0] = ip.nextLine();
                while(ac[0].length()>10){
                    System.out.print("10 characters of maximum: ");
                    ac[0] = ip.nextLine();
                }

                // Check if CustId is exist
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from Customer where CustId = '" + ac[0] +"'");
                if(rs.next()){
                    String id = rs.getString("CustId");
                    if(id.trim().equals(ac[0].trim())){
                        System.out.println("Username is already created. Choose another, please!");
                        condition = true;
                    }
                }else{
                    condition = false;
                }
            }while(condition==true);
                    
            System.out.println("Password, 10 characters of maximum: ");
            ac[4] = ip.nextLine();
            while(ac[4].length()>10){
                System.out.print("Maximum 10 characters, input again: ");
                ac[4] = ip.nextLine();
            }
            
            System.out.println("Full Name: ");
            ac[1] = ip.nextLine();
            while(regexFullname(ac[1])!= true){
                System.out.print("Invalid full name: ");
                ac[1] = ip.nextLine();
            }
            
            System.out.println("Address: ");
            ac[2] = ip.nextLine();
            System.out.println("Contact No: ");
            ac[3] = ip.nextLine();
            while(regexPhone(ac[3])!= true){
                System.out.print("Invalid Contact No, input again please: ");
                ac[3] = ip.nextLine();
            }
            
            ac[5] = "Disable";
            
            String sqlInsert = "insert into Customer values(?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sqlInsert);
            
            pst.setString(1, ac[0]);
            pst.setString(2, ac[1]);
            pst.setString(3, ac[2]);
            pst.setString(4, ac[3]);
            pst.setString(5, ac[4]);
            pst.setString(6, ac[5]);
            
            int row = pst.executeUpdate();
            if(row !=0){
                System.out.println("Your account is created, wait about 5 minutes for Authorized Account!");
            }else{
                System.out.println("Can not create this account!");
            }
        }catch(SQLException ex){
            System.out.println("SQLException ex - Register not successful");
        }
    }

        public void custManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagAdm = true;
        while(flagAdm){
            try{
                System.out.println("\nCustomer Form - " + userCust);
                System.out.println("-----------------------------");
                System.out.println("1. Account Management");
                System.out.println("2. Product Management");
                System.out.println("3. Sign out");
                System.out.println("4. Exit");
                System.out.print("Enter choice number: ");
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        custAccManagement();
                        break;
                    case 2:
                        custProductManagement();
                        break;
                    case 3:
                        flagAdm = false;
                        clrscr();
                        System.out.println("Customer is signed out!");
                        signOutAcc();
                        custMain();
                        break;
                    case 4:
                        flagAdm = false;
                        System.out.println("Thanks for using our service! ");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("");
                        break;
                }
            }catch(InputMismatchException ex){
                clrscr();
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }
    }
    
    public void custAccManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagCAM = true;
        while(flagCAM){
            try{
                System.out.println("\nAccount Management Form - " + userCust);
                System.out.println("-----------------------------");
                System.out.println("1. Account information");
                System.out.println("2. Edit account");
                System.out.println("3. Back");
                System.out.println("4. Exit");
                System.out.print("Enter choice number: ");
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        custShowInfo(this.userCust);
                        break;
                    case 2:
                        custAccEdit(this.userCust);
                        break;
                    case 3:
                        flagCAM = false;
                        System.out.println("");
                        break;
                    case 4:
                        clrscr();
                        System.out.println("Thanks' for using our service!");
                        System.exit(0);
                    default:
                        System.out.println("");
                }
            }catch(InputMismatchException ex){
                clrscr();
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }            
    }
    
    public void custShowInfo(String id){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Customer where CustId = '" + id +"'");
            if(rs.next()){
                System.out.println("Customer information form");
                System.out.println("--------------------------");
                System.out.println("Username: " + rs.getString("CustId"));
                System.out.println("Full name: " + rs.getString("CustName"));
                System.out.println("Address: " + rs.getString("CustAddress"));
                System.out.println("Contact No: "  + rs.getString("ContactNo"));
                System.out.println("Account status: "  + rs.getString("CustStatus"));
            }
        } catch (SQLException ex) {
            System.out.println("Can not load information from from Database.");
        }
    }
    
    public void custAccEdit(String id){
        Scanner ip = new Scanner(System.in);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Customer where CustId = '" + id +"'");
            if(rs.next()){
                System.out.println("Edit Acount form");
                System.out.println("--------------------------");
                System.out.println("1. Edit name ");
                System.out.println("2. Edit address ");
                System.out.println("3. Edit contact No ");
                System.out.println("4. Change password ");
                System.out.println("5. Back ");
                System.out.println("6. Exit ");
                System.out.print("Enter choice number: ");
                
                int t = ip.nextInt();
                switch (t) {
                    case 1:
                        clrscr();
                        custUpdateName(id);
                        break;
                    case 2:
                        clrscr();
                        custUpdateAddr(id);
                        break;
                    case 3:
                        clrscr();
                        custUpdateContactNo(id);
                        break;
                    case 4:
                        custChangePass(id);
                        break;
                    case 5:
                        break;
                    case 6:
                        System.exit(0);
                    default:
                        System.out.println("Exit edit Customer information form");
                }
            }else{
                System.out.println("Can not load information from from Database.");
            }
        } catch (SQLException ex) {
            System.out.println("SQLException " + ex.getMessage());
        }catch(InputMismatchException ex){
            clrscr();
            System.out.println("Invalid choice - Error: InputMismatchException");
            ip.nextLine();
        }catch(Exception ex){
            System.out.println("Invalid choice - Error: Exception");
        }
    }
    
    public void custUpdateName(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new name: ");
            String temp = ip.nextLine();
            while(regexFullname(temp)!= true){
                System.out.print("Invalid full name: ");
                temp = ip.nextLine();
            }
            String sqlUpdate = "update Customer set CustName = '"+ temp +"' where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Name is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update name of Account!");
        }
    }

    public void custUpdateAddr(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new address: ");
            String temp = ip.nextLine();
            String sqlUpdate = "update Customer set CustAddress = '"+ temp +"' where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Address is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update address of Account!");
        }
    }

    public void custUpdateContactNo(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new Contact No: ");
            String temp = ip.nextLine();
            while(regexPhone(temp)!= true){
                System.out.print("Invalid Contact No, input again please: ");
                temp = ip.nextLine();
            }
            
            String sqlUpdate = "update Customer set ContactNo = '"+ temp +"' where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Contact No is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update Contact No of Account!");
        }
    }
    
    public void custChangePass(String id){
        Scanner ip = new Scanner(System.in);
        boolean flag = true;
        int t = 0;
        do{
            t++;
//            clrscr();
            try {
                Statement st = conn.createStatement();
                ResultSet rs1 = st.executeQuery("select * from Customer where CustId = '" + id +"'");
                if(rs1.next()){
                    System.out.print("\nEnter current Password: ");
                    String opw = ip.nextLine();
                    System.out.print("\nEnter new Password: ");
                    String npw = ip.nextLine();
                    while(npw.length()>10){
                        System.out.print("Maximum 10 characters, input again: ");
                        npw = ip.nextLine();
                    }
                    System.out.print("\nConfirm new Password: ");
                    String cnpw = ip.nextLine();
                
                    String pw = rs1.getString("Pass");
                    if(pw.trim().equals(opw)){
                        if(npw.equals(cnpw)){
                            String sqlUpdate = "update Customer set Pass = '"+ npw +"' where CustId = '" + id +"'";
                            Statement pst = conn.createStatement();
                            int rs = pst.executeUpdate(sqlUpdate);
                            if(rs>=1){
                                System.out.println("Password has been changed!");
                                flag = false;
                            }
                        }else{
                            System.out.println("New password and confirm password does not match!");
                        }
                    }else{
                        System.out.println("\nIncorrect current password!");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Change password is not successful - " + ex.getMessage());
            }
            if(t==3){
                flag = false;
            }
        }while(flag);
    }

//------------------------------------------- Product table -------------------------------------------
public void custProductManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagCAM = true;
        while(flagCAM){
            try{
                System.out.println("\nProduct Management Form - " + userCust);
                System.out.println("-----------------------------");
                System.out.println("1. List of Product");  // by category or by all
                System.out.println("2. Order product");
                System.out.println("3. List of Orders");
                System.out.println("4. List of Orders by detail");
                System.out.println("5. Cancel Orders");
                System.out.println("6. Back");
                System.out.println("7. Exit");
                System.out.print("Enter choice number: ");
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        productList();
                        break;
                    case 2:
                        custProductOrder();
                        break;
                    case 3:
                        orderList(userCust);
                        break;
                    case 4:
                        orderLineList(userCust);
                        break;
                    case 5:
                        System.out.print("Input No. of your Order to cancel: ");
                        int row = ip.nextInt();
                        orderDelete(userCust, row);
                        break;
                    case 6:
                        flagCAM = false;
                        System.out.println("");
                        break;
                    case 7:
                        System.out.println("Thanks for using our service!");
                        System.exit(0);
                    default:
                        System.out.println("");
                }
            }catch(InputMismatchException ex){
                clrscr();
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }            
    }    

    public void custProductOrder(){
        Scanner ip = new Scanner(System.in);
        String orderid = null;
        int orderTimes = 0;
        boolean flag = true;
        while(flag){
            orderTimes++;
            try{
                System.out.print("Enter Id of product: ");
                String temp = ip.nextLine();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from Product where ProId = '" + temp +"'");

                // Check if product is exist
                int qtyOrder = 0;
                if(rs.next()){
                    String proid = rs.getString("ProId");
                    String proname = rs.getString("ProName");
                    int qtyCurrent = rs.getInt("Quantity");
                    float proprice = rs.getFloat("UniPrice");

                    if(qtyCurrent>0){
                        System.out.printf("At current, there are %d products of %s\n", qtyCurrent, temp);
                        System.out.print("Please input the quantity of product to order: ");
                        qtyOrder = ip.nextInt();
                        while(qtyOrder>qtyCurrent){
                            System.out.println("Not enough quantity, input agian: ");
                            qtyOrder = ip.nextInt();
                        }
                        
                    }else{
                        System.out.println("Sorry, this product is out of stock");
                    }

                // Tao Order Id
                    Calendar day = Calendar.getInstance();
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    LocalDate f = LocalDate.now();

                    java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
                    int y = f.getYear();
                    Month m = f.getMonth();
                    int d = f.getDayOfMonth();
                    Long t = date.getTime();
                    
                    if(orderTimes==1){
                        orderid = userCust+y+m+d+t;
                        String orderDate = simpleDateFormat.format(date).toString();
                        
                // Insert into Order table
                        makeOrder(orderid, orderDate, userCust);
                    }
                    
                    float price = proprice*qtyOrder;

                // Insert into Orderline table
                    makeOrderLine(orderid, proid, qtyOrder, price);
                    
                    ip.nextLine();
                    System.out.print("Continue chosing others product (y/n)? ");
                    String cont = ip.nextLine();
                    
                    if(cont.equals("y")||cont.equals("Y")){
                        flag = true;
                    }else{
                        flag = false;
                    }
                }else{
                    System.out.println("Sorry, we do not have this product");
                }
            }catch(SQLException e){
                System.out.println("Can not order product - " + e.getMessage());
            }
        }
    }

            // Insert into Order table
    public void makeOrder(String orderid, String orderdate, String usercust){
        try {
            String sqlInsert = "insert into Orders values(?,?,?,?)";
            PreparedStatement pst1 = conn.prepareStatement(sqlInsert);
            
            pst1.setString(1, orderid);
            pst1.setString(2, orderdate);
            pst1.setString(3, usercust);
            pst1.setString(4, null);
            
            int row = pst1.executeUpdate();
            if(row !=0){
                System.out.println("An order has been created");
            }else{
                System.out.println("Can not create an order");
            }
        } catch (SQLException ex) {
            System.out.println("Can not make Order");
        }
    }
    
            // Insert into Orderline table
            public void makeOrderLine(String id, String proid, int qtyOrder, float price){
        try {
            String sqlInsertOrderLine = "insert into OrderLine values(?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sqlInsertOrderLine);
            
            pst.setString(1, id);
            pst.setString(2, proid);
            pst.setInt(3, qtyOrder);
            pst.setFloat(4, price);
            
            int row = pst.executeUpdate();
            if(row !=0){
                System.out.println("Product has been insert OrderLine.");
            }else{
                System.out.println("Can not insert product into OrderLine.");
            }
        } catch (SQLException ex) {
            System.out.println("Can not make Orderline");
        }
    }
    


}
