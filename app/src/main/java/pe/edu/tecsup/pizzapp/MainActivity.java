package pe.edu.tecsup.pizzapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner1;
    private String tipoPizza;
    private String tipoMasa;
    private float prueba;
    private RadioGroup radioGroup;
    private CheckBox checkBox;
    private float costo_tipo;
    private float costo_comp;
    private float costo_mozarella;
    private float costo_jamon;
    private float costo_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = (Spinner) findViewById(R.id.spinner);
        tipoMasa=(String) getString(R.string.masa_gruesa);
        radioGroup =  (RadioGroup) findViewById(R.id.radioGroup);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        setSpinner1();
    }

    public void setSpinner1(){
        spinner1.setSelection(1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {

                if(pos==1)
                    costo_tipo=38;
                else if (pos==2)
                    costo_tipo=42;
                else if (pos==3)
                    costo_tipo=36;
                else if (pos==4)
                    costo_tipo=56;

                tipoPizza=(String) parent.getItemAtPosition(pos).toString();
                Toast.makeText(parent.getContext(),
                        getString(R.string.seleccion) + tipoPizza+" a S/."+ costo_tipo,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg) {

            }

        });
    }
    public void radioButtonClicked(View view) {

//        boolean checked = ((RadioButton) view).isChecked();
        // This check which radio button was clicked

            switch (view.getId()) {
                case R.id.radioButton1:

                    //Do something when radio button is clicked
//                    Toast.makeText(getApplicationContext(), getString(R.string.seleccion)+getString(R.string.masa_gruesa), Toast.LENGTH_SHORT).show();
                    tipoMasa = (String) getString(R.string.masa_gruesa);
                    break;

                case R.id.radioButton2:
                    //Do something when radio button is clicked
//                Toast.makeText(getApplicationContext(), getString(R.string.seleccion)+getString(R.string.masa_delgada), Toast.LENGTH_SHORT).show();
                    tipoMasa = (String) getString(R.string.masa_delgada);
                    break;

                case R.id.radioButton3:
                    //Do something when radio button is clicked
//                Toast.makeText(getApplicationContext(), getString(R.string.seleccion)+getString(R.string.masa_artesanal), Toast.LENGTH_SHORT).show();
                    tipoMasa = (String) getString(R.string.masa_artesanal);
                    break;

            }
            Toast.makeText(getApplicationContext(), getString(R.string.seleccion) + tipoMasa, Toast.LENGTH_SHORT).show();
//        }
    }

    public void androidCheckBoxClicked(View view) {
        // action for checkbox click
        switch (view.getId()) {
            case R.id.checkBox:
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked())
                {    Toast.makeText(this, getString(R.string.seleccion)+ checkBox.getText() , Toast.LENGTH_LONG).show();
                costo_mozarella=4;}
                else
                    costo_mozarella=0;
                break;
            case R.id.checkBox2:
                CheckBox checkBox2 = (CheckBox) view;
                if (checkBox2.isChecked())
                {   Toast.makeText(this, getString(R.string.seleccion)+ checkBox2.getText() , Toast.LENGTH_LONG).show();
                costo_jamon=8;}
                else
                    costo_jamon=0;
                break;
                //DO something when user check the box
        }


    }

    public double calcularCosto(){
        costo_comp=costo_mozarella+costo_jamon;
        costo_total=costo_tipo+costo_comp;
        if (Calendar.DAY_OF_WEEK==Calendar.SATURDAY)
            costo_total=(float) (costo_total-0.3*costo_total);
        return costo_total;
    }

    public void notificar(){
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Notification
        Notification notification = new NotificationCompat.Builder(this,"default")
                .setContentTitle("Pedido en camino")
                .setContentText("Su pedido está en camino")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            // Se define un objecto NotificationChannel en Android 8
            NotificationChannel channel = new NotificationChannel("default",
                    "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(0, notification);
        }else{
            NotificationManager notificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notification);
        }

    }

public void mostrarDialogo(){
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle(getString(R.string.confirmacion));
    alertDialog.setMessage(getString(R.string.pedido)+" " + tipoPizza+" "+tipoMasa+" a S/."+costo_total+" + IGV está en proceso de envio.");
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Alert dialog action goes here
                    dialog.dismiss();// use dismiss to cancel alert dialog
                }
            });
    alertDialog.show();
}



    public void ordenar(View view){
        calcularCosto();
        mostrarDialogo();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                notificar();
            }
        }, 10000);

    }






}


