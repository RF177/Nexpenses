package com.rf17.nexpenses.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;

public class UtilsApp {

    public static Intent goToGooglePlay(String id) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + id));

        return intent;
    }

    public static Intent goToSite(String site) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://" + site));

        return intent;
    }

    public static String getAppVersionName(Context context) {
        String res = "0.0.0.0";
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static int getAppVersionCode(Context context) {
        int res = 0;
        try {
            res = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Mostra Toast/Mensagem de erro na tela
     *
     * @param activity
     * 			- Activity/tela que sera utilizado o Toast
     * @param message
     * 			- Erro/mensagem/aviso
     */
    public static void showToast(Activity activity, String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void setAppColor(Window window, Toolbar toolbar){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppPreferences appPreferences = NexpensesApplication.getAppPreferences();
            window.setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            window.setNavigationBarColor(appPreferences.getPrimaryColorPref());
            if(toolbar != null) {
                toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            }
        }
    }

    /**
     * Esconde o teclado do dispositivo
     *
     * @param activity
     * 			- Activity/tela que ser� utilizado para esconder o teclado
     * @param view
     * 			- ...
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}
