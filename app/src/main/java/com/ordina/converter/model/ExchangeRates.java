package com.ordina.converter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangeRates {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("PreviousDate")
    @Expose
    private String previousDate;
    @SerializedName("PreviousURL")
    @Expose
    private String previousURL;
    @SerializedName("Timestamp")
    @Expose
    private String timestamp;
    @SerializedName("Valute")
    @Expose
    private Valute valute;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getPreviousURL() {
        return previousURL;
    }

    public void setPreviousURL(String previousURL) {
        this.previousURL = previousURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Valute getValute() {
        return valute;
    }

    public void setValute(Valute valute) {
        this.valute = valute;
    }
}