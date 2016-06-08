package com.github.luizjacomn.climax;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by luizjaco on 08/06/16.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.titulo_erro)
                .setMessage(R.string.mensagem_erro)
                .setPositiveButton(R.string.botao_ok, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
