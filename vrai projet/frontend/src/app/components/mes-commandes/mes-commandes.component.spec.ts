import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MesCommandesComponent } from './mes-commandes.component';

describe('MesCommandesComponent', () => {
  let component: MesCommandesComponent;
  let fixture: ComponentFixture<MesCommandesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MesCommandesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MesCommandesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
