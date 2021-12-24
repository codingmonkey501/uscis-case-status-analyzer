import { Component } from '@angular/core';
import LineChartJson from '../assets/line-chart.json';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
}

export const FORM_NAMES = LineChartJson.filter(l => l.center === 'SRC')[0]
                .formCategories.map(f => f.formName).sort((v1, v2) => v1 > v2 ? -1 : 1);


export const CENTER_NAMES = ['MSC', 'SRC', 'EAC', 'LIN', 'WAC'];