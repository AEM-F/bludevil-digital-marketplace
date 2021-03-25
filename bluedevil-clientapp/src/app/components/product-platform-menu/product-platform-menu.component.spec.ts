import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductPlatformMenuComponent } from './product-platform-menu.component';

describe('ProductPlatformMenuComponent', () => {
  let component: ProductPlatformMenuComponent;
  let fixture: ComponentFixture<ProductPlatformMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductPlatformMenuComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductPlatformMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
