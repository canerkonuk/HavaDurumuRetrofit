package com.example.androcaner.havadurumuretrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HavaDurumuAdapter extends RecyclerView.Adapter<HavaDurumuAdapter.HavaDurumuViewHolder> {

    private List<com.example.androcaner.havadurumuretrofit.List> list;
    private Context context;

    public HavaDurumuAdapter(List<com.example.androcaner.havadurumuretrofit.List> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HavaDurumuAdapter.HavaDurumuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_hava_durumu_row, viewGroup,false);
        HavaDurumuViewHolder havaDurumuViewHolder=new HavaDurumuViewHolder(view);
        return havaDurumuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HavaDurumuAdapter.HavaDurumuViewHolder havaDurumuViewHolder, int i) {

        //Tarihi güne çevirmek için(Türkçe dilde)...
        String tarıh=list.get(i).getDt_txt();
        DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=m_ISO8601Local.parse(tarıh);
            SimpleDateFormat sdf=new SimpleDateFormat("EEEE HH:mm", new Locale("tr","TR"));
            String gunismi=sdf.format(date);
            havaDurumuViewHolder.tv1.setText(gunismi);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.with(havaDurumuViewHolder.itemView.getContext()).load("https://openweathermap.org/img/w/"+list.get(i).getWeather().get(0).getIcon()+".png").placeholder(R.drawable.blabla).resize(50,50).into(havaDurumuViewHolder.cv);
        havaDurumuViewHolder.tv2.setText(list.get(i).getWeather().get(0).getDescription());
        Float dereceint=list.get(i).getMain().getTemp();
        havaDurumuViewHolder.tv3.setText(dereceint.intValue()+"°C");

    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    class HavaDurumuViewHolder extends RecyclerView.ViewHolder{

        TextView tv1,tv2,tv3;
        CircleImageView cv;



        public HavaDurumuViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1=(TextView) itemView.findViewById(R.id.gun);
            cv=(CircleImageView) itemView.findViewById(R.id.ogununhavaiconu);
            tv2=(TextView) itemView.findViewById(R.id.ogununhavaacıklaması);
            tv3=(TextView) itemView.findViewById(R.id.ogununhavaderecesi);
        }
    }


}
