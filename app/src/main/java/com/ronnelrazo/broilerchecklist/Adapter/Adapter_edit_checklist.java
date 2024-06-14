package com.ronnelrazo.broilerchecklist.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ronnelrazo.broilerchecklist.Checklist;
import com.ronnelrazo.broilerchecklist.Edit_Checklist;
import com.ronnelrazo.broilerchecklist.Edit_checklist_form;
import com.ronnelrazo.broilerchecklist.Farm_menu;
import com.ronnelrazo.broilerchecklist.R;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;
import com.ronnelrazo.broilerchecklist.model.model_edit_list;

import java.util.ArrayList;
import java.util.List;

public class Adapter_edit_checklist extends RecyclerView.Adapter<Adapter_edit_checklist.ViewHolder> {
    Context mContext;
    List<model_edit_list> newsList;
    List<model_edit_list> filteredList;
    Func func;

    public Adapter_edit_checklist(List<model_edit_list> list, Context context,Func func) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.func = func;
        this.filteredList = new ArrayList<>(list);



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_checklist_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_edit_list getData = newsList.get(position);
        int resId = R.anim.layout_animation_fadein;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        holder.card.setLayoutAnimation(animation);
        holder.audit_date.setText(func.header_html("Audit Date" , getData.getAudit_date(),""));
        holder.farm_name.setText(func.header_html("Farm Name" , String.format("%s (%s)", getData.getFarm_name(), getData.getHouse_flock()),""));
//        holder.farm_house_flock.setText(func.header_html("" , getData.getHouse_flock(), ""));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                func.intent(Edit_checklist_form.class,view.getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                Edit_checklist_form.GET_HOUSE_FLOCK = getData.getHouse_flock();
                Edit_checklist_form.GET_FARM_NAME = getData.getFarm_name();
                Edit_checklist_form.GET_AUDIT_DATE = getData.getAudit_date();
                Edit_checklist_form.GET_FARM_ORG = getData.getHouse_flock().split("-")[0];
                Edit_checklist_form.GET_BROILER_COUNT = getData.getBroiler_count();
                Edit_checklist_form.GET_BROILER_WGH = getData.getBroiler_wgh();

                Edit_checklist_form.GET_BROILER_REJECT_COUNT = getData.getBroiler_reject_count();
                Edit_checklist_form.GET_BROILER_REJECT_WGH = getData.getBroiler_reject_wgh();

                Edit_checklist_form.GET_AUTO_COUNT = getData.isAuto_count();
                Edit_checklist_form.GET_ID = getData.getId();
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
            for (model_edit_list data : newsList) {
                // Filter data based on your condition (for example, farm code or farm name)
                if (data.getFarm_name().toLowerCase().contains(query) ||
                        data.getAudit_date().toLowerCase().contains(query)) {
                    filteredList.add(data);
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter that data has changed
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialCardView card;
        public TextView audit_date,farm_name,farm_house_flock;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            audit_date = itemView.findViewById(R.id.audit_date);
            farm_name = itemView.findViewById(R.id.farm_name);
            farm_house_flock = itemView.findViewById(R.id.farm_house_flock);
        }
    }
}
