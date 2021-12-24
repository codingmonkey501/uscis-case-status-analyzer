import { Component } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label } from 'ng2-charts';
import GroupChartJson from '../../assets/group-chart.json';
import { FORM_NAMES } from '../app.component'
import {FormControl} from '@angular/forms';
import { DropDownOption } from '../models/model';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.css']
})

export class BarChartComponent {

  barChartOptions: ChartOptions = {
    //indexAxis: 'y',
    responsive: true,
    scales: {
        xAxes: [{ stacked: true }],
        yAxes: [{ stacked: true }]
    },
  };

  formNames: DropDownOption[] = FORM_NAMES.map(v => {
       var f : DropDownOption = { value : v, viewValue : v};
       return f;
    });

  processingCenterNames: DropDownOption[] = GroupChartJson.map(centerYear => { return {value: centerYear.center, viewValue: centerYear.center.substring(0,3) + ' Fiscal Year ' + centerYear.center.substring(3)}}).sort((n1, n2) => {
    if (n1 > n2) return 1;
    if (n1 < n2) return -1;
    return 0;
  });

  selectedFormName = 'I-485';
  selectedProcessingCenter = 'SRC21';

  barChartLabels: Label[] = GroupChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories.filter(f => f.formName == this.selectedFormName)[0].labels.map(v => v + 'XXXX');
  barChartData: ChartDataSets[] = GroupChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories.filter(f => f.formName == this.selectedFormName)[0].multiDataSets;


  barChartType: ChartType = 'bar';
  barChartLegend = true;
  barChartPlugins = [];
  
  
    selectCenter(event: Event) {
      this.selectedProcessingCenter = (event.target as HTMLSelectElement).value;
        var dataByFormType = GroupChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories
                  .filter(f => f.formName == this.selectedFormName);
        if (dataByFormType.length == 0) {
          this.barChartData = [];
          this.barChartLabels = [];
        } else {
          this.barChartData = dataByFormType[0].multiDataSets;
          this.barChartLabels = dataByFormType[0].labels.map(v => v + 'XXXX');
        }
    }
  
    selectFormName(event: Event) {
      this.selectedFormName = (event.target as HTMLSelectElement).value;
      var dataByFormType = GroupChartJson.filter(l => l.center == this.selectedProcessingCenter)[0].formCategories
              .filter(f => f.formName == this.selectedFormName);
      if (dataByFormType.length == 0) {
          this.barChartData = [];
          this.barChartLabels = [];
      } else {
          this.barChartData = dataByFormType[0].multiDataSets;
          this.barChartLabels = dataByFormType[0].labels.map(v => v + 'XXXX');
      }
    }
}
