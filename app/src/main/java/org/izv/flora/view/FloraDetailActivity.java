package org.izv.flora.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.izv.flora.R;
import org.izv.flora.model.entity.Flora;
import org.izv.flora.model.entity.Imagen;
import org.izv.flora.model.entity.ImagesRowsResponse;
import org.izv.flora.model.entity.RowsResponse;
import org.izv.flora.view.adapter.SliderAdapter;
import org.izv.flora.viewmodel.AddFloraViewModel;
import org.izv.flora.viewmodel.DeleteFloraViewModel;
import org.izv.flora.viewmodel.EditFloraViewModel;
import org.izv.flora.viewmodel.GetImagesViewModel;

public class FloraDetailActivity extends AppCompatActivity {

    Flora flora;

    private TextView tvNombre, tvFam, tvIdentificacion, tvAltitud, tvHabitat, tvFitosociologia;
    private TextView tvBiotipo, tvBioRepro, tvFructificacion, tvFloracion, tvExSex, tvPol, tvDispersion, tvNCro;
    private TextView tvReAsx, tvDistribuicion, tvBiologia, tvDemografia, tvAmenazas, tvMedidasPropuestas;
    private SliderView sv;

    private GetImagesViewModel givm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_detail);

        flora = getIntent().getParcelableExtra("flora");

        initalize();
    }

    private void initalize() {

        //Ponemos la navegacion de la action bar
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Cambiamos el titulo
        setTitle(flora.getNombre());

        givm = new ViewModelProvider(this).get(GetImagesViewModel.class);

        givm.getFloraImagesLiveData().observe(this, imagesRowsResponse -> {
            int[] images = new int[imagesRowsResponse.rows.length];
            for (int i = 0; i < images.length; i++) {
                images[i] = (int) imagesRowsResponse.rows[i].id;
            }

            sv = findViewById(R.id.image_slider);
            SliderAdapter sliderAdapter = new SliderAdapter(this, images);
            sv.setSliderAdapter(sliderAdapter);
            sv.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sv.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
            sv.startAutoCycle();
        });

        givm.getImages(flora.getId());

        tvNombre = findViewById(R.id.tvNombre);
        tvFam = findViewById(R.id.tvFam);
        tvIdentificacion = findViewById(R.id.tvIdentificacion);
        tvHabitat = findViewById(R.id.tvHabitat);
        tvAltitud = findViewById(R.id.tvAltitud);
        tvFitosociologia = findViewById(R.id.tvFitoso);
        tvBiotipo = findViewById(R.id.tvBiotipo);
        tvBioRepro = findViewById(R.id.tvBioRepro);
        tvFructificacion = findViewById(R.id.tvFructuacion);
        tvFloracion = findViewById(R.id.tvFloracion);
        tvExSex = findViewById(R.id.tvExpSex);
        tvPol = findViewById(R.id.tvPolinizacion);
        tvDispersion = findViewById(R.id.tvDispersion);
        tvNCro = findViewById(R.id.tvNCrom);
        tvReAsx = findViewById(R.id.tvRepAsex);
        tvDistribuicion = findViewById(R.id.tvDristribuicion);
        tvBiologia = findViewById(R.id.tvBiologia);
        tvDemografia = findViewById(R.id.tvDemografia);
        tvAmenazas = findViewById(R.id.tvAmenazas);
        tvMedidasPropuestas = findViewById(R.id.tvMedidasPropuestas);

        tvNombre.setText(flora.getNombre());
        tvFam.setText(flora.getFamilia());
        tvIdentificacion.setText(flora.getIdentificacion());
        tvHabitat.setText(flora.getHabitat());
        tvAltitud.setText(flora.getAltitud());
        tvFitosociologia.setText(flora.getFitosociologia());
        tvBiotipo.setText(flora.getBiotipo());
        tvBioRepro.setText(flora.getBiologia_reproductiva());
        tvFructificacion.setText(flora.getFructificacion());
        tvFloracion.setText(flora.getFloracion());
        tvExSex.setText(flora.getExpresion_sexual());
        tvPol.setText(flora.getPolinizacion());
        tvDispersion.setText(flora.getDispersion());
        tvNCro.setText(flora.getNumero_cromosomatico());
        tvReAsx.setText(flora.getReproduccion_asexual());
        tvDistribuicion.setText(flora.getDistribucion());
        tvBiologia.setText(flora.getBiologia());
        tvDemografia.setText(flora.getDemografia());
        tvAmenazas.setText(flora.getAmenazas());
        tvMedidasPropuestas.setText(flora.getMedidas_propuestas());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudetail, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteOption:
                deleteFlora();
                return true;
            case R.id.editOption:
                Intent intent = new Intent(getApplication().getApplicationContext(), AddFloraActivity.class);
                intent.putExtra("flora", flora);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteFlora() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Borrar");
        builder.setMessage("¿Estás seguro de que quieres borrar esta Flora?");
        builder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDelete();
                        Log.v("xyzyx", "Entro en confirmar");
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    void confirmDelete(){
        DeleteFloraViewModel dfvm;
        dfvm = new ViewModelProvider(this).get(DeleteFloraViewModel.class);
        dfvm.getFloraDeleteLiveData().observe(this, rowsResponse -> {
            if (rowsResponse.rows > 0){
                Toast.makeText(this, "Se ha borrado la flora.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Algo ha salido mal. Prueba más adelante.", Toast.LENGTH_LONG).show();
            }
        });
        dfvm.deleteFlora(flora.getId());
    }

}