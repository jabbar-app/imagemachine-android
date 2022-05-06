package com.example.imagemachine.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagemachine.R;
import com.example.imagemachine.adapter.MachineAdapter;
import com.example.imagemachine.handler.DBHandler;
import com.example.imagemachine.modal.Machine;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ListFragment extends Fragment {

    private ArrayList<Machine> machineArrayList;
    private DBHandler dbHandler;
    private MachineAdapter machineAdapter;
    private RecyclerView recyclerView;
    private Button openInputPopupDialogButton = null;
    private View popupInputDialogView = null;
    private EditText userNameEditText = null;
    private EditText passwordEditText = null;
    private EditText emailEditText = null;
    private Button saveUserDataButton = null;
    private Button cancelUserDataButton = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list, container, false);

        machineArrayList = new ArrayList<>();
        dbHandler = new DBHandler(getContext());
        machineArrayList = dbHandler.getMachine();
        machineAdapter = new MachineAdapter(machineArrayList, getContext());
        recyclerView = root.findViewById(R.id.recyclerMachine);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(machineAdapter);

        Button sort_name = root.findViewById(R.id.btn_name);
        Button sort_type = root.findViewById(R.id.btn_type);
        sort_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4DBBC7")));
                sort_type.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#aaaaaa")));
                machineArrayList = new ArrayList<>();
                machineArrayList = dbHandler.getMachine();
                machineAdapter = new MachineAdapter(machineArrayList, getContext());
                recyclerView.setAdapter(machineAdapter);
            }
        });

        sort_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#aaaaaa")));
                sort_type.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4DBBC7")));
                machineArrayList = new ArrayList<>();
                machineArrayList = dbHandler.getMachineType();
                machineAdapter = new MachineAdapter(machineArrayList, getContext());
                recyclerView.setAdapter(machineAdapter);
            }
        });

        Button add = root.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddActivity.class));
            }
        });

        return root;
    }
}