/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.nio.file.Files; 
import java.nio.file.*; 
/**
 *
 * @author hp
 */
public class filesystem {
    String path;
        public filesystem() {
          try
      {
          FileInputStream fis;
            fis = new FileInputStream("C:/Users/hp/Documents/NetBeansProjects/Admin/Path.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         path = (String) ois.readObject();
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
    }
    String getpath()
    {
        return path;
    }
    void crdir(String dpath)
    {
        dpath = path + dpath;
      //Creating a File object
      File file = new File(dpath);
      //Creating the directory
      boolean bool = file.mkdir();
      if(bool){
         System.out.println("Directory created successfully");
      }else{
         System.out.println("Sorry couldn’t create specified directory");
      }
    }
    void crfile(String dpath) {
    try {
        dpath = path + dpath;
      File myObj = new File(dpath);
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    }
  void deleteFolder(File file){
      for (File subFile : file.listFiles()) {
         if(subFile.isDirectory()) {
            deleteFolder(subFile);
         } else {
            subFile.delete();
         }
      }
      file.delete();
   }
   void deldir(String filePath) {
      //Creating the File object
      filePath = path + filePath;
      File file = new File(filePath);
      deleteFolder(file);
      System.out.println("Files deleted........");
   }
   void movdir(String src,String dest)
   {
       src = path + src;
       dest = path + dest;
       try{
        Path temp = Files.move(Paths.get(src),Paths.get(dest)); 
        if(temp != null) 
        { 
            System.out.println("File renamed and moved successfully"); 
        } 
        else
        { 
            System.out.println("Failed to move the file"); 
        } 
       }
       catch(IOException i)
       {
       }
   }}
