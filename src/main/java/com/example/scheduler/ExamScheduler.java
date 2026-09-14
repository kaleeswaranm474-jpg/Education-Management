package com.example.scheduler;

import com.example.entity.Exam;
import com.example.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamScheduler {

    private final ExamRepository examRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkUpcomingExams() {

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<Exam> exams = examRepository
                .findAll()
                .stream()
                .filter(exam ->
                        !exam.getExamDate().isBefore(today)
                                && !exam.getExamDate().isAfter(nextWeek))
                .toList();

        System.out.println();
        System.out.println("========== UPCOMING EXAM SCHEDULER ==========");

        if (exams.isEmpty()) {

            System.out.println(
                    "No upcoming exams in the next 7 days."
            );

        } else {

            for (Exam exam : exams) {

                System.out.println(
                        "Upcoming Exam: "
                                + exam.getExamName()
                                + " | Date: "
                                + exam.getExamDate()
                );
            }
        }

        System.out.println(
                "=============================================="
        );
    }
}