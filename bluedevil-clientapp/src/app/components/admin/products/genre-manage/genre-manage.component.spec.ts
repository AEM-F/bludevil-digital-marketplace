import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenreManageComponent } from './genre-manage.component';

describe('GenreManageComponent', () => {
  let component: GenreManageComponent;
  let fixture: ComponentFixture<GenreManageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenreManageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenreManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
