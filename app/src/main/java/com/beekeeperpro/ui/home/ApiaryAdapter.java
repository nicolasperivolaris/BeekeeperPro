package com.beekeeperpro.ui.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;

import java.util.ArrayList;
import java.util.List;

public class ApiaryAdapter extends RecyclerView.Adapter<ApiaryAdapter.ViewHolder> {

    private List<Apiary> apiaries;
    private final MutableLiveData<Integer> clickedId;

    public ApiaryAdapter() {
        apiaries = new ArrayList<>();
        clickedId = new MutableLiveData<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.apiary_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public void setApiaries(List<Apiary> apiaries) {
        this.apiaries = apiaries;
        notifyDataSetChanged();
    }

    public MutableLiveData<Integer> getClickedId() {
        return clickedId;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Integer id;
        private final ImageView image;
        private final TextView apiaryName;
        private final TextView apiaryLocation;
        private final TextView hiveCount;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.apiaryImage);
            apiaryName = view.findViewById(R.id.apiaryName);
            apiaryLocation = view.findViewById(R.id.apiaryLocation);
            hiveCount = view.findViewById(R.id.hiveCount);
            view.setOnClickListener(v -> clickedId.postValue(id));
        }

        public void bind(Apiary apiary){
            id = apiary.getId();
            apiaryName.setText(apiary.getName());
            apiaryLocation.setText(apiary.getLocation());
            hiveCount.setText(String.valueOf(apiary.getHivesCount()));
        }
    }

}
