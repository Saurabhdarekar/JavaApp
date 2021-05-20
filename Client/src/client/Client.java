/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;  
import java.io.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
class Client
{  
   
    public static void funStudent(Socket sock,DataInputStream in,DataOutputStream out)throws IOException{
        Scanner c = new Scanner(System.in);
        Student s = new Student();
        System.out.println("1)Teacher Files\n 2)My Files\n 3)Submission Folders");
        String op = c.nextLine();
        out.writeUTF(op);
        switch(op){
            case "1":s.viewtF(sock,in,out);
                break;
            case "2":s.myfiles(sock,in,out);
                break;
            case "3":s.sub(sock,in,out);
            default:
        }
    }
    public static void funTeacher(Socket sock,DataInputStream in,DataOutputStream out)throws IOException{
        Scanner c = new Scanner(System.in);
        Teacher t = new Teacher();
        System.out.println("1)My Public Files\n 2)My Files\n 3)Student Folders");
        String op = c.nextLine();
        out.writeUTF(op);
        switch(op){
            case "1":t.mypublicfiles(sock,in,out);
                break;
            case "2":t.myfiles(sock,in,out);
                break;
            case "3":t.viewsF(sock,in,out);
            default:
        }
    }
    public static void main(String args[])throws Exception
    {  
         InetAddress ip;
         Socket s;
        ip = InetAddress.getByName("localhost"); 
        s=new Socket(ip,8080);  
        DataInputStream in=new DataInputStream(s.getInputStream());  
        DataOutputStream out=new DataOutputStream(s.getOutputStream());  
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
        
        String st="",st2="",user=""; 
        do{
        System.out.println("Enter User type:");
        user = br.readLine();
        out.writeUTF(user); 
        out.flush();
        st = in.readUTF();
        System.out.println(st);
        }while(st.equals("Invalid User"));
        do{
        System.out.println("Enter the Username:");
        st = br.readLine();
        System.out.println("Enter the Passwords:");
        st2 = br.readLine();
        out.writeUTF(st);  
        out.flush();
        out.writeUTF(st2);  
        out.flush();
        st = in.readUTF();
        System.out.println(st);
        }while(!(st.equals("Access Granted")));
        if(user.equals("Teacher"))
        {
           funTeacher(s,in,out);
        }
        else
        {
            funStudent(s,in,out);
        }
        in.close();
        s.close();  
    }
}  
