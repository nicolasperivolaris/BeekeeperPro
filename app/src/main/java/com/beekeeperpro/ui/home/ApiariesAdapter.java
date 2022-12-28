package com.beekeeperpro.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeperpro.R;
import com.beekeeperpro.data.model.Apiary;

import java.util.ArrayList;
import java.util.List;

public class ApiariesAdapter extends RecyclerView.Adapter<ApiariesAdapter.ViewHolder> {

    private List<Apiary> apiaries;
    private Activity activity;

    public ApiariesAdapter(Activity activity) {
        apiaries = new ArrayList<>();
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.apiary_row_item, viewGroup, false);

        return new ViewHolder(view, activity);
    }

    public void setApiaries(List<Apiary> apiaries) {
        this.apiaries = apiaries;
        notifyDataSetChanged();
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int id;
        private final ImageView image;
        private final TextView apiaryName;
        private final TextView apiaryLocation;
        private final TextView hiveCount;

        public ViewHolder(View view, Activity activity) {
            super(view);
            image = view.findViewById(R.id.apiaryImage);
            apiaryName = view.findViewById(R.id.apiaryName);
            apiaryLocation = view.findViewById(R.id.apiaryLocation);
            hiveCount = view.findViewById(R.id.hiveCount);
            view.setOnClickListener(this);
        }

        public void bind(Apiary apiary){
            id = apiary.getId();
            apiaryName.setText(apiary.getName());
            apiaryLocation.setText(apiary.getLocation());
            hiveCount.setText(String.valueOf(apiary.getHivesCount()));
        }

        @Override
        public void onClick(View view) {
            //int position = getAdapterPosition();
            //int id = apiaries.get(position).getId();
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
            Bundle args = new Bundle();
            args.putInt("id", id);
            navController.navigate(R.id.action_nav_home_to_apiaryFragment, args);
        }
    }

}
