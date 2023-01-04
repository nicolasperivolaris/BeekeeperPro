package com.beekeeperpro.ui.apiary;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;

import java.util.ArrayList;
import java.util.List;

public class ApiaryListAdapter extends RecyclerView.Adapter<ApiaryListAdapter.ViewHolder> {

    private List<Apiary> apiaries;
    private final MutableLiveData<Apiary> clickedId;
    private final MutableLiveData<Apiary> onDeleteItem;
    private boolean editMode = false;

    public ApiaryListAdapter() {
        apiaries = new ArrayList<>();
        clickedId = new MutableLiveData<>();
        onDeleteItem = new MutableLiveData<>();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.apiary_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public void setApiaries(List<Apiary> apiaries) {
        this.apiaries = apiaries;
        notifyDataSetChanged();
    }

    public MutableLiveData<Apiary> getClickedId() {
        return clickedId;
    }

    public MutableLiveData<Apiary> getOnDeleteItem() {
        return onDeleteItem;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(apiaries.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return apiaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Apiary apiary;
        private final ImageView image;
        private final TextView apiaryName;
        private final TextView apiaryLocation;
        private final TextView hiveCount;
        private final ImageButton delete;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.apiaryImage);
            apiaryName = view.findViewById(R.id.apiaryName);
            apiaryLocation = view.findViewById(R.id.apiaryLocation);
            hiveCount = view.findViewById(R.id.hiveCount);
            delete = itemView.findViewById(R.id.action_delete);
            delete.setOnClickListener(v -> onDeleteItem.postValue(apiary));
            view.setOnClickListener(v -> clickedId.postValue(apiary));
        }

        public void bind(Apiary apiary) {
            this.apiary = apiary;
            apiaryName.setText(apiary.getName());
            apiaryLocation.setText(apiary.getLocation());
            hiveCount.setText(String.valueOf(apiary.getHivesCount()));
            delete.setVisibility(editMode ? View.VISIBLE : View.GONE);
        }
    }

}
