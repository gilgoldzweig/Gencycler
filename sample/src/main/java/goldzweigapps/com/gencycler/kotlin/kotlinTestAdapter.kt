package goldzweigapps.com.gencycler.kotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.annotations.annotations.GencyclerDataContainer
import goldzweigapps.com.gencycler.GeneratedKotlinTestAdapter
import goldzweigapps.com.gencycler.Profile2ContainerViewHolder
import goldzweigapps.com.gencycler.ProfileContainerViewHolder

@GencyclerAdapter(ProfileContainer::class)
//@GencyclerAdapter(ProfileContainer::class, Profile2Container::class)
class KotlinTestAdapter(private val context: Context,
//                        elements: ArrayList<GencyclerDataContainer>
                        elements: ArrayList<ProfileContainer>
) : GeneratedKotlinTestAdapter(context, elements) {


    override fun onBindProfileContainerViewHolder(profileContainerViewHolder: ProfileContainerViewHolder,
                                                  profileContainer: ProfileContainer,
                                                  position: Int) {

    }

    override fun onBindProfile2ContainerViewHolder(profile2ContainerViewHolder: Profile2ContainerViewHolder,
                                                   profile2Container: Profile2Container,
                                                   position: Int) {
    }
}


class some {
    lateinit var context: Context
    init {
        val adapter = KotlinTestAdapter(context)

        adapter.add(item)
        adapter + item
    }
}