package com.aqwas.androidtest.api;

import com.aqwas.androidtest.model.AddTaskModel;
import com.aqwas.androidtest.model.NotesModel;
import com.aqwas.androidtest.model.TasksModel;
import com.aqwas.androidtest.model.UserModel;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroService {

    @FormUrlEncoded
    @POST("auth/local")
    Single<UserModel> signIn(@Field("identifier") String email,
                             @Field("password") String password);


    @FormUrlEncoded
    @POST("auth/local/register")
    Single<UserModel> signUp(@Field("username") String username,
                             @Field("email") String email,
                             @Field("password") String password);

    @GET("notes")
    Single<ArrayList<NotesModel>> getNotes();

    @GET("notes")
    Single<ArrayList<NotesModel>> searchNotes(@Query("_contains") String text);

    @DELETE("notes/{Id}")
    Single<NotesModel> deleteNotes(@Path("Id") String text);

    @DELETE("tasks/{Id}")
    Single<TasksModel> deleteTasks(@Path("Id") String text);

    @GET("tasks")
    Single<ArrayList<TasksModel>> getTasks();

    @GET("tasks")
    Single<ArrayList<TasksModel>> searchTasks(@Query("_contains") String text);


    @FormUrlEncoded
    @POST("notes")
    Single<NotesModel> addNote(@Field("Title") String Title,
                               @Field("body") String body);

    @FormUrlEncoded
    @PUT("notes/{Id}")
    Single<NotesModel> editNote(@Path("Id") String text,
                                @Field("Title") String Title,
                                @Field("body") String body);

    @POST("tasks")
    Single<TasksModel> addTask(@Body AddTaskModel addTaskModel);


    @PUT("tasks/{Id}")
    Single<TasksModel> editTask(@Path("Id") String text,
                                @Body AddTaskModel addTaskModel);

    @FormUrlEncoded
    @PUT("tasks/{Id}")
    Single<TasksModel> checkOrUncheckTasks(@Path("Id") String id,
                                           @Field("isDone") Boolean isDone);


}
