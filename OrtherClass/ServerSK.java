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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author yup
 */
public class ServerSK extends Thread{
    private int clientNumber;
    private Socket socketOfServer;
    private String sout;
    
    public ServerSK(Socket socketOfServer, int clientNumber) {
        this.clientNumber = clientNumber;
        this.socketOfServer = socketOfServer;
        System.out.println("New connection with client# " + this.clientNumber + " at " + socketOfServer);
    }    
    
    @Override
    public void run() {
        CustSK cust = new CustSK();
        try{
            //Step3: Tạo (Mở) luồng vào ra trên Socket tại Server.
            BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            
            while(true){
                // Đọc dữ liệu tới server (Do client gửi tới).
                String line = is.readLine();     
                System.out.println("Client " + this.clientNumber +" :"+ line);
                
                String delimiter = ",";
                String[] tempArray = line.split(delimiter);
                
                if(tempArray[0].equals("custLogin")){
                    cust.createConnectionType4();
                    cust.custLogin(tempArray[1], tempArray[2]);
                    System.out.println("cust.cmt" + cust.cmt);
                    os.write("LoginOK");
                    os.newLine();
                    os.flush();
                    System.out.println("os.flush() is completed");
                    line = null;
                    
                }else{
                    System.out.println("not receive data from Client!");
                }
                
                if(line.length()>=3 && !line.equals("QUIT")){
                    Scanner in = new Scanner(System.in);                
                    System.out.print("Sever: ");
                    String input = in.nextLine();
                    
                    // Ghi vào luồng đầu ra của Socket tại Server  (Nghĩa là gửi tới Client).
                    os.write("Server: " + input);
                    os.newLine();   // Kết thúc dòng
                    os.flush();     // Đẩy dữ liệu đi
                }
                
                // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                if (line.equals("QUIT")) {
                    os.write("Server: OK QUIT Thi QUIT");
                    os.newLine();
                    os.flush();
                    break;
                }
            }
        }catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        Employee emp = new Employee();
        emp.createConnectionType4();
        
        ServerSocket listener = null;
        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;
        
        try{
            //Step2: Lắng nghe, chờ đợi yêu cầu của client tại cổng...
            listener = new ServerSocket(6268);
            while(true){
                // Chấp nhận một yêu cầu kết nối từ phía Client.
                Socket socketOfServer = listener.accept();
                
                // Đồng thời tạo (nhận được) một đối tượng (thread) Socket tại server 
                // de xu ly rieng tung yeu cau cua client.
                new ServerSK(socketOfServer, clientNumber++).start();
            }
        }catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        finally {
             listener.close();
        }
    }    
}
