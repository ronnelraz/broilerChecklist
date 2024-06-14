package com.ronnelrazo.broilerchecklist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ronnelrazo.broilerchecklist.Farm_menu;
import com.ronnelrazo.broilerchecklist.Menu;
import com.ronnelrazo.broilerchecklist.R;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;
import com.ronnelrazo.broilerchecklist.model.model_farm;
import com.ronnelrazo.broilerchecklist.model.model_menu;

import java.util.ArrayList;
import java.util.List;

public class Adapter_farm_list extends RecyclerView.Adapter<Adapter_farm_list.ViewHolder> {
    Context mContext;
    List<Checklist_farm_data> newsList;
    List<Checklist_farm_data> filteredList;

    Farm_menu farmMenu;
    String org_wip;

    int online;



    public Adapter_farm_list(List<Checklist_farm_data> list, Context context, Farm_menu farmMenu,String org_wip, int online) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.filteredList = new ArrayList<>(list);
        this.farmMenu = farmMenu;
        this.org_wip = org_wip;
        this.online = online;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Checklist_farm_data getData = newsList.get(position);
        int resId = R.anim.layout_animation_fadein;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);

        holder.card.setLayoutAnimation(animation);

        holder.farm_code.setText(getData.getFarm_code());
        holder.Farm_name.setText(getData.getFarm_name());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(online == 1){

                    farmMenu.Farm_house_flock_offline(getData.getFarm_code(),getData.getFarm_name(),view.getContext());

                }
                else if(online == 2){
                    farmMenu.Farm_house_flock_online(org_wip,getData.getFarm_code(),getData.getFarm_name(),view.getContext());
//                    farmMenu.Farm_house_flock_offline(getData.getFarm_code(),getData.getFarm_name(),view.getContext());

                }


            }
        });

    }
    @Override
    public int getItemCount() {
        return newsList.size();

    }

    // Method to filter data based on search query
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(newsList); // If query is empty, show all data
        } else {
            query = query.toLowerCase();
            for (Checklist_farm_data data : newsList) {
                // Filter data based on your condition (for example, farm code or farm name)
                if (data.getFarm_code().toLowerCase().contains(query) ||
                        data.getFarm_name().toLowerCase().contains(query)) {
                    filteredList.add(data);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter that data has changed
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialCardView card;
        public TextView Farm_name,farm_code;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            Farm_name = itemView.findViewById(R.id.Farm_name);
            farm_code = itemView.findViewById(R.id.farm_code);
        }
    }
}
