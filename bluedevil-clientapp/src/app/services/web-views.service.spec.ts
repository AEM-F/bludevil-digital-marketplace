import { TestBed } from '@angular/core/testing';

import { WebViewsService } from './web-views.service';

describe('WebViewsService', () => {
  let service: WebViewsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebViewsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
