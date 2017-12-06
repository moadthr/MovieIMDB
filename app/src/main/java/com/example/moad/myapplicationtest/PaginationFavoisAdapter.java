package com.example.moad.myapplicationtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.moad.myapplicationtest.model.Result;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moad on 25/11/2017.
 */

public class PaginationFavoisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Result> movies;
    private Context context;
    int layoutCard;
    public final ListItemClickListener monclickListener;

    public void setMovies(List<Result> movies) {
        this.movies = movies;
    }

    public List<Result> getMovies() {

        return movies;
    }

    public PaginationFavoisAdapter(ListItemClickListener Listener, Context context, int layou) {
        this.context = context;
        monclickListener = Listener;
        movies = new ArrayList<>();
        this.layoutCard = layou;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater, layoutCard);

                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater, int layout) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        viewHolder = new MovieVH(v1, layoutCard);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Result movie = movies.get(position);

        switch (getItemViewType(position)) {
            case ITEM:

                MovieVH movieVH = (MovieVH) holder;

                if (layoutCard != R.layout.cell_cards_3) {
                    if (movie.getTitle() != null)
                        movieVH.title.setText(movie.getTitle());
                    if (movie.getOriginalName() != null)
                        movieVH.title.setText(movie.getOriginalName());
                    movieVH.subtitle.setText(movie.getOverview());
                }
                String url = ApiKey.urlImage + "" + movie.getPosterPath();
                Picasso.with(movieVH.imageView.getContext()).load(url).fit().centerInside().into(movieVH.imageView);

                break;
            case LOADING:

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void add(Result mc) {
        movies.add(mc);
        notifyItemInserted(movies.size() - 1);
    }

    public void addAll(List<Result> mcList) {
        for (Result mc : mcList) {
            add(mc);
        }
    }

    public void remove(Result city) {
        int position = movies.indexOf(city);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        Result item = getItem(position);

        if (item != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return movies.get(position);
    }

    protected class MovieVH extends RecyclerView.ViewHolder {

        TextView title;
        private ImageView imageView;
        TextView subtitle;
        int layout;

        public MovieVH(View itemView, int layou) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    monclickListener.onListItemClick(movies.get(clickedPosition));
                }

            });
            if (layout != R.layout.cell_cards_3) {
                title = (TextView) itemView.findViewById(R.id.text);
                subtitle = (TextView) itemView.findViewById(R.id.overview);
            }
            imageView = (ImageView) itemView.findViewById(R.id.Cell_cards_img);
        }

    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }


    }
}


