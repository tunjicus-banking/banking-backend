package com.tunjicus.bank.newsHistory;

import com.tunjicus.bank.scheduled.TimeService;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class GetNewsHistoryDto {
     private final String title;
     private final SentimentType sentiment;
     private final BigDecimal modifier;
     private final Date createdAt;

     public GetNewsHistoryDto(NewsHistory newsHistory) {
         title = newsHistory.getTitle();
         sentiment = newsHistory.getSentiment();
         modifier = newsHistory.getModifier();
         createdAt = TimeService.calculateSimulatedDate(newsHistory.getCreatedAt());
     }
}
