/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author yup
 */
public class makeOrder {
    public Connection conn;
    
    public static void main(String[] args) {
        makeOrder m = new makeOrder();
        m.makeOrder();
    }
    
    // Insert into Order table
    public void makeOrder(){
//    public void makeOrder(String id, String sqlDate, String usercust){
        Calendar day = Calendar.getInstance();
        Date date = new Date();
        LocalDate f = LocalDate.now();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.println("f: " + f);
    //----------------------------------------------------    
        String date2;
        date2 = simpleDateFormat.format(new Date());
        System.out.println("date2: " +date2);
    //----------------------------------------------------    
    /*    
        try {
            String sqlInsert = "insert into Order values(?,?)";
            PreparedStatement pst1 = conn.prepareStatement(sqlInsert);
            
            pst1.setString(1, id);
            pst1.setDate(2, java.sql.Date.valueOf(f));
            pst1.setString(3, usercust);
            
            int row = pst1.executeUpdate();
            if(row !=0){
                System.out.println("New Product has been order.");
            }else{
                System.out.println("Can not order product!");
            }
        } catch (SQLException ex) {
            System.out.println("Can not make Order");
        }
        */
    }
    
}
