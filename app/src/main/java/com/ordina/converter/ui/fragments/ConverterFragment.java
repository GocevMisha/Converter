package com.ordina.converter.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ordina.converter.R;
import com.ordina.converter.api.NetworkService;
import com.ordina.converter.model.Conversion;
import com.ordina.converter.model.ExchangeRates;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConverterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    String[] currencies = {"EUR", "USD", "RUB", "JPY"};
    public ConverterFragment() {
    }

    private EditText amountEditText;
    private Spinner firstCurrencySpinner;
    private Spinner secondCurrencySpinner;
    private FrameLayout dateFrameLayout;
    private Button convertButton;
    private TextView resultTextView;
    private TextView dateTextView;
    private Calendar calendar;
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        realm = Realm.getDefaultInstance();
        amountEditText = view.findViewById(R.id.amount_tv);
        firstCurrencySpinner = view.findViewById(R.id.first_currency_sp);
        secondCurrencySpinner = view.findViewById(R.id.second_currency_sp);
        dateFrameLayout = view.findViewById(R.id.date_fl);
        convertButton = view.findViewById(R.id.convert_btn);
        resultTextView = view.findViewById(R.id.result_tv);
        dateTextView = view.findViewById(R.id.date_tv);


        final ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, currencies);
        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstCurrencySpinner.setAdapter(firstAdapter);
        firstCurrencySpinner.setSelection(1);

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, currencies);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondCurrencySpinner.setAdapter(firstAdapter);
        secondCurrencySpinner.setSelection(2);
        calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateTextView.setText(dateFormat.format(date));
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dateFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthDateFormat = new SimpleDateFormat("MM");
                SimpleDateFormat dayDateFormat = new SimpleDateFormat("dd");
                final Date date = new Date(calendar.getTimeInMillis());
                NetworkService.getInstance()
                        .getConverterApi()
                        .getExchangeRates(yearDateFormat.format(date), monthDateFormat.format(date), dayDateFormat.format(date))
                        .enqueue(new Callback<ExchangeRates>() {
                            @Override
                            public void onResponse(Call<ExchangeRates> call, Response<ExchangeRates> response) {
                                if(response.code()==404){
                                    resultTextView.setText("Курс ЦБ РФ на данную дату не установлен");
                                    return;
                                }
                                if(response.body()!=null) {
                                    ExchangeRates exchangeRates = response.body();
                                    String firstCurrency = (String) firstCurrencySpinner.getSelectedItem();
                                    String secondCurrency = (String) secondCurrencySpinner.getSelectedItem();
                                    double amount = Double.parseDouble(amountEditText.getText().toString());
                                    double result = 0;
                                    if (firstCurrency.equals(secondCurrency)) {
                                        result = amount;
                                    } else {
                                        if (firstCurrency.equals("RUB"))
                                            result = convertFromRouble(amount, getRate(exchangeRates, secondCurrency));
                                        else {
                                            if (secondCurrency.equals("RUB"))
                                                result = convertToRouble(amount, getRate(exchangeRates, firstCurrency));
                                            else {
                                                result = convert(amount, getRate(exchangeRates, firstCurrency), getRate(exchangeRates, secondCurrency));
                                            }
                                        }
                                    }
                                    resultTextView.setText(String.format("%.2f %n", result));
                                    realm.beginTransaction();
                                    Conversion conversion = realm.createObject(Conversion.class);
                                    conversion.setAmount(amount);
                                    conversion.setFirstCurrency(firstCurrency);
                                    conversion.setSecondCurrency(secondCurrency);
                                    conversion.setResult(result);
                                    conversion.setExchangeRatesDate(calendar.getTime());
                                    conversion.setConversionDate(Calendar.getInstance().getTime());
                                    realm.commitTransaction();
                                    realm.beginTransaction();
                                    RealmResults<Conversion> realmResults = realm.where(Conversion.class).sort("conversionDate", Sort.DESCENDING).findAll();
                                    for(int i=0; i<realmResults.size(); i++){
                                        if(i>9){
                                            realmResults.get(i).deleteFromRealm();
                                        }
                                    }
                                    realm.commitTransaction();
                                } else  resultTextView.setText("Ошибка");
                            }
                            @Override
                            public void onFailure(Call<ExchangeRates> call, Throwable t) {
                                resultTextView.setText("Ошибка");
                            }
                        });
            }
        });

        return view;
    }
    private double getRate(ExchangeRates exchangeRates, String currency){
        switch (currency){
            case "EUR":
                return exchangeRates.getValute().getEur().getValue();
            case "USD":
                return exchangeRates.getValute().getUsd().getValue();
            case "JPY":
                return exchangeRates.getValute().getJpy().getValue();
        }
        return 0;
    }

    private double convert(double amount, double firstRate, double secondRate){
        double rouble = convertToRouble(amount, firstRate);
        return  convertFromRouble(rouble, secondRate);
    }
    private double convertToRouble(double amount, double rate){
        return  amount*rate;
    }
    private double convertFromRouble(double amount, double rate){
        return  amount/rate;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateTextView.setText(dateFormat.format(date));
    }
}