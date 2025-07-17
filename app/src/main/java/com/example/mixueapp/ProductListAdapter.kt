package com.example.mixueapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductListAdapter(
    private val productList: List<Product>,
    private val onDetailClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.image_view)
        val tvProductName: TextView = itemView.findViewById(R.id.nama_produk)
        val tvProductDesc: TextView = itemView.findViewById(R.id.deskripsi_produk)
        val btnDetail: Button = itemView.findViewById(R.id.detail_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produk_list, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.ivProductImage.setImageResource(product.imageResId)
        holder.tvProductName.text = product.name
        holder.tvProductDesc.text = product.description

        holder.btnDetail.setOnClickListener {
            onDetailClick(product)
        }
    }

    override fun getItemCount(): Int = productList.size
}
