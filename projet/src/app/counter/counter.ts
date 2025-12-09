import { Component, Input } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-counter',
  templateUrl: './counter.html',
  styleUrls: ['./counter.css']
})
export class CounterComponent {
  // Property 'number'
  @Input() number: number = 0;

  // Increment function
  increment() {
    this.number++;
  }

  // Decrement function
  decrement() {
    this.number--;
  }

  // Reset function
  reset() {
    this.number = 0;
  }
}
