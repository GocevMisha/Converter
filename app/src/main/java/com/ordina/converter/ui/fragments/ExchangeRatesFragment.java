package com.ordina.converter.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ordina.converter.R;
import com.ordina.converter.api.NetworkService;
import com.ordina.converter.model.ExchangeRates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    public ExchangeRatesFragment() {
    }
    private FrameLayout dateFrameLayout;
    private TextView dateTextView;
    private TextView usdTextView;
    private TextView eurTextView;
    private TextView jpyTextView;
    private TextView errorTextView;
    private LinearLayout resultLinearLayout;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_rates, container, false);

        dateFrameLayout = view.findViewById(R.id.date_fl);
        dateTextView = view.findViewById(R.id.date_tv);
        usdTextView = view.findViewById(R.id.first_tv);
        eurTextView = view.findViewById(R.id.eur_tv);
        jpyTextView = view.findViewById(R.id.jpy_tv);
        errorTextView = view.findViewById(R.id.error_tv);
        resultLinearLayout = view.findViewById(R.id.result_ll);
        calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateTextView.setText(dateFormat.format(date));
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dateFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        NetworkService.getInstance()
                .getConverterApi()
                .getLastRates()
                .enqueue(new Callback<ExchangeRates>() {
                    @Override
                    public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                      setRates(response);
                    }
                    @Override
                    public void onFailure(Call<ExchangeRates> call, Throwable t) {
                        resultLinearLayout.setVisibility(View.GONE);
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText("Курс ЦБ РФ на данную дату не установлен");
                    }
                });


        return view;
    }

    private void setRates(Response<ExchangeRates> response){
        if(response.code()==404){
            resultLinearLayout.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Курс ЦБ РФ на данную дату не установлен");
            return;
        }
        if(response.body()!=null) {
            errorTextView.setVisibility(View.GONE);
            resultLinearLayout.setVisibility(View.VISIBLE);
            ExchangeRates exchangeRates = response.body();
            usdTextView.setText(exchangeRates.getValute().getUsd().getValue()+"₽");
            eurTextView.setText(exchangeRates.getValute().getEur().getValue()+"₽");
            jpyTextView.setText(exchangeRates.getValute().getJpy().getValue()+"₽");
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(exchangeRates.getDate());
                calendar.setTime(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                dateTextView.setText(dateFormat.format(date));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            resultLinearLayout.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Ошибка");
        }

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateTextView.setText(dateFormat.format(date));

        SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayDateFormat = new SimpleDateFormat("dd");
        NetworkService.getInstance()
                .getConverterApi()
                .getExchangeRates(yearDateFormat.format(date), monthDateFormat.format(date), dayDateFormat.format(date))
                .enqueue(new Callback<ExchangeRates>() {
                    @Override
                    public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                        setRates(response);
                    }

                    @Override
                    public void onFailure(Call<ExchangeRates> call, Throwable t) {
                        resultLinearLayout.setVisibility(View.GONE);
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText("Ошибка");
                    }
                });
    }
}