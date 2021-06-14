package nl.fhict.digitalmarketplace.service.user;

import nl.fhict.digitalmarketplace.model.response.StatisticsItemResponse;
import nl.fhict.digitalmarketplace.model.user.UserWebView;

public interface IViewsService {
    StatisticsItemResponse countAllWebsiteViews();
    StatisticsItemResponse countDailyWebsiteViews();
    UserWebView addWebsiteView();
}
