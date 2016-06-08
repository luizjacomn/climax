package com.github.luizjacomn.climax;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Math.*;

/**
 * Created by luizjaco on 08/06/16.
 */
public class ClimaAtual {
    private String mIcone;
    private Long mTempo;
    private double mTemperatura;
    private double mHumidade;
    private double mChancePrecipitacao;
    private String mResumo;
    private String mTimeZone;

    public String getIcone() {
        return mIcone;
    }

    public void setIcone(String mIcone) {
        this.mIcone = mIcone;
    }

    public int getIconId() {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, partly-cloudy-night
        int iconId = R.drawable.clear_day;

        if (mIcone.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if (mIcone.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if (mIcone.equals("rain")) {
            iconId = R.drawable.rain;
        } else if (mIcone.equals("snow")) {
            iconId = R.drawable.snow;
        } else if (mIcone.equals("sleet")) {
            iconId = R.drawable.sleet;
        } else if (mIcone.equals("wind")) {
            iconId = R.drawable.wind;
        } else if (mIcone.equals("fog")) {
            iconId = R.drawable.fog;
        } else if (mIcone.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if (mIcone.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if (mIcone.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    public Long getTempo() {
        return mTempo;
    }

    public String getTempoFormatado() {
        SimpleDateFormat formatador = new SimpleDateFormat("HH:mm");
        formatador.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date data = new Date(getTempo() * 1000);
        String stringTempo = formatador.format(data);
        return stringTempo;
    }

    public void setTempo(Long mTempo) {
        this.mTempo = mTempo;
    }

    public int getTemperatura() {
        return (int) round(mTemperatura);
    }

    public void setTemperatura(Double mTemperatura) {
        this.mTemperatura = mTemperatura;
    }

    public int getHumidade() {
        double porcentagem = mHumidade * 100;
        return (int) Math.round(porcentagem);
    }

    public void setHumidade(double mHumidade) {
        this.mHumidade = mHumidade;
    }

    public int getChancePrecipitacao() {
        double porcentagem = mChancePrecipitacao * 100;
        return (int) Math.round(porcentagem);
    }

    public void setChancePrecipitacao(double mChancePrecipitacao) {
        this.mChancePrecipitacao = mChancePrecipitacao;
    }

    public String getResumo() {
        return mResumo;
    }

    public void setResumo(String mResumo) {
        this.mResumo = mResumo;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String mTimeZone) {
        this.mTimeZone = mTimeZone;
    }

}
