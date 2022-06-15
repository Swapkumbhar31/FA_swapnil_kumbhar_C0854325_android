package ca.lambton.fa_swapnil_kumbhar_c0854325_android.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    private SharedPreferences preferences;
    private final String FILE_KEY = "users_settings";

    public UserSettings getInstance(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(this.FILE_KEY, Context.MODE_PRIVATE);
        }
        return this;
    }

    public boolean isFirstTimeOpen() {
        return preferences.getBoolean("is_first_time_open", true);
    }

    public void setIsFirstTimeOpen(boolean val) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_first_time_open", val);
        editor.apply();
    }
}
