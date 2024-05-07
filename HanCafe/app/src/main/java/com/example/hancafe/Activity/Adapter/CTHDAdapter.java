package com.example.hancafe.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hancafe.R;

import java.util.List;

import com.example.hancafe.Model.CTHD;

public class CTHDAdapter extends RecyclerView.Adapter<CTHDAdapter.CTHDViewHolder> {

    private List<CTHD> listCTHD;

    public CTHDAdapter(List<CTHD> listCTHD) {
        this.listCTHD = listCTHD;
    }
    @NonNull
    @Override
    public CTHDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productreport, parent, false);
        return new CTHDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CTHDViewHolder holder, int position) {
        CTHD cthd = listCTHD.get(position);
        if(cthd == null) {
            return;
        }
        String SL = String.valueOf(cthd.getSL());
        String Total = String.valueOf(cthd.getTotal());
        holder.tvNameSP.setText(cthd.getNameProduct());
        holder.tvSL.setText(SL);
        holder.tvTotalPrice.setText(Total);

    }

    @Override
    public int getItemCount() {
        if(listCTHD!=null) {
            return listCTHD.size();
        }
        return 0;
    }

    public class CTHDViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameSP, tvSL,tvTotalPrice;
        public CTHDViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSP = itemView.findViewById(R.id.tvNameSP);
            tvSL = itemView.findViewById(R.id.tvSL);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
