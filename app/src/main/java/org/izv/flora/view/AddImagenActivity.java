package org.izv.flora.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import org.izv.flora.R;
import org.izv.flora.model.entity.Imagen;
import org.izv.flora.viewmodel.AddFloraViewModel;
import org.izv.flora.viewmodel.AddImagenViewModel;

public class AddImagenActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launcher;
    private Intent resultadoImagen = null;
    private EditText etNombre, etDescripcion, etIdFlora;
    private AddImagenViewModel aivm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_imagen);
        initialize();
    }

    private void initialize() {
        launcher = getLauncher();
        Button btSelectImage;
        etDescripcion = findViewById(R.id.etDescripcion);
        etNombre = findViewById(R.id.etNombreImagen);
        etIdFlora = findViewById(R.id.etIdFlora);
        btSelectImage = findViewById(R.id.btSelectImage);
        btSelectImage.setOnClickListener(v -> {
            selectImage();
        });
        Button btAddImagen = findViewById(R.id.btAddImagen);
        btAddImagen.setOnClickListener(v -> {
            uploadDataImage();
        });
        aivm = new ViewModelProvider(this).get(AddImagenViewModel.class);
    }

    private void uploadDataImage() {
        String nombre = etNombre.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        String idFlora = etIdFlora.getText().toString();
        if(!(nombre.trim().isEmpty() ||
                idFlora.trim().isEmpty() ||
                resultadoImagen == null)) {
            Imagen imagen = new Imagen();
            imagen.nombre = nombre;
            imagen.descripcion = descripcion;
            imagen.idflora = Long.parseLong(idFlora);
            aivm.saveImagen(resultadoImagen, imagen);
        }
    }

    ActivityResultLauncher<Intent> getLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //respuesta al resultado de haber seleccionado una imagen
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        //copyData(result.getData());
                        resultadoImagen = result.getData();
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