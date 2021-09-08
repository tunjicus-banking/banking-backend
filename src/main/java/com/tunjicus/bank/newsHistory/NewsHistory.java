package com.tunjicus.bank.newsHistory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "news_history")
@Getter
@Setter
@NoArgsConstructor
public class NewsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private SentimentType sentiment;

    @Column
    private BigDecimal modifier;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public NewsHistory(String title, Sentiment sentiment) {
        this.title = title;
        this.sentiment = sentiment.sentiment();
        modifier = sentiment.value();
    }
}
