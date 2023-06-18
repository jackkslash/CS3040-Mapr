package uk.aston.maprapp.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.aston.maprapp.R;

public class ActivitiesListAdapter extends RecyclerView.Adapter<ActivitiesListAdapter.ActivitiesViewHolder>{

    LayoutInflater inflater;
    List<Activities> activities;

    public ActivitiesListAdapter(Context ctx, List<Activities> activities){
        this.inflater = LayoutInflater.from(ctx);
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.act_recycleview_item,parent, false);
        return new ActivitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (activities !=null){
            holder.nameView.setText(activities.get(position).getPlaceName());
            holder.locView.setText(activities.get(position).getVicinity());
            holder.rateView.setText(activities.get(position).getRating());
            holder.dirButton.setOnClickListener(view -> {

                String mURL = "http://plus.codes/"+activities.get(position).getPlusCode();

                Log.d("url", ""+mURL);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
                intent.setPackage("com.google.android.apps.maps");
                view.getContext().startActivity(intent);
            });
        }else {

        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivitiesViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameView;
        private final TextView locView;
        private final TextView rateView;
        private final Button dirButton;

        public ActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.textPlace);
            locView = itemView.findViewById(R.id.textLocation);
            rateView = itemView.findViewById(R.id.textRating);
            dirButton = itemView.findViewById(R.id.dirButton);

        }
    }

}
