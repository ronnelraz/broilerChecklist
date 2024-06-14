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
import com.ronnelrazo.broilerchecklist.Confirm_Checklist;
import com.ronnelrazo.broilerchecklist.R;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.model_confirm_list;

import java.util.ArrayList;
import java.util.List;

public class Adapter_confirm_checklist extends RecyclerView.Adapter<Adapter_confirm_checklist.ViewHolder>  {
    Context mContext;
    List<model_confirm_list> newsList;
    List<model_confirm_list> filteredList;
    private List<model_confirm_list> selectedItems;
    Func func;
    Confirm_Checklist confirmChecklist;



    public Adapter_confirm_checklist(List<model_confirm_list> list, Context context, Func func,Confirm_Checklist confirmChecklist) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.func = func;
        this.filteredList = new ArrayList<>(list);
        this.selectedItems = new ArrayList<>();
        this.confirmChecklist = confirmChecklist;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_checklist_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final model_confirm_list getData = newsList.get(position);
        int resId = R.anim.layout_animation_fadein;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        holder.card.setLayoutAnimation(animation);
        holder.audit_date.setText(func.header_html("Audit Date" , getData.getAudit_date(),""));
        holder.farm_name.setText(func.header_html("Farm Name" , String.format("%s (%s)", getData.getFarm_name(), getData.getHouse_flock()),""));
//        holder.farm_house_flock.setText(func.header_html("" , getData.getHouse_flock(), ""));
        if(getData.isChecked()){
            holder.checked.setVisibility(View.VISIBLE);
        }
        else{
            holder.checked.setVisibility(View.GONE);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getData.isChecked()) {
                    holder.checked.setVisibility(View.GONE);
                    holder.checked.setAnimation(func.fadeOut());
                    selectedItems.remove(getData);
                    getData.setChecked(false);
                    int color = Color.parseColor("#FFFFFF");
                    holder.card.setBackgroundTintList(ColorStateList.valueOf(color));
                } else {
                    holder.checked.setVisibility(View.VISIBLE);
                    holder.checked.setAnimation(func.fadeIn());
                    selectedItems.add(getData);
                    getData.setChecked(true);
                    confirmChecklist.confirm.show();
                    int color = Color.parseColor("#F2F6F2");
                    holder.card.setBackgroundTintList(ColorStateList.valueOf(color));
                }
                if (selectedItems.isEmpty()) {
                    confirmChecklist.confirm.hide();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

    public List<model_confirm_list> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<model_confirm_list> selectedItems) {
        this.selectedItems = selectedItems;
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public void setAllCheckedFalse() {
        for (model_confirm_list item : newsList) {
            item.setChecked(false);
        }
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialCardView card;
        public TextView audit_date,farm_name,farm_house_flock;
        public ImageView checked;


        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            audit_date = itemView.findViewById(R.id.audit_date);
            farm_name = itemView.findViewById(R.id.farm_name);
            farm_house_flock = itemView.findViewById(R.id.farm_house_flock);
            checked = itemView.findViewById(R.id.checked);

        }

    }
}
