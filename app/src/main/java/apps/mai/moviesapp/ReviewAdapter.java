package apps.mai.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mai_ on 24-Sep-16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>{


    Context context;
    ArrayList<String> reviews_link;

    public ReviewAdapter(Context context,
                          ArrayList<String> reviews_link) {
        this.context = context;
        this.reviews_link = reviews_link;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.textView.setText(reviews_link.get(position));


    }


    @Override
    public int getItemCount() {
        return reviews_link.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.review);
        }

    }


}