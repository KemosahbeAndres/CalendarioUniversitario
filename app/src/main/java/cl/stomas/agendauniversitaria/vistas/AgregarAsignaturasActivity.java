package cl.stomas.agendauniversitaria.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import cl.stomas.agendauniversitaria.R;
import cl.stomas.agendauniversitaria.controladores.CarreraController;
import cl.stomas.agendauniversitaria.db.Config;
import cl.stomas.agendauniversitaria.db.DAOAsignatura;
import cl.stomas.agendauniversitaria.modelos.Asignatura;
import cl.stomas.agendauniversitaria.modelos.Carrera;

public class AgregarAsignaturasActivity extends AppCompatActivity {
    Button addButton;
    TextInputEditText nameAsign;
    TextInputEditText descAsign;
    TextInputEditText docenAsign;
    MaterialAutoCompleteTextView colorAsign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_asignaturas);

        ActionBar actionbar = getSupportActionBar();

        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);

        }

        addButton = findViewById(R.id.addbutton2);
        nameAsign = findViewById(R.id.txtnameasig);
        descAsign = findViewById(R.id.txtdescasig);
        docenAsign = findViewById(R.id.txtdescdocente);
        colorAsign = findViewById(R.id.txtcolores);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edition_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_menu_item){
            save();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        if (nameAsign.getText().toString().isEmpty()){
            Toast.makeText(AgregarAsignaturasActivity.this,"No dejar el campo nombre vacio", Toast.LENGTH_SHORT).show();
        }else if (descAsign.getText().toString().isEmpty()){
            Toast.makeText(AgregarAsignaturasActivity.this,"No dejar el campo descripcion vacio", Toast.LENGTH_SHORT).show();
        }else if (docenAsign.getText().toString().isEmpty()){
            Toast.makeText(AgregarAsignaturasActivity.this,"No dejar el campo docente vacio", Toast.LENGTH_SHORT).show();
        }else if (colorAsign.getText().toString().isEmpty()){
            Toast.makeText(AgregarAsignaturasActivity.this,"No dejar el campo color vacio", Toast.LENGTH_SHORT).show();
        }else{
            //aqui añadir la transferencia de los datos a la bd y el intent final
            Asignatura envio = new Asignatura(nameAsign.getText().toString(),descAsign.getText().toString(),colorAsign.getText().toString(),docenAsign.getText().toString());
            CarreraController controlador= new CarreraController(AgregarAsignaturasActivity.this);
            Config configurador = Config.getConfig(AgregarAsignaturasActivity.this);
            long idCarrera=configurador.getIdCarrera();
            Carrera carrera= controlador.execute(idCarrera);
            DAOAsignatura enviofinal= new DAOAsignatura(AgregarAsignaturasActivity.this);
            enviofinal.insert(envio,carrera.getSemestreActual());
            finish();
        }
    }

}