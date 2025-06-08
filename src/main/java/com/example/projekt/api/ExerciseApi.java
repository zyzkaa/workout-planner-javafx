package com.example.projekt.api;

import com.example.projekt.model.dto.BodyPartsDto;
import com.example.projekt.model.dto.ExercisePage;
import com.example.projekt.model.dto.WorkoutResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ExerciseApi {
    @GET("exercises")
    Call<WorkoutResponse<ExercisePage>> getExercises(@Query("limit") int limit);

    @GET("bodyparts/{name}/exercises")
    Call<WorkoutResponse<ExercisePage>> getExercisesByBodyPart(@Path("name") String name, @Query("limit") int limit, @Query("offset") int offset);

    @GET("bodyparts")
    Call<WorkoutResponse<List<BodyPartsDto>>> getBodyParts();

    @GET("exercises")
    Call<WorkoutResponse<ExercisePage>> getExercisesBySearch(@Query("search") String search, @Query("limit") int limit, @Query("offset") int offset);
}
