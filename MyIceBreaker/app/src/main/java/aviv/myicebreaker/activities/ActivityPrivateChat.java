package aviv.myicebreaker.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.module.Adapters.BubbleChatAdapter;
import aviv.myicebreaker.module.BubbleType;
import aviv.myicebreaker.module.CustomObjects.BubbleChatObject;
import aviv.myicebreaker.module.JsonParser;
import aviv.myicebreaker.network.BaseListener;
import aviv.myicebreaker.network.Connectivity;
import aviv.myicebreaker.network.ConnectivityError;
import aviv.myicebreaker.network.ResponseObject;

/**
 * Created by Aviad on 16/09/2016.
 */
public class ActivityPrivateChat extends AppCompatActivity implements View.OnClickListener, BaseListener {

    private static final String CONVERSATION_KEY = "conversationKey";
    private EditText mInputMessageView;
    private LinearLayout layoutBtnSendMsg;
    private Socket mSocket;
    private boolean mTyping = false;
    private JSONObject chatIdJson;
    private ListView listChat;
    private ArrayList<BubbleChatObject> arrayListBubbleChat;
    private BubbleChatAdapter bubbleChatAdapter;
    private int revealPicCounter;
    private ImageButton btnFire, btnBackFromChat;
    private Connectivity connectivity;
    private String chatId;
    private int questionType = 0;
    private boolean isLike;
    private String questionId;
    private Button btnLikeQuestion, btnDislikeQuestion;
    private String localUserId;

    {
        try {
            mSocket = IO.socket("http://79.183.12.183:8082/");
        } catch (URISyntaxException e) {
            Log.e("exception socket", e + "");
        }
    }


    private Emitter.Listener hello = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d("data msg ", Arrays.toString(args) + "!");

                }
            });
        }
    };

    private Emitter.Listener incomingMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {


                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("incomingMessage ", data + "!");
                    Log.d("data msg2 ", data + "!");
                    String username;
                    String message;
                    try {
                        username = data.getString("senderId");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.e("exception ", e + " ");
                        return;
                    }
                    if (revealPicCounter < 20) {
                        if (arrayListBubbleChat.get(arrayListBubbleChat.size() - 1).getBubbleType() == BubbleType.LOCAL_USER) {
                            if (++revealPicCounter % 2 == 0) {
                                Log.d("nextStep ", revealPicCounter + "");
                                connectivity.updateRevealStage(chatId);
                            }
                        }
                    }
                    addMessage(username, message, BubbleType.OTHER_SIDE_USER);

                }
            });
        }

    };

    private Emitter.Listener socketError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("socketError ", data + "!");

                }
            });
        }
    };
    private Emitter.Listener typing = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("typing  ", data + "!");

                }
            });
        }
    };
    private Emitter.Listener stoppedTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("stopped typing ", data + "!");

                }
            });
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_private_chat);
        chatId = "57c914fd3288e95b0ab2abb2";
        try {
            chatIdJson = new JSONObject("{'chatId':'" + chatId + "'}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questionId = "50";

        initSocketListeners();
        connectivity = new Connectivity(this);
        initViews();
        initActionBar();

        arrayListBubbleChat = new ArrayList<>();
        createChat();
        loadConversationSharedPreferences();
        bubbleChatAdapter = new BubbleChatAdapter(this, arrayListBubbleChat);
        listChat.setAdapter(bubbleChatAdapter);
        bubbleChatAdapter.notifyDataSetChanged();


        Log.d("after", getString(R.string.server_ip));

    }

    private void createChat() {
        Log.d("createChat ", "connect");

        arrayListBubbleChat.add(new BubbleChatObject(BubbleType.OTHER_SIDE_USER, "היי מה נשמע?", "13:33", 0));
        arrayListBubbleChat.add(new BubbleChatObject(BubbleType.OTHER_SIDE_USER, "אללהוואלה באלה גאלה טאלה שמאלה שאלה בלבלה", "13:33", 0));
        arrayListBubbleChat.add(new BubbleChatObject(BubbleType.OTHER_SIDE_USER, "היי מה נשמע?", "13:33", 0));
        arrayListBubbleChat.add(new BubbleChatObject(BubbleType.OTHER_SIDE_USER, "היdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמע?", "13:33", 0));
        arrayListBubbleChat.add(new BubbleChatObject(BubbleType.LOCAL_USER, "היdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמיdscdscdscי מה נשמע?", "13:33", 0));
        arrayListBubbleChat.add(new BubbleChatObject(BubbleType.QUESTION, "זה עם הפיצה", "13:33", 0));

        bubbleChatAdapter = new BubbleChatAdapter(this, arrayListBubbleChat);
        listChat.setAdapter(bubbleChatAdapter);

    }

    private void initViews() {
        btnFire = (ImageButton) findViewById(R.id.btnFire);
        btnBackFromChat = (ImageButton) findViewById(R.id.btnBackFromChat);
        mInputMessageView = (EditText) findViewById(R.id.inputMessage);
        listChat = (ListView) findViewById(R.id.listChat);
        btnLikeQuestion = (Button) findViewById(R.id.btnLikeQuestion);
        btnDislikeQuestion = (Button) findViewById(R.id.btnDislikeQuestion);
        mInputMessageViewOnTypingListener();

        layoutBtnSendMsg = (LinearLayout) findViewById(R.id.layoutBtnSendMsg);
        btnFire.setOnClickListener(this);
        layoutBtnSendMsg.setOnClickListener(this);
        btnLikeQuestion.setOnClickListener(this);
        btnDislikeQuestion.setOnClickListener(this);
        btnBackFromChat.setOnClickListener(this);


    }

    private void initSocketListeners() {
        mSocket.on("hello", hello);
        mSocket.on("socketError", socketError);
        mSocket.on("typing", typing);
        mSocket.on("stoppedTyping", stoppedTyping);
        mSocket.on("incomingMessage", incomingMessage);
        mSocket.connect();
        mSocket.emit("joinChat", chatId);
    }

    private void mInputMessageViewOnTypingListener() {
        Log.d("typingListener, ", "on");
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //   if (null == mUsername) return;
                //   if (!mSocket.connected()) return;

                String typingInfo = "{'chatId':'57c914fd3288e95b0ab2abb2','senderId':'57b726b26470e2cb10896ac1'}";

                JSONObject typingJson = null;
                try {
                    typingJson = new JSONObject(typingInfo);
                    if (mInputMessageView.length() > 0 && !mTyping) {
                        Log.d("yourTyping", "true");
                        mSocket.emit("typingEvent", typingJson);
                        mTyping = true;
                    }
                    if (mInputMessageView.length() == 0) {
                        stoppedTypingMethod();
                        mTyping = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //   mTypingHandler.removeCallbacks(onTypingTimeout);
                //   mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void addMessage(String username, String message, BubbleType bubbleType) {
        arrayListBubbleChat.add(new BubbleChatObject(bubbleType, message, receiveCurrentTime(), 0));
        bubbleChatAdapter.notifyDataSetChanged();
        scrollToBottom();
        Log.d("username: ", username + " message: " + message);
    }


    private void attemptSend() throws JSONException {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        } else if (mInputMessageView.length() > 0) {
            addMessage("425435243", message, BubbleType.LOCAL_USER);

            stoppedTypingMethod();
        }

        String messageInfo = "{'chatId':'57c914fd3288e95b0ab2abb2','message':'" + mInputMessageView.getText().toString().trim() + "','senderId':'57b726b26470e2cb10896ac1','receiverId':'57bc5bd59d71fb922f3e9b3f'}";
        mInputMessageView.setText("");

        Log.d("chatIdJson", chatIdJson.toString());
        JSONObject messageInfoJson = new JSONObject(messageInfo);
        Log.d("messageInfoJson", messageInfo);

        //  mSocket.emit("new message", message);

        mSocket.emit("sendMessage", messageInfoJson);
    }

    private void stoppedTypingMethod() {
        mTyping = false;
        String typingInfo = "{'chatId':'57c914fd3288e95b0ab2abb2','senderId':'57b726b26470e2cb10896ac1'}";

        JSONObject typingJson = null;
        try {
            typingJson = new JSONObject(typingInfo);
            mSocket.emit("stopTypingEvent", typingJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("socket ", "disconnected");
        mSocket.emit("leaveChat", chatId);

        mSocket.disconnect();
        // mSocket.off("new message", onNewMessage);
    }


    @Override
    public void onStop() {

        Log.d("socket for2", "leave");
        mSocket.emit("leaveChat", chatIdJson);

        mSocket.disconnect();
        saveConversationSharedPreferences(arrayListBubbleChat);
        //   mSocket.off("new message", onNewMessage);
        super.onStop();
    }

    private void initActionBar() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//TODO change status bar lower than 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            //TODO change color status bar
        }
    }

    private void scrollToBottom() {

        listChat.smoothScrollToPosition(bubbleChatAdapter.getCount() - 1);
    }

    private void saveConversationSharedPreferences(ArrayList<BubbleChatObject> arrConversation) {
        if (arrConversation != null) {
            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            String strConversation = JsonParser.parseConversationToJson(arrConversation);
            editor.putString(CONVERSATION_KEY, strConversation);
            Log.d("save Conversation ", strConversation);
            editor.apply();
        }
    }

    private void loadConversationSharedPreferences() {
        Log.d("inside load", " well");

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String restoredConversation = sharedPreferences.getString(CONVERSATION_KEY, "");

        if (restoredConversation.length() > 0) {

            Log.d("restoredConve", restoredConversation + " z");
            arrayListBubbleChat = JsonParser.parseConversationToObject(restoredConversation);


        }

    }

    private String receiveCurrentTime() {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        return simpleDateFormat.format(calender.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutBtnSendMsg:
                try {
                    attemptSend();
                } catch (JSONException e) {
                    Log.e("whylikethis", e.toString());
                }
                break;
            case R.id.btnFire:
                localUserId = Singleton.getInstance().getNewUser().getId();
                Log.d("activeLocal", localUserId + "");
                connectivity.firePressed(chatId, localUserId, questionType);
                btnFire.setBackgroundResource(R.drawable.fire_pressed);
                revealPicCounter = revealPicCounter + 2;
                break;
            case R.id.btnLikeQuestion:
            case R.id.btnDislikeQuestion:
                if (v.getId() == R.id.btnLikeQuestion) {
                    isLike = true;
                } else {
                    isLike = false;
                }
                //  isLike = v.getId() == R.id.btnLikeQuestion;
                connectivity.likeDislikeQuestion(questionId, isLike);
                break;
            case R.id.btnBackFromChat:
                finish();
                onBackPressed();
                break;
        }
    }

    @Override
    public void receiveServerResponse(ConnectivityError error, ResponseObject response) {
        if (error == null) {

            switch (response.getTypeCode()) {
                case ResponseObject.QUESTION_FROM_SERVER:
                    addMessage("noNeed", response.getContent(), BubbleType.QUESTION);
                    break;
                case ResponseObject.CURRENT_REVEAL_STAGE:
                    Log.d("currentReveal", response.getContent());
            }
        }
    }
}
