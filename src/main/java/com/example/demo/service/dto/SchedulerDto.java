package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerDto {
    private Long id;
    private TheatreDto theatre;
    private PerformanceDto performance;
    private LocalDate date;
}
