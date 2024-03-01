import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';
import { CartStore } from './cart.store';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy{

  // NOTE: you are free to modify this component

  private router = inject(Router)
  private store = inject(CartStore)

  itemCount!: number
  sub = new Subscription

  ngOnInit(): void {
    this.sub = this.store.getItemCount.subscribe({
      next: (value) => this.itemCount = value,
      error: (err) => console.log(err)
    })
  }

  ngOnDestroy(): void {
      this.sub.unsubscribe()
  }

  checkout(): void {
    this.router.navigate([ '/checkout' ])
  }
}
