//package com.example.projekt.service;
//
//import com.example.projekt.WeekDay;
//import com.example.projekt.controller.WorkoutCreatorController;
//import com.example.projekt.model.entity.Exercise;
//import com.example.projekt.model.entity.ExerciseDetails;
//import com.example.projekt.model.entity.Plan;
//import com.example.projekt.model.entity.Workout;
//import com.example.projekt.repository.ExerciseRepository;
//import com.example.projekt.repository.PlanRepository;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import mapper.Mapper;
//
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//@NoArgsConstructor
//public class WorkoutService {
//    @Getter
//    private final static WorkoutService instance = new WorkoutService();
//
//    private final ExerciseRepository exerciseRepository = ExerciseRepository.getINSTANCE();
//    private final PlanRepository planRepository = PlanRepository.getINSTANCE();
//
//    public void savePlan(Map<WeekDay, WorkoutCreatorController> workoutMap, String title){
//        Plan plan = new Plan();
//        plan.setTitle(title);
//        plan.setAdded(new Date());
//
//        Set<Workout> workoutList = new HashSet<>();
//        workoutMap.forEach((weekDay, workoutCreator) -> {
//            Workout workout = new Workout();
//            workout.setDay(weekDay);
//            workout.setPlan(plan);
//
//            Set<ExerciseDetails> exerciseList = new HashSet<>();
//            workoutCreator.getExercisesWithData().forEach(exercise -> {
//                ExerciseDetails exerciseDetails = new ExerciseDetails();
//                exerciseDetails.setRepetitions(exercise.getReps());
//                exerciseDetails.setSets(exercise.getSets());
//                exerciseDetails.setWorkout(workout);
//
//                Exercise exerciseFetched = exerciseRepository.findById(exercise.getExercise().getExerciseId());
//                if(exerciseFetched == null) {
//                    exerciseFetched = Mapper.fromDto(exercise.getExercise());
//                    exerciseRepository.add(exerciseFetched);
//                }
//                exerciseDetails.setExercise(exerciseFetched);
//                exerciseList.add(exerciseDetails);
//            });
//
//            workout.setExercises(exerciseList);
//            workoutList.add(workout);
//        });
//
//        plan.setWorkouts(workoutList);
//        planRepository.add(plan);
//    }
//}
