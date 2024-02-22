package com.app.chatgpt.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.chatgpt.R;
import com.app.chatgpt.adapter.ChatAdapter;
import com.app.chatgpt.api.ApiClient;
import com.app.chatgpt.api.ServiceApi;
import com.app.chatgpt.model.AnswerResponse;
import com.app.chatgpt.model.Choice;
import com.app.chatgpt.model.Message;
import com.app.chatgpt.model.QuestionRequest;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtQuestion;
    MaterialCardView btnSend;
    RecyclerView chatRecycler;
    LinearLayout descLayout;
    List<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;
    private static final String AUTH = "Bearer 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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtQuestion = findViewById(R.id.edtQuestion);
        btnSend = findViewById(R.id.btnSend);
        descLayout = findViewById(R.id.descLayout);
        chatRecycler = findViewById(R.id.chatRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        chatRecycler.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(MainActivity.this, messageList);
        chatRecycler.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtQuestion.length() != 0) {
                    descLayout.setVisibility(View.GONE);
                    addToChat("system", edtQuestion.getText().toString().trim());
                    askQuestion(edtQuestion.getText().toString().trim());
                    edtQuestion.setText("");
                }
            }
        });

    }

    public void askQuestion(String question) {
        messageList.add(new Message("assistant", "Typing..."));
        Log.e("MESSAGE", question);
        Message messages = new Message();
        messages.setRole("system");
        messages.setContent(question);
        List<Message> messageArrayList = new ArrayList<>();
        messageArrayList.add(messages);
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setModel("gpt-3.5-turbo");
        questionRequest.setMessages(messageArrayList);
        ServiceApi api = ApiClient.getClient().create(ServiceApi.class);
        Call<AnswerResponse> call = api.askQuestion(questionRequest, AUTH);
        call.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Message message = response.body().getChoices().get(0).getMessage();
                    addResponseChat(message);
                } else {
                    addResponseChat(new Message("assistant", "Oops...!!\nLooks like you have provided wrong API key.\nplease contact your developer..."));
                }
            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addToChat(String role, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(role, message));
                chatAdapter.notifyDataSetChanged();
                chatRecycler.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });
    }

    public void addResponseChat(Message message) {
        messageList.remove(messageList.size() - 1);
        addToChat(message.getRole(), message.getContent());
    }

}