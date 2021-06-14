package nl.fhict.digitalmarketplace.controller.user;

import nl.fhict.digitalmarketplace.service.user.IViewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/views")
public class WebViewsController {
    private IViewsService viewsService;

    public WebViewsController(IViewsService viewsService) {
        this.viewsService = viewsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/countAll")
    public ResponseEntity<Object> getAllViewsCount(){
        return ResponseEntity.ok(viewsService.countAllWebsiteViews());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/countAllDaily")
    public ResponseEntity<Object> getAllViewsDailyCount(){
        return ResponseEntity.ok(viewsService.countDailyWebsiteViews());
    }

    @PostMapping
    public ResponseEntity<Object> addView(){
        return ResponseEntity.ok(viewsService.addWebsiteView());
    }
}
