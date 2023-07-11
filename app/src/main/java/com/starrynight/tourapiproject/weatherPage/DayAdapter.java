package com.starrynight.tourapiproject.weatherPage;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.DayObservationalFit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayHolder> {

    private List<DayObservationalFit> items;

    public DayAdapter(List<DayObservationalFit> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.weather__day_view, parent, false);

        return new DayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DayHolder holder, int position) {
        DayObservationalFit item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(DayObservationalFit item) {
        items.add(item);
    }

    public void setItems(ArrayList<DayObservationalFit> items) {
        this.items = items;
    }

    public DayObservationalFit getItem(int position) {
        return items.get(position);
    }

    public static class DayHolder extends RecyclerView.ViewHolder {

        TextView day;
        TextView date;
        TextView observationFit;

        public DayHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day_day);
            date = itemView.findViewById(R.id.day_date);
            observationFit = itemView.findViewById(R.id.day_observation_fit);

            itemView.setClickable(false);
        }

        public void setItem(DayObservationalFit item) {
            day.setText(item.getDay());
            if (item.getDay().equals("일")) day.setTextColor(Color.parseColor("#EC3E4D"));
            if (item.getDay().equals("오늘")) {
                day.setTextColor(Color.parseColor("#DADAE5"));
                observationFit.setTextColor(Color.parseColor("#DADAE5"));
                observationFit.setBackgroundResource(R.drawable.wt__day_today);
            }
            date.setText(item.getDate());
            observationFit.setText(item.getObservationalFit());
        }
    }

}
