package com.ordina.converter.ui.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ordina.converter.R;
import com.ordina.converter.model.Conversion;
import com.ordina.converter.ui.HistoryAdapter;

import java.util.ArrayList;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


public class HistoryFragment extends Fragment {

    public HistoryFragment() {
    }

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyRecyclerView = view.findViewById(R.id.history_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        historyRecyclerView.setLayoutManager(linearLayoutManager);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Conversion> conversions = realm.where(Conversion.class).sort("conversionDate", Sort.DESCENDING).findAll();
        realm.commitTransaction();
        historyAdapter = new HistoryAdapter(conversions);
        historyRecyclerView.setAdapter(historyAdapter);
        historyAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                linearLayoutManager.scrollToPosition(0);
            }
        });



        return view;
    }



}