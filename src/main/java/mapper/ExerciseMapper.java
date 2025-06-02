package mapper;

import com.example.projekt.model.dto.ExerciseDto;
import com.example.projekt.model.entity.Exercise;
import com.example.projekt.model.entity.ExerciseDetails;

public class ExerciseMapper {

    public static Exercise fromDto (ExerciseDto exerciseDto) {
        Exercise result = new Exercise();
        result.setId(exerciseDto.getExerciseId());
        result.setName(exerciseDto.getName());
        return result;
    }

}
