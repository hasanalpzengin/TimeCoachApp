package com.hasanalpzengin.lifeorganizator;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class LanguageFragment extends Fragment {

    LinearLayout turkishLayout, englishLayout;

    public LanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language, container, false);

        turkishLayout = view.findViewById(R.id.turkish);
        englishLayout = view.findViewById(R.id.english);

        turkishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLang(getContext(), "tr");
                Toast.makeText(getContext(), getString(R.string.language_changed), Toast.LENGTH_SHORT).show();
                Intent restart = new Intent(getContext(), ResultActivity.class);
                startActivity(restart);
            }
        });

        englishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLang(getContext(), "en");
                Toast.makeText(getContext(), getString(R.string.language_changed), Toast.LENGTH_SHORT).show();
                Intent restart = new Intent(getContext(), ResultActivity.class);
                startActivity(restart);
            }
        });

        return view;
    }

    public static Context changeLang(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("lang",lang).apply();

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N_MR1) {
            return context.createConfigurationContext(configuration);
        }else{
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        }
    }

}
