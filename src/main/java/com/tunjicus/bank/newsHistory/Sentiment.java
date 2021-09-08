package com.tunjicus.bank.newsHistory;

import java.math.BigDecimal;

public record Sentiment(SentimentType sentiment, BigDecimal value) {}
