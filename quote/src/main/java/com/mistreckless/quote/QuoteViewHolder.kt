package com.mistreckless.quote

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.mistreckless.ui_core.GlideRequests
import kotlinx.android.synthetic.main.item_quote.view.*
import kotlinx.coroutines.*

class QuoteViewHolder(
    parent: ViewGroup,
    private val glideRequests: GlideRequests
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_quote, parent, false)
) {

    private var job: Job = Job()

    fun bind(model: QuoteModel, payloads: List<Any> = emptyList()) {
        with(itemView) {
            tvTicker.text = model.ticker
            tvNameDetails.text = context.getString(
                R.string.quote_name,
                model.latestTradeExchangeName,
                model.paperName
            )
            tvChangeDetails.text = context.getString(
                R.string.quote_change_value,
                model.latestTradePrice,
                model.latestTradePoints
            )
            tvChange.text =
                context.getString(
                    model.percentSessionChangeType.textScheme,
                    model.percentSessionChange
                )


            val target: Target<Bitmap> = object : CustomViewTarget<ImageView, Bitmap>(imgLogo) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    logoContainer?.visibility = View.GONE
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val emptyBitmap = Bitmap.createBitmap(
                        resource.width,
                        resource.height,
                        resource.config
                    )
                    if (resource.sameAs(emptyBitmap)) {
                        logoContainer?.visibility = View.GONE
                    } else {
                        logoContainer?.visibility = View.VISIBLE
                        imgLogo?.setImageBitmap(resource)
                    }
                }

            }
            glideRequests
                .asBitmap()
                .load(model.logoUrl)
                .into(target)

            if (payloads.isEmpty()) {
                tvChange.setTextColor(
                    ContextCompat.getColor(
                        context,
                        model.percentSessionChangeType.textColor
                    )
                )
                percentChangeLayout.background = ContextCompat.getDrawable(context, R.color.white)
            } else {
                when (model.percentChangeType) {
                    PercentChangeType.POSITIVE -> {
                        job.cancel()
                        job = GlobalScope.launch(Dispatchers.Main) {
                            tvChange.setTextColor(ContextCompat.getColor(context, R.color.white))
                            percentChangeLayout.background = ContextCompat.getDrawable(
                                context,
                                R.drawable.quote_change_positive_background
                            )

                            delay(500)

                            percentChangeLayout?.background =
                                ContextCompat.getDrawable(context, R.color.white)
                            tvChange?.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    model.percentSessionChangeType.textColor
                                )
                            )
                        }
                    }
                    PercentChangeType.NEGATIVE -> {
                        job.cancel()
                        job = GlobalScope.launch(Dispatchers.Main) {
                            tvChange.setTextColor(ContextCompat.getColor(context, R.color.white))
                            percentChangeLayout.background = ContextCompat.getDrawable(
                                context,
                                R.drawable.quote_change_negative_background
                            )

                            delay(500)

                            percentChangeLayout?.background =
                                ContextCompat.getDrawable(context, R.color.white)
                            tvChange?.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    model.percentSessionChangeType.textColor
                                )
                            )
                        }
                    }
                    PercentChangeType.NONE -> {
                        tvChange.setTextColor(
                            ContextCompat.getColor(
                                context,
                                model.percentSessionChangeType.textColor
                            )
                        )
                        percentChangeLayout.background =
                            ContextCompat.getDrawable(context, R.color.white)
                    }
                }
            }
        }
    }

    fun recycle() {
        job.cancel()
    }


}