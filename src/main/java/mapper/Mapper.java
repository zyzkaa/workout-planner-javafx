package mapper;

import com.example.projekt.WeekDay;
import com.example.projekt.controller.WorkoutCreatorController;
import com.example.projekt.model.dto.ExerciseDto;
import com.example.projekt.model.entity.Exercise;
import com.example.projekt.model.entity.ExerciseDetails;
import com.example.projekt.model.entity.Workout;
import com.example.projekt.repository.ExerciseRepository;

import java.util.HashSet;
import java.util.Set;

public class Mapper {

    public static Exercise fromDto (ExerciseDto exerciseDto) {
        Exercise result = new Exercise();
        result.setId(exerciseDto.getExerciseId());
        result.setName(exerciseDto.getName());
        return result;
    }

}
