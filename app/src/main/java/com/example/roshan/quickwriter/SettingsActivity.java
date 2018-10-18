package com.example.roshan.quickwriter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    RadioButton robotoSlab, roboto, montserrat, openSans, ptSerif, colorLight, colorDark, colorAutomatic;
    SharedPreferences.Editor settingsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ThemeHelper.isDarkTheme(SettingsActivity.this)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.roshan.quickwriter.FontPreference", Context.MODE_PRIVATE);
        settingsEditor = sharedPreferences.edit();
        String fontFamily = sharedPreferences.getString("fontFamily", "robotoSlab");
        String colorTheme = sharedPreferences.getString("colorTheme", "automatic");
        robotoSlab = findViewById(R.id.fontRadioRobotoSlab);
        roboto = findViewById(R.id.fontRadioRoboto);
        montserrat = findViewById(R.id.fontRadioMontserrat);
        openSans = findViewById(R.id.fontRadioOpenSans);
        ptSerif = findViewById(R.id.fontRadioPTSerif);
        colorLight = findViewById(R.id.colorLight);
        colorDark = findViewById(R.id.colorDark);
        colorAutomatic = findViewById(R.id.colorAutomatic);

        switch(fontFamily) {
            case "robotoSlab":
                robotoSlab.toggle();
                break;
            case "roboto":
                roboto.toggle();
                break;
            case "montserrat":
                montserrat.toggle();
                break;
            case "ptSerif":
                ptSerif.toggle();
                break;

            case "openSans":
                openSans.toggle();
                break;

            default:
        }

        switch(colorTheme) {
            case "automatic":
                colorAutomatic.toggle();
                break;
            case "light":
                colorLight.toggle();
                break;
            case "dark":
                colorDark.toggle();
                default:
        }

        Toolbar toolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onRadioButtonClicked(View view) {

        switch(view.getId()) {
            case R.id.fontRadioRobotoSlab:
                robotoSlab.toggle();
                settingsEditor.putString("fontFamily", "robotoSlab");
                settingsEditor.commit();
                break;
            case R.id.fontRadioRoboto:
                roboto.toggle();
                settingsEditor.putString("fontFamily", "roboto");
                settingsEditor.commit();
                break;
            case R.id.fontRadioMontserrat:
                montserrat.toggle();
                settingsEditor.putString("fontFamily", "montserrat");
                settingsEditor.commit();
                break;
            case R.id.fontRadioPTSerif:
                ptSerif.toggle();
                settingsEditor.putString("fontFamily", "ptSerif");
                settingsEditor.commit();
                break;

            case R.id.fontRadioOpenSans:
                openSans.toggle();
                settingsEditor.putString("fontFamily", "openSans");
                settingsEditor.commit();
                break;

            case R.id.colorAutomatic:
                colorAutomatic.toggle();
                settingsEditor.putString("colorTheme", "automatic");
                settingsEditor.commit();
                recreate();
                break;
            case R.id.colorLight:
                colorLight.toggle();
                settingsEditor.putString("colorTheme", "light");
                settingsEditor.commit();
                recreate();
                break;
            case R.id.colorDark:
                colorDark.toggle();
                settingsEditor.putString("colorTheme", "dark");
                settingsEditor.commit();
                recreate();
                break;

        }
    }
}
