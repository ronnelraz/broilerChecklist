package com.ronnelrazo.broilerchecklist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.novoda.merlin.Merlin;
import com.ronnelrazo.broilerchecklist.Cancel_menu;
import com.ronnelrazo.broilerchecklist.Confirm_Checklist;
import com.ronnelrazo.broilerchecklist.Edit_Checklist;
import com.ronnelrazo.broilerchecklist.Farm_menu;
import com.ronnelrazo.broilerchecklist.Menu;
import com.ronnelrazo.broilerchecklist.R;
import com.ronnelrazo.broilerchecklist.Transction_menu;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.model_menu;

import java.util.List;

public class Adapter_menu_list extends RecyclerView.Adapter<Adapter_menu_list.ViewHolder> {
    Context mContext;
    List<model_menu> newsList;

    Boolean active;

    Menu menu;

    private String[] getMenuAccessArray;

    public Adapter_menu_list(List<model_menu> list, Context context, Boolean active, Menu menu) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.active = active;
        this.menu = menu;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_menu getData = newsList.get(position);

        if (active){
            if(getData.getPosition() == 0){
                holder.card.setEnabled(true);
            }
        }
        else{
            if(getData.getPosition() == 0){
                holder.card.setEnabled(false);

                holder.card.setStrokeColor(R.color.disabled);
                holder.card.setStrokeWidth(2);
                holder.card.getBackground().setTint(Color.parseColor("#AAAAAA"));
                holder.menu_title.setTextColor(R.color.dark_blue);
                holder.logo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3E3E3E")));

            }
        }

        holder.menu_title.setText(getData.getTitle());
        // Assuming holder.logo is your ImageView
        holder.logo.setBackgroundResource(getData.getIcon());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getData.getPosition() == 0){
                    Func.getInstance(v.getContext()).log("Sync Data");
                    menu.download_Data(v.getContext());
                }
                else if(getData.getPosition() == 1){
                    Func.getInstance(v.getContext()).intent(Farm_menu.class,v.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else if(getData.getPosition() == 2){
                    Func.getInstance(v.getContext()).intent(Edit_Checklist.class,v.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                }

                else if(getData.getPosition() == 3){
                    Func.getInstance(v.getContext()).intent(Confirm_Checklist.class,v.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                }

                else if(getData.getPosition() == 4){
                    Func.getInstance(v.getContext()).intent(Cancel_menu.class,v.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else if(getData.getPosition() == 5){
                    Func.getInstance(v.getContext()).intent(Transction_menu.class,v.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });

    }





    @Override
    public int getItemCount() {
        return newsList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialCardView card;
        public ImageView logo;
        public TextView menu_title;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            logo = itemView.findViewById(R.id.logo);
            menu_title = itemView.findViewById(R.id.menu_title);


        }
    }
}
