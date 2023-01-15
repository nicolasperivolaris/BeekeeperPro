package com.beekeeperpro.ui.menu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.beekeeperpro.R;

public abstract class EditMenuProvider implements MenuProvider {
    private boolean edition = false;

    protected abstract void onEditButton();

    protected abstract void onFinishButton();

    public final int editId = 1;
    public final int finishId = 2;
    private Menu menu;

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
        menu.findItem(finishId).setVisible(false);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        this.menu = menu;
        menu.add(Menu.NONE, editId, Menu.NONE, R.string.action_edit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, finishId, Menu.NONE, R.string.action_finish)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setVisible(false);
        if (menuItem.getItemId() == editId) {
            onEditButton();
            menu.findItem(finishId).setVisible(true);
            edition = true;
            return true;
        }
        if (menuItem.getItemId() == finishId) {
            onFinishButton();
            menu.findItem(editId).setVisible(true);
            edition = false;
            return true;
        }
        return false;
    }

    public boolean isEdition() {
        return edition;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }
}
