/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package area_of_rectangle;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

class rectangle
{
    int lower_x;
    int lower_y;
    int upper_x;
    int upper_y;
    
    static void SortlowerX(Vector<rectangle> rect,int N)
    {
        int flag = 1;
        rectangle temp;
        for(int i=0;i< N && flag==1;i++)
        {
            flag = 0;
            for (int j=0;j <N-1;j++)
            {
                if (rect.elementAt(j).lower_x > rect.elementAt(j+1).lower_x )
                {
                    Collections.swap(rect,j,j+1);
                    flag = 1;
                }
            }
        }
    }
    
    static void SortupperX(Vector<rectangle> rect,int N)
    {
        int flag = 1;
        rectangle temp;
        for(int i=0;i< N && flag==1;i++)
        {
            flag = 0;
            for (int j=0;j <N-1;j++)
            {
                if (rect.elementAt(j).upper_x > rect.elementAt(j+1).upper_x )
                {
                    Collections.swap(rect,j,j+1);
                    flag = 1;
                }
            }
        }
    }
    
    static void add_rec(rectangle rect, Vector<rectangle> rects)
    {
        if (rects.size() == 0)
        {
            rects.add(rect);
        }
        else
        {
            int index = bin_search(rects, 0, rects.size()-1, rect);
            rects.add(index, rect);
        }
    }
    
    static void remove_rec(rectangle rect, Vector<rectangle> rects)
    {
        int index = bin_search(rects, 0, rects.size()-1, rect);
        rects.remove(index);
    }
    
    
    static int bin_search(Vector<rectangle> list, int low, int high, rectangle rec)
    {
        int mid = (low+high)/2;
        if (list.elementAt(mid).lower_y == rec.lower_y)
        {
            if (list.elementAt(mid).upper_y == rec.upper_y)
                return mid;
            else if (list.elementAt(mid).upper_y < rec.upper_y)
            {
                if (mid == high) return  mid+1;
                return bin_search(list,mid+1,mid,rec);
            }
            else
            {
                if (mid == low)return mid;
                return bin_search(list,low,mid-1,rec);
            }
        }
        else if (list.elementAt(mid).lower_y < rec.lower_y)
        {
            if (mid == high)
            {
                return mid+1;
            }
            return bin_search(list, mid+1,high, rec);
        }
        else
        {
            if (mid == low)
            {
                return  mid;
            }
            return bin_search(list, low, mid-1, rec);
        }
    }
    
    // calculate the total vertical length covered by rectangles in the active set
    static int sweep(Vector<rectangle> active)
    {
        int n = active.size();
        int length = 0;
        int start, end;
        int i = 0;
        while (i < n)
        {
            start = active.elementAt(i).lower_y;
            end = active.elementAt(i).upper_y;
            while (i < n && active.elementAt(i).lower_y <= end)
            {
                if (active.elementAt(i).upper_y > end)
                {
                    end = active.elementAt(i).upper_y;
                }
                i++;
            }
            length =length + end - start;
        }
        return length;
    }

    static int horizontal_sweep(Vector<rectangle> active)
    {
        int n = active.size();
        int length = 0,count=0;
        int next, end,start,upper=0,lower=0;
        for(int i=0;i<n;i++)
        {
            length = (active.elementAt(0).upper_y-active.elementAt(0).lower_y);
            start = active.elementAt(i).lower_y;
            if(i<n-1) next = active.elementAt(i+1).lower_y;
            else next = 999999;
            end = active.elementAt(i).upper_y;
            if(next<end) 
            {
                if(count==0) 
                {
                    lower=start;
                    if(end>upper) upper=end;
                }
                count++;
            }
            else 
            {
                count--;
                if(end>upper) upper=end;
                if (count==0) length =  (upper-lower);
               
            }
        }
        if(count>0)length =  (upper-lower);
        return length;
    }
    
    static double UnionArea( Vector<rectangle> rects, int N)
    {
        double area=0.0;
        Vector<rectangle> left = new Vector(rects);
        Vector<rectangle> right = new Vector(rects);
        SortlowerX(left,N);
        SortupperX(right,N);
        
        Vector<rectangle> active = new Vector<>();
        int current = left.elementAt(0).lower_x;
        int next=left.elementAt(0).lower_x;
        int i = 0, j = 0;
        
        while (j < N)
        {
            // add all recs that start at current
            while (i < N && left.elementAt(i).lower_x == current)
            {
                // add left[i] to Active set
                add_rec(left.elementAt(i), active);
                i++;
                
            }
            // remove all recs that end at current
            while (j < N && right.elementAt(j).upper_x == current)
            {
                // remove right[j] from Active set
                remove_rec(right.elementAt(j), active);
                j++;
            }
            // find next event x
            if (i < N && j < N)
            {
                if (left.elementAt(i).lower_x <= right.elementAt(j).upper_x)
                {
                    next = left.elementAt(i).lower_x;
                }
                else 
                {
                    next = right.elementAt(j).upper_x;
                }
            }
            else if (j < N)
            {
                next = right.elementAt(j).upper_x;
            }
            // distance to next event
            int horizontal = next - current;
            // figure out vertical dist
            // int vertical = sweep(active);
            int vertical = horizontal_sweep(active);
            //System.out.println("hori : "+horizontal);
            //System.out.println("vert : "+vertical);
            area += vertical * horizontal;

            current = next;
        }
        return area;
    }
    
    
}

public class Area_of_rectangle {

   public static Scanner in;
    public static void main(String[] args) throws IOException 
    {
        in = new Scanner(new File("input.txt"));
        int T=in.nextInt();
        for(int l=0;l<T;l++)
        {
            int N=in.nextInt();
            Vector<rectangle> rects = new Vector<>();
            for(int i=0;i<N;i++)
            {
                 rectangle rect=new rectangle();
                 rect.lower_x=in.nextInt();
                 rect.lower_y=in.nextInt();
                 rect.upper_x=in.nextInt();
                 rect.upper_y=in.nextInt();
                 rects.addElement(rect);
            }
            double area=rectangle.UnionArea(rects,N);
            System.out.println("Area : "+area);
            System.out.println();
        }
    }
    
}
