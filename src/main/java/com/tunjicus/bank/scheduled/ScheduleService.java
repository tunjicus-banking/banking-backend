package com.tunjicus.bank.scheduled;

import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.accounts.models.AccountId;
import com.tunjicus.bank.accounts.models.IdAndCompanyId;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.accounts.repositories.SavingsRepository;
import com.tunjicus.bank.employmentHistory.EmploymentHistory;
import com.tunjicus.bank.employmentHistory.EmploymentHistoryRepository;
import com.tunjicus.bank.items.ItemRepository;
import com.tunjicus.bank.newsHistory.NewsHistory;
import com.tunjicus.bank.newsHistory.NewsHistoryRepository;
import com.tunjicus.bank.newsHistory.Sentiment;
import com.tunjicus.bank.newsHistory.SentimentType;
import com.tunjicus.bank.security.ApiKeys;
import com.tunjicus.bank.transactions.Transaction;
import com.tunjicus.bank.transactions.TransactionRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    // Run every "two weeks"
    private static final int salariesSchedule = TimeService.simulatedMinutesPerDay * 14 * 1000;

    // Run every "month"
    private static final int interestSchedule = TimeService.simulatedMinutesPerDay * 30 * 1000;

    // Run daily
    private static final int newsSchedule = 1000 * 60 * 60 * 24;

    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SavingsRepository savingsRepository;
    private final ItemRepository itemRepository;
    private final NewsHistoryRepository newsHistoryRepository;
    private final ApiKeys apiKeys;
    private final OkHttpClient client = new OkHttpClient();
    private final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    // Delay one hour so repeated starts don't trigger event
    @Transactional
    @Scheduled(fixedRate = newsSchedule, initialDelay = 1000 * 60 * 60)
    public void getNewsStory() {
        logger.info("Updating news story");

        final String apiUrl = String.format("https://newsapi.org/v2/top-headlines?country=us&apiKey=%s&pageSize=1", apiKeys.getNewsApi());
        final String jsonString;
        final String title;

        try {
            jsonString = runGet(apiUrl);
        } catch (IOException e) {
            logger.error("Failed to get news article");
            e.printStackTrace();
            return;
        }

        Sentiment sentiment;
        try {
            var jsonObject = new JSONObject(jsonString);
            var articles = jsonObject.getJSONArray("articles");

            if (articles.length() == 0) {
                logger.warn("No articles returned from api");
                return;
            }

            title = articles.getJSONObject(0).get("title").toString();
            if (sameAsLastNewsEntry(title)) {
                logger.warn(String.format("The article '%s' is the most recently saved news article, skipping", title));
                return;
            }

            sentiment = getSentiment(title);
            if (sentiment == null) {
                logger.error("Failed to get sentiment");
                return;
            }

        } catch (JSONException e) {
            logger.error("Failed to parse json response from api");
            e.printStackTrace();
            return;
        }

        if (sentiment.sentiment().equals(SentimentType.NEUTRAL)) {
            logger.info("getNewsStory returned a neutral sentiment, no change");
            return;
        }

        updateRatesAndPrices(sentiment);
        newsHistoryRepository.save(new NewsHistory(title, sentiment));

        var negativeSentiment = sentiment.sentiment().equals(SentimentType.NEG);
        if (negativeSentiment) {
            logger.info(String.format("getNewsStory returned a negative event. Prices increased and interest rates dropped by %s%%", sentiment.value()));
        } else {
            logger.info(String.format("getNewsStory returned a positive event. Prices decreased and interest rates increased by %s%%", sentiment.value()));
        }
    }

    @Transactional
    public void updateRatesAndPrices(Sentiment sentiment) {
        if (sentiment.sentiment().equals(SentimentType.NEUTRAL)) return;
        final var negativeSentiment = sentiment.sentiment().equals(SentimentType.NEG);
        final var positiveRate = sentiment.value().add(BigDecimal.ONE);
        final var negativeRate = BigDecimal.ONE.subtract(sentiment.value());

        final var interestModifier = negativeSentiment ? negativeRate : positiveRate;
        final var pricesModifier = negativeSentiment ? positiveRate : negativeRate;

        savingsRepository.updateInterestRates(interestModifier);
        itemRepository.updatePrices(pricesModifier);
    }

    @Transactional
    @Scheduled(fixedRate = salariesSchedule, initialDelay = salariesSchedule)
    public void paySalaries() {
        logger.info("Running paySalaries");

        var history = employmentHistoryRepository.findAllByEndDateIsNull();
        for (EmploymentHistory employment : history) {
            var userAccountId = getUserAccount(employment.getUserId());
            if (userAccountId == null) {
                logger.warn(
                        String.format(
                                "Failed to find valid account for user %d",
                                employment.getUserId()));
                continue;
            }

            var companyAccount = getCompanyAccount(employment.getPositionId());
            if (companyAccount == null) {
                logger.warn(
                        String.format(
                                "Failed to find valid company account for position %d",
                                employment.getPositionId()));
                continue;
            }

            accountRepository.addMoney(employment.getSalary(), userAccountId.getId());
            var transaction =
                    new Transaction(
                            companyAccount.getCompanyId(),
                            userAccountId.getId(),
                            companyAccount.getId(),
                            AccountType.CHECKING.label,
                            userAccountId.getId(),
                            userAccountId.getType(),
                            employment.getSalary());
            transactionRepository.save(transaction);
        }

        logger.info("paySalaries task finished");
    }

    @Transactional
    @Scheduled(fixedRate = interestSchedule, initialDelay = interestSchedule)
    public void interestPayments() {
        logger.info("Applying interest payments");

        var savingsAccounts = savingsRepository.findAll();
        for (var account : savingsAccounts) {
            var mainAccount = accountRepository.findById(account.getId()).orElse(null);
            if (mainAccount == null) {
                logger.warn(
                        String.format("Failed to find main account with id %d", account.getId()));
                continue;
            }

            if (account.getInterestRate().compareTo(BigDecimal.ZERO) == 0) continue;
            var interest = mainAccount.getFunds().multiply(account.getInterestRate());
            mainAccount.setFunds(mainAccount.getFunds().add(interest));
            accountRepository.save(mainAccount);
        }

        logger.info("interestPayments task finished");
    }

    private IdAndCompanyId getCompanyAccount(int positionId) {
       return accountRepository.findCompanyAccount(positionId).orElse(null);
    }

    private AccountId getUserAccount(int userId) {
        final var accountsToCheck = new String[] {AccountType.CHECKING.label, AccountType.SAVINGS.label};

        for (var accountType : accountsToCheck) {
            var account =
                    accountRepository.findByUserIdAndTypeAndClosedIsFalse(userId, accountType);
            if (account.isPresent()) {
                return new AccountId(account.get().getId(), accountType);
            }
        }

        return null;
    }

    private Sentiment getSentiment(String str) {
        final var url = "http://text-processing.com/api/sentiment/";
        final var requestBody = new FormBody.Builder().add("text", str).build();
        final var request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            final var body = response.body();
            assert body != null;

            final var json = new JSONObject(body.string());
            final var label = json.get("label");
            final var probability = json.getJSONObject("probability");

            return switch (label.toString()) {
                case "neutral" -> new Sentiment(SentimentType.NEUTRAL, BigDecimal.valueOf(Double.parseDouble(probability.get("neutral").toString())));
                case "neg" -> new Sentiment(SentimentType.NEG, getSentimentDifference(probability));
                case "pos" -> new Sentiment(SentimentType.POS, getSentimentDifference(probability));
                default -> throw new IllegalStateException("Unexpected value: " + label);
            };
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BigDecimal getSentimentDifference(JSONObject probability) throws JSONException {
        final var pos = BigDecimal.valueOf(Double.parseDouble(probability.get("pos").toString()));
        final var neg = BigDecimal.valueOf(Double.parseDouble(probability.get("neg").toString()));

        return pos.subtract(neg).abs();
    }

    private String runGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private boolean sameAsLastNewsEntry(String title) {
        var entry = newsHistoryRepository.findTop1ByOrderByCreatedAtDesc();
        if (entry.isEmpty()) return false;
        return entry.get().getTitle().equals(title);
    }
}
