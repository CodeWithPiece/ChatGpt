package com.app.chatgpt.api;


import com.app.chatgpt.model.AnswerResponse;
import com.app.chatgpt.model.QuestionRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServiceApi {

    @POST("chat/completions")
    Call<AnswerResponse> askQuestion(@Body QuestionRequest questionRequest
            , @Header("Authorization") String authHeader);

}



