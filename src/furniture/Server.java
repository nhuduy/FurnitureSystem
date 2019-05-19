/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yup
 */
public class Server {
    
    // Connection
//    public Connection conn;
//    public void createConnectionType4(){
//        try{
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433; databaseName = Furni; integratedSecurity=true","sa","sa");
//            System.out.println("Database is connected!");
//            
//        }catch(ClassNotFoundException e){
//            System.out.println("Error1: " + e.getMessage());
//        }catch(SQLException e){
//            System.out.println("Error2: " + e.getMessage());
//        }
//    }
    
    public static void main(String[] args) {
        try {
            Server s = new Server();
            Employee emp = new Employee();
            
            emp.createConnectionType4();
            emp.frmMain();
            
        } catch (SQLException ex) {
            System.out.println("Error: Can not load main form!");
        }
        

    // Chay thu phuong thuc lua chon
//        Employee emp = new Employee();
//        emp.createConnectionType4();
////        emp.orderList();
////        emp.orderManagement();
//   
////        emp.productList();
//        emp.productManagement("A17080", "a17080");
    
    }
}
