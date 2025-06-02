package com.example.projekt.service;

import com.example.projekt.WeekDay;
import com.example.projekt.controller.WorkoutCreatorController;
import com.example.projekt.model.entity.Exercise;
import com.example.projekt.model.entity.ExerciseDetails;
import com.example.projekt.model.entity.Plan;
import com.example.projekt.model.entity.Workout;
import com.example.projekt.repository.ExerciseRepository;
import com.example.projekt.repository.PlanRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mapper.ExerciseMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class PlanService {
    @Getter
    private static PlanService instance = new PlanService();

    ExerciseRepository exerciseRepositoryInstance = ExerciseRepository.getINSTANCE();

    public List<Plan> getAll() {
        return PlanRepository.getINSTANCE().findAll();
    }

    public void addPlan(Map<WeekDay, WorkoutCreatorController> workoutMap, String title) {
        List<Workout> workoutList = new ArrayList<>();
        workoutMap.forEach((weekDay, workoutCreator) -> {
            List<ExerciseDetails> exerciseList = new ArrayList<>();
            workoutCreator.getExercisesWithData().forEach(exercise -> {
                Exercise exerciseFetched = exerciseRepositoryInstance.findById(exercise.getExercise().getExerciseId());
                if(exerciseFetched == null) {
                    exerciseFetched = ExerciseMapper.fromDto(exercise.getExercise());
                    exerciseRepositoryInstance.add(exerciseFetched);
                }
                ExerciseDetails exerciseDetails = new ExerciseDetails();
                exerciseDetails.setExercise(exerciseFetched);
                exerciseDetails.setRepetitions(exercise.getReps());
                exerciseDetails.setSets(exercise.getSets());
                exerciseList.add(exerciseDetails);
            });
            Workout workout = new Workout();
            workout.setExercises(exerciseList);
            workout.setDay(weekDay);
            workoutList.add(workout);
        });
        Plan plan = new Plan();
        plan.setTitle(title);
        plan.setWorkouts(workoutList);
        PlanRepository.getINSTANCE().add(plan);
    }
}
