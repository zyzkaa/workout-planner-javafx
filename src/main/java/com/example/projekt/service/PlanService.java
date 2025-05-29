package com.example.projekt.service;

import com.example.projekt.model.entity.Plan;
import com.example.projekt.repository.PlanRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PlanService {
    @Getter
    private static PlanService instance = new PlanService();

    public List<Plan> getAll() {
        return PlanRepository.getINSTANCE().findAll();
    }


}
