package cl.stomas.agendauniversitaria;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import cl.stomas.agendauniversitaria.controladores.CarreraController;
import cl.stomas.agendauniversitaria.db.Config;
import cl.stomas.agendauniversitaria.db.DB;
import cl.stomas.agendauniversitaria.modelos.Carrera;
import cl.stomas.agendauniversitaria.modelos.Semestre;
import cl.stomas.agendauniversitaria.vistas.AgendaActivity;
import cl.stomas.agendauniversitaria.vistas.AgregarCarreraActivity;
import cl.stomas.agendauniversitaria.vistas.CarreraActivity;
import cl.stomas.agendauniversitaria.vistas.SeleccionarCarreraActivity;

public class MainActivity extends AppCompatActivity {
    private Config config;
    private CarreraController finder;
    private final static String[] dias = new String[]{
            "Sabado", "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"
    };
    private final static String[] meses = new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    private TextView txtFechaHoy;
    private TextView txtCarrera;
    private Button btnAgenda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        finder = new CarreraController(this);
        config = Config.getConfig(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initApplicationState();
        }

        config.load();

        txtFechaHoy = findViewById(R.id.txtDia);
        txtCarrera = findViewById(R.id.txtCarrera);
        btnAgenda = findViewById(R.id.btnAgenda);

        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgendaActivity.class);
                startActivity(intent);
            }
        });

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.reset();
            }
        });

        Button btnCarrera = findViewById(R.id.btnCarrera);

        btnCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CarreraActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        config = Config.getConfig(this);
        config.load();
        Carrera carrera = finder.execute(config.getIdCarrera());
        //Carrera carrera = DB.carreras(this).get(config.getIdCarrera());
        if(carrera != null){
            txtCarrera.setText(carrera.getNombre());
            Semestre semestre = carrera.getSemestreActual();
            if(semestre != null){
                long id = semestre.getId();
                config.setIdSemestre(id);
                config.save();
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int dia_semana = calendar.get(Calendar.DAY_OF_WEEK);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        txtFechaHoy.setText(dias[dia_semana]+" "+dia+" de "+meses[mes]);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initApplicationState(){
        config = Config.getConfig(this);
        config.load();
        // Verificamos si existen carreras en la base de datos
        if(DB.carreras(this).getAll().size() <= 0){
            // Si no existen entonces creamos una Carrera y la seleccionamos
            Intent add_semestre_intent = new Intent(MainActivity.this, AgregarCarreraActivity.class);
            startActivity(add_semestre_intent);
        }else{
            // Si hay carreras en la base de datos entonces verificamos seleccion
            if(config.getIdCarrera() < 0) { // No hay carrera seleccionada en la configuracion entonces pedimos una
                Intent sel_carrera_intent = new Intent(MainActivity.this, SeleccionarCarreraActivity.class);
                startActivity(sel_carrera_intent);
            }else{ // Hay una carrera seleccionada entonces
                // Verificamos que el id seleccionado sea correcto y exista en la base de datos
                if(DB.carreras(this).get(config.getIdCarrera()) == null){
                    // Si no existe entonces la creamos y seleccionamos
                    Intent add_semestre_intent = new Intent(MainActivity.this, AgregarCarreraActivity.class);
                    startActivity(add_semestre_intent);
                }
                // Si existe entonces podemos proseguir
            }
        }
        config.load(); // Recargamos cualquier configuracion que haya sido modificada!
    }
}