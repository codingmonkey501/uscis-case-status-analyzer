import { Component } from '@angular/core';
import { ChartType } from 'chart.js';
import { MultiDataSet, Label } from 'ng2-charts';
import LineChartJson from '../../assets/line-chart.json';
import { DropDownOption } from '../models/model';
import { CENTER_NAMES } from '../app.component';

@Component({
  selector: 'app-doughnut-chart',
  templateUrl: './doughnut-chart.component.html',
  styleUrls: ['./doughnut-chart.component.css']
})

export class DoughnutChartComponent {

  doughnutChartLabels: Label[] = CENTER_NAMES.map(v => [v + ' Total Received', v+ ' Still Pending', v+' Already Finalized']);

  doughnutChartData: MultiDataSet = [
      CENTER_NAMES.map(c => {
        return LineChartJson.filter(d => c === d.center)[0].formCategories
            .map(f => {
                return f.multiDataSets
                    //.filter(set => set.label === 'Received')
                    .map(set => set.data.reduce((sum, count) => sum + count, 0))
                    .reduce((sum, count) => sum + count, 0);
            }).reduce((sum, count) => sum + count, 0);
      }),

      CENTER_NAMES.map(c => {
        return LineChartJson.filter(d => c === d.center)[0].formCategories
            .map(f => {
                return f.multiDataSets
                    .filter(set => set.label === 'Status in progress' || set.label === 'Status in progress (Finger print)' || set.label === 'Received')
                    .map(set => set.data.reduce((sum, count) => sum + count, 0))
                    .reduce((sum, count) => sum + count, 0);
            }).reduce((sum, count) => sum + count, 0);
      }),
      CENTER_NAMES.map(c => {
          return LineChartJson.filter(d => c === d.center)[0].formCategories
              .map(f => {
                  return f.multiDataSets
                      .filter(set => set.label === 'Cancelled, rejected, withdrawled' || set.label === 'Approved')
                      .map(set => set.data.reduce((sum, count) => sum + count, 0))
                      .reduce((sum, count) => sum + count, 0);
              }).reduce((sum, count) => sum + count, 0);
      }),
  ];
  doughnutChartType: ChartType = 'doughnut';

}