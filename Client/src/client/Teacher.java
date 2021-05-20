/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author hp
 */
public class Teacher {
        void rec(Socket sock,DataInputStream in,DataOutputStream out,String name)throws IOException
    {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
                String FILE_TO_RECEIVED = "D://" + name;
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
    void myfiles(Socket sock,DataInputStream in,DataOutputStream out)
    {
        InputStream inputStream ;
        ObjectInputStream objectInputStream;
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try{

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        inputStream = sock.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
            ArrayList<String> Filename1 = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> date = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> remark1 = (ArrayList<String>) objectInputStream.readObject();
            for (int i = 0; i < Filename1.size(); i++) {
                System.out.println(Filename1 + "\t" + date + "\t" + remark1 + "\n");
            }
            System.out.println("1)Upload \n 2)Download  \n 3)Delete \n 4)Back \n");
            while(true){
            Scanner s = new Scanner(System.in);
            String ch = s.nextLine();
            out.writeUTF(ch);
            String Filename,path,dpath;
            if(ch.equals("Back"))
                break;
            switch(ch)
            {
                case "1":
                    System.out.println("Enter File name:");
                    Filename = s.nextLine();
                    out.writeUTF(Filename);
                    System.out.println("Enter src File path:");
                    path = s.nextLine() + Filename;
                    System.out.println("Enter remark");
                    String remark = s.nextLine();
                    out.writeUTF(remark);
                    up(sock, in, out, path);
                    break;
                case "2":
                    System.out.println("Enter File to be Downloaded:");
                    Filename = s.nextLine();
                    out.writeUTF(Filename);
                    System.out.println("Enter destination File path:");
                    dpath = s.nextLine() + Filename;
                    rec(sock, in, out, dpath);
                    break;
                case "3":System.out.println("Enter File Name:");
                    Filename = s.nextLine();
                    out.writeUTF(Filename);
                    break;
               /* case "3":System.out.println("Enter File Name:");
                    Filename = s.nextLine();
                    System.out.println("Enter File path:");
                    path = s.nextLine() + Filename;
                    out.writeUTF(Filename);
                    break;*/
                case "4":break;
            }  
            }
        }catch(ClassNotFoundException ex)
        {System.err.println(ex);
        }
        catch(IOException i)
        { }
    }
    void mypublicfiles(Socket sock,DataInputStream in,DataOutputStream out)
    {
        InputStream inputStream ;
        ObjectInputStream objectInputStream;
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try{

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        inputStream = sock.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
            ArrayList<String> Filename1 = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> date = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> remark1 = (ArrayList<String>) objectInputStream.readObject();
            for (int i = 0; i < Filename1.size(); i++) {
                System.out.println(Filename1 + "\t" + date + "\t" + remark1 + "\n");
            }
            System.out.println("1)Upload \n 2)Download \n 3)Delete\n 4)Back \n");
            while(true){
            Scanner s = new Scanner(System.in);
            String ch = s.nextLine();
            out.writeUTF(ch);
            String Filename,path;
            switch(ch)
            {
                case "1":
                    System.out.println("Enter File name:");
                    Filename = s.nextLine();
                    out.writeUTF(Filename);
                    System.out.println("Enter File path:");
                    path = s.nextLine() + Filename;
                    System.out.println("Enter remark");
                    String remark = s.nextLine();
                    out.writeUTF(remark);
                    up(sock, in, out, path);
                    break;
                case "2":
                    System.out.println("Enter File to be Downloaded:");
                    Filename = s.nextLine();
                    out.writeUTF(Filename);
                    rec(sock, in, out, Filename);
                    break;
              /*  case "3":System.out.println("Enter Folder Name:");
                    String Folname = s.nextLine();
                    out.writeUTF(Folname);
                    break;*/
                case "3":System.out.println("Enter File Name:");
                    Filename = s.nextLine();
                    out.writeUTF(Filename);
                    break;
                case "4":return;
            }  
            }
        }catch(ClassNotFoundException ex)
        {System.err.println(ex);
        }
        catch(IOException i)
        { }
    }
    void viewsF(Socket sock,DataInputStream in,DataOutputStream out)
    {
        InputStream inputStream ;
        ObjectInputStream objectInputStream;
        Scanner s = new Scanner(System.in);
        try{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        inputStream = sock.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
        System.out.println("To view classes press 1");
        String o = s.nextLine();
        out.writeUTF(o);
        if(o.equals("1"))
        {
            ArrayList<String> classes = (ArrayList<String>) objectInputStream.readObject();
            System.out.println("Classes and batches:");
            for (int i = 0; i < classes.size(); i++) {
                System.out.println(classes.get(i));
            } 
        }
        System.out.println("Enter Class or Practicals:");
        String name = s.nextLine();
        out.writeUTF(name);
        System.out.println("Enter subject:");
        String subject = s.nextLine();
        //out.writeUTF(subject);
        ArrayList<String> f = (ArrayList<String>) objectInputStream.readObject();
            System.out.println("Students in class:");
            for (int i = 0; i < f.size(); i++) {
                System.out.println(f.get(i));
            }
            while(true){
            System.out.println("\n\nEnter Student roll no \n To return Type back");
            String ch = s.nextLine();
            out.writeUTF(ch);
            if(ch.equals("back"))
            {
                out.writeUTF(ch);
                break;
            }
            if(f.contains(ch))
            {
                out.writeUTF(ch);
            }
            else{
                System.out.println("Invalid Roll no");
                continue;
            }
            ArrayList<String> Filename1 = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> date = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> remark1 = (ArrayList<String>) objectInputStream.readObject();
            for (int i = 0; i < Filename1.size(); i++) {
                System.out.println(Filename1 + "\t" + date + "\t" + remark1 + "\n");
            }
            while(true){
            System.out.println("\n\nTo Download Type Download \n To return Type back");
                ch = s.nextLine();
                out.writeUTF(ch);
                    switch(ch)
                    {
                        case "Download":
                            System.out.println("Enter File to be Downloaded");
                            String Filename = s.nextLine();
                            out.writeUTF(Filename);
                            rec(sock, in, out, Filename);
                            break;
                        case "back":break;
                    }  
                    if(ch.equals("back"))
                    {
                        break;
                    }
                }
            }
        }
        catch(ClassNotFoundException c){}
        catch(IOException i){}
    }
}
