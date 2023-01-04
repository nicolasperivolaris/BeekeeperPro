package com.beekeeperpro.utils;

import android.app.DatePickerDialog;
import android.widget.EditText;

import com.beekeeperpro.MainActivity;

import java.text.DateFormat;
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
}
