package com.appTec.RegistrateApp.view.generic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class InformationDialog {

    private static AlertDialog.Builder alertDialog = null;

    public static void createDialog(Context context) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
        }
    }

    public static void setTitle(String title) {
        if (alertDialog != null) {
            alertDialog.setTitle(title);
        }
    }

    public static void setMessage(String message) {
        if (alertDialog != null) {
            alertDialog.setMessage(message);
        }
    }

    public static void showDialog() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }
}
