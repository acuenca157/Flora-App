package org.izv.flora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.izv.flora.model.entity.CreateResponse;
import org.izv.flora.model.entity.RowsResponse;
import org.izv.flora.model.entity.Flora;
import org.izv.flora.model.api.FloraClient;
import org.izv.flora.view.AddFloraActivity;
import org.izv.flora.view.AddImagenActivity;
import org.izv.flora.view.adapter.FloraAdapter;
import org.izv.flora.view.adapter.viewholder.FloraViewHolder;
import org.izv.flora.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private FloraClient floraClient;
    private FloatingActionButton fabAdd;

    private RecyclerView rvFlora;
    private FloraAdapter floraAdapter;
    private FloraViewHolder floraViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        Log.v("xyzyx", "Wyyyyy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }

    private void initialize() {
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> openAddActivity());
        FloatingActionButton fabImagen = findViewById(R.id.fabImagen);
        fabImagen.setOnClickListener(v -> openAddImagenActivity());

        rvFlora = findViewById(R.id.rv);
        rvFlora.setLayoutManager(new LinearLayoutManager(this));

        floraAdapter = new FloraAdapter(this);

        rvFlora.setAdapter(floraAdapter);

        MainActivityViewModel mavm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mavm.getFloraLiveData().observe(this, floraPlural -> {
            Log.v("xyzyx", floraPlural.toString());
            floraAdapter.setLista(floraPlural);
        });

        mavm.getFlora();
    }

    private void openAddImagenActivity() {
        Intent intent = new Intent(this, AddImagenActivity.class);
        startActivity(intent);
    }

    private void openAddActivity() {
        Intent intent = new Intent(this, AddFloraActivity.class);
        startActivity(intent);
    }
}