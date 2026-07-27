package model;

import java.sql.Time;
import java.sql.Timestamp;

public class sale {
    private  int saleId;
    private int staffId;
    private  Timestamp saleDate;


    public sale(int saleId, int staffId, Timestamp saleDate){
        this.saleId = saleId;
        this.staffId = staffId;
        this.saleDate = saleDate;
    }

    public void setSaleId(int saleId){
        this.saleId = saleId;
    }
    public void setStaffId(int staffId){
        this.staffId = staffId;
    }
    public void setSaleDate(Timestamp saleDate){
        this.saleDate = saleDate;
    }
    public int  getSaleId ()
    {
        return saleId;
    }
    public int getStaffId()
    {
        return staffId;
    }
    public Timestamp getSaleDate() {
        return saleDate;
    }
}
