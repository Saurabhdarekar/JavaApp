/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
/**
 *
 * @author hp
 */
class Filelist
{
        ArrayList<String> Filename = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> remark = new ArrayList<String>();
}
class node {
    String Filename;
    boolean isfile;
    //node[] arr;
    ArrayList<node> arr;
    public node(String name,boolean is){
        this.Filename = name;
        this.isfile = is;
        arr = new ArrayList<node>();
    }
}
public class tree implements Serializable{
    private static final long serialVersionUID = -5399605122490343339L;
    node head;
    public tree(String name,boolean is) {
        this.head = new node(name,is);
    }
    void add(String file,boolean is)
    {
        String[] list = file.split("/");
        int len = list.length;
        int i;
        node temp = head;
        for(i=1;i<len;i++)
        {
            int loc = this.find(list[i],temp);
            if(loc == -1)
            {
                node n = new node(list[i],is);
                temp.arr.add(n);
                temp = n;
                System.out.println(loc);
            }
            else
            {
                if(i == len-1)
                {
                    System.out.println("Already");
                    break;
                }
                temp = temp.arr.get(loc);
            }
        }
    }
    int find(String s,node n)
    {
        int loc=-1;
        for(int i = 0; i < n.arr.size(); i++)
        {
            node a = n.arr.get(i);
            if(a.Filename.equals(s))
            {
                loc = i;
                return loc;
            }
        }
        return loc;
    }
    void print(node n)
    {
        node temp = n;
        if(n.Filename.equals("null"))
        {
            return; 
        }
        else
        {
            if(n.isfile){
            System.out.println(n.Filename);
            }
            for (int i = 0; i < n.arr.size(); i++) {
               this.print(temp.arr.get(i));
            }
        }
        return;
    }
    void del(String filepath)
    {
        String[] list = filepath.split("/");
        int len = list.length;
                int i;
        node temp = head;
        for(i=1;i<len;i++)
        {
            if(len == 1)
            {
                temp = null;
                break;
            }
            int loc = this.find(list[i],temp);
                if(i == len-1)
                {
                    temp.arr.remove(loc);
                    break;
                }
                temp = temp.arr.get(loc);
        }
    }
}

