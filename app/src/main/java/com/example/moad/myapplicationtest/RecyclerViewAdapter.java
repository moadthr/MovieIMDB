package com.example.moad.myapplicationtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moad.myapplicationtest.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by moad on 11/11/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NameViewHolder>{

    private int numberItems ;
    public final  ListItemClickListener monclickListener ;
    private List<Result> listRatedMovies ;
    int layoutCard ;



    public RecyclerViewAdapter(ListItemClickListener Listener, int n, List<Result> list,int layoutCard) {
        monclickListener = Listener;
        this.numberItems=n;
        listRatedMovies= list;
        this.layoutCard=layoutCard;
    }




    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(layoutCard,parent,false);

//        Context context = parent.getContext();
//        int  layoutIdForListItem = R.layout.name_list_item;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean bool =false ;
//        View view = inflater.inflate(layoutIdForListItem,parent, bool);
//        NameViewHolder viewholder = new NameViewHolder(view);
        return  new NameViewHolder(view,layoutCard);

    }



    public int getItemCount() {
        return numberItems;
    }

    public  void onBindViewHolder(NameViewHolder holder, int position){

        holder.bind(listRatedMovies.get(position));
    }


    class NameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        int layout ;
        TextView title ;
        private ImageView imageView;
        TextView subtitle ;

        public NameViewHolder(View itemView,int layoutt) {
            super(itemView);
//            listItemNameView =(TextView) itemView.findViewById(R.id.tv_item_name);
//            listItemPrenomView =(TextView) itemView.findViewById(R.id.tv_item_prenom);
//            listItemAgeView =(TextView) itemView.findViewById(R.id.tv_item_age);
            layout= layoutt;
            if(layout != R.layout.cell_cards_3){

            title = (TextView) itemView.findViewById(R.id.text);
            subtitle = (TextView) itemView.findViewById(R.id.overview);

             }

            imageView = (ImageView) itemView.findViewById(R.id.Cell_cards_img);
            itemView.setOnClickListener(this);
        }

        void bind (Result res){

            if(layout != R.layout.cell_cards_3) {
                title.setText(res.getTitle());
                subtitle.setText(res.getOverview());
            }

            String url = ApiKey.urlImage+""+res.getPosterPath();
            Picasso.with(imageView.getContext()).load(url).fit().centerInside().into(imageView);

        }

        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            monclickListener.onListItemClick(listRatedMovies.get(clickedPosition));
        }

    }
}
