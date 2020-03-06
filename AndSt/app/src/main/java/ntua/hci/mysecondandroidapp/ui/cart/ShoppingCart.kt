package ntua.hci.mysecondandroidapp.ui.cart

import android.content.Context
import android.widget.Toast
import io.paperdb.Paper
import ntua.hci.mysecondandroidapp.data.model.CartItems

class ShoppingCart {

    companion object {
        fun addItem(cartItem: CartItems) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart.singleOrNull { it.product.itemName == cartItem.product.itemName }
            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {
                targetItem.quantity++
            }
            ShoppingCart.saveCart(cart)
        }

        fun removeItem(cartItem: CartItems, context: Context) {
            val cart = ShoppingCart.getCart()

            val targetItem = cart.singleOrNull { it.product.itemName == cartItem.product.itemName }
            if (targetItem != null) {
                if (targetItem.quantity > 0) {
                    targetItem.quantity--
                } else {
                    cart.remove(targetItem)
                }
            }

            ShoppingCart.saveCart(cart)
        }

        fun saveCart(cart: MutableList<CartItems>) {
            Paper.book().write("cart", cart)
        }

        fun getCart(): MutableList<CartItems> {
            return Paper.book().read("cart", mutableListOf())
        }

        fun getShoppingCartSize(): Int {
            var cartSize = 0
            ShoppingCart.getCart().forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
    }

}