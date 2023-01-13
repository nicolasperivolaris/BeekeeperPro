package com.beekeeperpro.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.EditText;

import androidx.core.content.FileProvider;

import com.beekeeperpro.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static void initDatePicker(EditText field) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crée un date picker dans le format de date local
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                field.getContext(),
                (datePicker, year1, month1, day1) -> {
                    // Mise à jour du champ de date avec la date sélectionnée
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, year1);
                    calendar1.set(Calendar.MONTH, month1);
                    calendar1.set(Calendar.DAY_OF_MONTH, day1);
                    Date date = calendar1.getTime();
                    field.setText(dateFormat.format(date));
                },
                year, month, day);
        datePickerDialog.show();
    }

    public static int convertStringToArrayPosition(String value, int arrayId) {
        String[] array = MainActivity.instance.getResources().getStringArray(arrayId);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static Uri getEmptyPictureFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    "JPEG_" + timeStamp + "_",  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileProvider.getUriForFile(context, "com.beekeeperpro.fileprovider", image);
    }
}
