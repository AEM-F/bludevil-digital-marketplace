import { TestBed } from '@angular/core/testing';

import { TokenLocalStorageService } from './token-local-storage.service';

describe('TokenLocalStorageService', () => {
  let service: TokenLocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenLocalStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
