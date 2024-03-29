package com.beekeeperpro.ui.inspection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.MainActivity;
import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Inspection;

import java.util.ArrayList;
import java.util.List;

public class InspectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean editMode = false;
    private List<Inspection> inspectionList;
    private final MutableLiveData<Inspection> onClickedItem;
    private final MutableLiveData<Inspection> onDeleteItem;
    private final MutableLiveData<Inspection> onAddInspectionItem;

    public InspectionListAdapter() {
        super();
        this.inspectionList = new ArrayList<>();
        onClickedItem = new MutableLiveData<>();
        onDeleteItem = new MutableLiveData<>();
        onAddInspectionItem = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inspection_row_item, parent, false);
            return new InspectionViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inspection_row_header, parent, false);
            return new InspectionViewHolderHeader(itemView);
        }
    }

    public void setInspectionList(List<Inspection> inspections) {
        this.inspectionList = inspections;
        notifyDataSetChanged();
    }

    public MutableLiveData<Inspection> getOnClickedItem() {
        return onClickedItem;
    }

    public MutableLiveData<Inspection> getOnDeleteItem() {
        return onDeleteItem;
    }

    public MutableLiveData<Inspection> getOnAddInspectionItem() {
        return onAddInspectionItem;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InspectionViewHolder)
            ((InspectionViewHolder) holder).bind(inspectionList.get(position - 1));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return inspectionList.size() + 1;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public class InspectionViewHolder extends RecyclerView.ViewHolder {
        private Inspection inspection;
        private final TextView inspectionDate;
        private final TextView inspectionTemper;
        private final TextView inspectionQueenCondition;
        private final TextView inspectionHiveCondition;
        private final ImageView phytoUsed;
        private final ImageButton delete;

        public InspectionViewHolder(@NonNull View itemView) {
            super(itemView);
            inspectionDate = itemView.findViewById(R.id.inspection_date);
            inspectionTemper = itemView.findViewById(R.id.inspection_temper);
            inspectionQueenCondition = itemView.findViewById(R.id.inspection_queen_condition);
            inspectionHiveCondition = itemView.findViewById(R.id.inspection_hive_condition);
            phytoUsed = itemView.findViewById(R.id.phyto_used);
            delete = itemView.findViewById(R.id.action_delete);
            delete.setOnClickListener(v -> onDeleteItem.postValue(inspection));
        }

        void bind(Inspection inspection) {
            this.inspection = inspection;
            inspectionDate.setText(inspection.getInspectionDate().toString());
            inspectionTemper.setText(inspection.getTemper());
            inspectionQueenCondition.setText(String.valueOf(inspection.getQueenCondition()));
            inspectionHiveCondition.setText(inspection.getHiveCondition());
            phytoUsed.setVisibility(inspection.getPhytosanitaryUsed().equals(
                    MainActivity.instance.getResources().getStringArray(R.array.phytosanitary_values)[0]) ? View.INVISIBLE : View.VISIBLE);
            delete.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
    }

    public static class InspectionViewHolderHeader extends RecyclerView.ViewHolder {
        public InspectionViewHolderHeader(@NonNull View itemView) {
            super(itemView);
        }
    }
}