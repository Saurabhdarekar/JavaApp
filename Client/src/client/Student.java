/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.util.Scanner;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class Student {
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
    void up(Socket sock,DataInputStream in1,DataOutputStream out1,String name)throws IOException
    {
        String myFile = name;
    DataOutputStream dos = new DataOutputStream(sock.getOutputStream()); //get the output stream of the socket
    dos.writeInt((int) myFile.length()); //write in the length of the file

    InputStream in = new FileInputStream(myFile); //create an inputstream from the file
    OutputStream out = sock.getOutputStream(); //get output stream
    byte[] buf = new byte[8192]; //create buffer
    int len = 0;
    while ((len = in.read(buf)) != -1) {
        out.write(buf, 0, len); //write buffer
    }
    in.close(); //clean up
    out.close();
    }
    void viewtF(Socket sock,DataInputStream in,DataOutputStream out)
    {
        try{

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Teachers Name:");
        String name = br.readLine();
        out.writeUTF(name);
            InputStream inputStream ;
            ObjectInputStream objectInputStream;
            inputStream = sock.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            ArrayList<String> Filename = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> date = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> remark = (ArrayList<String>) objectInputStream.readObject();
            for (int i = 0; i < Filename.size(); i++) {
                System.out.println(Filename + "\t" + date + "\t" + remark + "\n");
            }
            System.out.println("\n\nTo Download Type Filename \n To return Type back");
            while(true){
            Scanner s = new Scanner(System.in);
            String ch = s.nextLine();
            out.writeUTF(ch);
            if(ch.equals("back"))
            {
                out.writeUTF(ch);
                break;
            }
            if(Filename.contains(ch))
            {
                out.writeUTF(ch);
                rec(sock, in, out, ch);
            }
            else{
                System.out.println("Invalid File name");
            }
            }
        }
        catch(ClassNotFoundException ex)
        {System.err.println(ex);
        }
        catch(IOException i)
        {
            
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
            ArrayList<String> Filename = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> date = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> remark = (ArrayList<String>) objectInputStream.readObject();
            for (int i = 0; i < Filename.size(); i++) {
                System.out.println(Filename + "\t" + date + "\t" + remark + "\n");
            }
            while(true){
            System.out.println("1)Upload \n 2)Download \n 4)Delete \n 5)Back \n");
            
            while(true){
                Scanner s = new Scanner(System.in);
            String ch = s.nextLine();
            out.writeUTF(ch);
            String Fname,path,dpath;
            switch(ch)
            {
                case "1":
                    System.out.println("Enter File name:");
                    Fname = s.nextLine();
                    out.writeUTF(Fname);
                    System.out.println("Enter src File path:");
                    path = s.nextLine() + Fname;
                    System.out.println("Enter remark");
                    String remark1 = s.nextLine();
                    out.writeUTF(remark1);
                    up(sock, in, out, path);
                    break;
                case "2":
                    System.out.println("Enter File to be Downloaded:");
                    Fname = s.nextLine();
                    out.writeUTF(Fname);
                    System.out.println("Enter destination File path:");
                    dpath = s.nextLine() + Filename;
                    rec(sock, in, out, dpath);
                    break;
                case "3":System.out.println("Enter File Name:");
                    Fname = s.nextLine();
                    System.out.println("Enter File path:");
                    path = s.nextLine() + Fname;
                    out.writeUTF(Fname);
                    break;
                case "4":break;
            }  
            if(ch.equals("back"))
            {
                out.writeUTF(ch);
                break;
            }
            }
            }
        }        catch(ClassNotFoundException ex)
        {System.err.println(ex);
        }
        catch(IOException i)
        { }
    }
    void sub(Socket sock,DataInputStream in,DataOutputStream out)throws IOException
    {
        String st = "";
        Scanner c = new Scanner(System.in);
        System.out.println("Subjects:");
        try{
        InputStream inputStream = sock.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ArrayList<String> arr = (ArrayList<String>) objectInputStream.readObject();
        for (int i = 0; i < arr.size(); i++) {
            st = in.readUTF();
            arr.add(st);
            System.out.println(st);
        }
        System.out.println("Type Subject name to select:");
        st = c.nextLine();
        out.writeUTF(st);
            ArrayList<String> Filename1 = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> date = (ArrayList<String>) objectInputStream.readObject();
            ArrayList<String> remark1 = (ArrayList<String>) objectInputStream.readObject();
            if(Filename1.size()==0)
            {
                System.out.println("No Files");
            }
            else
            {
            for (int i = 0; i < Filename1.size(); i++) {
                System.out.println(Filename1 + "\t" + date + "\t" + remark1 + "\n");
            }
            }
        
        System.out.println(" 1) Upload\n 2)Download\n 3)Move from myfiles:");

        String remark;
        while(true){
        st = c.nextLine();
        out.writeUTF(st);
        switch(st){
            case "1":System.out.println("Enter Filename:");
                String Filename = c.nextLine();
                System.out.println("Enter Path to upload:");
                String path = c.nextLine();
                out.writeUTF(Filename);
                System.out.println("Enter remark:");
                remark = c.nextLine();
                out.writeUTF(remark);
                path = path + Filename;
                up(sock, in, out, path);
                break;
            case "2":System.out.println("Enter Filename:");
                st = c.nextLine();
                out.writeUTF(st);
                System.out.println("Enter remark:");
                remark = c.nextLine();
                out.writeUTF(remark);
                rec(sock, in, out, st);
                break;
            case "3":System.out.println("Enter Filename:");
                st = c.nextLine();
                System.out.println("Enter new name:");
                String mpath = c.nextLine();
                System.out.println("Enter remark:");
                remark = c.nextLine();
                out.writeUTF(st);
                out.writeUTF(mpath);
                out.writeUTF(remark);
                break;
        }
                if(st.equals("back"))
                {
                    break;
                }
        }
        }catch(ClassNotFoundException ex)
        {System.err.println(ex);
        }
    }
}
