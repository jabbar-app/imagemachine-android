package com.example.imagemachine.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.imagemachine.R;
import com.example.imagemachine.handler.DBHandler;
import com.example.imagemachine.modal.Machine;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class ScanFragment extends Fragment {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(getContext(), "No Matching Machine Data", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<Machine> machines = new ArrayList<>();
                    DBHandler dbHandler = new DBHandler(getContext());
                    machines = dbHandler.getMachineQr(result.getContents());
                    if(machines == null || machines.isEmpty())
                    {
                        Toast.makeText(getContext(), "No Matching Machine Data", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("machineId", machines.get(0).getMachineID());
                        intent.putExtra("machineName", machines.get(0).getMachineName());
                        intent.putExtra("machineType", machines.get(0).getMachineType());
                        intent.putExtra("machineQr", machines.get(0).getMachineQr());
                        intent.putExtra("machineMt", machines.get(0).getMachineMt());
                        startActivity(intent);
                    }
                }
            });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        Button scan = root.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                options.setPrompt("Scan a barcode");
                options.setCameraId(0);
                options.setOrientationLocked(true);
                options.setBeepEnabled(false);
                options.setBarcodeImageEnabled(true);
                barcodeLauncher.launch(options);
            }
        });

        return root;
    }
}