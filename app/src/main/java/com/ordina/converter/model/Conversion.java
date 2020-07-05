package com.ordina.converter.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Conversion extends RealmObject {
        private String firstCurrency;
        private String secondCurrency;
        private Double amount;
        private Double result;
        private Date exchangeRatesDate;
        private Date conversionDate;

        public Conversion(){
        }



        public String getFirstCurrency() {
                return firstCurrency;
        }

        public void setFirstCurrency(String firstCurrency) {
                this.firstCurrency = firstCurrency;
        }

        public String getSecondCurrency() {
                return secondCurrency;
        }

        public void setSecondCurrency(String secondCurrency) {
                this.secondCurrency = secondCurrency;
        }

        public Double getAmount() {
                return amount;
        }

        public void setAmount(Double amount) {
                this.amount = amount;
        }

        public Double getResult() {
                return result;
        }

        public void setResult(Double result) {
                this.result = result;
        }

        public Date getExchangeRatesDate() {
                return exchangeRatesDate;
        }

        public void setExchangeRatesDate(Date exchangeRatesDate) {
                this.exchangeRatesDate = exchangeRatesDate;
        }

        public Date getConversionDate() {
                return conversionDate;
        }

        public void setConversionDate(Date conversionDate) {
                this.conversionDate = conversionDate;
        }
}
