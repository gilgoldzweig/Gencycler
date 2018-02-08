package goldzweigapps.com.gencycler.adapters

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import goldzweigapps.com.gencycler.AdType
import goldzweigapps.com.gencycler.ProfileType
import java.io.IOException
import java.util.ArrayList
import kotlin.Int

abstract class GencyclerBrandNewAdpapter(val context: Context, var elementList: ArrayList<GencyclerDataType> = ArrayList()) : GencyclerAdapterExtensions(elementList) {
   private val inflater: LayoutInflater = LayoutInflater.from(context)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when(viewType) {
           2131361853 -> ProfileTypeViewHolder(inflater.inflate(viewType, parent, false))
           2131361855 -> AdTypeViewHolder(inflater.inflate(viewType, parent, false))
           else -> throw IOException("unsupported type, only (ProfileType, AdType) are supported")
   }
   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      when (holder) {
      is ProfileTypeViewHolder ->
              holder.onBindProfileTypeViewHolder(position, elementList[position] as ProfileType)

      is AdTypeViewHolder ->
              holder.onBindAdTypeViewHolder(position, elementList[position] as AdType)

      }
   }

   override fun getItemCount(): Int = elements.size

   override fun getItemViewType(position: Int): Int = when(elementList[position]) {

           is ProfileType -> 2131361853
           is AdType -> 2131361855
           else -> -1
   }
   abstract fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType)

   abstract fun AdTypeViewHolder.onBindAdTypeViewHolder(position: Int, element: AdType)

   class ProfileTypeViewHolder(view: View) : ViewHolder(view) {
      val name: AppCompatTextView = view.findViewById(2131230887)

      val age: AppCompatTextView = view.findViewById(2131230889)

      val profileImage: AppCompatImageView = view.findViewById(2131230888)
   }

   class AdTypeViewHolder(view: View) : ViewHolder(view) {
      val adTitle: AppCompatTextView = view.findViewById(2131230893)

      val adPreview: AppCompatImageView = view.findViewById(2131230894)
   }
}
