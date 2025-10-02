import { Component, Inject } from '@angular/core';

import {
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogTitle,
  MatDialogContent,
  MatDialogActions,
  MatDialogClose,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { PestManagement } from '../../../models/PestManagement';
import { CommonModule } from '@angular/common';

import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-update-pest-dialog',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatChipsModule,
    MatDialogClose,
    MatButtonModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    ReactiveFormsModule,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './update-pest-dialog.component.html',
  styleUrl: './update-pest-dialog.component.scss',
})
export class UpdatePestDialogComponent {
  form: FormGroup;

  riceStages = [
    'SEEDLING',
    'TILLERING',
    'STEM_ELONGATION',
    'PANICLE_INITIATION',
    'BOOTING',
    'FLOWERING',
    'MILKING',
    'DOUGH',
    'MATURE',
  ];
  constructor(
    public dialogRef: MatDialogRef<UpdatePestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PestManagement,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      title: [data.title],
      description: [data.description],
      stages: [data.stages || []],
      symptoms: this.fb.array(data.symptoms.map((s) => this.fb.control(s))),
      prevention: this.fb.array(
        data.prevention.map((p) =>
          this.fb.group({
            title: [p.title],
            text: this.fb.array(p.text.map((t) => this.fb.control(t))),
          })
        )
      ),
    });
  }

  // convenience getter
  get prevention(): FormArray {
    return this.form.get('prevention') as FormArray;
  }

  get stages(): FormArray {
    return this.form.get('stages') as FormArray;
  }

  get symptoms(): FormArray {
    return this.form.get('symptoms') as FormArray;
  }
  toggleStage(stage: string): void {
    const current = this.stages.value as string[];
    if (current.includes(stage)) {
      this.stages.setValue(current.filter((s) => s !== stage));
    } else {
      this.stages.setValue([...current, stage]);
    }
  }

  // returns the controls array for prevention[i].text
  getPreventionTextControls(index: number): AbstractControl[] {
    return (
      (this.prevention.at(index).get('text') as FormArray) || { controls: [] }
    ).controls;
  }

  addPreventionText(preventionIndex: number) {
    const arr = this.prevention.at(preventionIndex).get('text') as FormArray;
    arr.push(this.fb.control(''));
  }

  removePreventionText(preventionIndex: number, textIndex: number) {
    const arr = this.prevention.at(preventionIndex).get('text') as FormArray;
    arr.removeAt(textIndex);
  }
  addSymptom() {
    this.symptoms.push(this.fb.control(''));
  }

  removeSymptom(index: number) {
    this.symptoms.removeAt(index);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  save(): void {
    if (this.form.valid) {
      const updated: PestManagement = {
        ...this.data,
        ...this.form.value,
      };
      this.dialogRef.close(updated);
    }
  }
}
