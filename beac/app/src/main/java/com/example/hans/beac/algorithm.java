package com.example.hans.beac;

import java.util.ArrayList;

/**
 * Created by Hans on 2017-05-13.
 */
public class algorithm{

    ArrayList<RP>rps=new ArrayList<>();
    void dijik(){

    }
    public static int knn(ArrayList<KalmanFilter>kfs,double val){

        int tore=0;
        double value=val;
        for(int k=0;k<init.rps.size();k++){
            double temp=0;
            for (int i = 0; i < kfs.size(); i++) {
                temp+=Math.pow(init.rps.get(k).bea.get(i).rssi-Math.abs(kfs.get(i).X),2);
                if(value>temp){
                    value=temp;
                    tore=k+1;
                }



            }
        }
        return tore;


    }
}
