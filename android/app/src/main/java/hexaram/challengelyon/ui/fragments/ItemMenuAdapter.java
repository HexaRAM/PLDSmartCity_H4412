package hexaram.challengelyon.ui.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import hexaram.challengelyon.R;



public class ItemMenuAdapter extends RecyclerView.Adapter<ItemMenuAdapter.ItemMenuViewHolder> {

    private LayoutInflater inflater;
    List<ItemMenu> data = Collections.emptyList();
    private ClickListener clickListener;
    private Context context;

    public ItemMenuAdapter(Context context, List<ItemMenu> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public ItemMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        ItemMenuViewHolder holder = new ItemMenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemMenuViewHolder holder, final int position) {
        ItemMenu current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    class ItemMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView title;
        ImageView icon;

        public ItemMenuViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.itemClicked(v,getPosition());
            }
        }
    }
}