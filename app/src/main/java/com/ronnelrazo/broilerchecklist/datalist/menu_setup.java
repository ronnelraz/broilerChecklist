package com.ronnelrazo.broilerchecklist.datalist;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.novoda.merlin.Merlin;
import com.ronnelrazo.broilerchecklist.Adapter.Adapter_menu_list;
import com.ronnelrazo.broilerchecklist.Menu;
import com.ronnelrazo.broilerchecklist.R;
import com.ronnelrazo.broilerchecklist.model.model_menu;

import java.util.ArrayList;
import java.util.List;

public class menu_setup {

    Context mContext;

    List<model_menu> list = new ArrayList<>();
    RecyclerView.Adapter adapter;

    String[] Title = new String[]{
            "Sync",
            "Create",
            "Edit",
            "Confirm",
            "Cancel",
            "Transaction list",

    };

    int[] icon = new int[]{
            R.drawable.icons8_synchronize,
            R.drawable.new_create,R.drawable.new_edit,
            R.drawable.new_confirm,R.drawable.new_cancel,
            R.drawable.new_checklist
    };

    private  int count(){
        return Title.length;
    }



    public void Menu(RecyclerView gridlayout, boolean b, Menu menu){
        list.clear();
        for (int i = 0; i < count(); i++) {
            model_menu items = new model_menu(
                    i,
                    icon[i],
                    Title[i]

            );
            list.add(items);
        }
        GridLayoutManager layout_manager = new GridLayoutManager(mContext, 2);
        adapter = new Adapter_menu_list(list,mContext,b,menu);
        gridlayout.setLayoutManager(layout_manager);
        gridlayout.setHasFixedSize(true);
        gridlayout.setItemViewCacheSize(999999999);
        gridlayout.setAdapter(adapter);
    }
}
