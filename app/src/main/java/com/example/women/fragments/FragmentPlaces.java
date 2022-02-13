package com.example.women.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.women.R;
import com.example.women.activities.ReportActivityPlaces;
import com.example.women.adaptrs.PlaceRecyclerViewAdapter;
import com.example.women.networking_getdata.ApiInterfaceGetReport;
import com.example.women.networking_getdata.ClassGetReport;
import com.example.women.networking_senddata.ApiClient;
import com.example.women.util.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPlaces extends Fragment {

    private View parent_view;
    private SwipeRefreshLayout swipe_refresh;
    private final static int LOADING_DURATION = 2000;
    private RecyclerView recyclerView;
    private PlaceRecyclerViewAdapter mAdapter;
    private List<ClassGetReport> get_data_class;
    private ApiInterfaceGetReport apiInterface;
    FloatingActionButton fab;

    public FragmentPlaces() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_orginal, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        parent_view = view.findViewById(android.R.id.content);
        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout);


        fab = view.findViewById(R.id.btn_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ReportActivityPlaces.class);
                startActivity(intent);
            }
        });

        // Initialize activity center progress handler
        final LinearLayout lyt_progress = view.findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        recyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initComponent();
            }
        }, LOADING_DURATION + 400);

        return view;
    }

    //Api methods
    private void initComponent() {

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);



        apiInterface = ApiClient.getApiClient().create(ApiInterfaceGetReport.class);
        Call<List<ClassGetReport>> call = apiInterface.getDataPlaces();
        call.enqueue(new Callback<List<ClassGetReport>>() {
            @Override
            public void onResponse(Call<List<ClassGetReport>> call, Response<List<ClassGetReport>> response) {

                if (!response.isSuccessful()) {

                    Toasty.warning(getContext(), R.string.warning_toast, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                get_data_class = response.body();
                mAdapter = new PlaceRecyclerViewAdapter(get_data_class, getContext());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<ClassGetReport>> call, Throwable t) {

                Toasty.error(getContext(), R.string.error_toast, Toasty.LENGTH_SHORT, true).show();
//                Log.i("MyTAG",t.getMessage());
            }
        });

        // swipeLayout methods for refreshList
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullAndRefresh();
            }
        });
    }

    private void pullAndRefresh() {
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initComponent();
                swipeProgress(false);
            }
        }, 3000);
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }

}
