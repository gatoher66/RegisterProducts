package com.galata.registerproducts.vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.galata.registerproducts.MainActivity;
import com.galata.registerproducts.R;
import com.galata.registerproducts.modelo.ConexionSQLite;
import com.galata.registerproducts.utilidades.Utilidades;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProductActivity extends AppCompatActivity {

    private TextView initialTextView;
    private TextInputEditText nameEditText;
    private TextInputEditText clave;
    private TextInputEditText precio;
    private TextInputEditText detalle;
    private TextInputEditText cantidad;

    private int colorExtra;
    private Intent intent;
    ConexionSQLite conn;

    Boolean bn=false, bp=false, bd=false, bca=false;
    TextInputLayout inno, inpr, inde, inca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setEnterTransition(new Fade());
        }
        nameEditText = findViewById(R.id.nombrepa);
        initialTextView = findViewById(R.id.initiala);
        clave = findViewById(R.id.keya);
        precio = findViewById(R.id.pricea);
        detalle = findViewById(R.id.descriptiona);
        cantidad = findViewById(R.id.cuantosa);

        inno = findViewById(R.id.innoma);
        inpr = findViewById(R.id.inprea);
        inde = findViewById(R.id.indeta);
        inca = findViewById(R.id.incana);

        intent = getIntent();
        String nameExtra = intent.getStringExtra(MainActivity.EXTRA_NAME);
        String initialExtra = intent.getStringExtra(MainActivity.EXTRA_INITIAL);
        colorExtra = intent.getIntExtra(MainActivity.EXTRA_COLOR, 0);
        String claExtra = intent.getStringExtra(MainActivity.EXTRA_CLA);
        String preExtra = intent.getStringExtra(MainActivity.EXTRA_PRE);
        String detExtra = intent.getStringExtra(MainActivity.EXTRA_DET);
        String canExtra = intent.getStringExtra(MainActivity.EXTRA_CAN);

        RelativeLayout relative = findViewById(R.id.relative_colora);
        relative.setBackgroundColor(colorExtra);
        nameEditText.setText(nameExtra);
        nameEditText.setSelection(nameEditText.getText().length());
        initialTextView.setText(initialExtra.toUpperCase());
        clave.setText(claExtra);
        precio.setText(preExtra);
        precio.setSelected(false);
        detalle.setText(detExtra);
        cantidad.setText(canExtra);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // add initialTextView
                    initialTextView.setText("");
                } else if (s.length() >= 1) {
                    // initialTextView set to first letter of nameEditText and add name stringExtra
                    initialTextView.setText(String.valueOf(s.charAt(0)).toUpperCase());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button updateButton = findViewById(R.id.btn_actualizar);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( nameEditText.getText().toString().equals("") ){
                    inno.setError("Campo requerido");
                    bn=false;
                }else{
                    bn=true;
                    inno.setError(null);
                }
                if( precio.getText().toString().equals("") ){
                    inpr.setError("Campo requerido");
                    bp=false;
                }else{
                    bp=true;
                    inpr.setError(null);
                }
                if( detalle.getText().toString().equals("") ){
                    inde.setError("Campo requerido");
                    bd=false;
                }else{
                    bd=true;
                    inde.setError(null);
                }
                if( cantidad.getText().toString().equals("") ){
                    inca.setError("Campo requerido");
                    bca=false;
                }else{
                    bca=true;
                    inca.setError(null);
                }

                if( bn && bp && bd && bca ){
                    conn = new ConexionSQLite(getApplicationContext(), "bd_prod", null, 1);
                    SQLiteDatabase bd = conn.getWritableDatabase();
                    String paramentros[] = {clave.getText().toString()};

                    ContentValues values = new ContentValues();
                    values.put(Utilidades.CAMPO_NOMBRE, nameEditText.getText().toString());
                    values.put(Utilidades.CAMPO_PRECIO, precio.getText().toString());
                    values.put(Utilidades.CAMPO_DESC, detalle.getText().toString());
                    values.put(Utilidades.CAMPO_CANTIDAD, cantidad.getText().toString());

                    bd.update(Utilidades.TABLA_PRODUCTOS, values, Utilidades.CAMPO_CLAVE+"=?", paramentros);
                    Toast.makeText(getApplicationContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    bd.close();

                    intent.putExtra(MainActivity.EXTRA_UPDATE, true);
                    intent.putExtra(MainActivity.EXTRA_NAME, String.valueOf(nameEditText.getText()));
                    intent.putExtra(MainActivity.EXTRA_COLOR, colorExtra);
                    intent.putExtra(MainActivity.EXTRA_INITIAL, String.valueOf(nameEditText.getText().charAt(0)).toUpperCase());
                    intent.putExtra(MainActivity.EXTRA_CLA, String.valueOf(clave.getText()));
                    intent.putExtra(MainActivity.EXTRA_PRE, String.valueOf(precio.getText()));
                    intent.putExtra(MainActivity.EXTRA_DET, String.valueOf(detalle.getText()));
                    intent.putExtra(MainActivity.EXTRA_CAN, String.valueOf(cantidad.getText()));
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeActivity( View view )
    {
        onBackPressed();
    }
}