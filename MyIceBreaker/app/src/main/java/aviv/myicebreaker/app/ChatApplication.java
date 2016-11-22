package aviv.myicebreaker.app;


import android.app.Application;
import android.provider.SyncStateContract;
import android.util.Log;

import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;


import java.net.URISyntaxException;


public class ChatApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://79.181.246.70:8082");
        } catch (URISyntaxException e) {
            Log.e("exception socket", e + "");

        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
