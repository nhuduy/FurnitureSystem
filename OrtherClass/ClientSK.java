/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yup
 */
public class ClientSK{
    String clUser;
    String clPw;
    String clOut;
    public Connection conn;
    public String ipStream;
    
    public static void main(String[] args) {
        boolean flagM = true;
        while(flagM){
            try {
                Socket socketOfClient = new Socket("localhost",  6268);
                ClientSK cl = new ClientSK();
                Scanner in = new Scanner(System.in);

                System.out.println("\nFurniture system");
                System.out.println("------------------------");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter choice number: ");
                int num = in.nextInt();

                switch (num) {
                    case 1:
                        in.nextLine();
                        System.out.print("Enter User: ");
                        cl.clUser = in.nextLine();
                        System.out.print("Enter Password: ");
                        cl.clPw = in.nextLine();
                        
                        cl.clOut = "custLogin," + cl.clUser + "," + cl.clPw;
                        
                        if(cl.clUser!=null){
                            cl.loginSK(cl.clOut);
                        }
                        System.out.println("cl.ipStream" + cl.ipStream);
                        break;
                    default:
                        throw new AssertionError();
                }
            }catch (IOException ex) {
                System.out.println("IOException");;
            }
        } 
    }
    

    public void loginSK(String str) throws IOException{
        ClientSK cl = new ClientSK();
        String delimiter = ",";
        String[] tempArray = str.split(delimiter);
        
        Scanner in = new Scanner(System.in);
        Socket socketOfClient = new Socket("localhost",  6268);
        
        boolean flag = true;
        while(flag){
            System.out.print("Client: ");
            try{
                // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
                
                // Ghi dữ liệu vào luồng đầu ra của Socket tại Client.
                os.write(str);// + " " + new Date());
                os.newLine(); // kết thúc dòng
                os.flush();  // đẩy dữ liệu đi.
                System.out.println("str: " + str);
                
                // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
                BufferedReader is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
                
                // Đọc dữ liệu trả lời từ phía server
                // Bằng cách đọc luồng đầu vào của Socket tại Client.
                String responseLine = is.readLine();
//                System.out.println("responseLine: " + responseLine);
                cl.ipStream = is.readLine();
                if(cl.ipStream.equals("LoginOK")){
//                    socketOfClient.close();
                    cl.custManagement();
//                    break;
                }
                if(responseLine.equals("QUIT")){
                    os.close();
                    is.close();
                    socketOfClient.close();
//                    System.exit(0);
                    break;
                }
            }catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
                flag = false;
            }catch(NullPointerException ex){
                System.out.println("Error: NullPointerException");
                flag = false;
            }catch (IOException e) {
                System.err.println("IOException:  " + e);
                flag = false;
            }
        }
    }

public void custManagement(){
        Scanner ip = new Scanner(System.in);
        boolean flagAdm = true;
        while(flagAdm){
            try{
                System.out.println("\nCustomer Form - " + clUser);
                System.out.println("-----------------------------");
                System.out.println("1. Account Management");
                System.out.println("2. Product Management");
                System.out.println("3. Exit");
                System.out.print("Enter choice number: ");
                int num = ip.nextInt();
                switch (num) {
                    case 1:
                        System.out.println("1. Account Management");
//                        custAccManagement();
                        break;
                    case 2:
//                        custProductManagement();
                        break;
                    case 3:
                        flagAdm = false;
                        System.out.println("Thanks for using our service! ");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("");
                        break;
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
