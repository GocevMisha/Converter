package com.ordina.converter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Valute {
    public ValuteModel getUsd() {
        return usd;
    }

    public void setUsd(ValuteModel usd) {
        this.usd = usd;
    }

    public ValuteModel getEur() {
        return eur;
    }

    public void setEur(ValuteModel eur) {
        this.eur = eur;
    }

    public ValuteModel getJpy() {
        return jpy;
    }

    public void setJpy(ValuteModel jpy) {
        this.jpy = jpy;
    }

    @SerializedName("USD")
    @Expose
    private ValuteModel usd;
    @SerializedName("EUR")
    @Expose
    private ValuteModel eur;
    @SerializedName("JPY")
    @Expose
    private ValuteModel jpy;


}
