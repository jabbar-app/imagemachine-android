package com.example.imagemachine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagemachine.R;
import com.example.imagemachine.handler.DBHandler;
import com.example.imagemachine.modal.Image;
import com.example.imagemachine.modal.Machine;
import com.example.imagemachine.view.DetailActivity;

import java.util.ArrayList;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.ViewHolder>{

    private ArrayList<Machine> machineArrayList;
    private Context context;

    public MachineAdapter(ArrayList<Machine> machineArrayList, Context context) {
        this.machineArrayList = machineArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineAdapter.ViewHolder holder, int position) {
        Machine machine = machineArrayList.get(position);
        holder.machineName.setText(machine.getMachineName());
        holder.machineType.setText(machine.getMachineType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("machineId", machine.getMachineID());
                intent.putExtra("machineName", machine.getMachineName());
                intent.putExtra("machineType", machine.getMachineType());
                intent.putExtra("machineQr", machine.getMachineQr());
                intent.putExtra("machineMt", machine.getMachineMt());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(holder.itemView.getContext()).create();
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
                                DBHandler dbHandler = new DBHandler(holder.itemView.getContext());
                                dbHandler.deleteMachine(machine.getMachineID());
                                Toast.makeText(holder.itemView.getContext(), "Machine Data Has Been Deleted", Toast.LENGTH_LONG).show();
                                machineArrayList.remove(machine);
                                notifyDataSetChanged();
                            }
                        });
                alertDialog.show();
            }
        });

        DBHandler dbHandler = new DBHandler(holder.itemView.getContext());
        ArrayList<Image> image = dbHandler.getImage(machine.getMachineQr());
        if(image == null || image.isEmpty())
        {
            holder.thumbnail.setImageResource(R.color.black);
        }
        else{
            holder.thumbnail.setImageURI(Uri.parse(image.get(0).getImageUri()));
        }
    }

    @Override
    public int getItemCount() {
        return machineArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView machineName, machineType;
        private ImageView delete, thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            machineName = itemView.findViewById(R.id.name);
            machineType = itemView.findViewById(R.id.type);
            delete = itemView.findViewById(R.id.delete);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
