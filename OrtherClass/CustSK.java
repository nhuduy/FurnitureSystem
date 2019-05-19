/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author yup
 */
public class CustSK extends Product{
    public String custUser;
    public String custPw;
    public String custStatus;
    public String cmt = null;

    ClientSK cl = new ClientSK();

//public CustSK(){
//    this.custUser = cl.clUser;
//    this.custPw = cl.clPw;
//}    
    
    public void custLogin(String u, String p){
            try {
                
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select * from Customer where CustId = '" + u +"'");
                
                if(rs.next()){
                    String id = rs.getString("CustId");
                    String pw = rs.getString("Pass");
                    String name = rs.getString("CustName");
                    String statusC = rs.getString("CustStatus");
                    
                    if(id.trim().equals(u) && pw.trim().equals(p)){
                        this.custUser = id;
                        this.custPw = pw;
                        this.cmt = "login ok, replay from CustSK";
                    }else{
                        this.cmt = "login fail, replay from CustSK";
                    }
                }else{
                    this.cmt = "Account does not exist, replay from CustSK";
                }
            } catch (SQLException ex) {
                System.out.println("Can not login account, replay from CustSK");
            }
            System.out.println(this.cmt);
        }
    
    
    
}

