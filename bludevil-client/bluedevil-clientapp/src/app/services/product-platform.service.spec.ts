import { TestBed } from '@angular/core/testing';

import { ProductPlatformService } from './product-platform.service';

describe('ProductPlatformService', () => {
  let service: ProductPlatformService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductPlatformService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
