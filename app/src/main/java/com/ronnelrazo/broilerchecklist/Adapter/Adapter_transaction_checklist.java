package com.ronnelrazo.broilerchecklist.Adapter;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.ronnelrazo.broilerchecklist.API.API;
import com.ronnelrazo.broilerchecklist.API.API_TRANSACTION;
import com.ronnelrazo.broilerchecklist.Cancel_menu;
import com.ronnelrazo.broilerchecklist.R;
import com.ronnelrazo.broilerchecklist.SharedPref.SharedPref;
import com.ronnelrazo.broilerchecklist.Transction_menu;
import com.ronnelrazo.broilerchecklist.func.Func;
import com.ronnelrazo.broilerchecklist.model.model_confirm_list;
import com.ronnelrazo.broilerchecklist.model.transaction_data;
import com.ronnelrazo.broilerchecklist.model.transaction_data_offline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_transaction_checklist extends RecyclerView.Adapter<Adapter_transaction_checklist.ViewHolder>  {
    Context mContext;
    List<transaction_data_offline> newsList;
    List<transaction_data_offline> filteredList;
    private List<transaction_data_offline> selectedItems;
    Func func;
    Transction_menu transctionMenu;



    public Adapter_transaction_checklist(List<transaction_data_offline> list, Context context, Func func, Transction_menu transctionMenu) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.func = func;
        this.filteredList = new ArrayList<>(list);
        this.selectedItems = new ArrayList<>();
        this.transctionMenu = transctionMenu;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_checklist_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final transaction_data_offline getData = newsList.get(position);
        int resId = R.anim.layout_animation_fadein;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mContext, resId);
        holder.card.setLayoutAnimation(animation);

        holder.card.setEnabled(false);
        holder.audit_date.setText(func.header_html("Audit Date" , getData.getAUDIT_DATE(),""));
        holder.farm_name.setText(func.header_html("Farm Name" , String.format("%s (%s)", getData.getFARM_NAME(), getData.getHOUSE_FLOCK()),""));

        holder.approveby.setText(func.header_html("Approve By" , String.format("%s", TextUtils.isEmpty(getData.getAPPROVE_BY()) ? "" : getData.getAPPROVE_BY()),""));

        String status =  getData.getCANCEL_FLAG().equals("Y") ? "Canceled" : getData.getAPPROVE_FLAG().equals("N") ? "waiting" : "Approved";

        holder.status.setText(func.header_html("Status" , String.format("%s", status),""));
        holder.document_no.setText(func.header_html("Document No" , String.format("%s", "N/A"),""));

        int icon_status = getData.getCANCEL_FLAG().equals("Y") ? R.drawable.cancel_trn : getData.getAPPROVE_FLAG().equals("N") ? R.drawable.icons8_sand_watch : R.drawable.check;
        Drawable newDrawable = mContext.getResources().getDrawable(icon_status);
        newDrawable.setBounds(0, 0, newDrawable.getIntrinsicWidth(), newDrawable.getIntrinsicHeight());
        holder.status.setCompoundDrawablesRelativeWithIntrinsicBounds(newDrawable, null, null, null);
        loadDataOL(holder,getData);
        if(getData.isCHECKED()){
            holder.checked.setVisibility(View.VISIBLE);
        }
        else{
            holder.checked.setVisibility(View.GONE);
        }


        if(getData.getCANCEL_FLAG().equals("Y")){
            holder.card.setEnabled(false);
        }
        else{
            holder.card.setEnabled(true);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getData.isCHECKED()) {
                    holder.checked.setVisibility(View.GONE);
                    holder.checked.setAnimation(func.fadeOut());
                    selectedItems.remove(getData);
                    getData.setCHECKED(false);
                    int color = Color.parseColor("#FFFFFF");
                    holder.card.setBackgroundTintList(ColorStateList.valueOf(color));
                } else {
                    holder.checked.setVisibility(View.VISIBLE);
                    holder.checked.setAnimation(func.fadeIn());
                    selectedItems.add(getData);
                    getData.setCHECKED(true);
                    transctionMenu.confirm.show();
                    int color = Color.parseColor("#F2F6F2");
                    holder.card.setBackgroundTintList(ColorStateList.valueOf(color));
                }
                if (selectedItems.isEmpty()) {
                    transctionMenu.confirm.hide();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

    public void loadDataOL(ViewHolder holder,transaction_data_offline getData){
        API.getClient().TRANSACTION_Broiler_Checklist(SharedPref.getORG_CODE(),SharedPref.getUSER(),getData.getAUDIT_DATE(),getData.getHOUSE_FLOCK()).enqueue(new Callback<API_TRANSACTION>() {
            @Override
            public void onResponse(Call<API_TRANSACTION> call, Response<API_TRANSACTION> response) {
                if (response.isSuccessful()) {
                    API_TRANSACTION res = response.body();
                    if (res != null && res.isSuccess()) {
                        List<transaction_data> checklistItems = res.getTransactionDataList();
                        if (checklistItems != null) {
                            String getDocument_No = checklistItems.get(0).getDocumentNo();
                            String getApproveFlag = checklistItems.get(0).getApprove_flag();
                            String getApproveBy = checklistItems.get(0).getApproveBy();


                            holder.card.setEnabled(true);

                            holder.approveby.setText(func.header_html("Approve By" , String.format("%s", TextUtils.isEmpty(getApproveBy) ? "" : getApproveBy),""));

                            String status =  getData.getCANCEL_FLAG().equals("Y") ? "Canceled" : getApproveFlag.equals("N") ? "waiting" : "Approved";

                            holder.status.setText(func.header_html("Status" , String.format("%s", status),""));
                            holder.document_no.setText(func.header_html("Document No" , String.format("%s", getDocument_No),""));


                            int icon_status = getData.getCANCEL_FLAG().equals("Y") ? R.drawable.cancel_trn : getApproveFlag.equals("N") ? R.drawable.icons8_sand_watch : R.drawable.check;
                            Drawable newDrawable = mContext.getResources().getDrawable(icon_status);
                            newDrawable.setBounds(0, 0, newDrawable.getIntrinsicWidth(), newDrawable.getIntrinsicHeight());
                            holder.status.setCompoundDrawablesRelativeWithIntrinsicBounds(newDrawable, null, null, null);

                        } else {
                            holder.card.setEnabled(false);
                            holder.document_no.setText(func.header_html("Document No" , String.format("%s", getData.getDOCUMENT_NO()),""));
                        }

                    } else {
                        Toast.makeText(mContext, getData.getFARM_NAME() + " not yet sync.\nFarm House Flock: " + getData.getHOUSE_FLOCK(), Toast.LENGTH_SHORT).show();
                        holder.card.setEnabled(false);

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<API_TRANSACTION> call, Throwable t) {
                func.log("OnFailure : " + t.getMessage());

            }
        });
    }

    public List<transaction_data_offline> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<transaction_data_offline> selectedItems) {
        this.selectedItems = selectedItems;
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public void setAllCheckedFalse() {
        for (transaction_data_offline item : newsList) {
            item.setCHECKED(false);
        }
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public MaterialCardView card;
        public TextView audit_date,farm_name,approveby,status,document_no;
        public ImageView checked;


        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            audit_date = itemView.findViewById(R.id.audit_date);
            farm_name = itemView.findViewById(R.id.farm_name);
            checked = itemView.findViewById(R.id.checked);
            approveby = itemView.findViewById(R.id.approveby);
            status = itemView.findViewById(R.id.status);
            document_no = itemView.findViewById(R.id.document_no);

        }

    }
}
