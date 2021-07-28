package com.example.campus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{
    private Context context;
    private List<Post> posts;
    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @NotNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPosterName;
        private ImageView ivPosterImage;
        private TextView tvTime;
        private TextView tvPostBody;
        private ImageView ivPostPicture;
        private ImageView ivCurrentUser;
        private TextView tvBody;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPosterName = itemView.findViewById(R.id.tvPosterName);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPostBody = itemView.findViewById(R.id.tvPostBody);
            ivPostPicture = itemView.findViewById(R.id.ivPostPicture);
            ivCurrentUser = itemView.findViewById(R.id.ivCurrentUser);
            tvBody = itemView.findViewById(R.id.tvBody);

        }
        public void bind(Post post) {
            //Bind the post data to the view elements
            tvPostBody.setText(post.getDescription());
            tvPosterName.setText(post.getPoster().getUsername());
            Glide.with(context)
                    .load(post.getPoster().getParseFile("Profile_pic").getUrl())
                    .circleCrop() // create an effect of a round profile picture
                    .into(ivPosterImage);
            tvTime.setText(calculateTimeAgo(post.getCreatedAt()));
            Glide.with(context)
                    .load(ParseUser.getCurrentUser().getParseFile("Profile_pic").getUrl())
                    .circleCrop() // create an effect of a round profile picture
                    .into(ivCurrentUser);
            ParseFile image = post.getImage();
            if (image != null) {
                int radius = 50; // corner radius, higher value = more rounded
                int margin = 0; // crop margin, set to 0 for corners with no crop
                GlideApp.with(context)
                        .load(image.getUrl())
                        .fitCenter() // scale image to fill the entire ImageView
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(ivPostPicture);
            }
        }
    }
    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
    // add posts to recycler
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}
