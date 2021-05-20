/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author hp
 */


class Filelist
{
        void print()
        {

        }
}
class node {
    String Filename;
    //node[] arr;
    ArrayList<node> arr;
    public node(String name){
        this.Filename = name;
        arr = new ArrayList<node>();
    }
}
public class tree implements Serializable{
    private static final long serialVersionUID = -5399605122490343339L;
    node head;
    public tree(String name) {
        this.head = new node(name);
    }
    void add(String file)
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
                node n = new node(list[i]);
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
        if(n.Filename.equals(""))
        {
            return; 
        }
        else
        {
            System.out.println(n.Filename);
            for (int i = 0; i < n.arr.size(); i++) {
               this.print(temp.arr.get(i));
            }
        }
        return;
    }
}

