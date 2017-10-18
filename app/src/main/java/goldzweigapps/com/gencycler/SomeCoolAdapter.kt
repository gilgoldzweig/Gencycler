package goldzweigapps.com.gencycler

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.annotations.annotations.GencyclerViewHolder
import goldzweigapps.com.annotations.annotations.GencyclerViewField
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import goldzweigapps.com.gencycler.adapters.GencyclerBrandNewAdpapter


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

@GencyclerAdapter(
        GencyclerViewHolder(R.layout.type_one,
                GencyclerViewField("name", R.id.type_one_one_text, AppCompatTextView::class),
                GencyclerViewField("age", R.id.type_one_two_text, AppCompatTextView::class),
                GencyclerViewField("profileImage", R.id.type_one_three_text, AppCompatImageView::class),
                classType = ProfileType::class),

        GencyclerViewHolder(R.layout.type_two,
                GencyclerViewField("adTitle", R.id.type_two_one_text, AppCompatTextView::class),
                GencyclerViewField("adPreview", R.id.type_two_three_text, AppCompatImageView::class),
                classType = AdType::class))

class BrandNewAdpapter(context: Context, elements: ArrayList<GencyclerDataType>):
        GencyclerBrandNewAdpapter(context, elements) {

    override fun ProfileTypeViewHolder
            .onBindProfileTypeViewHolder(position: Int, element: ProfileType) {

        name.text = element.name

        onClick {
        }
        onLongClick {

        }
    }

    override fun AdTypeViewHolder
            .onBindAdTypeViewHolder(position: Int, element: AdType) {

    }
class ss(context: Context, elements: ArrayList<GencyclerDataType>) {
    val adapter = BrandNewAdpapter(context, elements)
    fun main() {
        adapter + TypeTwo()

    }
}
}