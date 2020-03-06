package ntua.hci.mysecondandroidapp.ui.product


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.product_row_item.view.*
import ntua.hci.mysecondandroidapp.R
import ntua.hci.mysecondandroidapp.data.model.CartItems
import ntua.hci.mysecondandroidapp.data.model.ProductItems
import ntua.hci.mysecondandroidapp.ui.cart.ShoppingCart
import ntua.hci.mysecondandroidapp.ui.main.MainActivity


class ProductAdapter(var context: Context, var products: List<ProductItems> = arrayListOf()) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProductAdapter.ViewHolder {
        // The layout design used for each list item
        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, null)
        return ViewHolder(view)

    }

    // This returns the size of the list.
    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(viewHolder: ProductAdapter.ViewHolder, position: Int) {
        //we simply call the `bindProduct` function here
        viewHolder.bindProduct(products[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // This displays the product information for each item
        fun bindProduct(product: ProductItems) {
            itemView.product_name.text = product.itemName
            itemView.store_name.text = product.storename
            itemView.product_price.text = "$${product.price.toString()}"
            itemView.addToCart.setOnClickListener { view ->

                val item = CartItems(product)

                ShoppingCart.addItem(item)

                Toast.makeText(
                    (itemView.context as MainActivity),
                    "${product.itemName} added to your cart",
                    Toast.LENGTH_LONG
                ).show()

            }

            itemView.removeItem.setOnClickListener { view ->

                val item = CartItems(product)

                ShoppingCart.removeItem(item, itemView.context)


                Toast.makeText(
                    (itemView.context as MainActivity),
                    "${product.itemName} removed from your cart",
                    Toast.LENGTH_LONG
                ).show()

            }

            }

        }

    }