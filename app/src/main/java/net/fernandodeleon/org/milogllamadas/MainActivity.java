package net.fernandodeleon.org.milogllamadas;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void consultarCPLlamadas(){

    }
}
