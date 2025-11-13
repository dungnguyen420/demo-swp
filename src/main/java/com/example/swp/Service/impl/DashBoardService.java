package com.example.swp.Service.impl;

import com.example.swp.DTO.DailyRevenueDTO;
import com.example.swp.DTO.RevenueSummaryDTO;
import com.example.swp.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final OrderRepository orderRepository;

    public RevenueSummaryDTO getRevenueSummary() {
        Long total = orderRepository.getTotalRevenue();

        LocalDate today = LocalDate.now();
        Long todayRevenue = orderRepository.getRevenueBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );

        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate firstDayNextMonth = firstDayOfMonth.plusMonths(1);

        Long monthRevenue = orderRepository.getRevenueBetween(
                firstDayOfMonth.atStartOfDay(),
                firstDayNextMonth.atStartOfDay()
        );

        return new RevenueSummaryDTO(
                total.doubleValue(),
                todayRevenue.doubleValue(),
                monthRevenue.doubleValue()
        );
    }

    public List<DailyRevenueDTO> getRevenueLast7Days() {
        List<Object[]> rows = orderRepository.getRevenueLast7DaysRaw();
        List<DailyRevenueDTO> result = new ArrayList<>();

        for (Object[] row : rows) {

            LocalDate day = ((java.sql.Date) row[0]).toLocalDate();
            Long revenue = ((Number) row[1]).longValue();

            // format ngày
            String dayLabel = day.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Double revenueValue = revenue.doubleValue();

            result.add(new DailyRevenueDTO(dayLabel, revenueValue));
        }
        return result;
    }
    public Long getRevenueWithFilters(String filter,
                                      String from,
                                      String to,
                                      String username) {

        LocalDateTime start = null;
        LocalDateTime end   = null;
        LocalDate today = LocalDate.now();


        if (from != null && !from.isBlank()) {
            start = LocalDate.parse(from).atStartOfDay();
        }
        if (to != null && !to.isBlank()) {
            end = LocalDate.parse(to).plusDays(1).atStartOfDay();
        }


        if ((start == null || end == null) && filter != null && !filter.isBlank()) {
            switch (filter) {
                case "today" -> {
                    start = today.atStartOfDay();
                    end   = today.plusDays(1).atStartOfDay();
                }
                case "week" -> {
                    start = today.minusDays(6).atStartOfDay();
                    end   = today.plusDays(1).atStartOfDay();
                }
                case "month" -> {
                    LocalDate first = today.withDayOfMonth(1);
                    start = first.atStartOfDay();
                    end   = first.plusMonths(1).atStartOfDay();
                }
            }
        }

        String userParam = (username != null && !username.isBlank()) ? username.trim() : null;

        return orderRepository.getRevenueAdvanced(start, end, userParam);
    }

}
