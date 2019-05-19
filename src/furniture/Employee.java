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
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author yup
 */
public class Employee extends Product{
    public String userAcc;
    public String userRole;
    
    public void frmMain() throws SQLException{    
        Scanner ip = new Scanner(System.in);
        boolean flagM = true;
        while(flagM){
            try{
                System.out.println("\nMain Form");
                System.out.println("------------------------");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter choice number: ");

                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        flagM = false;
                        loginAcc();
                        break;
                    case 2:
                        flagM = false;
                        clrscr();
                        registerAcc();
                        break;
                    case 3:
                        flagM = false;
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Sorry your choice number is invalid!\n");
                }
            }catch(InputMismatchException ex){
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }
            System.out.println("Program finished!");
    }

    public void loginAcc(){
        boolean flagAc = true;
        while(flagAc){
            try {
                Scanner ip = new Scanner(System.in);
                System.out.println("\nLogin Form");
                System.out.println("------------------------");
                System.out.print("Enter Username (type e to exit): ");
                String us = ip.nextLine();
                
                Statement st = conn.createStatement();
                ResultSet rs1 = st.executeQuery("select * from Employee where EmpId = '" + us +"'");
                
                if(rs1.next()){
                    String id = rs1.getString("EmpId");
                    String pw = rs1.getString("Pass");
                    String fn = rs1.getString("EmpName");
                    String ro = rs1.getString("EmpRole");
                    
                    boolean flagPw =  false;
                    int times = 0;
                    do{
                        String pa = null;
                        if(times==0){
                            System.out.print("Enter Password: ");
                            pa = ip.nextLine();
                        }else if(times<5){
                            System.out.printf("\n%d. Enter Password agian - %d times left: " , times, (5-times));
                            pa = ip.nextLine();
                        }else{
                            System.out.println("Sorry, please login agian!");
                        }
                        
                        if(id.trim().equals(us) && pw.trim().equals(pa)){
                            this.userAcc = us;
                            this.userRole = ro;
                            flagAc = false;
                            flagPw = false;
                            clrscr();
                            System.out.println("\nHi: " + fn.trim());
                            if(ro.trim().equals("Admin")){
                                Adm(this.userAcc, this.userRole);
                            }else{
                                Emp(this.userAcc, this.userRole);
                            }
                        }else{
                            times++;
                            System.out.println("Incorrect Password.");
                            if(times<5){
                                flagPw = true;
                            }else{
                                flagPw = false;
                                System.out.println("Login agian please!");
                            }
                        }
                    }while(flagPw); 
                }else{
                    if(us.equals("e")){
                        flagAc = false;
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
    this.userAcc = null;
    this.userRole = null;
}

    public void registerAcc(){
        try{
            Scanner ip = new Scanner(System.in);
            String[] ac = new String[6];
            
            boolean condition = false;
            do{
                System.out.println("Register Form - " + userAcc);
                System.out.println("-------------------------------");
                System.out.print("Usename, 10 characters of maximum: ");
                ac[0] = ip.nextLine();
                while(ac[0].length()>10){
                    System.out.print("10 characters of maximum: ");
                    ac[0] = ip.nextLine();
                }

                // Check if EmpId is exist
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select EmpId,EmpName from Employee where EmpId = '" + ac[0] +"'");
                if(rs.next()){
                    String id = rs.getString("EmpId");
                    if(id.trim().equals(ac[0].trim())){
                        System.out.println("Username is already created. Chose another username, please!");
                        condition = true;
                    }
                }else{
                    condition = false;
                }
            }while(condition==true);
                    
            System.out.print("Password, 10 characters of maximum: ");
            ac[4] = ip.nextLine();
            while(ac[4].length()>10){
                System.out.print("Maximum 10 characters, input again: ");
                ac[4] = ip.nextLine();
            }
            
            System.out.print("Full Name [a-z, A-Z], Space: ");
            ac[1] = ip.nextLine();
            while(regexFullname(ac[1])!= true){
                System.out.print("Invalid full name: ");
                ac[1] = ip.nextLine();
            }
            
            System.out.print("Address: ");
            ac[2] = ip.nextLine();
            
            System.out.print("Contact No: ");
            ac[3] = ip.nextLine();
            while(regexPhone(ac[3])!= true){
                System.out.print("Invalid Contact No, input again please: ");
                ac[3] = ip.nextLine();
            }
            
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select EmpId,EmpName from Employee where EmpId = '" + ac[0] +"'");
            if(rs.next()){
                if(userRole.trim().equals("Admin")){
                    System.out.print("User Role: ");
                    ac[5] = ip.nextLine();
                }else{
                    ac[5] = "Staff";
                }
            }else{
                ac[5] = "Staff";
            }
            
            String sqlInsert = "insert into Employee values(?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sqlInsert);
            
            pst.setString(1, ac[0]);
            pst.setString(2, ac[1]);
            pst.setString(3, ac[2]);
            pst.setString(4, ac[3]);
            pst.setString(5, ac[4]);
            pst.setString(6, ac[5]);
            
            int row = pst.executeUpdate();
            
            if(row !=0){
                System.out.println("Your account is created.");
            }
            if(userRole!=null){
                if(userRole.trim().equals("Admin")){
                    System.out.println("New Account has been created by Admin.");
                }else{
                    clrscr();
                    System.out.println("New Account has been created.");
                    loginAcc();
                }
            }else{
                loginAcc();
            }
        }catch(SQLException ex){
            System.out.println("SQLException ex - Register not successful");
        }
    }
    
    public void Adm(String id, String role) throws SQLException{
        Scanner ip = new Scanner(System.in);
        boolean flagAdm = true;
        while(flagAdm){
            try{
                System.out.println("\nAdmin Form - " + id);
                System.out.println("-----------------------------");
                System.out.println("1. Employee Management");
                System.out.println("2. Customer Management");
                System.out.println("3. Product Management");
                System.out.println("4. Order Management");
                System.out.println("5. Category Management");
                System.out.println("6. Sign out");
                System.out.println("7. Exit");
                System.out.print("Enter choice number: ");

                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        employeeManagement();
                        break;
                    case 2:
                        clrscr();
                        custManagement();
                        break;
                    case 3:
                        clrscr();
                        productManagement(id, role);
                        break;
                    case 4:
                        clrscr();
                        orderManagement();
                        break;
                    case 5:
                        clrscr();
                        categoryManagement(id, role);
                        break;
                    case 6:
                        signOutAcc();
                        frmMain();
                    case 7:
                        flagAdm = false;
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Program exit!");
                }
            }catch(InputMismatchException ex){
                System.out.println("Admin Form: Error - InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Admin Form: Error - Exception");
            }
        }
    }

    public void accManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagEM = true;
        while(flagEM){
            try {
                System.out.println("\nAccount Management Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. Account information");
                System.out.println("2. Edit Account information");
                System.out.println("3. Back");
                System.out.println("4. Sign out");
                System.out.println("5. Exit");
                System.out.print("Enter choice number: ");

                int num = ip.nextInt();
                switch (num) {
                case 1:
                    clrscr();
                    accInfo();
                    break;
                case 2:
                    clrscr();
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("select * from Employee where EmpId = '" + this.userAcc +"'");
                    if(rs.next()){
                        System.out.println("Edit Account information");
                        System.out.println("--------------------------");
                        System.out.println("1. Edit Name ");
                        System.out.println("2. Edit Address ");
                        System.out.println("3. Edit ContactNo ");
                        System.out.println("4. Change Password ");
                        System.out.println("5. Back");
                        System.out.print("Enter choice: ");
                    }
                    int t = ip.nextInt();
                    switch (t) {
                        case 1:
                            updateAccName(this.userAcc);
                            break;
                        case 2:
                            updateAccAddr(this.userAcc);
                            break;
                        case 3:
                            updateAccContact(this.userAcc);
                            break;
                        case 4:
                            changeAccPass(this.userAcc);
                            break;
                        case 5:
                        default:
                            System.out.println("Exit edit information");
                    }
                break;
                case 3:
                    clrscr();
                    flagEM = false;
                    break;
                case 4:
                    clrscr();
                    System.out.println("Account is signed out!");
                    signOutAcc();
                    frmMain();
                    break;
                case 5:
                    clrscr();
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    flagEM = false;
                    System.out.println("Choice number is incorrect\n");
                }
            } catch (SQLException ex) {
                System.out.println("Action on Account Management Form does not successful!");
            }
        }
    }
    public void Emp(String id, String role){
        Scanner ip = new Scanner(System.in);
        boolean flagAdm = true;
        while(flagAdm){
            try{
                System.out.println("\nEmployee Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. Account Management");
                System.out.println("2. Customer Management");
                System.out.println("3. Product Management");
                System.out.println("4. Order Management");
                System.out.println("5. Category Management");
                System.out.println("6. Sign out");
                System.out.println("7. Exit");
                System.out.print("Enter choice number: ");
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        accManagement();
                        break;
                    case 2:
                        clrscr();
                        custManagement();
                        break;
                    case 3:
                        clrscr();
                        productManagement(id, role);
                        break;
                    case 4:
                        clrscr();
                        orderManagement();
                        break;
                    case 5:
                        clrscr();
                        categoryManagement(id, role);
                        break;
                    case 6:
                        signOutAcc();
                        frmMain();
                        break;
                    case 7:
                        flagAdm = false;
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Program exit!");
                }
            }catch(InputMismatchException ex){
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch (SQLException ex) {
                System.out.println("Employee Form - Error: SQLException");
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }
    }
    
    public void employeeManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagEM = true;
        while(flagEM){
            try {
                System.out.println("\nEmployee Management Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. List of Employee");
                System.out.println("2. Add new Employee");
                System.out.println("3. Edit Employee information");
                System.out.println("4. Delete Employee");
                System.out.println("5. Back");
                System.out.println("6. Sign out");
                System.out.println("7. Exit");
                System.out.print("Enter choice number: ");
                
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        empList();
                        break;
                    case 2:
                        clrscr();
                        registerAcc();
                        break;
                    case 3:
                        clrscr();
                        ip.nextLine();
                        System.out.print("Enter Username to edit: ");
                        String u = ip.nextLine();
                        
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery("select * from Employee where EmpId = '" + u +"'");
                        if(rs.next()){
                            System.out.println("Edit Employee information");
                            System.out.println("--------------------------");
                            System.out.println("1. Edit name: " + rs.getString("EmpName"));
                            System.out.println("2. Edit Address: " + rs.getString("EmpAddress"));
                            System.out.println("3. Edit ContactNo: "  + rs.getString("ContactNo"));
                            System.out.println("4. Edit Password: "  + rs.getString("Pass"));
                            System.out.println("5. Edit Role: "  + rs.getString("EmpRole"));
                            System.out.println("6. Back");
                            System.out.print("Enter choice: ");
                        }
                        int t = ip.nextInt();
                        switch (t) {
                            case 1:
                                updateAccName(u);
                                break;
                            case 2:
                                updateAccAddr(u);
                                break;
                            case 3:
                                updateAccContact(u);
                                break;
                            case 4:
                                updateAccPass(u);  // method for admin
                                break;
                            case 5:
                                updateEmpRole(u);
                                break;
                            case 6:
                            default:
                                System.out.println("Exit edit information");
                        }
                        break;
                    case 4:
                        ip.nextLine();
                        System.out.print("Enter Username to delete: ");
                        String id = ip.nextLine();
                        deleteAcc(id);
                        break;
                    case 5:
                        flagEM = false;
                        break;
                    case 6:
                        flagEM = false;
                        signOutAcc();
                        frmMain();
                        break;
                    case 7:
                        System.out.println("Program exit!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Thank you!\n");
                }
            }catch(InputMismatchException ex){
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch (SQLException ex) {
                System.out.println("Customer Management Form - Error: SQLException");
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }
    }

public void empList(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Employee");
            System.out.println("Employees list");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("No.\tUsername\tFull Name\t\t\tAddress\t\t\t\t\t\t\t  ContactNo\tPass\tEmpRole");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
            
            int count = 0;
            while(rs.next()){
                System.out.println(++count
                        + "\t" + rs.getString("EmpId")
                        + "\t" + rs.getString("EmpName")
                        + "\t" + rs.getString("EmpAddress")
                        + "" + rs.getString("ContactNo")
                        + "\t" + rs.getString("Pass")
                        + "" + rs.getString("EmpRole"));
            }
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from Employee!");
        }
    }    
    
    public void updateAccName(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new name: ");
            String temp = ip.nextLine();
            while(temp.length()>10){
                    System.out.print("10 characters of maximum: ");
                    temp = ip.nextLine();
                }
            
            String sqlUpdate = "update Employee set EmpName = '"+ temp +"' where EmpId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                clrscr();
                System.out.println("Name is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update name of Account!");
        }
    }
    
public void updateAccAddr(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new address: ");
            String temp = ip.nextLine();
            String sqlUpdate = "update Employee set EmpAddress = '"+ temp +"' where EmpId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Address is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update address!");
        }
    }

public void updateAccContact(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new ContactNo: ");
            String temp = ip.nextLine();
            while(regexPhone(temp)!= true){
                System.out.print("Invalid Contact No, input again please: ");
                temp = ip.nextLine();
            }
            
            String sqlUpdate = "update Employee set ContactNo = '"+ temp +"' where EmpId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("ContactNo is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Sorry, can not update ContactNo!");
        }
    }

public void updateAccPass(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new password: ");
            String temp = ip.nextLine();
            while(temp.length()>10){
                System.out.print("Maximum 10 characters, input again: ");
                temp = ip.nextLine();
            }
            String sqlUpdate = "update Employee set Pass = '"+ temp +"' where EmpId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Password is changed!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not change Password!");
        }
    }

public void updateEmpRole(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new role: ");
            String temp = ip.nextLine();
            String sqlUpdate = "update Employee set EmpRole = '"+ temp +"' where EmpId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("EmpRole is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update EmpRole!");
        }
    }

    public void deleteAcc(String id){
        try {
            String sqlDelete = "delete from Employee where EmpId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlDelete);
            if(rs>=1){
                System.out.println("Delete successful!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not delete Account!");
        }
    }

    // ==============Employee========================
    
    public void accInfo(){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Employee where EmpId = '" + this.userAcc +"'");
            if(rs.next()){
                System.out.println("Account information");
                System.out.println("--------------------------");
                System.out.println("Name: " + rs.getString("EmpName"));
                System.out.println("Address: " + rs.getString("EmpAddress"));
                System.out.println("ContactNo: "  + rs.getString("ContactNo"));
                System.out.println("Password: "  + rs.getString("Pass"));
                System.out.println("Role: "  + rs.getString("EmpRole"));
            }
        } catch (SQLException ex) {
            System.out.println("Can not load information from Database");
        }
    }

    public void changeAccPass(String id){
        boolean flag = true;
        int t = 0;
        do{
            t++;
            try {
                Scanner ip = new Scanner(System.in);
                System.out.print("Enter current Password: ");
                String opw = ip.nextLine();
                System.out.print("Enter new Password: ");
                String npw = ip.nextLine();
                while(npw.length()>10){
                    System.out.print("Maximum 10 characters, input new Password again: ");
                    npw = ip.nextLine();
                }
                
                System.out.print("Confirm new Password: ");
                String cnpw = ip.nextLine();

                Statement st = conn.createStatement();
                ResultSet rs1 = st.executeQuery("select EmpId,EmpName,Pass,EmpRole from Employee where EmpId = '" + id +"'");
                if(rs1.next()){
                    String pw = rs1.getString("Pass");
                    if(pw.trim().equals(opw)){
                        if(npw.equals(cnpw)){
                            String sqlUpdate = "update Employee set Pass = '"+ npw +"' where EmpId = '" + id +"'";
                            Statement pst = conn.createStatement();
                            int rs = pst.executeUpdate(sqlUpdate);
                            if(rs>=1){
                                System.out.println("Password has been changed!");
                                flag = false;
                            }
                        }else{
                            System.out.println("\nNew password and confirm password does not match!");
                        }
                    }else{
                        System.out.println("\nIncorrect current password!"); 
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Change password is not successful!");
            }
            if(t==3){
                flag = false;
            }
        }while(flag);
    }



    
    
    //--------------------------------- Customer table ------------------------------------
    public void custManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagEM = true;
        while(flagEM){
            try {
                System.out.println("\nCustomer Management Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. List of Customer");
                System.out.println("2. Add new Customer");
                System.out.println("3. Edit Customer information");
                System.out.println("4. Delete Customer");
                System.out.println("5. Back");
                System.out.println("6. Sign out");
                System.out.println("7. Exit");
                System.out.print("Enter choice number: ");
                
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        custList();
                        break;
                    case 2:
                        clrscr();
                        custAdd();
                        break;
                    case 3:
                        clrscr();
                        ip.nextLine();
                        System.out.print("Enter Username of Customer to edit: ");
                        String u = ip.nextLine();
                        
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery("select * from Customer where CustId = '" + u +"'");
                        
                        if(rs.next()){
                            System.out.println("Edit Customer information");
                            System.out.println("--------------------------");
                            System.out.println("1. Edit name: " + rs.getString("CustName"));
                            System.out.println("2. Edit Address: " + rs.getString("CustAddress"));
                            System.out.println("3. Edit ContactNo: "  + rs.getString("ContactNo"));
                            System.out.println("4. Edit Password: "  + rs.getString("Pass"));
                            System.out.println("5. Edit Status: "  + rs.getString("CustStatus"));
                            System.out.println("6. Back");
                            System.out.print("Enter choice: ");
                            
                            int t = ip.nextInt();
                            switch (t) {
                                case 1:
                                    clrscr();
                                    custUpdateName(u);
                                    break;
                                case 2:
                                    clrscr();
                                    custUpdateAddr(u);
                                    break;
                                case 3:
                                    clrscr();
                                    custUpdateContactNo(u);
                                    break;
                                case 4:
                                    clrscr();
                                    custUpdatePass(u);
                                    break;
                                case 5:
                                    clrscr();
                                    custUpdateStatus(u);
                                    break;
                                case 6:
                                default:
                                    System.out.println("Exit edit Customer information form");
                            }
                        }else{
                            System.out.println("Customer does not exist!");
                        }
                        break;
                    case 4:
                        ip.nextLine();
                        System.out.print("Enter Username to delete: ");
                        String u4 = ip.nextLine();
                        
                        Statement st4 = conn.createStatement();
                        ResultSet rs1 = st4.executeQuery("select * from Customer where CustId = '" + u4 +"'");
                        if(rs1.next()){
                            custDelete(u4);
                        }else{
                            System.out.println("\nCustomer does not exist!");
                        }
                        break;
                    case 5:
                        flagEM = false;
                        break;
                    case 6:
                        clrscr();
                        flagEM = false;
                        signOutAcc();
                        frmMain();
                    case 7:
                        System.out.println(this.userRole.trim() + " signed out!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Thank you!\n");
                }
            }catch(InputMismatchException ex){
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch (SQLException ex) {
                System.out.println("Customer Management Form - Error: SQLException");
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }
        }
    }
    
public void custList(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Customer");
            System.out.println("Customers list");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("No.\tUsername\tFull Name\t\t\tAddress\t\t\t\t\t\t\t  ContactNo\tPass\tCustStatus");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
            
            int count = 0;
            while(rs.next()){
                System.out.println(++count
                        + "\t" + rs.getString("CustId")
                        + "\t" + rs.getString("CustName")
                        + "\t" + rs.getString("CustAddress")
                        + "" + rs.getString("ContactNo")
                        + "\t" + rs.getString("Pass")
                        + "" + rs.getString("CustStatus"));
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from Customer!");
        }
    }    

    public void custAdd(){
            try{
            Scanner ip = new Scanner(System.in);
            String[] ac = new String[6];
            
            boolean condition = false;
            do{
                System.out.println("Create new Customer Form - " + userAcc);
                System.out.println("-----------------------------------------");
                System.out.print("CustId, 10 characters of maximum: ");
                ac[0] = ip.nextLine();
                while(ac[0].length()>10){
                    System.out.print("10 characters of maximum: ");
                    ac[0] = ip.nextLine();
                }

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from Customer where CustId = '" + ac[0] +"'");
                if(rs.next()){
                    String id = rs.getString("CustId");
                    if(id.trim().equals(ac[0].trim())){
                        System.out.println("CustId is already created. Chose another ID, please!");
                        condition = true;
                    }
                }else{
                    condition = false;
                }
            }while(condition==true);
                    
            System.out.print("Password, 10 characters of maximum: ");
            ac[4] = ip.nextLine();
            while(ac[4].length()>10){
                System.out.print("Maximum 10 characters, input again: ");
                ac[4] = ip.nextLine();
            }
            System.out.print("Full Name: ");
            ac[1] = ip.nextLine();
            while(regexFullname(ac[1])!= true){
                System.out.print("Invalid full name: ");
                ac[1] = ip.nextLine();
            }
            
            System.out.print("Address: ");
            ac[2] = ip.nextLine();
            System.out.print("Contact No: ");
            ac[3] = ip.nextLine();
            while(regexPhone(ac[3])!= true){
                System.out.print("Invalid Contact No, input again please: ");
                ac[3] = ip.nextLine();
            }
            
            System.out.print("Status (Enable/Disable): ");
            ac[5] = ip.nextLine();
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
                System.out.println("Customer account is created.");
            }else{
                System.out.println("Customer account can not be created.");
            }
        }catch(SQLException ex){
            System.out.println("SQLException ex - Create new Customer is not successful.");
        }
    }

    public void custUpdateName(String id){
            try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new full name of Customer: ");
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
            System.out.println("Can not update name of Customer!");
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
            System.out.println("Can not update address of Customer!");
        }
    }

    public void custUpdateContactNo(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new ContactNo: ");
            String temp = ip.nextLine();
            while(regexPhone(temp)!= true){
                System.out.print("Invalid Contact No, input again please: ");
                temp = ip.nextLine();
            }
            
            String sqlUpdate = "update Customer set ContactNo = '"+ temp +"' where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("ContactNo is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update ContactNo of Customer!");
        }
    }

    public void custUpdatePass(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new password: ");
            String temp = ip.nextLine();
            while(temp.length()>10){
                System.out.print("Maximum 10 characters, input again: ");
                temp = ip.nextLine();
            }
            
            String sqlUpdate = "update Customer set Pass = '"+ temp +"' where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Password of Customer is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update Password of Customer!");
        }
    }

    public void custUpdateStatus(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Set Customer status to (Enable/Disable): ");
            String temp = ip.nextLine();
            String sqlUpdate = "update Customer set CustStatus = '"+ temp +"' where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                System.out.println("Customer status has been changed to " + temp);
            }
        } catch (SQLException ex) {
            System.out.println("Can not update status for Customer!");
        }
    }

    public void custDelete(String id){
        try {
            String sqlDelete = "delete from Customer where CustId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlDelete);
            if(rs>=1){
                System.out.println("\nDelete successful!");
            }
        } catch (SQLException ex) {
            System.out.println("\nCan not delete Customer!");
        }
    }    




    // --------------------------- Product table ---------------------------
    public void productManagement(String userAcc, String userRole){
        Scanner ip = new Scanner(System.in);
        boolean flag = true;
        while(flag){
            try{
                System.out.println("\nProduct Management Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. List of Product");
                System.out.println("2. Add new product");
                System.out.println("3. Edit product information");
                System.out.println("4. Delete product");
                System.out.println("5. Back");
                System.out.println("6. Sign out");
                System.out.println("7. Exit");
                System.out.print("Enter choice number: ");

                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        productList();
                        break;
                    case 2:
                        clrscr();
                        productAdd(userAcc, userRole);
                        break;
                    case 3:
                        clrscr();
                        ip.nextLine();
                        System.out.print("Input ProId of product to edit: ");
                        String proid = ip.nextLine();
                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery("select * from Product where ProId = '" + proid +"'");
                        if(rs.next()){
                            System.out.println("Edit Product information");
                            System.out.println("--------------------------");
                            System.out.println("1. Edit Name ");
                            System.out.println("2. Edit UniPrice ");
                            System.out.println("3. Edit Quantity ");
                            System.out.println("4. Edit TypeId ");
                            System.out.println("5. Back");
                            System.out.print("Enter choice: ");
                        }
                        int t = ip.nextInt();
                        switch (t) {
                            case 1:
                                productEditName(proid);
                                break;
                            case 2:
                                productEditPrice(proid);
                                break;
                            case 3:
                                productEditQty(proid);
                                break;
                            case 4:
                                productEditTypeId(proid);
                                break;
                            case 5:
                            default:
                                System.out.println("Exit edit information");
                        }
                        break;
                    case 4:
                        ip.nextLine();
                        System.out.print("Enter ProId to delete: ");
                        String id = ip.nextLine();
                        
                        Statement st1 = conn.createStatement();
                        ResultSet rs1 = st1.executeQuery("select * from Product where ProId = '" + id +"'");
                        if(rs1.next()){
                            System.out.println("ok!");
                            productDelete(id);
                        }else{
                            System.out.println("Product is not exist!");
                        }
                        break;
                    case 5:
                        flag = false;
                        break;
                    case 6:
                        clrscr();
                        flag = false;
                        System.out.println("Account is signed out!");
                        signOutAcc();
                        frmMain();
                        break;
                    case 7:
                        System.out.println("Goodbye " + userRole);
                        System.exit(0);
                    default:
                        System.out.println("Thank you!\n");
                }
            }catch(InputMismatchException ex){
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }    
        }
    }    

    //-------------------------------------- Orders and OrderLine table -------------------------------------------------
    public void orderManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flag = true;
        while(flag){
            try{
                System.out.println("\nOrder Management Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. List of Order");
                System.out.println("2. List of OrderLine");
                System.out.println("3. Manage Order");
                System.out.println("4. Back");
                System.out.println("5. Sign out");
                System.out.println("6. Exit");
                System.out.print("Enter choice number: ");

                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        orderList();
                        break;
                    case 2:
                        clrscr();
                        orderLineList();
                        break;
                    case 3:
                        System.out.print("Input No. of Order to manage: ");
                        int row = ip.nextInt();
                        System.out.println("");
                        System.out.println("1. Confirm Delivery for Order");
                        System.out.println("2. Cancel Order");
                        System.out.println("3. Back");
                        System.out.print("Enter choice number: ");
                        int choice = ip.nextInt();
                        
                        switch (choice) {
                            case 1:
                                orderVerify(row);
                                break;
                            case 2:
                                orderDelete(row);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 4:
                        flag = false;
                        break;
                    case 5:
                        flag = false;
                        clrscr();
                        System.out.println("Account is signed out!");
                        signOutAcc();
                        frmMain();
                        break;
                    case 6:
                        System.out.println("Goodbye " + userRole);
                        System.exit(0);
                    default:
                        System.out.println("Thank you!\n");
                }
            }catch(InputMismatchException ex){
                System.out.println("Invalid choice - Error: InputMismatchException");
                ip.nextLine();
            }catch(Exception ex){
                System.out.println("Invalid choice - Error: Exception");
            }    
        }
    }
    
}

