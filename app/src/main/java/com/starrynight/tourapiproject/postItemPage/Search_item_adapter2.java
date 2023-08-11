package com.starrynight.tourapiproject.postItemPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.postWritePage.SearchObservingPointActivity;

import java.util.ArrayList;

/**
 * className :  Search_item_adapter2
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-06-16
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-06-16      jinhyeok      최초생성
 */
public class Search_item_adapter2 extends RecyclerView.Adapter<Search_item_adapter2.ViewHolder> {
    ArrayList<Search_item> searchItemArrayList;
    OnsearchItemClickListener2 listener;


    public Search_item_adapter2(ArrayList<Search_item> searchItemArrayList, SearchObservingPointActivity searchObservingPointActivity) {
        this.searchItemArrayList = searchItemArrayList;
    }

    public Search_item getItem(int position) {
        return searchItemArrayList.get(position);
    }

    @NonNull
    @Override
    public Search_item_adapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_add_search_item, parent, false);
        return new Search_item_adapter2.ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_item_adapter2.ViewHolder holder, final int position) {

        holder.itemName.setText(searchItemArrayList.get(position).getItemName());
        holder.address.setText(searchItemArrayList.get(position).getAddress());

    }

    public void setOnItemClicklistener(OnsearchItemClickListener2 listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return searchItemArrayList.size();
    }

    public void filterList(ArrayList<Search_item> filteredList) {
        searchItemArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView address;

        public ViewHolder(@NonNull View itemView, final OnsearchItemClickListener2 listener) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            address = itemView.findViewById(R.id.address);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(Search_item_adapter2.ViewHolder.this, v, position);
                    }
                }
            });

        }
    }
}
