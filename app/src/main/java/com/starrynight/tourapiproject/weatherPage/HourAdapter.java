package com.starrynight.tourapiproject.weatherPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.HourObservationalFit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourHolder> {

    private List<HourObservationalFit> items;

    public HourAdapter(List<HourObservationalFit> items) {
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
        HourObservationalFit item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(HourObservationalFit item) {
        items.add(item);
    }

    public void setItems(ArrayList<HourObservationalFit> items) {
        this.items = items;
    }

    public HourObservationalFit getItem(int position) {
        return items.get(position);
    }

    public static class HourHolder extends RecyclerView.ViewHolder {

        View hourLayout;
        TextView hour;
        TextView observationFit;

        public HourHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            hourLayout = itemView.findViewById(R.id.hour_layout);
            hour = itemView.findViewById(R.id.hour_hour);
            observationFit = itemView.findViewById(R.id.hour_observation_fit);

            itemView.setClickable(false);
        }

        public void setItem(HourObservationalFit item) {
            if (item.getHour().equals("23")) hourLayout.setPadding(0, 0, 24, 0);
            hour.setText(item.getHour());
            observationFit.setText(item.getObservationalFit());
        }
    }

}
