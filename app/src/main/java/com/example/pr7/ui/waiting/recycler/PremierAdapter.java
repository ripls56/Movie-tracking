package com.example.pr7.ui.waiting.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pr7.R;
import com.example.pr7.ui.filmdetail.FilmDetail;
import com.example.pr7.ui.top.models.Film;
import com.example.pr7.ui.waiting.models.Genre;
import com.example.pr7.ui.waiting.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PremierAdapter extends RecyclerView.Adapter<PremierAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private static ArrayList<Item> filmArrayList;

    public PremierAdapter(Context context, ArrayList<Item> filmArrayList) {
        this.inflater = LayoutInflater.from(context);
        PremierAdapter.filmArrayList = filmArrayList;
    }

    @NonNull
    @Override
    public PremierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.premier_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StringBuilder genreString = new StringBuilder();
        Item item = filmArrayList.get(position);
        Picasso.get().load(item.getPosterUrlPreview()).into(holder.filmImg);
        holder.filmName.setText(item.getNameRu());
        for (int i = 0; i < item.getGenres().size(); i++) {
            Genre genre = item.getGenres().get(i);
            if (i == item.getGenres().size() - 1)
            {
                genreString.append(genre.getGenre()).append(".");
            }
            else
            {
                genreString.append(genre.getGenre()).append(", ");
            }
        }
        if (item.getGenres().size() != 0){
            holder.filmGenres.setText(String.format("%s%s", genreString.substring(0, 1).toUpperCase(), genreString.substring(1)));
        }
        holder.filmYear.setText(String.valueOf(item.getYear()));
    }

    @Override
    public int getItemCount() {
        return filmArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView filmImg;
        final TextView filmName;
        final TextView filmGenres;
        final TextView filmYear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filmImg = itemView.findViewById(R.id.premierfilms_img);
            filmName = itemView.findViewById(R.id.premierfilms_name);
            filmGenres = itemView.findViewById(R.id.premierfilms_genres);
            filmYear = itemView.findViewById(R.id.premierfilms_year);
            itemView.setOnClickListener(view -> {
                Item item = PremierAdapter.filmArrayList.get(getAbsoluteAdapterPosition());
                Intent intent = new Intent(view.getContext(), FilmDetail.class);
                intent.putExtra("filmId", item.getKinopoiskId());
                view.getContext().startActivity(intent);
            });
        }
    }
}
