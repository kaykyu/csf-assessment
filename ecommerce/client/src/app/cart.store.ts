
// TODO Task 2

import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Cart, LineItem, StoreSlice } from "./models";

// Use the following class to implement your store
@Injectable()
export class CartStore extends ComponentStore<StoreSlice>{

    constructor() { super(INIT_SLICE) }

    readonly addNewProduct = this.updater<LineItem>(
        (slice: StoreSlice, product: LineItem) => {
            return {
                cart: [...slice.cart, product]
            }
        }
    )

    readonly getItemCount = this.select<number>(
        (slice: StoreSlice) => slice.cart.length
    )

    readonly getCart = this.select<Cart>(
        (slice: StoreSlice) => {
            return {lineItems: slice.cart}
        }
    )

    readonly checkout = this.updater<void>(
        (_slice: StoreSlice, _value: void) => INIT_SLICE
    )
}

const INIT_SLICE = {
    cart: []
}