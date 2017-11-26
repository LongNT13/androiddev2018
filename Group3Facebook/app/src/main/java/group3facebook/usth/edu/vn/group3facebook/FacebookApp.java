package group3facebook.usth.edu.vn.group3facebook;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dangvinhbao on 26/11/2017.
 */

public class FacebookApp extends Application {
    private RequestQueue queue ;
    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this.getApplicationContext());
    }

    public RequestQueue getQueue() {
        return queue;
    }
}
