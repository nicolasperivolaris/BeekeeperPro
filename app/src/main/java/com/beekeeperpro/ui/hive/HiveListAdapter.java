package com.beekeeperpro.ui.hive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Hive;

import java.util.ArrayList;
import java.util.List;

public class HiveListAdapter extends RecyclerView.Adapter<HiveListAdapter.HiveViewHolder> {

    private List<Hive> hiveList;
    private final MutableLiveData<Integer> clickedId;

    public HiveListAdapter() {
        this.hiveList = new ArrayList<>();
        clickedId = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public HiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hive_row_item, parent, false);
        return new HiveViewHolder(view);
    }

    public void setHiveList(List<Hive> hives){
        this.hiveList = hives;
        notifyDataSetChanged();
    }

    public MutableLiveData<Integer> getClickedId() {
        return clickedId;
    }

    @Override
    public void onBindViewHolder(@NonNull HiveViewHolder holder, int position) {
        holder.bind(hiveList.get(position));
    }

    @Override
    public int getItemCount() {
        return hiveList.size();
    }

    public class HiveViewHolder extends RecyclerView.ViewHolder {
        private Integer id;
        private final TextView textViewName;
        private final TextView textViewCode;
        private final TextView textViewStrength;
        private final TextView textViewCreationDate;

        public HiveViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewCode = itemView.findViewById(R.id.text_view_code);
            textViewStrength = itemView.findViewById(R.id.text_view_strength);
            textViewCreationDate = itemView.findViewById(R.id.text_view_creation_date);
            itemView.setOnClickListener(v->clickedId.postValue(id));
        }

        public void bind(Hive hive){
            textViewName.setText(hive.getName());
            textViewCode.setText(hive.getCode());
            textViewStrength.setText(String.valueOf(hive.getStrength()));
            textViewCreationDate.setText(hive.getCreationDate().toString());
        }
    }
}