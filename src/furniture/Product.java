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
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yup
 */
public class Product extends Server{
    String ProId;
    String ProName;
    int Price;
    int Quantity;
    String TypeId;
    
//    public Product(String id, String name, int p, int q, String type){
//    this.ProId = id;
//    this.ProName = name;
//    this.Price = p;
//    this.Quantity = q;
//    this.TipeId = type;
//    }

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
    
    // Connection
    public Connection conn;
    public void createConnectionType4(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433; databaseName = Furni; integratedSecurity=true","sa","sa");
            System.out.println("Database is connected!");
            
        }catch(ClassNotFoundException e){
            System.out.println("Error1: " + e.getMessage());
        }catch(SQLException e){
            System.out.println("Error2: " + e.getMessage());
        }
    }

// ------------------------ Category table ------------------------    

    public void categoryManagement(String userAcc, String userRole){
        Scanner ip = new Scanner(System.in);
        boolean flag = true;
        while(flag){
            try{
                System.out.println("\nCategory Management Form - " + userAcc);
                System.out.println("-----------------------------");
                System.out.println("1. List of Category");
                System.out.println("2. Add new Category");
                System.out.println("3. Edit Category name");
                System.out.println("4. Delete Category");
                System.out.println("5. Back");
                System.out.println("6. Exit");
                System.out.println("Enter choice number");

                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        clrscr();
                        categoryList();
                        break;
                    case 2:
                        clrscr();
                        categoryAdd(userAcc, userRole);
                        break;
                    case 3:
                        categoryUpdateName();
                        break;
                    case 4:
                        categoryDelete();
                        break;
                    case 5:
                        flag = false;
                        break;
                    case 6:
                        System.out.println("Goodbye " + userRole);
                        System.exit(0);
                    default:
                        System.out.println("Thank you!\n");
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
    
    public void categoryList(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Category");
            System.out.println("Categories list");
            System.out.println("--------------------------------------");
            System.out.println("TypeId\t\tType Name");
            System.out.println("--------------------------------------");
            
            while(rs.next()){
                System.out.println(rs.getString("TypeId")
                        + "\t" + rs.getString("TypeName"));
            }
            System.out.println("--------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from Category!");
        }        
    }
    
    public void categoryAdd(String id, String role){
        try{
            Scanner ip = new Scanner(System.in);
            String ac1, ac2;
            
            boolean condition = false;
            do{
                System.out.println("Add new Category Form - " + id);
                System.out.println("-------------------------------------");
                System.out.println("Enter new Type ID of Product [a-z, A-Z, 0-9], 10 characters of maximum: ");
                ac1 = ip.nextLine();
                while(ac1.length()>10 || (Pattern.matches("[a-zA-Z]+[0-9]*", ac1) != true)){
                    System.out.print("Maximum 10 characters [a-z, A-Z, 0-9]: ");
                    ac1 = ip.nextLine();
                }

                // Check if TypeId is exist
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from Category where TypeId = '" + ac1 +"'");
                if(rs.next()){
                    String pId = rs.getString("TypeId");
                    if(pId.trim().equals(ac1.trim())){
                        System.out.println("TypeId of Product is exist. Choose another Id, please!");
                        condition = true;
                    }
                }else{
                    condition = false;
                }
            }while(condition);
                    
            System.out.println("Name of Category: ");
            ac2 = ip.nextLine();
            
            String sqlInsert = "insert into Category values(?,?)";
            PreparedStatement pst = conn.prepareStatement(sqlInsert);
            
            pst.setString(1, ac1);
            pst.setString(2, ac2);
            
            int row = pst.executeUpdate();
            if(row !=0){
                System.out.println("New Category has been added!");
            }else{
                System.out.println("Can not add new Category!");
            }
        }catch(SQLException ex){
            System.out.println("Add Category not successful");
        }
    }

    public void categoryUpdateName(){
        try {
            Scanner ip = new Scanner(System.in);
            
            System.out.println("Enter TypeId to edit: ");
            String u = ip.nextLine();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Category where TypeId = '" + u +"'");
            if(rs.next()){
                System.out.println("Enter new name: ");
                String temp = ip.nextLine();
                
                String sqlUpdate = "update Category set TypeName = '"+ temp +"' where TypeId = '" + rs.getString("TypeId") + "'";
                Statement pst = conn.createStatement();
                int rs1 = pst.executeUpdate(sqlUpdate);
                if(rs1>=1){
                    System.out.println("Category name is updated!");
                }
            }
        }catch (SQLException ex) {
            System.out.println("Can not update name of Category!");
        }
    }    
    
    public void categoryDelete(){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.println("Enter TypeId to delete: ");
            String u = ip.nextLine();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Category where TypeId = '" + u +"'");
            if(rs.next()){
                String sqlDelete = "delete from Category where TypeId = '" + rs.getString("TypeId") + "'";
                Statement pst = conn.createStatement();
                int rs1 = pst.executeUpdate(sqlDelete);
                if(rs1>=1){
                    System.out.println("Delete successful!");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Can not delete Type of Category!");
        }
    }
    
    
    
    // --------------------------- Product table ---------------------------

     public void productList(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Product");
            System.out.println("Products list");
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.println("Id\t\tName\t\t\t\t\tUnite price\tQuantity\tTypeId");
            System.out.println("----------------------------------------------------------------------------------------------------");
            
            while(rs.next()){
                System.out.println(rs.getString("ProId")
                        + "\t" + rs.getString("ProName")
                        + "\t" + rs.getFloat("UniPrice")
                        + "\t\t" + rs.getInt("Quantity")
                        + "\t\t" + rs.getString("TypeId"));
            }
            System.out.println("----------------------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from ProductCategory!");
        }
    }   
    
    public void productAdd(String id, String role){
        try{
            Scanner ip = new Scanner(System.in);
            String ac1, ac2, ac5;
            float ac3;
            int ac4; 
            
            System.out.println("Input TypeId of product: ");
            ac5 = ip.nextLine();
            
            // Check TypeId is exist
            Statement st0 = conn.createStatement();
            ResultSet rs0 = st0.executeQuery("select * from Category where TypeId = '" + ac5 +"'");
            if(rs0.next()){
                
                // Input ProId
                boolean condition = false;
                do{
                    System.out.println("Add new product Form ");
                    System.out.println("------------------------- ");
                    System.out.println("Input ProId of Product [a-z, A-Z, 0-9], 10 characters maximum: ");
                    ac1 = ip.nextLine();
                    while(ac1.length()>10 || (Pattern.matches("[a-zA-Z0-9]+", ac1) != true)){
                        System.out.print("Invalid ProId: ");
                        ac1 = ip.nextLine();
                    }

                    // Check if ProId is exist
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("select * from Product where ProId = '" + ac1 +"'");
                    if(rs.next()){
                        String pId = rs.getString("ProId");
                        if(pId.trim().equals(ac1.trim())){
                            System.out.println("ProId is exist. Choose another, please!");
                            condition = true;
                        }
                    }else{
                        condition = false;
                    }
                }while(condition);
                
                // Input orther informaition of product
                System.out.println("Name of Product: ");
                ac2 = ip.nextLine();
                while(ac2.equals("")){
                    System.out.print("Do not allow null, input agian please: ");
                    ac2 = ip.nextLine();
                }
                System.out.println("Input unit price: ");
                ac3 = ip.nextFloat();
                while(ac3<0){
                    System.out.print("Unit Price is greater than or equal to 0: ");
                    ac3 = ip.nextFloat();
                }
                System.out.println("Input quantity: ");
                ac4 = ip.nextInt();
                while(ac4<0){
                    System.out.print("Quantity is greater than or equal to 0: ");
                    ac4 = ip.nextInt();
                }
                ip.nextLine();
                
                // Insert into Product table
                String sqlInsert = "insert into Product values(?,?,?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sqlInsert);
                pst.setString(1, ac1);
                pst.setString(2, ac2);
                pst.setFloat(3, ac3);
                pst.setInt(4, ac4);
                pst.setString(5, ac5);
                int row = pst.executeUpdate();
                if(row !=0){
                    System.out.println("New Product has been added.");
                }else{
                    System.out.println("Can not add new product!");
                }
            }else{
                System.out.println("Create new TypeId on Category first!");
            }
        }catch(InputMismatchException ex){
            System.out.println("Error: " + ex.getMessage());
        }catch(SQLException ex){
            System.out.println("Add product not successful.");
        }
    }

    public void productEditName(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new name: ");
            String temp = ip.nextLine();
            String sqlUpdate = "update Product set ProName = '"+ temp +"' where ProId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                clrscr();
                System.out.println("Name of product is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update name of product!");
        }
    }

    public void productEditPrice(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Enter new unit price: ");
            float temp = ip.nextFloat();
            String sqlUpdate = "update Product set UniPrice = '"+ temp +"' where ProId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                clrscr();
                System.out.println("Unit price of product is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update unit price of product!");
        }
    }
    
    public void productEditQty(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Input Quantity: ");
            int temp = ip.nextInt();
            String sqlUpdate = "update Product set Quantity = '"+ temp +"' where ProId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                clrscr();
                System.out.println("Quantity of product is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update Quantity of product!");
        }
    }
    
    public void productEditTypeId(String id){
        try {
            Scanner ip = new Scanner(System.in);
            System.out.print("Input new TypeId (Make sure TypeId must have in Category table): ");
            int temp = ip.nextInt();
            String sqlUpdate = "update Product set TypeId = '"+ temp +"' where ProId = '" + id +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlUpdate);
            if(rs>=1){
                clrscr();
                System.out.println("TypeId of product is updated!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not update Type of product!");
        }
    }    
    
    public void productDelete(String proid){
        try {
            String sqlDelete = "delete from Product where ProId = '" + proid +"'";
            Statement pst = conn.createStatement();
            int rs = pst.executeUpdate(sqlDelete);
            if(rs>=1){
                System.out.println("Delete product successful!");
            }
        } catch (SQLException ex) {
            System.out.println("Delete product is not successful!");
        }
    }    
    
    
//-------------------------------------------- Orders and OrderLine table -------------------------------------------------
    
    public void orderList(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Orders");
            System.out.println("\nOrders list");
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("No.\tOrderId\t\t\t\t\tOrderDate\tCustId\t\tDeliveryDate");
            System.out.println("--------------------------------------------------------------------------------------------");
            
            int count = 0;
            while(rs.next()){
                count++;
                System.out.println(count
                        + "\t" + rs.getString("OrderId")
                        + "\t" + rs.getString("OrderDate")
                        + "\t" + rs.getString("CustId")
                        + "\t" + rs.getString("DeliveryDate"));
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from Orders!");
        }
    }

    public void orderList(String custid){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Orders where CustId = '" + custid + "'");
            System.out.println("\nOrders list by Customer");
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("No.\tOrderId\t\t\t\t\tOrderDate\tCustId\t\tDeliveryDate");
            System.out.println("--------------------------------------------------------------------------------------------");
            
            int count = 0;
            while(rs.next()){
                count++;
                System.out.println(count
                        + "\t" + rs.getString("OrderId")
                        + "\t" + rs.getString("OrderDate")
                        + "\t" + rs.getString("CustId")
                        + "\t" + rs.getString("DeliveryDate"));
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from Orders!");
        }
    }
    
        
    public void orderLineList(){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from OrderLine");
            System.out.println("\nOrderline list");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("No.\tOrderId\t\t\t\t\tProId\t\tQuantity\tPrice");
            System.out.println("-------------------------------------------------------------------------------------");
            
            int count = 0;
            while(rs.next()){
                count++;
                System.out.println(count
                        + "\t" + rs.getString("OrderId")
                        + "\t" + rs.getString("ProId")
                        + "\t" + rs.getInt("Quantity")
                        + "\t\t" + rs.getFloat("Price"));
            }
            System.out.println("-------------------------------------------------------------------------------------");
        }catch(SQLException e){
            System.out.println("Can not load data from OrderLine!");
        }
    }
    
    public void orderLineList(String custid){
        try{
            Statement st0 = conn.createStatement();
            ResultSet rs0 = st0.executeQuery("select * from Orders where CustId = '" + custid + "'");
            
            System.out.println("\nOrderline list of " + custid);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("No.\tOrderId\t\t\t\t\tProId\t\tQuantity\tPrice");
            System.out.println("-------------------------------------------------------------------------------------");
            
            float sumOfPrice = 0;
            int count0 = 0;
            while(rs0.next()){
                count0++;
                String orderid = rs0.getString("OrderId");
                System.out.println("No." +count0);
            
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from OrderLine where OrderId = '" + orderid + "'");
                float orderprice = 0;
                int count = 0;
                while(rs.next()){
                    count++;
                    orderprice += rs.getFloat("Price");
                    System.out.println(count
                            + "\t" + rs.getString("OrderId")
                            + "\t" + rs.getString("ProId")
                            + "\t" + rs.getInt("Quantity")
                            + "\t\t" + rs.getFloat("Price"));
                }
                System.out.println("\t\t\t\t\t\t\t   --------------------------");
                System.out.println("\t\t\t\t\t\t    Sum of price of order No." +count0 + ": " + orderprice);
                sumOfPrice += orderprice;
            }
            
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("\t\t\t\t\t\t  Total of price of all orders: " + sumOfPrice);
        }catch(SQLException e){
            System.out.println("Can not load data from OrderLine!");
        }
    }
    
    public void orderDelete(int r){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Orders");
            
            int count = 0;
            while(rs.next()){
                count++;
                String orderIdDelete = rs.getString("OrderId");
                if(count==r){
                    System.out.println("orderIdDelete: " + orderIdDelete);
                    
                    try {
                        String sqlDelete = "delete from OrderLine where OrderId = '" + orderIdDelete +"'";
                        Statement pst = conn.createStatement();
                        int rs1 = pst.executeUpdate(sqlDelete);

                        String sqlDelete1 = "delete from Orders where OrderId = '" + orderIdDelete +"'";
                        Statement pst1 = conn.createStatement();
                        int rs2 = pst.executeUpdate(sqlDelete1);
                        
                        if(rs1>=1 && rs2>=1){
                            System.out.println("Delete Order successful!");
                        }
                    } catch (SQLException ex) {
                        System.out.println("Can not delete Order!");
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("Can not load data from Orders!");
        }
    }
    
    public void orderDelete(String custid, int row){
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Orders where CustId = '" + custid + "'");
            int count = 0;
            while(rs.next()){
                count++;
                String orderIdDelete = rs.getString("OrderId");
                if(count==row){
                    try {
                        String sqlDelete = "delete from OrderLine where OrderId = '" + orderIdDelete +"'";
                        Statement pst = conn.createStatement();
                        int rs1 = pst.executeUpdate(sqlDelete);

                        String sqlDelete1 = "delete from Orders where OrderId = '" + orderIdDelete +"'";
                        Statement pst1 = conn.createStatement();
                        int rs2 = pst.executeUpdate(sqlDelete1);
                        
                        if(rs1>=1 && rs2>=1){
                            System.out.println("Your Order is deleted successful!");
                        }
                    } catch (SQLException ex) {
                        System.out.println("Can not delete your Order!");
                    }
                    
                }
            }
        }catch(SQLException e){
            System.out.println("Can not load data from Orders!");
        }
    }
    
    public void orderVerify(int r){
        try{
            Calendar day = Calendar.getInstance();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String deliveryDate = simpleDateFormat.format(date).toString();
            
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from Orders");
            int count = 0;
            while(rs.next()){
                count++;
                if(r==count){
                    String orid = rs.getString("OrderId");
                    System.out.println("orid: " + orid);
                    try {
                        String sqlUpdate = "update Orders set DeliveryDate = '" + deliveryDate +"' where OrderId = '" + orid +"'";
                        Statement pst = conn.createStatement();
                        int rs1 = pst.executeUpdate(sqlUpdate);
                        if(rs1>=1){
                            System.out.println("Delivery order successful!");
                            productQtyUpdate(orid);
                        }
                    } catch (SQLException ex) {
                        System.out.println("Can not delivery order!");
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("Can not load data from Orders!");
        }
    }

    public void productQtyUpdate(String id){
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select ProId, Quantity from OrderLine where OrderId = '" + id + "'");
            
            while(rs.next()){
                int qtyOrder = rs.getInt("Quantity");
                String proidOrder = rs.getString("ProId");
                
                Statement st1 = conn.createStatement();
                ResultSet rs1 = st1.executeQuery("select Quantity from Product where ProId = '" + proidOrder + "'");
                while(rs1.next()){
                    int qtyProduct = rs1.getInt("Quantity");
                    int qtyUpdate = qtyProduct - qtyOrder;
                    
//                    System.out.println("qtyProduct" + qtyProduct);
//                    System.out.println("qtyOrder" + qtyOrder);
//                    System.out.println("qtyUpdate" + qtyUpdate);
                    
                    String sqlUpdateQty = "update Product set Quantity = '" + qtyUpdate +"' where ProId = '" + proidOrder + "'";
                    Statement pst = conn.createStatement();
                    int rs2 = pst.executeUpdate(sqlUpdateQty);
                    
                    if(rs2>=1){
                            System.out.println("Update product quantity successful!");
                    }
                }        
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean regexPhone(String str) {
        boolean mobile = Pattern.matches("[0]{1}[1-9]{1}[0-9]{8}", str);
        boolean phone = Pattern.matches("[0]{1}[1-9]{1}[0-9]{9}", str);
        return (mobile || phone);
    }
    
    public boolean regexUsername(String str) {
        boolean name = Pattern.matches("\\D+[\\D\\d]*[!@#$%&]*[\\D\\d]*", str);
        return name;
    }
    
    public boolean regexFullname(String str) {
        boolean name = Pattern.matches("[a-zA-Z\\s]*", str);
        return name;
    }
    
    public boolean regexAddress(String str) {
        boolean name = Pattern.matches("[a-zA-Z0-9\\s]*", str);
        return name;
    }
    
}
