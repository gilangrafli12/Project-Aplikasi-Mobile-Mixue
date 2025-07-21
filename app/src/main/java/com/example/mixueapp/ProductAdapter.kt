package com.example.mixueapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.text.NumberFormat
import java.util.*

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.product_image)
        val productName: TextView = view.findViewById(R.id.product_name)
        val productDescription: TextView = view.findViewById(R.id.product_description)
        val productPrice: TextView = view.findViewById(R.id.product_price)
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

        val formattedPrice =
            NumberFormat.getNumberInstance(Locale("in", "ID")).format(product.harga)
        holder.productPrice.text = "Rp $formattedPrice"

        // Load image from URL using Glide
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.contoh_produk) // Gambar default sementara loading
//                    .error(R.drawable.error_image)             // Gambar default jika gagal load
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(holder.productImage)
    }

    override fun getItemCount(): Int = products.size
}
