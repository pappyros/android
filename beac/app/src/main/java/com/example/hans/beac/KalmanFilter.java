package com.example.hans.beac;

/**
 * Created by Hans on 2017-02-23.
 */
class KalmanFilter {
    public String Kalmanid;
    private double Q = 0.00001;
    private double R = 0.001;
    public double X = 0, P = 1, K;


    //첫번째값을 입력받아 초기화 한다. 예전값들을 계산해서 현재값에 적용해야 하므로 반드시 하나이상의 값이 필요하므로~
    KalmanFilter(double initValue,String Kalmanid) {
        this.Kalmanid=Kalmanid;
        X = initValue;
    }

    //예전값들을 공식으로 계산한다
    private void measurementUpdate(){
        K = (P + Q) / (P + Q + R);
        P = R * (P + Q) / (R + P + Q);
    }
    //현재값을 받아 계산된 공식을 적용하고 반환한다
    public double update(double measurement){
        measurementUpdate();
        X = X + (measurement - X) * K;

        return X;
    }
}


