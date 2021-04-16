import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductPlatformManageComponent } from './product-platform-manage.component';

describe('ProductPlatformManageComponent', () => {
  let component: ProductPlatformManageComponent;
  let fixture: ComponentFixture<ProductPlatformManageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductPlatformManageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductPlatformManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
