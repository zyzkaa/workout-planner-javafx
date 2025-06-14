package com.example.projekt.service;

import com.example.projekt.api.ExerciseApi;
import com.example.projekt.api.ExerciseApiService;
import com.example.projekt.model.dto.BodyPartsDto;
import com.example.projekt.model.dto.ExercisePage;
import com.example.projekt.model.dto.WorkoutResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ExercisesService {
    @Getter
    private static ExercisesService INSTANCE = new ExercisesService();

    private final ExerciseApi exerciseApi = ExerciseApiService.getInstance().getExerciseApi();

    public ExercisePage getExercisePageResponse(Response<WorkoutResponse<ExercisePage>> response){
        if(!response.isSuccessful()) return null;

        WorkoutResponse<ExercisePage> body = response.body();

        if (body == null) return null;
        return body.getData();
    }

    public ExercisePage fetchExercisesBySearch(String search, int limit, int offset){
        try {
            Response<WorkoutResponse<ExercisePage>> response = exerciseApi
                    .getExercisesBySearch(search, limit, offset * limit).execute();

            return  getExercisePageResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ExercisePage fetchExercisesByBodyPart(String bodyPart, int limit, int offset){
        try {
            Response<WorkoutResponse<ExercisePage>> response = exerciseApi
                    .getExercisesByBodyPart(bodyPart, limit, offset * limit).execute();

            return  getExercisePageResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BodyPartsDto> fetchBodyParts() {
        List<BodyPartsDto> bodyParts = new ArrayList<>();

        try {
            Response<WorkoutResponse<List<BodyPartsDto>>> response = exerciseApi
                    .getBodyParts().execute();

            if(!response.isSuccessful()) return bodyParts;
            WorkoutResponse<List<BodyPartsDto>> body = response.body();
            if (body == null) return bodyParts;

            bodyParts = body.getData()
                    .stream()
                    .filter(bp -> !bp.getName().equals("cardio"))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bodyParts;
    }
}
