package goldzweigapps.com.gencycler.java;

//import android.content.Context;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//
//import goldzweigapps.com.annotations.annotations.GencyclerDataContainer;
//import goldzweigapps.com.annotations.annotations.RecyclerAdapter;
//import goldzweigapps.com.gencycler.recyclerAdapters.CustomJavaAdapterName;

import android.content.Context;
import goldzweigapps.com.annotations.annotations.GencyclerAdapter;
import goldzweigapps.com.annotations.annotations.GencyclerDataContainer;
import goldzweigapps.com.gencycler.GeneratedJavaTestAdapter;
import goldzweigapps.com.gencycler.JavaProfileContainerViewHolder;
import goldzweigapps.com.gencycler.Profile3ContainerViewHolder;
import goldzweigapps.com.gencycler.kotlin.Profile3Container;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by gilgoldzweig on 15/03/2018.
 */

@GencyclerAdapter({JavaProfileContainer.class, Profile3Container.class})
public class JavaTestAdapter extends GeneratedJavaTestAdapter {
    public JavaTestAdapter(Context context, ArrayList<GencyclerDataContainer> elements) {
        super(context, elements, true);
    }

    @Override
    public void onBindJavaProfileContainerViewHolder(@NotNull JavaProfileContainerViewHolder javaProfileContainerViewHolder,
                                                     @NotNull JavaProfileContainer javaProfileContainer, int position) {

    }

    @Override
    public void onBindProfile3ContainerViewHolder(@NotNull Profile3ContainerViewHolder profile3ContainerViewHolder,
                                                  @NotNull Profile3Container profile3Container, int position) {

    }
}
