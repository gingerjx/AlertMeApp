package com.example.alertmeapp.fragments.alert;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.data.Vote;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.requests.VoteRequest;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.AlertMeServiceImpl;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final FragmentActivity activity;
    private final List<AlertItem> alertList;
    private final PorterDuffColorFilter GREEN = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter RED = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter GRAY = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private AlertMeServiceImpl alertMeServiceImpl = new AlertMeServiceImpl();
    private final long USER_ID = 1L;


    public MyListRecyclerViewAdapter(FragmentActivity activity, List<AlertItem> items) {
        this.alertList = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Alert alert = alertList.get(position).getAlert();
        String distance = alertList.get(position).getDistance();

        holder.titleView.setText(alert.getTitle());
        holder.typeView.setText(alert.getAlertType().getName());
        holder.rangeView.setText(distance);
        holder.alertVotes.setText(String.valueOf(alert.getNumber_of_votes()));
        int color = getColorBasedOnAlertType(alert.getAlertType().getName());
        holder.materialCardView.setStrokeColor(color);
        holder.upvote.setColorFilter(GRAY);
        holder.downvote.setColorFilter(GRAY);
        findVote(new VoteRequest(alert.getId(), USER_ID), holder);
        holder.titleView.setOnClickListener(v -> openAlertDetails(alert.getId(), holder.titleView));
        holder.upvote.setOnClickListener(v -> handleUpVote(holder.upvote, holder.downvote, holder.alertVotes, alert));
        holder.downvote.setOnClickListener(v -> handleDownVote(holder.upvote, holder.downvote, holder.alertVotes, alert));
    }

    private void openAlertDetails(long id, TextView titleView) {
        titleView.requestFocus();
        Bundle bundle = new Bundle();
        bundle.putLong("alertId", id);
        NavController navController = Navigation.findNavController(activity, R.id.fragmentController);
        navController.navigate(R.id.alertDetailsFragment, bundle);
    }

    private int getColorBasedOnAlertType(String alertType) {
        int color = 0;
        switch (alertType) {
            case "danger":
                color = Color.parseColor("#960018"); //CARMINE
                break;
            case "warning":
                color = Color.parseColor("#FF8000"); //ORANGE
                break;
            case "information":
                color = Color.parseColor("#007FFF"); //AZURE
                break;
            case "curiosity":
                color = Color.GREEN;
                break;
        }
        return color;
    }

    private void handleUpVote(ImageView upvote, ImageView downvote, TextView alertVotes, Alert alert) {
        if (upvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) + 1;
            if (downvote.getColorFilter().equals(RED))
                numberOfVotes++;
            upvote.setColorFilter(GREEN);
            downvote.setColorFilter(GRAY);
            alertVotes.setText(String.valueOf(numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            alertMeServiceImpl.updateAlert(alert);
            alertMeServiceImpl.createVote(new VoteRequest(alert.getId(), USER_ID, true));
        } else if (upvote.getColorFilter().equals(GREEN)) {
            upvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(--numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            alertMeServiceImpl.updateAlert(alert);
            alertMeServiceImpl.findAndDeleteVote(new VoteRequest(alert.getId(), USER_ID));
        }
    }

    private void handleDownVote(ImageView upvote, ImageView downvote, TextView alertVotes, Alert alert) {
        if (downvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) - 1;
            if (upvote.getColorFilter().equals(GREEN))
                numberOfVotes--;
            downvote.setColorFilter(RED);
            upvote.setColorFilter(GRAY);
            alertVotes.setText(String.valueOf(numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            alertMeServiceImpl.updateAlert(alert);
            alertMeServiceImpl.createVote(new VoteRequest(alert.getId(), USER_ID, false));
        } else if (downvote.getColorFilter().equals(RED)) {
            downvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(++numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            alertMeServiceImpl.updateAlert(alert);
            alertMeServiceImpl.findAndDeleteVote(new VoteRequest(alert.getId(), USER_ID, false));
        }
    }

    private void findVote(VoteRequest voteRequest, ViewHolder holder) {
        Call<ResponseSingleData<Vote>> call = service.findVote(voteRequest);
        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
                if (response.body() == null) {
                    holder.upvote.setColorFilter(GRAY);
                    holder.downvote.setColorFilter(GRAY);
                } else if (response.body().getData().isUpped())
                    holder.upvote.setColorFilter(GREEN);
                else if (!response.body().getData().isUpped())
                    holder.downvote.setColorFilter(RED);
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Vote>> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView typeView;
        private final TextView rangeView;
        private final TextView alertVotes;
        public final ImageView upvote;
        private final ImageView downvote;
        private final MaterialCardView materialCardView;

        public ViewHolder(View view) {
            super(view);
            materialCardView = view.findViewById(R.id.material_card);
            titleView = view.findViewById(R.id.titleTextView);
            typeView = view.findViewById(R.id.typeTextView);
            rangeView = view.findViewById(R.id.rangeTextView);
            alertVotes = view.findViewById(R.id.alert_votes);
            upvote = view.findViewById(R.id.upvote);
            downvote = view.findViewById(R.id.downvote);
        }
    }
}