package com.example.pr7.ui.top.recycler;

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
import com.example.pr7.ui.top.models.Genre;
import com.example.pr7.ui.top.models.Top;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private static ArrayList<Film> filmArrayList;

    public TopAdapter(Context context, ArrayList<Film> filmArrayList) {
        this.inflater = LayoutInflater.from(context);
        this.filmArrayList = filmArrayList;
    }

    @NonNull
    @Override
    public TopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.top_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StringBuilder genreString = new StringBuilder();
        Film film = filmArrayList.get(position);
        Picasso.get().load(film.getPosterUrlPreview()).into(holder.filmImg);
        holder.filmName.setText(film.getNameRu());
        for (int i = 0; i < film.getGenres().size(); i++) {
            Genre genre = film.getGenres().get(i);
            if (i == film.getGenres().size() - 1)
            {
                genreString.append(genre.getGenre()).append(".");
            }
            else
            {
                genreString.append(genre.getGenre()).append(", ");
            }
        }
        holder.filmGenres.setText(String.format("%s%s", genreString.substring(0, 1).toUpperCase(), genreString.substring(1)));
        holder.filmRating.setText(film.getRating());
        holder.filmYear.setText(film.getYear());
    }

    @Override
    public int getItemCount() {
        return filmArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView filmImg;
        final TextView filmName;
        final TextView filmGenres;
        final TextView filmRating;
        final TextView filmYear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filmImg = itemView.findViewById(R.id.topfilms_img);
            filmName = itemView.findViewById(R.id.topfilms_name);
            filmGenres = itemView.findViewById(R.id.topfilms_genres);
            filmRating = itemView.findViewById(R.id.topfilms_rating);
            filmYear = itemView.findViewById(R.id.topfilms_year);
            itemView.setOnClickListener(view -> {
                Film film = TopAdapter.filmArrayList.get(getAbsoluteAdapterPosition());
                Intent intent = new Intent(view.getContext(), FilmDetail.class);
                intent.putExtra("filmId", (int)film.getFilmId());
                view.getContext().startActivity(intent);
            });
        }
    }
}
