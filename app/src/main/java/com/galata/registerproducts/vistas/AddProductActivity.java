package com.galata.registerproducts.vistas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.galata.registerproducts.MainActivity;
import com.galata.registerproducts.R;
import com.galata.registerproducts.modelo.ConexionSQLite;
import com.galata.registerproducts.utilidades.Utilidades;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class AddProductActivity extends AppCompatActivity {

    private TextView initialTextView;
    private TextInputEditText nameEditText;
    private TextInputEditText clave;
    private TextInputEditText precio;
    private TextInputEditText detalle;
    private TextInputEditText cantidad;
    private NestedScrollView nt;

    private int color;
    private Intent intent;
    private Random randomGenerator = new Random();
    private RelativeLayout relative;

    //variables para validar
    Boolean bc=false, bn=false, bp=false, bdd=false, bca=false;
    TextInputLayout incl, inno, inpr, inde, inca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setEnterTransition(new Fade());
        }
        nameEditText = findViewById(R.id.nombrep);
        initialTextView = findViewById(R.id.initial);
        clave = findViewById(R.id.key);
        precio = findViewById(R.id.price);
        detalle = findViewById(R.id.description);
        cantidad = findViewById(R.id.cuantos);
        detalle.setSelected(false);

        incl = findViewById(R.id.incla);
        inno = findViewById(R.id.innom);
        inpr = findViewById(R.id.inpre);
        inde = findViewById(R.id.indes);
        inca = findViewById(R.id.incan);

        int colors[] = getResources().getIntArray(R.array.initial_colors);
        color = colors[ randomGenerator.nextInt(50) ];
        relative = findViewById(R.id.relative_color);
        relative.setBackgroundColor(color);

        intent = getIntent();
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    // add initialTextView
                    initialTextView.setText("");
                } else if (count >= 1) {
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

        nt = findViewById(R.id.scrolladd);
        cantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                nt.scrollTo(0, nt.getBottom());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if( result != null ){
            clave.setText( result.getContents() );
        }else{
            Toast.makeText(getApplicationContext(), "Error al escanear", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState( Bundle estado )
    {
        estado.putInt("colorRelative", color);
        super.onSaveInstanceState(estado);
    }

    @Override
    public void onRestoreInstanceState( Bundle estado )
    {
        super.onRestoreInstanceState(estado);
        color = estado.getInt("colorRelative");
        relative.setBackgroundColor(color);
    }

    public void escanearCodigoDeBarras(View view )
    {
        new IntentIntegrator(AddProductActivity.this).initiateScan();
    }



    public void onClickRegistrarProducto( View view )
    {

        //validamos primero
        ConexionSQLite conexion = new ConexionSQLite(this, "bd_prod", null, 1);
        SQLiteDatabase bdt = conexion.getReadableDatabase();;
        String auxCla = "";
        String paramentros[] = {clave.getText().toString()};
        try {
            String query = "SELECT " + Utilidades.CAMPO_CLAVE +
                    " FROM " + Utilidades.TABLA_PRODUCTOS + " WHERE " + Utilidades.CAMPO_CLAVE + " = ?";
            Cursor cursor = bdt.rawQuery( query, paramentros );
            cursor.moveToFirst();
            auxCla = cursor.getString(0);
            cursor.close();
        }catch (Exception e){

        }
        bdt.close();
        if( clave.getText().toString().equals("") ){
            incl.setError("Campo requerido");
            bc=false;
        }else
            if( clave.getText().toString().equals(auxCla) ){
                incl.setError("La clave ya existe!");
                Toast.makeText(getApplicationContext(), "Por favor, ingrese una clave diferente.", Toast.LENGTH_SHORT).show();
                bc=false;
                } else{
                    bc=true;
                    incl.setError(null);
                }

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
            bdd=false;
        }else{
            bdd=true;
            inde.setError(null);
        }
        if( cantidad.getText().toString().equals("") ){
            inca.setError("Campo requerido");
            bca=false;
        }else{
            bca=true;
            inca.setError(null);
        }
        if( bc && bn && bp && bdd && bca ){
            SQLiteDatabase bd = conexion.getWritableDatabase();
            initialTextView.setText("");
            String insertProduct = "INSERT INTO " + Utilidades.TABLA_PRODUCTOS + "(" +Utilidades.CAMPO_CLAVE+ ", " +
                    Utilidades.CAMPO_NOMBRE + ", " + Utilidades.CAMPO_PRECIO + ", " +
                    Utilidades.CAMPO_DESC + ", " + Utilidades.CAMPO_CANTIDAD +
                    ") values('" + clave.getText().toString() +"', '" +
                    nameEditText.getText().toString() + "', '" + Float.parseFloat( precio.getText().toString() ) +
                    "', '" + detalle.getText().toString() + "', " + Integer.parseInt( cantidad.getText().toString() ) + ")";

            String insertCardProduct = "INSERT INTO " + Utilidades.TABLA_CARD_PRODUCTO +
                    "(" + Utilidades.CAMPO_INICIAL + ", "  +
                    Utilidades.CAMPO_COLOR + ") values('" + String.valueOf(nameEditText.getText().charAt(0)).toUpperCase() +
                    "', '" + color + "')";
            bd.execSQL(insertProduct);
            bd.execSQL(insertCardProduct);
            Toast.makeText( getApplicationContext(), "Registro exitoso ", Toast.LENGTH_SHORT ).show();
            bd.close();
            intent.putExtra(MainActivity.EXTRA_NAME, String.valueOf(nameEditText.getText()));
            intent.putExtra(MainActivity.EXTRA_INITIAL, String.valueOf(nameEditText.getText().charAt(0)));
            intent.putExtra(MainActivity.EXTRA_COLOR, color);
            intent.putExtra(MainActivity.EXTRA_CLA, String.valueOf(clave.getText()));
            intent.putExtra(MainActivity.EXTRA_PRE, String.valueOf(precio.getText()));
            intent.putExtra(MainActivity.EXTRA_DET, String.valueOf(detalle.getText()));
            intent.putExtra(MainActivity.EXTRA_CAN, String.valueOf(cantidad.getText()));

            setResult(RESULT_OK, intent);
            supportFinishAfterTransition();
        }
        //onBackPressed();
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