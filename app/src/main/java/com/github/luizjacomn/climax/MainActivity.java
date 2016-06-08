package com.github.luizjacomn.climax;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by luizjaco on 07/06/16.
 * https://developer.forecast.io
 * 8023168941567f2195e0da0e0ca51ddb/latitude,longitude
 */
public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private ClimaAtual mClimaAtual;

    @InjectView(R.id.label_tempo) TextView mLabelTempo;
    @InjectView(R.id.label_temperatura) TextView mLabelTemperatura;
    @InjectView(R.id.valor_humidade) TextView mValorHumidade;
    @InjectView(R.id.valor_precipitacao) TextView mValorPrecipitacao;
    @InjectView(R.id.label_resumo) TextView mLabelResumo;
    @InjectView(R.id.iconeView) ImageView mIconeView;
    @InjectView(R.id.atualizaView) ImageView mAtualizaView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = -4.9708;
        final double longitude = -39.0161;

        mAtualizaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrevisao(latitude, longitude);
            }
        });

        Log.d(TAG, "Codigo principal executando...");
    }

    private void getPrevisao(double latitude, double longitude) {
        final String apiKey = "8023168941567f2195e0da0e0ca51ddb";
        final String URL = "https://api.forecast.io/forecast/";
        String previsaoURI = URL + apiKey + "/" + latitude + "," + longitude;

        if (isInternetDisponivel()) {
            toggleAtualizar();

            OkHttpClient cliente = new OkHttpClient();
            Request request = new Request.Builder().url(previsaoURI).build();

            Call call = cliente.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleAtualizar();
                        }
                    });
                    alertarUsuarioSobreErro();
                }

                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleAtualizar();
                        }
                    });
                    try {
                        String dadosJSON = response.body().string();
                        Log.v(TAG, dadosJSON);
                        if (response.isSuccessful()) {
                            mClimaAtual = getDetalhesAtuais(dadosJSON);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    atualizarDados();
                                }
                            });
                        } else {
                            alertarUsuarioSobreErro();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exceção capturada: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exceção capturada: ", e);
                    }
                }
            });

        } else {
            Toast.makeText(this, R.string.mensagem_rede_indisponivel, Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleAtualizar() {
        if (mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mAtualizaView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAtualizaView.setVisibility(View.VISIBLE);
        }
    }

    private void atualizarDados() {
        mLabelTemperatura.setText(mClimaAtual.getTemperatura() + "");
        mLabelTempo.setText(mClimaAtual.getTempoFormatado() + "");
        mValorHumidade.setText(mClimaAtual.getHumidade() + "%");
        mValorPrecipitacao.setText(mClimaAtual.getChancePrecipitacao() + "%");
        mLabelResumo.setText(mClimaAtual.getResumo() + "");
        Drawable drawable = getResources().getDrawable(mClimaAtual.getIconId());
        mIconeView.setImageDrawable(drawable);
    }

    private ClimaAtual getDetalhesAtuais(String dadosJSON) throws JSONException {
            JSONObject previsao = new JSONObject(dadosJSON);
            String timeZone = previsao.getString("timezone");
            Log.i(TAG, "JSON -> " + timeZone);

            JSONObject atualmente = previsao.getJSONObject("currently");
            ClimaAtual climaAtual = new ClimaAtual();
            climaAtual.setHumidade(atualmente.getDouble("humidity"));
            climaAtual.setTemperatura(atualmente.getDouble("temperature"));
            climaAtual.setTempo(atualmente.getLong("time"));
            climaAtual.setIcone(atualmente.getString("icon"));
            climaAtual.setChancePrecipitacao(atualmente.getDouble("precipProbability"));
            climaAtual.setResumo(atualmente.getString("summary"));
            climaAtual.setTimeZone(timeZone);

            Log.d(TAG, climaAtual.getTempoFormatado());
        return climaAtual;
    }

    private boolean isInternetDisponivel() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isDisponivel = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isDisponivel = true;
        }
        return isDisponivel;
    }

    private void alertarUsuarioSobreErro() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "dialogo_erro");
    }
}