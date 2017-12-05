package com.example.moad.myapplicationtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moad.myapplicationtest.model.TvShow;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moad.myapplicationtest.model.Result;
import com.example.moad.myapplicationtest.model.TopRated;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * /**
 * Created by moad on 28/11/2017.
 */

public class PaginationTvShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<TvShow> Tvshows;
    private Context context;
    int layoutCard;
    public final ListItemClickListener monclickListener;

    public void setMovies(List<TvShow> movies) {
        this.Tvshows = movies;
    }

    public List<TvShow> getMovies() {

        return Tvshows;
    }

    public PaginationTvShowAdapter(ListItemClickListener Listener, Context context, int layou) {
        this.context = context;
        monclickListener = Listener;
        Tvshows = new ArrayList<>();
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
        TvShow tvshow = Tvshows.get(position);

        switch (getItemViewType(position)) {
            case ITEM:

                MovieVH movieVH = (MovieVH) holder;

                if (layoutCard != R.layout.cell_cards_3) {
                    movieVH.title.setText(tvshow.getOriginalName());
                    movieVH.subtitle.setText(tvshow.getOverview());
                }

                String url = ApiKey.urlImage + "" + tvshow.getPosterPath();
                Picasso.with(movieVH.imageView.getContext()).load(url).fit().centerInside().into(movieVH.imageView);

                break;
            case LOADING:
//                Do nothing
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == Tvshows.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return Tvshows == null ? 0 : Tvshows.size();
    }

    public void add(TvShow mc) {
        Tvshows.add(mc);
        notifyItemInserted(Tvshows.size() - 1);
    }

    public void addAll(List<TvShow> mcList) {
        for (TvShow mc : mcList) {
            add(mc);
        }
    }

    public void remove(TvShow city) {
        int position = Tvshows.indexOf(city);
        if (position > -1) {
            Tvshows.remove(position);
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
        add(new TvShow());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = Tvshows.size() - 1;
        TvShow item = getItem(position);

        if (item != null) {
            Tvshows.remove(position);
            notifyItemRemoved(position);
        }
    }

    public TvShow getItem(int position) {
        return Tvshows.get(position);
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
      //              monclickListener.onListItemClick(Tvshows.get(clickedPosition));
                }

            });
            layout = layou;
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
