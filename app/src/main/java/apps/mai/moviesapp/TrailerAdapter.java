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

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.CustomViewHolder>{


    Context context;
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
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.trailer_image);
            this.textView = (TextView) view.findViewById(R.id.trailer_name);
        }

    }


    /*
    private class ViewHolder {
        ImageView image_trailer;
        TextView title_trailer;
    }

    @Override
    public int getCount() {
        return trailers_link.size();
    }

    @Override
    public Object getItem(int i) {
        return trailers_link.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String trailer_link = trailers_link.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //convertView = mInflater.inflate(R.layout.trailer_item, null);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.trailer_item,parent,false);
            holder = new ViewHolder();
            holder.title_trailer = (TextView) convertView.findViewById(R.id.trailer_name);
            holder.image_trailer = (ImageView) convertView.findViewById(R.id.trailer_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title_trailer.setText("Trailer " + (position + 1));
        //holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }*/
}
