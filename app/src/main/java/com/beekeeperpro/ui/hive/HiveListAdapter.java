package com.beekeeperpro.ui.hive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;

import java.util.ArrayList;
import java.util.List;

public class HiveListAdapter extends RecyclerView.Adapter<HiveListAdapter.HiveViewHolder> {
    private boolean editMode = false;
    private List<Hive> hiveList;
    private final MutableLiveData<Hive> onClickedItem;
    private final MutableLiveData<Hive> onDeleteItem;
    private final MutableLiveData<Hive> onAddInspectionItem;

    public HiveListAdapter() {
        this.hiveList = new ArrayList<>();
        onClickedItem = new MutableLiveData<>();
        onDeleteItem = new MutableLiveData<>();
        onAddInspectionItem = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public HiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hive_row_item, parent, false);
        return new HiveViewHolder(view);
    }

    public void setHiveList(List<Hive> hives) {
        this.hiveList = hives;
        notifyDataSetChanged();
    }

    public MutableLiveData<Hive> getOnClickedItem() {
        return onClickedItem;
    }

    public MutableLiveData<Hive> getOnDeleteItem() {
        return onDeleteItem;
    }

    public MutableLiveData<Hive> getOnAddInspectionItem() {
        return onAddInspectionItem;
    }

    @Override
    public void onBindViewHolder(@NonNull HiveViewHolder holder, int position) {
        holder.bind(hiveList.get(position));
    }

    @Override
    public int getItemCount() {
        return hiveList.size();
    }

    void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public class HiveViewHolder extends RecyclerView.ViewHolder {
        private Hive hive;
        private final TextView textViewName;
        private final TextView textViewCode;
        private final TextView textViewStrength;
        private final TextView textViewCreationDate;
        private final Button addInspection;
        private final ImageButton delete;

        public HiveViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewCode = itemView.findViewById(R.id.text_view_code);
            textViewStrength = itemView.findViewById(R.id.text_view_strength);
            textViewCreationDate = itemView.findViewById(R.id.text_view_creation_date);
            delete = itemView.findViewById(R.id.action_delete);
            addInspection = itemView.findViewById(R.id.inspection_bt);
            //if you click on a row
            itemView.setOnClickListener(v -> onClickedItem.postValue(hive));
            delete.setOnClickListener(v -> onDeleteItem.postValue(hive));
            addInspection.setOnClickListener(v -> onAddInspectionItem.postValue(hive));
        }

        void bind(Hive hive) {
            this.hive = hive;
            textViewName.setText(hive.getName());
            textViewCode.setText(hive.getCode());
            textViewStrength.setText(MainActivity.instance.getResources().getStringArray(R.array.strength_bar_values)[hive.getStrength()]);
            if (hive.getHivingDate() != null)
                textViewCreationDate.setText(hive.getHivingDate().toString());
            delete.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
    }
}