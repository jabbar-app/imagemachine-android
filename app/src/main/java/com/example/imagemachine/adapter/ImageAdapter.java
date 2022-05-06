package com.example.imagemachine.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagemachine.R;
import com.example.imagemachine.handler.DBHandler;
import com.example.imagemachine.modal.Image;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    Activity activity;
    private ArrayList<Image> imageArrayList;
    private Context context;
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<Image> selectList = new ArrayList<>();

    public ImageAdapter(ArrayList<Image> imageArrayList, Context context) {
        this.imageArrayList = imageArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Image image = imageArrayList.get(position);
        holder.imageView.setImageURI(Uri.parse(image.getImageUri()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(holder.itemView.getContext());
                View dialogView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.imagefull_layout, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                AlertDialog dialog1 = dialog.create();

                ImageView imageView    = (ImageView) dialogView.findViewById(R.id.image);
                imageView.setImageURI(Uri.parse(image.getImageUri()));

                dialog1.show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!isEnable)
                {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater= actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.menu,menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            isEnable=true;
                            // create method
                            ClickItem(holder);
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            int id=menuItem.getItemId();
                            // use switch condition
                            switch(id)
                            {
                                case R.id.menu_delete:
                                    for(Image s:selectList)
                                    {
                                        imageArrayList.remove(s);
                                        DBHandler dbHandler = new DBHandler(holder.itemView.getContext());
                                        dbHandler.deleteImage(s.getImageId());
                                    }
                                    actionMode.finish();
                                    break;

                                case R.id.menu_select_all:
                                    if(selectList.size()==imageArrayList.size())
                                    {
                                        isSelectAll=false;
                                        selectList.clear();
                                    }
                                    else
                                    {
                                        isSelectAll=true;
                                        selectList.clear();
                                        selectList.addAll(imageArrayList);
                                    }
                                    notifyDataSetChanged();
                                    break;
                            }
                            // return true
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            isEnable=false;
                            isSelectAll=false;
                            selectList.clear();
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                }
                else {
                    ClickItem(holder);
                }
                return true;
            }
        });

        if(isSelectAll)
        {
            holder.blur.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.blur.setVisibility(View.GONE);
        }
    }

    private void ClickItem(ViewHolder holder) {
        Image s=imageArrayList.get(holder.getAdapterPosition());
        if(holder.blur.getVisibility()==View.GONE)
        {
            holder.blur.setVisibility(View.VISIBLE);
            selectList.add(s);
        }
        else
        {
            holder.blur.setVisibility(View.GONE);
            selectList.remove(s);

        }
    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private View blur;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            blur = itemView.findViewById(R.id.blur);
        }
    }
}
