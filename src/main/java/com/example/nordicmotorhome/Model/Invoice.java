package com.example.nordicmotorhome.Model;


import com.example.nordicmotorhome.Admin;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Invoice{

    private int invoice_ID;

    //Referencing to a specific booking
    private int booking_ID;

    //the seasonal percent for the booking
    private int season_percent;

    //Amount * extraPrice. is set outside class
    private int extra;
    //sum of Distance to pickup, and drop off, if not 0
    private double outsideKmFee;

    //Fee is combined of multiple fees, outsideKMFee, extraKmFee, FuelFee
    private double fee;

    //Final Price, starts as the basePrice, then accumulated with all fees, and extra charges.
    //if cancellation then, certain percentage of that accumulated price is final price
    private double price;


    //A Check after rent, extraKm and fuelCheck By mechanic
    //should be able to register it, then invoice is either updated or generated
    private int extra_km;
    private boolean fuelCheck;

    //constructors
    public Invoice() {fuelCheck=false; }
    public Invoice(int booking_ID, int season, double price,int extra, double fee) {
        this.booking_ID = booking_ID;
        this.season_percent = season;
        this.price = price;
        this.extra = extra;
        this.fee = fee;


        //wil always start as 0 and false
        extra_km = 0;
        fuelCheck = false;
    }

    //Getters & Setters
    public int getInvoice_ID() { return invoice_ID; }
    public void setInvoice_ID(int invoice_ID) { this.invoice_ID = invoice_ID; }

    public int getBooking_ID() { return booking_ID; }
    public void setBooking_ID(int booking_ID) { this.booking_ID = booking_ID; }

    public int getSeason() { return season_percent; }
    public void setSeason(int season) { this.season_percent = season; }

    public int getExtra_km() { return extra_km; }
    public void setExtra_km(int extra_km) { this.extra_km = extra_km; }

    public double getFee() { return fee; }
    public void setFee(double fee) {
        if(fee == 0) {
            this.fee = fee;
        }
        else this.fee += fee;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getSeason_percent() { return season_percent; }
    public void setSeason_percent(int season_percent) { this.season_percent = season_percent; }

    public double getOutsideKmFee() { return outsideKmFee; }
    public void setOutsideKmFee(double outsideKmFee) { this.outsideKmFee = outsideKmFee; }

    public int getExtra() { return extra; }
    public void setExtra(int extra) { this.extra = extra; }

    public boolean isFuelCheck() { return fuelCheck; }
    public void setFuelCheck(boolean fuelCheck) { this.fuelCheck = fuelCheck; }

    //methods Calculator
    public void updateInvoice(int seasonP,Admin admin, Booking booking){
        DecimalFormat deci = new DecimalFormat("##.##");
        fee = 0;
        price = admin.getBasePrice();

        season_percent = seasonP;

        //Setting the seasonal percentage
        if(season_percent != 0) {
            price = price + (price * ((double) season_percent / 100));
        }

        //multiplying number of days with price per day
        price = (price * booking.getDaysTotal());

        if(booking.getExtras() != 0) {
            extra = booking.getExtras() * admin.getExtraPrice();
            fee = extra;
        }



        //OutsideKMFee
        if(booking.getTotalKm() != 0) {
            outsideKmFee = booking.getTotalKm() * admin.getCollectFee();
            outsideKmFee = Double.parseDouble(deci.format(outsideKmFee).replace(",","."));
            fee += outsideKmFee;
        }



        //FuelCheck And Extra KM

            if(fee == 0){
                if(fuelCheck) {
                    fee += admin.getFuelFee();
                }
                if(extra_km != 0){
                    fee += extra_km * admin.getKmFee();
                }
            }
            else {
                if(fuelCheck) {
                    fee += admin.getFuelFee();
                }
                if(extra_km != 0) {
                    fee += (extra_km * admin.getKmFee());
                }
            }
            price = Double.parseDouble(deci.format(price).replace(",","."));
            fee = Double.parseDouble(deci.format(fee).replace(",","."));

            price += fee;
    }

}

