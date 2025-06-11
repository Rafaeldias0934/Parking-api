package com.example.demo_park_api.Utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    public static String generateReceipt() {
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0,19);
        return receipt.replace("-","")
                .replace(":", "")
                .replace("T","-");

    }

    public static BigDecimal CalculateCost(LocalDateTime entryDate, LocalDateTime exitDate) {
        double FIRST_15_MINUTES = 5.00;
        double FIRST_60_MINUTES = 9.25;
        double ADD_15_MINUTES = 1.75;

        long minutes = entryDate.until(exitDate, ChronoUnit.MINUTES);

        double total = 0.0;

        if (minutes <= 15) {
            total = 5.00;
        } else if (minutes <= 60) {
            total = 9.25;
        } else {
            long extraMinutes = minutes - 60;
            long additional = (long) Math.ceil(extraMinutes / 15.0);
            total = FIRST_60_MINUTES + ADD_15_MINUTES * additional;
        }
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal CalculateDiscount(BigDecimal cost, long numberOfTimes) {
       BigDecimal PERCENTAGE_DISCOUNT = new BigDecimal("0.30");
        BigDecimal discount;

        if ((numberOfTimes > 0) && (numberOfTimes % 10 == 0)) {
            discount = cost.multiply(PERCENTAGE_DISCOUNT);
        } else  {
            discount = BigDecimal.ZERO;
        }

        return discount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
