package apps.mai.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.CustomViewHolder>{


    private Context context;
    ArrayList<String> trailers_link;

    public TrailerAdapter(Context context, //resourceId=your layout
                          ArrayList<String> trailers_link) {
        this.context = context;
        this.trailers_link = trailers_link;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.textView.setText("Trailer " + (position + 1));
        holder.imageView.setImageResource(R.drawable.ic_play);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trailer_key = trailers_link.get(position);
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v="+trailer_key)));

            }
        });


    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomViewHolder holder = (CustomViewHolder) view.getTag();
            int position = holder.getPosition();

            String trailerKey = trailers_link.get(position);
            Toast.makeText(context, trailerKey, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public int getItemCount() {
        return trailers_link.size();
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.trailer_image) ImageView imageView;
        @BindView(R.id.trailer_name) TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
