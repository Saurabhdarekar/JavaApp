/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;  
import java.io.*;
import java.util.*;  
import java.text.*; 
public class Server{  

    public static void main(String args[])throws Exception{  
        ServerSocket ss=new ServerSocket(8080);  
        
        while(true)
        {
            Socket s = null;
            try{
        s=ss.accept();  
        DataInputStream in=new DataInputStream(s.getInputStream());  
        DataOutputStream out=new DataOutputStream(s.getOutputStream());  
        Thread t = new ClientHandler(s,in,out); 
                System.out.println("connected");
                // Invoking the start() method 
        t.start();
            }
            catch (Exception e){ 
            s.close(); 
            e.printStackTrace(); 
            } 
        }
    }
}  
    class ClientHandler extends Thread  
{  
        final Socket socket;
        final DataInputStream dis;
        final DataOutputStream dos;
    // Constructor     
    public ClientHandler(Socket s,DataInputStream in,DataOutputStream out)
    {
        socket = s;
        dis = in;
        dos = out;
    }
        
    public static void funTeacher(String st1,Socket s,DataInputStream in,DataOutputStream out)
    {
        try{
        String input = "",input1 = "";
        input = in.readUTF();
        Teacher t = new Teacher(st1);
        switch(input){
            case "1"://My Public Files
                t.showmpf(s);
                input1 = in.readUTF();
                switch(input1){
                    case "1":t.addmpf(s,in,out);
                        break;
                    case "2":t.downmpf(s, in, out);
                        break;
                    case "3":t.delmpf(s, in, out);
                        break;
                    case "4":break;
                }
                break;
            case "2"://See Myfiles
                t.showmyf(s);
                input1 = in.readUTF();
                switch(input1){
                    case "1":t.addmyf(s,in,out);
                        break;
                    case "2":t.downmyf(s, in, out);
                        break;
                   // case "3":t.crfolmyf(s, in, out);
                     //   break;
                    case "3":t.delmyf(s, in, out);
                        break;
                    case "4":break;
                }
                break;
            case "3"://See student files
                String op = in.readUTF();
                if(op.equals("1"))
                {
                    t.showmyclass(s);
                }
                String classes = in.readUTF();
                t.getstudents(s,classes);
                String ch = in.readUTF();
                if(ch.equals("back")){
                    break;
                }
                String STUname = ch;
                t.getsub(s,ch,in,out);
                break;
        }
        }
        catch(IOException i){}
    }
        public static void funStudent(String st1,Socket s,DataInputStream in,DataOutputStream out)
    {
        try{
        String input = "",input1 = "";
        Student stu = new Student(st1);
        stu.showmyf(s);
        input = in.readUTF();
        switch(input){
            case "1"://See Teacher files
                String Tname = in.readUTF();
                stu.showteacherfiles(s,Tname);
                while(true)
                {
                    String get = in.readUTF();
                if(get.equals("back"))
                {
                    break;
                }
                    stu.downloadtf(s,Tname,get,in,out);
                }
                break;
            case "2"://See Myfiles
                stu.showmyf(s);
                while(true)
                {
                    input1 = in.readUTF();
                switch(input1){
                    case "1":stu.addmyf(s,in,out);
                        break;
                    case "2":stu.downmyf(s, in, out);
                        break;
                    case "3":stu.delmyf(s, in, out);
                        break;
                    case "4":break;
                }
                if(input1.equals("back"))
                {
                    break;
                }
                }
                break;
            case "3"://See sub files
                 stu.subno(s,in,out);
                break;
        }
        }
        catch(IOException i){}
    }
    public static void logTeacher(Socket s,DataInputStream in,DataOutputStream out){
        Map<String, String> Password = new HashMap<String, String>();
        Scanner myObj = new Scanner(System.in); 
        try{
                             HashMap<String, String> map = null;
      try
      {
          FileInputStream fis = new FileInputStream("C:/Users/hp/Documents/NetBeansProjects/Admin/Teacher.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         map = (HashMap) ois.readObject();
         ois.close();
         fis.close();
      }catch(IOException ioe)
      {
         ioe.printStackTrace();
         return;
      }catch(ClassNotFoundException c)
      {
         System.out.println("Class not found");
         c.printStackTrace();
         return;
      }
            String st1 = "", st2 = "";
            do{
            st1 = in.readUTF();
            st2 = in.readUTF();
            System.out.println(st1+st2);
            if(st2.equals(map.get(st1)))
            {
                out.writeUTF("Access Granted");
                funTeacher(st1,s,in,out);
                break;
            }
            else
            {
                out.writeUTF("Access not Granted");
            }
            }while(true);  
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
        public static void logStudent(Socket s,DataInputStream in,DataOutputStream out){
        Map<String, String> Password = new HashMap<String, String>();
        Scanner myObj = new Scanner(System.in); 
        try{
                             HashMap<String, String> map = null;
      try
      {
          FileInputStream fis = new FileInputStream("C:/Users/hp/Documents/NetBeansProjects/Admin/Student.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         map = (HashMap) ois.readObject();
         ois.close();
         fis.close();
      }catch(IOException ioe)
      {
         ioe.printStackTrace();
         return;
      }catch(ClassNotFoundException c)
      {
         System.out.println("Class not found");
         c.printStackTrace();
         return;
      }
      System.err.println("hi2");
      String st1 = "", st2 = "";
      st1 = in.readUTF();
      st2 = in.readUTF();
      System.err.println("hi3");
      if(st2.equals(map.get(st1)))
      {
          out.writeUTF("Access Granted");
          funStudent(st1,s,in,out);
      }
      else
      {
          out.writeUTF("Access not Granted");
      } 
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void run(){
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
        try{
        String st = dis.readUTF();
        do{
        switch(st)
        {
            case "Teacher":
                //dos.writeUTF("Teacher");
                logTeacher(socket,dis,dos);
                System.err.println("hi");
                break;
            case "Student":
                //dos.writeUTF("Student");
                logStudent(socket,dis, dos);
                System.err.println("hi");
                break;
        }  
        }while(true);
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }    
    }