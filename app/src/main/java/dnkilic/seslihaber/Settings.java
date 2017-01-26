package dnkilic.seslihaber;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by ismail on 1/26/2017.
 */

public class Settings extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);




    }
}
