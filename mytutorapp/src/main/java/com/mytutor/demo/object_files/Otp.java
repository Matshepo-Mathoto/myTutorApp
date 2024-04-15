package com.mytutor.demo.object_files;

public class Otp {
    private int otp;
    public Otp(int otp)
    {
        setOtp(otp);
    }
    public void setOtp(int otp){this.otp = otp;}
    public int getOtp(){return otp;}
    public boolean equals(Otp other)
    {
        if(this.otp == other.getOtp()){return true;}
        return false;
    }
}
