package org.izv.flora.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.izv.flora.R;
import org.izv.flora.model.entity.Flora;
import org.izv.flora.model.entity.Imagen;
import org.izv.flora.viewmodel.AddFloraViewModel;
import org.izv.flora.viewmodel.AddImagenViewModel;
import org.izv.flora.viewmodel.EditFloraViewModel;
import org.izv.flora.viewmodel.MainActivityViewModel;

import java.util.Date;

public class AddFloraActivity extends AppCompatActivity {

    private EditText etNombre, etFamilia, etIdentificacion, etAltitud, etHabitat, etFitosociologia;
    private EditText etBiotipo, etBioRepro, etFructificacion, etFloracion, etExSex, etPol, etDispersion, etNCro;
    private EditText etReAsx, etDistribuicion, etBiologia, etDemografia, etAmenazas, etMedidasPropuestas;
    private ImageView ivPhoto;
    private Button btAdd;

    MutableLiveData<Uri> muri;
    private AddImagenViewModel aivm;
    private EditFloraViewModel efvm;

    private ActivityResultLauncher<Intent> launcher;
    private Intent resultadoImagen = null;

    private Flora flora;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flora);
        initialize();
    }

    private void uploadDataImage(String id) {

        String nombre = etNombre.getText().toString();
        String idFlora = id;
        if(!(nombre.trim().isEmpty() ||
                idFlora.trim().isEmpty() ||
                resultadoImagen == null)) {
            Imagen imagen = new Imagen();
            String d = String.valueOf(new Date().getTime());
            imagen.nombre = nombre + d;
            imagen.descripcion = "";
            imagen.idflora = Long.parseLong(idFlora);
            aivm.saveImagen(resultadoImagen, imagen);
        }
    }

    private void initialize() {

        flora = getIntent().getParcelableExtra("flora");

        efvm = new ViewModelProvider(this).get(EditFloraViewModel.class);
        efvm.getEditFloraLiveData().observe(this, aLong -> {
            if(aLong > 0) {
                Toast.makeText(AddFloraActivity.this, "Se ha editado la flora", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(AddFloraActivity.this, "Algo ha salido mal, intentalo de nuevo.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
        aivm.getAddImagenLiveData().observe(this, aLong -> {
            if(flora != null){
                if (aLong > 0)
                    Toast.makeText(AddFloraActivity.this, "Se ha añadido la foto correctamente.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddFloraActivity.this, "Algo ha salido mal, intentalo de nuevo.", Toast.LENGTH_LONG).show();
            }
        });
        muri = new MutableLiveData<>();
        muri.observe(this, uri -> {
            if(flora != null){
                uploadDataImage(flora.getId() + "");
            }
            else
                ivPhoto.setImageURI(uri);
        });
        launcher = getLauncher();

        //Ponemos la navegacion de la action bar
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Cambiamos el titulo
        if (flora != null)
            setTitle(flora.getNombre());
        else
            setTitle("Crear Flora");

        AddFloraViewModel avm = new ViewModelProvider(this).get(AddFloraViewModel.class);
        avm.getAddFloraLiveData().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(aLong > 0) {
                    Toast.makeText(AddFloraActivity.this, "Se ha creado la flora", Toast.LENGTH_LONG).show();
                    Log.v("xyzyx", "ID: " + aLong);
                    uploadDataImage(aLong.toString());
                    finish();
                } else {
                    Toast.makeText(AddFloraActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Capturamos todos los inputs

        ivPhoto = findViewById(R.id.ivFlora);
        ivPhoto.setOnClickListener((View v) -> {
            selectImage();
        });
        etNombre = findViewById(R.id.etNombre);
        etFamilia = findViewById(R.id.etFamilia);
        etIdentificacion = findViewById(R.id.etIdentificacion);
        etAltitud = findViewById(R.id.etAltitud);
        etHabitat = findViewById(R.id.etHabitat);
        etFitosociologia = findViewById(R.id.etFitosociologia);
        etBiotipo = findViewById(R.id.etBiotipo);
        etBioRepro = findViewById(R.id.etBioReproductiva);
        etFructificacion = findViewById(R.id.etFructificacion);
        etFloracion = findViewById(R.id.etFloracion);
        etExSex = findViewById(R.id.etExpSexual);
        etPol = findViewById(R.id.etPolinizacion);
        etDispersion = findViewById(R.id.etDispersion);
        etNCro = findViewById(R.id.etnCromosomatico);
        etReAsx = findViewById(R.id.etrAsexual);
        etDistribuicion = findViewById(R.id.etDistribuicion);
        etBiologia = findViewById(R.id.etBiologia);
        etDemografia = findViewById(R.id.etDemografia);
        etAmenazas = findViewById(R.id.etAmenazas);
        etMedidasPropuestas = findViewById(R.id.etMedidasPropuestas);
        btAdd = findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flora != null){
                    Flora newFlora = getFlora();
                    newFlora.setId(flora.getId());
                    efvm.editFlora(flora.getId(), newFlora);
                } else {
                    if (!etNombre.getText().toString().isEmpty()) {
                        Flora flora = getFlora();
                        avm.createFlora(getFlora());
                    } else {
                        Toast.makeText(AddFloraActivity.this, "El Nombre no puede estar vacío", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        if (flora != null){
            setCampos();
        }
    }

    private void setCampos() {
        etNombre.setEnabled(false);
        etNombre.setText(flora.getNombre());
        etFamilia.setText(flora.getFamilia());
        etIdentificacion.setText(flora.getIdentificacion());
        etAltitud.setText(flora.getAltitud());
        etHabitat.setText(flora.getHabitat());
        etFitosociologia.setText(flora.getFitosociologia());
        etBiotipo.setText(flora.getBiotipo());
        etBioRepro.setText(flora.getBiologia_reproductiva());
        etFructificacion.setText(flora.getFructificacion());
        etFloracion.setText(flora.getFloracion());
        etExSex.setText(flora.getExpresion_sexual());
        etPol.setText(flora.getPolinizacion());
        etDispersion.setText(flora.getDispersion());
        etNCro.setText(flora.getNumero_cromosomatico());
        etReAsx.setText(flora.getReproduccion_asexual());
        etDistribuicion.setText(flora.getDistribucion());
        etBiologia.setText(flora.getBiologia());
        etDemografia.setText(flora.getDemografia());
        etAmenazas.setText(flora.getAmenazas());
        etMedidasPropuestas.setText(flora.getMedidas_propuestas());
    }

    private Flora getFlora(){
        Flora flora = new Flora();

        flora.setNombre(etNombre.getText().toString());
        flora.setFamilia(etFamilia.getText().toString());
        flora.setIdentificacion(etIdentificacion.getText().toString());
        flora.setAltitud(etAltitud.getText().toString());
        flora.setHabitat(etHabitat.getText().toString());
        flora.setFitosociologia(etFitosociologia.getText().toString());
        flora.setBiotipo(etBiotipo.getText().toString());
        flora.setBiologia_reproductiva(etBioRepro.getText().toString());
        flora.setFloracion(etFloracion.getText().toString());
        flora.setFructificacion(etFructificacion.getText().toString());
        flora.setExpresion_sexual(etExSex.getText().toString());
        flora.setPolinizacion(etPol.getText().toString());
        flora.setDispersion(etDispersion.getText().toString());
        flora.setNumero_cromosomatico(etNCro.getText().toString());
        flora.setReproduccion_asexual(etReAsx.getText().toString());
        flora.setDistribucion(etDistribuicion.getText().toString());
        flora.setBiologia(etBiologia.getText().toString());
        flora.setDemografia(etDemografia.getText().toString());
        flora.setAmenazas(etAmenazas.getText().toString());
        flora.setMedidas_propuestas(etMedidasPropuestas.getText().toString());

        return flora;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> getLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //respuesta al resultado de haber seleccionado una imagen
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        //copyData(result.getData());
                        resultadoImagen = result.getData();
                        muri.setValue(resultadoImagen.getData());
                    }
                }
        );
    }

    Intent getContentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        return intent;
    }

    void selectImage() {
        Intent intent = getContentIntent();
        launcher.launch(intent);
    }
}