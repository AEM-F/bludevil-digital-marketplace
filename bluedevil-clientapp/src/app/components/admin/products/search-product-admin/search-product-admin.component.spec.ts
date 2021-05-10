import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchProductAdminComponent } from './search-product-admin.component';

describe('SearchProductAdminComponent', () => {
  let component: SearchProductAdminComponent;
  let fixture: ComponentFixture<SearchProductAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchProductAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchProductAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
