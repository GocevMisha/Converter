package com.ordina.converter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ordina.converter.R;
import com.ordina.converter.model.Conversion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class HistoryAdapter extends RealmRecyclerViewAdapter<Conversion, HistoryAdapter.ViewHolder> {


    public HistoryAdapter(RealmResults<Conversion> conversions) {
        super(conversions, true);
    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.conversion_item, viewGroup, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, int i) {
        if(getData()!=null) {
            Conversion conversion = getData().get(i);
            viewHolder.bind(conversion);
            viewHolder.itemView.setTag(i);
        }

    }


    static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView exchangeRatesDateTextView;
        private TextView amountTextView;
        private TextView resultTextView;
        private TextView firstCurrencyTextView;
        private TextView secondCurrencyTextView;
        private TextView dateTextView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            exchangeRatesDateTextView = itemView.findViewById(R.id.exchange_rates_date_tv );
            amountTextView= itemView.findViewById(R.id.amount_tv );
            resultTextView = itemView.findViewById(R.id.result_tv );
            firstCurrencyTextView = itemView.findViewById(R.id.first_currency_tv );
            secondCurrencyTextView = itemView.findViewById(R.id.second_currency_tv );
            dateTextView = itemView.findViewById(R.id.date_tv );
        }

        private void bind(@NonNull Conversion conversion) {
            exchangeRatesDateTextView.setText("По курсу на "+ new SimpleDateFormat("dd.MM.yyyy").format(conversion.getExchangeRatesDate()));
            amountTextView.setText(conversion.getAmount().toString());
            resultTextView.setText(conversion.getResult().toString());
            firstCurrencyTextView.setText(conversion.getFirstCurrency());
            secondCurrencyTextView.setText(conversion.getSecondCurrency());
            dateTextView.setText(new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(conversion.getConversionDate()));

        }


    }



}
