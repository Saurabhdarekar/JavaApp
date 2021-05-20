/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.OutputStream;
import java.net.Socket;
import static server.Student.DB_PASSWD;
import static server.Student.DB_URL;
import static server.Student.DB_USER;
/**
 *
 * @author hp
 */
public class Teacher {
    String reg_id,dept_name,name,path;
       static final String DB_URL =
      "jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false";
   static final String DB_DRV =
      "com.mysql.jdbc.Driver";
   static final String DB_USER = "root";
   static final String DB_PASSWD = "1234";
    public Teacher(String regid) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
        String sql = "select * from teachers where reg_id=" + regid + ";";
        resultSet = statement.executeQuery(sql);
        reg_id = regid;
        filesystem f = new filesystem();
        path= f.getpath();
        while(resultSet.next())
        {
            dept_name = resultSet.getString("dept_name");
            name = resultSet.getString("name");
        }
          }catch(SQLException ex){
          System.err.println(ex);
          }
          finally{
               try {
              //    resultSet.close();
                  statement.close();
                  connection.close();
               } catch (SQLException ex) {
               }
        }
    }
    void rec(Socket sock,DataInputStream in,DataOutputStream out,String name)throws IOException
    {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
                String FILE_TO_RECEIVED = path + "/" + name;
          // receive file
                byte [] mybytearray  = new byte [999999999];
                InputStream is = sock.getInputStream();
                fos = new FileOutputStream(FILE_TO_RECEIVED);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                current = bytesRead;

                do {
                   bytesRead =
                      is.read(mybytearray, current, (mybytearray.length-current));
                   if(bytesRead >= 0) current += bytesRead;
                } while(bytesRead > -1);

                bos.write(mybytearray, 0 , current);
                bos.flush();
                System.out.println("File " + FILE_TO_RECEIVED
                    + " downloaded (" + current + " bytes read)");
            }
              finally {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
              }
    }
    void up(Socket sock,DataInputStream in,DataOutputStream out,String name)throws IOException
    {
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    String FILE_TO_SEND = name; 
      while (true) {
        try {
          // send file
          File myFile = new File (FILE_TO_SEND);
          byte [] mybytearray  = new byte [(int)myFile.length()];
          fis = new FileInputStream(myFile);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray,0,mybytearray.length);
          os = sock.getOutputStream();
          System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
          os.write(mybytearray,0,mybytearray.length);
          os.flush();
          System.out.println("Done.");
        }
        catch(IOException i)
        {
            
        }
        finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (sock!=null) sock.close();
        }
    }
    }
    void addmpf(Socket s, DataInputStream in, DataOutputStream out)
    {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Connection connection = null;
        Statement statement = null;
        try{
            String name = in.readUTF();
            String remark = in.readUTF();
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
         String sql = "insert into " + reg_id + "mypublicfiles values(" + name + ",sysdate()," + remark + ")";
         statement.executeUpdate(sql);
         String path = reg_id + "/mypublicfiles/" + name;
            rec(s, in, out, path);
          }catch(SQLException ex){
          System.err.println(ex);
      }catch(IOException i){}
        finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
      }
        
    }
    void delmpf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    {
        Scanner c = new Scanner(System.in);
        String name = in.readUTF();
        String path = reg_id + "/mypublicfiles/" + name;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement(); 
        String sql = "delete from " + reg_id + "mypublicfiles where Filename='" + name + ";";
        statement.executeUpdate(sql);
        }
        catch(SQLException ex){
          System.err.println(ex);
      }
        finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
        }
        filesystem f = new filesystem();
        f.deldir(path);
    }
    void downmpf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    {
        String name = reg_id + "/mypublicfiles/" + in.readUTF();
        up(s, in, out, name);
    }
    void showmpf(Socket socket)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Filelist f = new Filelist();
        Scanner c = new Scanner(System.in);
        try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
        String sql = "select * from " + reg_id + "mypublicfiles;";
        resultSet = statement.executeQuery(sql);
        ArrayList<String> Filename1 = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> remark1 = new ArrayList<String>();
        while(resultSet.next())
        {
            Filename1.add(resultSet.getString("Filename"));
            date.add(resultSet.getString("dt"));
            remark1.add(resultSet.getString("remark"));
        }
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(Filename1);
        objectOutputStream.writeObject(date);
        objectOutputStream.writeObject(remark1);
          }catch(SQLException ex){
          System.err.println(ex);
          }catch(IOException i)
          {}
          finally{
               try {
              //    resultSet.close();
                  statement.close();
                  connection.close();
               } catch (SQLException ex) {
               }
           }
    }
    void addmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    {

        Scanner c = new Scanner(System.in);
        String filename = in.readUTF();
        String dpath = reg_id + "/myfiles/" + filename;
        String remark = in.readUTF();
        rec(s, in, out, dpath);
        Connection connection = null;
        Statement statement = null;
        try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
         String sql = "insert into " + reg_id + "myfiles values(" + filename + ",sysdate()," + remark + ")";
         statement.executeUpdate(sql);
          }catch(SQLException ex){
          System.err.println(ex);
      }
        finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
      }
        
    }
    void downmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    { 
        String filename = in.readUTF();
        String dpath = reg_id + "/myfiles/" + filename;
        up(s, in, out, dpath);  
    }
    void crfolmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    { 
        String path = "D://" + reg_id + "myfiles.ser";
        tree t = null;
        try
      {
         FileInputStream fis;
         fis = new FileInputStream(path);
         ObjectInputStream ois = new ObjectInputStream(fis);
         t = (tree) ois.readObject();
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
        Scanner c = new Scanner(System.in);
        String filename = in.readUTF();
        String dpath = in.readUTF() + filename;
        filesystem f = new filesystem();
        f.crdir(dpath);
        t.add(dpath,false);
       try
      {
          FileOutputStream fos;
            fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
            oos.close();
            fos.close();
            }catch(IOException ioe)
            {
               ioe.printStackTrace();
            }
    }
    void delmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    {
      
        Scanner c = new Scanner(System.in);
        String filename = in.readUTF();
        String dpath = reg_id + "myfiles" + filename;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement(); 
        String sql = "delete from " + reg_id + "myfiles where Filename='" + filename + ";";
        statement.executeUpdate(sql);
        }
        catch(SQLException ex){
          System.err.println(ex);
      }
        finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
        }
        filesystem f = new filesystem();
        f.deldir(dpath);
    }
    void showmyf(Socket s)throws IOException
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Filelist f = new Filelist();
        Scanner c = new Scanner(System.in);
        try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
        String sql = "select * from " + reg_id + "myfiles;";
        resultSet = statement.executeQuery(sql);
        ArrayList<String> Filename1 = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> remark1 = new ArrayList<String>();
        while(resultSet.next())
        {
            Filename1.add(resultSet.getString("Filename"));
            date.add(resultSet.getString("dt"));
            remark1.add(resultSet.getString("remark"));
        }
        OutputStream outputStream = s.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(Filename1);
        objectOutputStream.writeObject(date);
        objectOutputStream.writeObject(remark1);
          }catch(SQLException ex){
          System.err.println(ex);
          }catch(IOException i)
          {}
          finally{
               try {
              //    resultSet.close();
                  statement.close();
                  connection.close();
               } catch (SQLException ex) {
               }
           }
    }
    void showmyclass(Socket socket)throws IOException
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> a = new ArrayList<String>();
              try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
         String sql = "select * from " + reg_id + "myclass;";
         resultSet = statement.executeQuery(sql);
         while(resultSet.next())
        {
            a.add(resultSet.getString("classname"));
        }
         statement.executeUpdate(sql);
      }catch(SQLException ex){
          System.err.println("not");
      }finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
      }
       OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // make a bunch of messages to send.
        objectOutputStream.writeObject(a);
    }
    void getstudents(Socket socket,String subject)throws IOException
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<String> a = new ArrayList<String>();
              try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
         String[] classname = subject.split("-");
         if(subject.contains("-"))
         {
         String sql = "select roll_no from students where classname=" + classname[0] + ";";
         resultSet = statement.executeQuery(sql);
         }
         else{
         String sql = "select roll_no from students where classname=" + classname[0] + " and batch=" + classname[1] + ";";
         resultSet = statement.executeQuery(sql);
         }
         while(resultSet.next())
        {
            a.add(resultSet.getString("roll_no"));
        }
      }catch(SQLException ex){
          System.err.println("not");
      }finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
      }
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // make a bunch of messages to send.
        objectOutputStream.writeObject(a);
    }
    void getsub(Socket s,String st, DataInputStream in, DataOutputStream out)throws IOException{
                Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
              try{
         connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
         statement=connection.createStatement();
         String sql = "select * from " + reg_id + "myclass;";
         resultSet = statement.executeQuery(sql);
         String subject = resultSet.getString("subject");
        sql = "select * from " + st + subject + ";";
         resultSet = statement.executeQuery(sql);
        ArrayList<String> Filename1 = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> remark1 = new ArrayList<String>();
        while(resultSet.next())
        {
            Filename1.add(resultSet.getString("Filename"));
            date.add(resultSet.getString("dt"));
            remark1.add(resultSet.getString("remark"));
        }
        OutputStream outputStream = s.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(Filename1);
        objectOutputStream.writeObject(date);
        objectOutputStream.writeObject(remark1);
        String ch = in.readUTF();
        while(ch.equals("back"))
        {
            ch = in.readUTF();
            if(ch.equals("Download")){
                String Filename =  st + "/" + subject + "/" + in.readUTF();
                up(s, in, out, Filename);
            }
        }
      }catch(SQLException ex){
          System.err.println(ex);
      }catch(IOException i){
          System.err.println(i);
      } finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
      }
    }
}
