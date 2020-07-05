package com.ordina.converter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ordina.converter.ui.fragments.ConverterFragment;
import com.ordina.converter.ui.fragments.ExchangeRatesFragment;
import com.ordina.converter.ui.fragments.HistoryFragment;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    final FragmentManager fm = getSupportFragmentManager();
    final Fragment historyFragment = new HistoryFragment();
    final Fragment converterFragment = new ConverterFragment();
    final Fragment exchangeRatesFragment = new ExchangeRatesFragment();
    BottomNavigationView bottomNavigationView;
    Fragment active = historyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        bottomNavigationView = findViewById(R.id.bnv);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.converter);
        fm.beginTransaction().add(R.id.fragments, converterFragment, "converter").commit();
        fm.beginTransaction().add(R.id.fragments, historyFragment, "history").hide(historyFragment).commit();
        fm.beginTransaction().add(R.id.fragments, exchangeRatesFragment, "exchange_rates").hide(exchangeRatesFragment).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.converter:
                    fm.beginTransaction().hide(active).show(converterFragment).commit();
                    active = converterFragment;
                    return true;

                case R.id.history:
                    fm.beginTransaction().hide(active).show(historyFragment).commit();
                    active = historyFragment;
                    return true;

                case R.id.exchange_rates:
                    fm.beginTransaction().hide(active).show(exchangeRatesFragment).commit();
                    active = exchangeRatesFragment;
                    return true;

                default: return false;
            }
        }
    };

}