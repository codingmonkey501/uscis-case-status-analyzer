import { Component } from '@angular/core';
import { ChartType, ChartOptions } from 'chart.js';
import { SingleDataSet, Label, monkeyPatchChartJsLegend, monkeyPatchChartJsTooltip } from 'ng2-charts';

import LineChartJson from '../../assets/line-chart.json';
import { DropDownOption } from '../models/model';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.css']
})

export class PieChartComponent {

  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels: Label[] = ['MSC', 'SRC', 'EAC', 'LIN', 'WAC'];
  public pieChartData: SingleDataSet = this.pieChartLabels.map(c => {
    return LineChartJson.filter(d => c === d.center)[0].formCategories
        .map(f => {
            return f.multiDataSets
                .filter(set => set.label === 'Received' || set.label === 'Status in progress' || set.label === 'Status in progress (Finger print)')
                .map(set => set.data.reduce((sum, count) => sum + count, 0))
                .reduce((sum, count) => sum + count, 0);
        }).reduce((sum, count) => sum + count, 0);;
  });
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartPlugins = [];

  constructor() {
    monkeyPatchChartJsTooltip();
    monkeyPatchChartJsLegend();
  }

}