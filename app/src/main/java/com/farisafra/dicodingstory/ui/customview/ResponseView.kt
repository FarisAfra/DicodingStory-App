package com.farisafra.dicodingstory.ui.customview

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.farisafra.dicodingstory.R

class ResponseView(
    context: Context,
    private val message: Int,
    private val image: Int,
    private val action: (() -> Unit)? = null
): AlertDialog(context) {
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.response_layout)

        val messageView = findViewById<TextView>(R.id.tvResponse)
        messageView.text = context.getString(message)

        val imageView = findViewById<ImageView>(R.id.ivResponse)
        imageView.setImageResource(image)

        val dismissButton = findViewById<Button>(R.id.btnDismiss)
        dismissButton.setOnClickListener {
            dismiss()
            action?.invoke()
        }
    }
}