import { inject } from "@angular/core";
import { CanActivateFn } from "@angular/router";
import { CartStore } from "./cart.store";

export const canCheckout: CanActivateFn = 
    (_route, _state) => {
        const store = inject(CartStore)
        var itemCount: number = 0
        store.getItemCount.subscribe({
            next: (value) => itemCount = value
        })
        if (itemCount > 0)
            return true
        alert('Your cart is empty.')
        return false
    }
