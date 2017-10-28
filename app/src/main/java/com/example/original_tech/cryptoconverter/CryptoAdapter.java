package com.example.original_tech.cryptoconverter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Original-Tech on 10/21/2017.
 */
public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.DataObjectHolder> {

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex, String price, String currency);
    }
    List<JSONObject> arrayOfData;
    private final ListItemClickListener mOnclickListener;
    Context context;

    public CryptoAdapter(List<JSONObject> arrayOfData, ListItemClickListener listener, Context context) {
        this.arrayOfData = arrayOfData;
        this.mOnclickListener=listener;
        this.context=context;
    }

    @Override
    public CryptoAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.custommainlayout,parent,false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(CryptoAdapter.DataObjectHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayOfData.size();
    }

    public void updateAdapter(List<JSONObject> cards) {
        arrayOfData = cards;
        notifyDataSetChanged();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView priceRate;
        TextView volume;
        TextView time;
        TextView supplier;
        TextView currency;
        public DataObjectHolder(View itemView) {
            super(itemView);
            priceRate=(TextView) itemView.findViewById(R.id.price_rate);
            volume=(TextView) itemView.findViewById(R.id.volume);
            time=(TextView) itemView.findViewById(R.id.time);
            supplier=(TextView) itemView.findViewById(R.id.supplier);
            currency=(TextView) itemView.findViewById(R.id.currency_logo);
            itemView.setOnClickListener(this);
            itemView.setId(itemView.getId());
        }

        public void bind(final int position) throws JSONException {
            priceRate.setText(arrayOfData.get(position).getString("price_rate"));
            volume.setText(arrayOfData.get(position).getString("volume"));
            time.setText(String.valueOf(arrayOfData.get(position).getString("last_update")));
            supplier.setText(arrayOfData.get(position).getString("last_market"));
            currency.setText((arrayOfData.get(position).getString("cryptocurrency"))+" - "+(arrayOfData.get(position).getString("currency")));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition=getAdapterPosition();
            String price=priceRate.getText().toString();
            String curr=currency.getText().toString();
            mOnclickListener.onListItemClick(clickedPosition,price,curr);
        }
    }
}
