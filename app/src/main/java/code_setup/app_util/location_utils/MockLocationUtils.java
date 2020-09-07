package code_setup.app_util.location_utils;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;

public class MockLocationUtils {
    /**
     * For Build.VERSION.SDK_INT < 18 i.e. JELLY_BEAN_MR2
     * Check if MockLocation setting is enabled or not
     *
     * @param context Pass Context object as parameter
     * @return Returns a boolean indicating if MockLocation is enabled
     */
    public static Boolean isMockLocationEnabled(Context context) {
        return !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }

    /**
     * For Build.VERSION.SDK_INT >= 18 i.e. JELLY_BEAN_MR2
     * Check if the location recorded is a mocked location or not
     *
     * @param location Pass Location object received from the OS's onLocationChanged() callback
     * @return Returns a boolean indicating if the received location is mocked
     */
    public static boolean isMockLocation(Location location) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && location != null && location.isFromMockProvider();
    }
}
