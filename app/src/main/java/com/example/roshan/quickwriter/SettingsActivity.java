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

    RadioButton robotoSlab, roboto, montserrat, openSans, ptSerif;
    SharedPreferences.Editor settingsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.roshan.quickwriter.FontPreference", Context.MODE_PRIVATE);
        settingsEditor = sharedPreferences.edit();
        String fontFamily = sharedPreferences.getString("fontFamily", "robotoSlab");
        robotoSlab = findViewById(R.id.fontRadioRobotoSlab);
        roboto = findViewById(R.id.fontRadioRoboto);
        montserrat = findViewById(R.id.fontRadioMontserrat);
        openSans = findViewById(R.id.fontRadioOpenSans);
        ptSerif = findViewById(R.id.fontRadioPTSerif);
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

        }
    }
}
