package com.beekeeperpro.ui.menu;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.MenuProvider;

import com.beekeeperpro.R;

public class VRMenuProvider implements MenuProvider{
    private static boolean state = false;

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);

    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if( menuItem.getItemId() == R.id.action_vr_switch) {
            SwitchCompat view = ((SwitchCompat)menuItem.getActionView());
            if(view.isChecked()){
                SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechRecognizer.startListening(intent);

            } else {

            }
            return true;
        }
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }
}
