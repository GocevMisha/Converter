package com.ordina.converter.api;

import com.ordina.converter.model.ExchangeRates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ConverterApi {
    @GET("/archive/{year}/{month}/{day}/daily_json.js")
    Call<ExchangeRates> getExchangeRates(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    @GET("/daily_json.js")
    Call<ExchangeRates> getLastRates();
}
