package com.example.projekt.service;

import com.example.projekt.WeekDay;
import com.example.projekt.controller.WorkoutCreatorController;
import com.example.projekt.model.entity.Exercise;
import com.example.projekt.model.entity.ExerciseDetails;
import com.example.projekt.model.entity.Plan;
import com.example.projekt.model.entity.Workout;
import com.example.projekt.repository.ExerciseRepository;
import com.example.projekt.repository.PlanRepository;
import com.example.projekt.util.AppConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.*;

@NoArgsConstructor
public class PlanService {
    @Getter
    private final static PlanService instance = new PlanService();

    private static final ExerciseRepository exerciseRepository = ExerciseRepository.getINSTANCE();
    private static final PlanRepository planRepository = PlanRepository.getINSTANCE();

    public List<Plan> getAll() {
        return planRepository.findAll();
    }

    public Plan getById(int id) {
        return planRepository.findById(id);
    }

    public void deleteById(int id) {
        planRepository.deleteById(id);
    }

    public void savePlan(Map<WeekDay, WorkoutCreatorController> workoutMap, String title){
        Plan plan = new Plan();
        plan.setTitle(title);
        plan.setAdded(new Date());

        Set<Workout> workoutList = new HashSet<>();
        workoutMap.forEach((weekDay, workoutCreator) -> {
            Workout workout = new Workout();
            workout.setDay(weekDay);
            workout.setPlan(plan);

            Set<ExerciseDetails> exerciseList = new HashSet<>();
            workoutCreator.getExercisesWithData().forEach(exercise -> {
                ExerciseDetails exerciseDetails = new ExerciseDetails();
                exerciseDetails.setRepetitions(exercise.getReps());
                exerciseDetails.setSets(exercise.getSets());
                exerciseDetails.setWorkout(workout);

                Exercise exerciseFetched = exerciseRepository.findById(exercise.getExercise().getExerciseId());
                if(exerciseFetched == null) {
                    exerciseFetched = new Exercise();
                    exerciseFetched.setId(exercise.getExercise().getExerciseId());
                    exerciseFetched.setName(exercise.getExercise().getName());
                    exerciseRepository.add(exerciseFetched);
                }
                exerciseDetails.setExercise(exerciseFetched);
                exerciseList.add(exerciseDetails);
            });

            workout.setExercises(exerciseList);
            workoutList.add(workout);
        });

        plan.setWorkouts(workoutList);
        planRepository.add(plan);
    }

    public static void savePdf(int planId) {
        Plan plan = planRepository.findById(planId);
        if (plan == null) return;

        String title = plan.getTitle();
        String filePath = AppConfig.getProperty("pdf.path") + title + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDFont titleFont = PDType1Font.COURIER;
            PDFont dayFont = PDType1Font.COURIER_BOLD;
            PDFont exerciseFont = PDType1Font.COURIER;

            int titleFontSize = 20;
            int dayFontSize = 17;
            int exerciseFontSize = 13;

            float margin = 50;
            float leading = 20;

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            float y = page.getMediaBox().getHeight() - margin;

            content.beginText();
            content.setFont(titleFont, titleFontSize);

            float textWidth = titleFont.getStringWidth(title) / 1000 * titleFontSize;
            float pageWidth = page.getMediaBox().getWidth();
            float startX = (pageWidth - textWidth) / 2;
            content.newLineAtOffset(startX, y);
            content.showText(title);
            y -= leading;
            content.newLineAtOffset(margin - startX, -leading);

            for (Workout workout : plan.getWorkouts().stream()
                    .sorted(Comparator.comparing(w -> w.getDay().getNumber())).toList()) {
                if (y < margin + leading * 2) {
                    content.endText();
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    content.beginText();
                    content.setFont(titleFont, titleFontSize);
                    y = page.getMediaBox().getHeight() - margin;
                    content.newLineAtOffset(margin, y);
                }

                content.setFont(dayFont, dayFontSize);
                content.showText(workout.getDay().getDisplayName());
                y -= leading;
                content.newLineAtOffset(0, -leading);

                for (ExerciseDetails exercise : workout.getExercises()) {
                    if (y < margin + leading) {
                        content.endText();
                        content.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        content.beginText();
                        y = page.getMediaBox().getHeight() - margin;
                        content.newLineAtOffset(margin, y);
                    }

                    content.setFont(exerciseFont, exerciseFontSize);
                    String line = exercise.getExercise().getName() + ": " +
                            exercise.getSets() + " x " + exercise.getRepetitions();
                    content.showText(line);
                    y -= leading * 2;
                    content.newLineAtOffset(0, -leading * 2);
                }

            }

            content.endText();
            content.close();

            File file = new File(filePath);
            file.getParentFile().mkdirs();
            document.save(file);
            System.out.println("PDF saved at: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
