package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.model.response.StatisticsItemResponse;
import nl.fhict.digitalmarketplace.model.user.UserWebView;
import nl.fhict.digitalmarketplace.repository.user.UserWebViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class ViewsService implements IViewsService {
    private Logger logger = LoggerFactory.getLogger(ViewsService.class);
    private UserWebViewRepository userWebViewRepository;

    public ViewsService(UserWebViewRepository userWebViewRepository) {
        this.userWebViewRepository = userWebViewRepository;
    }

    @Override
    public StatisticsItemResponse countAllWebsiteViews() {
        return new StatisticsItemResponse("allViews", userWebViewRepository.count());
    }

    @Override
    public StatisticsItemResponse countDailyWebsiteViews() {
        LocalDateTime todayStart = LocalDate.now().atTime(0, 1);
        LocalDateTime todayEnd = LocalDate.now().atTime(23,59);
        LocalDateTime utcTimeTodayStart = todayStart.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime utcTimeTodayEnd = todayEnd.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        StatisticsItemResponse response = new StatisticsItemResponse("dailyViews", userWebViewRepository.countAllByCreationDateBetween(utcTimeTodayStart, utcTimeTodayEnd));
        return response;
    }

    @Override
    public UserWebView addWebsiteView() {
       return userWebViewRepository.save(new UserWebView());
    }
}
