package org.example.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

@Data
public class ExpenseSummaryResponse {
    private BigDecimal totalAmountSpent;
    private BigDecimal totalAmountSpentAverage;
    private int totalExpenses;

    private Map<String, BigDecimal> totalByCategory;
    private Map<YearMonth, BigDecimal> monthlyBreakdown;
}
