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

import java.util.*;

@NoArgsConstructor
public class PlanService {
    @Getter
    private static PlanService instance = new PlanService();

    ExerciseRepository exerciseRepositoryInstance = ExerciseRepository.getINSTANCE();

    public List<Plan> getAll() {
        return PlanRepository.getINSTANCE().findAll();
    }

    public Plan getById(int id) {
        return PlanRepository.getINSTANCE().findById(id);
    }

    public void deleteById(int id) {
        PlanRepository.getINSTANCE().deleteById(id);
    }

    public void addPlan(Map<WeekDay, WorkoutCreatorController> workoutMap, String title) {
        Set<Workout> workoutList = new HashSet<>();
        Plan plan = new Plan();
        workoutMap.forEach((weekDay, workoutCreator) -> {
            Set<ExerciseDetails> exerciseList = new HashSet<>();
            Workout workout = new Workout();
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
                exerciseDetails.setWorkout(workout);
                exerciseList.add(exerciseDetails);
            });
            workout.setPlan(plan);
            workout.setExercises(exerciseList);
            workout.setDay(weekDay);
            workoutList.add(workout);
        });

        plan.setTitle(title);
        plan.setWorkouts(workoutList);
        plan.setAdded(new Date());
        PlanRepository.getINSTANCE().add(plan);
        System.out.println("DODANO PLAN NOWY YIPEEEEE");
    }
}
