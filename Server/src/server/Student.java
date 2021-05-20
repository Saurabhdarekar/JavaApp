 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.FileSystem;
/**
 *
 * @author hp
 */
public class Student {
    String rollno,path;
    String name,classname,dept_name,birthdate,batch,regid;
   static final String DB_URL =
      "jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false";
   static final String DB_DRV =
      "com.mysql.jdbc.Driver";
   static final String DB_USER = "root";
   static final String DB_PASSWD = "1234";
    public Student(String roll_no) {
        rollno = roll_no;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        filesystem f = new filesystem();
        path = f.path;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement();
        String sql = "select * from student where roll_no=" + rollno + ";";
        resultSet = statement.executeQuery(sql);
        while(resultSet.next())
        {
        dept_name = resultSet.getString("dept_name");
        classname = resultSet.getString("classname"); 
        birthdate = resultSet.getString("birthdate"); 
        regid = resultSet.getString("reg_id"); 
        }
        }
        catch(SQLException ex){
          System.err.println(ex);
      }finally{
         try {
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
    }
    }
        void rec(Socket socket,DataInputStream in1,DataOutputStream out1,String name)throws IOException
    {
        String fileName = path+name;
        DataInputStream dis = new DataInputStream(socket.getInputStream()); //get the socket's input stream
        int size = dis.readInt(); //get the size of the file.
        InputStream in = socket.getInputStream(); 
        OutputStream out = new FileOutputStream(fileName); //stream to write out file
        int totalBytesRead = 0;
        byte[] buf = new byte[8192]; //buffer
        int len = 0;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len); //write buffer
        }

        out.close(); //clean up
        in.close();
    }
    void up(Socket sock,DataInputStream in,DataOutputStream out,String name)throws IOException
    {
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    String FILE_TO_SEND = path + "/" + name; 
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
    void showteacherfiles(Socket s,String Tname)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement(); 
        String sql = "select * from teachers where name='" + Tname + "';";
        resultSet = statement.executeQuery(sql);
        String regid = resultSet.getString("reg_id");
        sql = "select * from " + regid + "mypublicfiles;";
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
        }
        catch(SQLException ex){
          System.err.println(ex);
      }catch(IOException ex){
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
       void addmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    {
        Scanner c = new Scanner(System.in);
        String filename = in.readUTF();
        String dpath = rollno + "/myfiles/" + filename;
        String remark = in.readUTF();
        rec(s, in, out, dpath);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement(); 
        String sql = "insert into " + rollno + "myfiles values(" + filename + ",sysdate()," + remark + ");";
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
    }
    void downmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    { 
        String filename = in.readUTF();
        String dpath = rollno + "/myfiles/" + filename;
        up(s, in, out, dpath);  
    }
   /* void crfolmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    { 
        String path = rollno + "myfiles.ser";
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
    }*/
    void delmyf(Socket s, DataInputStream in, DataOutputStream out)throws IOException
    {
        Scanner c = new Scanner(System.in);
        String filename = in.readUTF();
        String dpath = in.readUTF() + filename;
             Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement(); 
        String sql = "delete from " + rollno + "myfiles where Filename='" + filename + ";";
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

    }
    void showmyf(Socket s)throws IOException
    {
     Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement(); 
        String sql = "select * from " + rollno + "myfiles;";
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
        }
        catch(SQLException ex){
          System.err.println(ex);
      }catch(IOException ex){
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

    void downloadtf(Socket s,String Tname,String Filename,DataInputStream in, DataOutputStream out)throws IOException
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        statement=connection.createStatement();
        String sql = "select reg_id from teachers where name=" + Tname + ";";
        resultSet = statement.executeQuery(sql);
        regid = resultSet.getString("reg_id"); 
        Filename = regid + "/mypublicfiles/" + Filename;
        }
        catch(SQLException ex){
          System.err.println(ex);
      }finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
        }
        up(s, in, out, Filename);
    }
    String subno(Socket s,DataInputStream in, DataOutputStream out)throws IOException
    {
        String num;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String subject = "";
        try{
        connection=DriverManager.getConnection
            (DB_URL,DB_USER,DB_PASSWD);
        ArrayList<String> arr = new ArrayList<String>();
        statement=connection.createStatement();
        String sql = "select * from " + rollno + "Subfol;";
        resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {                
                arr.add(resultSet.getString("subject"));
            }
        OutputStream outputStream = s.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(arr);
        subject = in.readUTF();
        sql = "select * from " + rollno + "Subfol where subject=" + subject + ";";
        resultSet = statement.executeQuery(sql);
        String Teacher = resultSet.getString("teacher");
        sql = "select * from " + rollno + subject + Teacher + ";";
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
        objectOutputStream.writeObject(Filename1);
        objectOutputStream.writeObject(date);
        objectOutputStream.writeObject(remark1);
        String Filename,remark,Filepath;
        
        while(true)
        {
            String ch = in.readUTF();
                if(ch.equals("back"))
                {
                    break;
                }
            switch(ch){
            case "1":
                Filename = in.readUTF();
                Filepath = rollno + "/" + subject + "/" + Filename;
                rec(s, in, out, Filepath);
                remark = in.readUTF();
                sql = "insert into " + rollno + subject + " values('" + Filename + "',sysdate()','" + remark + "';";
                statement.executeUpdate(sql);
                break;
            case "2":
                Filename = in.readUTF();
                Filepath = rollno + "/" + subject + "/" + Filename;
                up(s, in, out, Filepath);
                break;
            case "3":
                
                Filename = in.readUTF();
                Filepath = in.readUTF();
                remark = in.readUTF();
                Filepath = rollno + "/myfiles/" + Filename;
                String Filepath2 = rollno + "/" + subject + "/" + Filename;
                filesystem file = new filesystem();
                file.movdir(Filepath, Filepath2);
                sql = "insert into " + rollno + subject + " values('" + Filename + "',sysdate()','" + remark + "';";
                statement.executeUpdate(sql);
                break;
            }
        }
        }
        catch(SQLException ex){
          System.err.println(ex);
      } catch(IOException ex){
          System.err.println(ex);
      }finally{
         try {
        //    resultSet.close();
            statement.close();
            connection.close();
         } catch (SQLException ex) {
         }
        }
        return subject;
    }
    
}
