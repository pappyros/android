package com.example.hans.beac;

import java.util.ArrayList;

/**
 * Created by Hans on 2017-05-13.
 */
public class RP {
    int RPname,x,y;
    ArrayList<Integer>child;
    ArrayList<beac>bea;

    public RP(int A,ArrayList<beac>C,int x,int y){
        this.RPname=A;
       // this.child=B;
        this.bea=C;
        this.x=x;
        this.y=y;
    }
}
