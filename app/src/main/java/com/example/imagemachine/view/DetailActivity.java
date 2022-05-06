package com.example.imagemachine.view;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Messenger;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagemachine.R;
import com.example.imagemachine.adapter.ImageAdapter;
import com.example.imagemachine.adapter.MachineAdapter;
import com.example.imagemachine.handler.DBHandler;
import com.example.imagemachine.modal.Image;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Uri> mArrayUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#14A0B4"));
        }

        TextView name = findViewById(R.id.name);
        TextView type = findViewById(R.id.type);
        TextView id = findViewById(R.id.id);
        TextView date = findViewById(R.id.date);
        TextView qrtext = findViewById(R.id.qrText);
        EditText hidden = findViewById(R.id.hidden);

        name.setText(getIntent().getStringExtra("machineName"));
        type.setText(getIntent().getStringExtra("machineType"));
        id.setText("Machine ID: #" + getIntent().getStringExtra("machineId"));
        date.setText("Last Maintenance: " + getIntent().getStringExtra("machineMt"));
        qrtext.setText("(" +getIntent().getStringExtra("machineQr") + ")");
        hidden.setText(getIntent().getStringExtra("machineQr"));

        Button add = findViewById(R.id.addImage);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        TextView delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelete();
            }
        });

        Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra("machineName", getIntent().getStringExtra("machineName"));
                intent.putExtra("machineType", getIntent().getStringExtra("machineType"));
                intent.putExtra("machineMt", getIntent().getStringExtra("machineMt"));
                intent.putExtra("machineId", getIntent().getStringExtra("machineId"));
                startActivity(intent);
            }
        });

        generateQr();
        getImage();
    }

    private void showDelete() {
        AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this).create();
        alertDialog.setTitle("Delete Machine Data");
        alertDialog.setMessage("Are you sure to delete this Machine Data");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBHandler dbHandler = new DBHandler(DetailActivity.this);
                        dbHandler.deleteMachine(getIntent().getStringExtra("machineId"));
                        Toast.makeText(DetailActivity.this, "Machine Data Has Been Deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailActivity.this, NavigationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        DetailActivity.this.finish();
                    }
                });
        alertDialog.show();
    }

    private void generateQr() {
        try {
            EditText hidden = findViewById(R.id.hidden);
            String machineQr = hidden.getText().toString();
            MultiFormatWriter multi = new MultiFormatWriter();
            BitMatrix bitMatrix = multi.encode(machineQr, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView qrimage = findViewById(R.id.qrImage);
            qrimage.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.getClipData() != null) {
                if(data.getClipData().getItemCount() > 10)
                {
                    Toast.makeText(DetailActivity.this, "Image Can Not Be More Than 10", Toast.LENGTH_LONG).show();
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < 10; i++) {
                        Uri imageurl = data.getClipData().getItemAt(i).getUri();
                        DBHandler dbHandler = new DBHandler(DetailActivity.this);
                        dbHandler.addImage(getIntent().getStringExtra("machineQr"), imageurl.toString());
                    }
                    getImage();
                }
                else{
                    ClipData mClipData = data.getClipData();
                    int cout = data.getClipData().getItemCount();
                    for (int i = 0; i < cout; i++) {
                        Uri imageurl = data.getClipData().getItemAt(i).getUri();
                        DBHandler dbHandler = new DBHandler(DetailActivity.this);
                        dbHandler.addImage(getIntent().getStringExtra("machineQr"), imageurl.toString());
                    }
                    getImage();
                }
            } else {
                Uri sourceTreeUri = data.getData();
                this.getContentResolver().takePersistableUriPermission(
                        sourceTreeUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                Uri imageurl = data.getData();
                DBHandler dbHandler = new DBHandler(DetailActivity.this);
                dbHandler.addImage(getIntent().getStringExtra("machineQr"), imageurl.toString());
                getImage();
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void getImage() {
        ArrayList<Image> imageArrayList = new ArrayList<>();
        DBHandler dbHandler = new DBHandler(DetailActivity.this);
        imageArrayList = dbHandler.getImage(getIntent().getStringExtra("machineQr"));
        ImageAdapter imageAdapter = new ImageAdapter(imageArrayList, DetailActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerImage);
        recyclerView.setAdapter(imageAdapter);
    }

}