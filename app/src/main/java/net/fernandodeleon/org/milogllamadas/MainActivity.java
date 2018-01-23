package net.fernandodeleon.org.milogllamadas;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int CODIGO_SOLICITUD = 1;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
    }

    public void mostrarLlamadas(View v){
        if(verEstadoPermiso()){
            consultarCPLlamadas();
        }else{
            solicitarPermiso();
        }
    }

    public void solicitarPermiso(){
        //Read Call Log
        //Write Call Log
        boolean solicitarPermisoRCL = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG);
        boolean solicitarPermisoWCL = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALL_LOG);

        if(solicitarPermisoRCL && solicitarPermisoWCL){
            Toast.makeText(MainActivity.this, "Los permisos fueron otorgados", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG}, CODIGO_SOLICITUD);
        }
    }

    public boolean verEstadoPermiso(){
        boolean permisoReadCallLog = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
        boolean permisoWriteCAllLog = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED;

        if(permisoReadCallLog  && permisoWriteCAllLog){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CODIGO_SOLICITUD:
                if(verEstadoPermiso()){
                    Toast.makeText(this, "Ya está activo el permiso", Toast.LENGTH_LONG).show();
                    consultarCPLlamadas();
                }else{
                    Toast.makeText(this, "No se activo el permiso", Toast.LENGTH_LONG).show();
                }
        }
    }

    public void consultarCPLlamadas(){
        TextView tvLlamadas = (TextView) findViewById(R.id.tvLlamadas);
        tvLlamadas.setText("");

        Uri direccionUriLlamadas = CallLog.Calls.CONTENT_URI;

        //Numero, fecha, tipo, duración
        String[] campos = {
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION
        };

        ContentResolver contentResolver = getContentResolver();
        Cursor registros = contentResolver.query(direccionUriLlamadas, campos, null, null, CallLog.Calls.DATE + " DESC");
        while(registros.moveToNext()){
            //OBTENER LOS DATOS A PARTIR DEL INDICE DE LA COLUMNA
            String numero = registros.getColumnName(registros.getColumnIndex(campos[0]));
            Long fecha = registros.getLong(registros.getColumnIndex(campos[1]));
            int tipo = registros.getInt(registros.getColumnIndex(campos[2]));
            String duracion = registros.getColumnName(registros.getColumnIndex(campos[3]));
            String tipoLlamada = "";

            //VALIDACION TIPO DE LLAMADA
            switch(tipo){
                case CallLog.Calls.INCOMING_TYPE:
                    tipoLlamada = getResources().getString(R.string.entrada);
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    tipoLlamada = getResources().getString(R.string.salida);
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    tipoLlamada = getResources().getString(R.string.perdida);
                    break;
                default:
                    tipoLlamada = getResources().getString(R.string.desconocida);
            }

            String detalle = getResources().getString(R.string.etiqueta_numero) + numero +
            "\n" + getResources().getString(R.string.etiqueta_fecha) + android.text.format.DateFormat.format("dd/mm/yy k:mm", fecha) +
            "\n" + getResources().getString(R.string.etiqueta_tipo) + tipo +
            "\n" + getResources().getString(R.string.etiqueta_duracion) + duracion ;

            tvLlamadas.append(detalle);
        }

    }
}
