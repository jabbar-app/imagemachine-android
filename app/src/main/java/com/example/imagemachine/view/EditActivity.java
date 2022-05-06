package com.example.imagemachine.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.imagemachine.R;
import com.example.imagemachine.handler.DBHandler;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#14A0B4"));
        }

        EditText name    = (EditText) findViewById(R.id.name);
        EditText type    = (EditText) findViewById(R.id.type);
        EditText date  = (EditText) findViewById(R.id.date);
        Button close = (Button) findViewById(R.id.btn_close);
        Button submit = (Button) findViewById(R.id.btn_submit);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name.setText(getIntent().getStringExtra("machineName"));
        type.setText(getIntent().getStringExtra("machineType"));
        date.setText(getIntent().getStringExtra("machineMt"));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals(""))
                {
                    name.setError("Required");
                }
                else {
                    if(type.getText().toString().equals(""))
                    {
                        type.setError("Required");
                    }
                    else{
                        if(date.getText().toString().equals("0000-00-00"))
                        {
                            date.setError("Required");
                        }
                        else {
                            DBHandler dbHandler = new DBHandler(EditActivity.this);
                            dbHandler.updateMachine(getIntent().getStringExtra("machineId"),name.getText().toString(), type.getText().toString(), date.getText().toString());
                            Intent i = new Intent(EditActivity.this, NavigationActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }
        });
    }
}