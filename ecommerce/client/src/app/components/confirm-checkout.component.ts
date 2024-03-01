import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartStore } from '../cart.store';
import { Observable, tap } from 'rxjs';
import { Cart, LineItem, Order } from '../models';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrl: './confirm-checkout.component.css'
})
export class ConfirmCheckoutComponent implements OnInit {

  // TODO Task 3
  private fb = inject(FormBuilder)
  private store = inject(CartStore)
  private prodSvc = inject(ProductService)
  private router = inject(Router)

  form!: FormGroup
  cart$!: Observable<Cart>
  sum: number = 0
  cart!: Cart

  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      address: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      priority: this.fb.control<boolean>(false),
      comments: this.fb.control<string>('')
    })

    this.cart$ = this.store.getCart.pipe(
      tap(value => {
        this.cart = value
        value.lineItems.forEach(
          value => this.sum += (value.price * value.quantity)
        )
      })
    )
  }

  checkout() {
    const order: Order = {
      name: this.form.value.name,
      address: this.form.value.address,
      priority: this.form.value.priority,
      comments: this.form.value.comments,
      cart: this.cart
    }
    this.prodSvc.checkout(order).subscribe({
      next: (value) => {
        alert(`Order placed:\nOrder ID: ${value.orderId}`)
        this.store.checkout()
        this.router.navigate(['/'])
      },
      error: (err) => alert(`Something went wrong.\nError: ${err.message}`)
    })
  }
}
