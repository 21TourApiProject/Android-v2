package com.starrynight.tourapiproject.weatherPage2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.HourObservationFit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourHolder> {

    private List<HourObservationFit> items;

    public HourAdapter(List<HourObservationFit> items) {
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public HourHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.weather__hour_view, parent, false);

        return new HourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HourHolder holder, int position) {
        HourObservationFit item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(HourObservationFit item) {
        items.add(item);
    }

    public void setItems(ArrayList<HourObservationFit> items) {
        this.items = items;
    }

    public HourObservationFit getItem(int position) {
        return items.get(position);
    }

    public static class HourHolder extends RecyclerView.ViewHolder {

        TextView hour;
        TextView observationFit;

        public HourHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.hour_hour);
            observationFit = itemView.findViewById(R.id.hour_observation_fit);

            itemView.setClickable(false);
        }

        public void setItem(HourObservationFit item) {
            hour.setText(item.getHour());
            observationFit.setText(item.getObservationFit() + "%");
        }
    }

}
