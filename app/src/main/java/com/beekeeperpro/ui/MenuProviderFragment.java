package com.beekeeperpro.ui;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.beekeeperpro.R;

public abstract class MenuProviderFragment extends Fragment implements MenuProvider {

    protected abstract void onSaveButton();

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.findItem(R.id.save_button).setVisible(true);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if( menuItem.getItemId() == R.id.save_button) {
            onSaveButton();
            return true;
        }
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }
}
