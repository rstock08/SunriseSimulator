package goldsquadron.sunrise;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.TimePicker;

public class alarmActivity extends AppCompatActivity {
    private Button setButton;
    private Button cancelButton;
    private int guHour;
    private int guMinutes;
    private int sleepHour;
    private int sleepMinutes;
    private TimePicker getUpTimeP;
    private TimePicker sleepTimeP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelActivity();
            }

        });
        getUpTimeP = (TimePicker)findViewById(R.id.guTimePicker);
        sleepTimeP = (TimePicker)findViewById(R.id.slTimePicker);
        setButton = findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });
    }
    public void cancelActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    public void setAlarm(){
        guHour = getUpTimeP.getCurrentHour();
        guMinutes = getUpTimeP.getCurrentMinute();
        sleepHour = sleepTimeP.getCurrentHour();
        sleepMinutes = sleepTimeP.getCurrentMinute();
        AlertDialog alertDialog = new AlertDialog.Builder(alarmActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alarm set successful");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }
}
