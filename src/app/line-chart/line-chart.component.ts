import { Component, } from '@angular/core';
import { ChartDataSets, ChartType, ChartOptions } from 'chart.js';
import { Color, Label } from 'ng2-charts';
import LineChartJson from '../../assets/line-chart.json';
import {FormControl} from '@angular/forms';
import { FORM_NAMES, CENTER_NAMES } from '../app.component'
import { DropDownOption } from '../models/model';

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent {

  formNames: DropDownOption[] = FORM_NAMES.map(v => { return { value : v, viewValue : v};});

  processingCenterNames: DropDownOption[] = CENTER_NAMES.map(v => { return {value: v, viewValue: v};});

  selectedFormName = 'I-485';
  selectedProcessingCenter = 'SRC';

  lineChartData: ChartDataSets[] = LineChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories.filter(f => f.formName == this.selectedFormName)[0].multiDataSets;

  lineChartLabels: Label[] = LineChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories.filter(f => f.formName == this.selectedFormName)[0].labels;

  lineChartOptions = {
    responsive: true,
  };

  lineChartColors: Color[] = [
    {
      borderColor: 'black',
      backgroundColor: 'rgba(255,255,0,0.28)',
    },
  ];

  lineChartLegend = true;
  lineChartPlugins = [];
  lineChartType: ChartType = 'line';

  selectCenter(event: Event) {
    this.selectedProcessingCenter = (event.target as HTMLSelectElement).value;
      var dataByFormType = LineChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories.filter(f => f.formName == this.selectedFormName);

      if (dataByFormType.length == 0) {
        this.lineChartData = [];
        this.lineChartLabels = [];
      } else {
        this.lineChartData = dataByFormType[0].multiDataSets;
        this.lineChartLabels = dataByFormType[0].labels;
      }
  }

  selectFormName(event: Event) {
    this.selectedFormName = (event.target as HTMLSelectElement).value;
    var dataByFormType = LineChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories.filter(f => f.formName == this.selectedFormName);
    if (dataByFormType.length == 0) {
        this.lineChartData = [];
        this.lineChartLabels = [];
    } else {
        this.lineChartData = dataByFormType[0].multiDataSets;
        this.lineChartLabels = dataByFormType[0].labels;
    }
  }

}
