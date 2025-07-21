package com.example.mixueapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mixueapp.DetailProdukActivity
import com.example.mixueapp.R
import com.example.mixueapp.Product
import java.text.NumberFormat
import java.util.*

class ProdukDetailAdapter(
    private var products: List<Product>
) : RecyclerView.Adapter<ProdukDetailAdapter.ProductViewHolder>() {

    fun updateData(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.productName.text = product.nama
        holder.productDescription.text = product.deskripsi

        val formattedPrice = NumberFormat.getNumberInstance(Locale("in", "ID")).format(product.harga)
        holder.productPrice.text = "Rp. $formattedPrice"

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .into(holder.productImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailProdukActivity::class.java).apply {
                putExtra("productId", product.id) // ← kirim ID ke activity
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = products.size
}
