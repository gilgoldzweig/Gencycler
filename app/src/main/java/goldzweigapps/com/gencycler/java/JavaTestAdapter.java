package goldzweigapps.com.gencycler.java;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import goldzweigapps.com.annotations.annotations.GencyclerDataType;
import goldzweigapps.com.annotations.annotations.RecyclerAdapter;
import goldzweigapps.com.gencycler.recyclerAdapters.CustomJavaAdapterName;

/**
 * Created by gilgoldzweig on 15/03/2018.
 */

@RecyclerAdapter(customName = "CustomJavaAdapterName")
public class JavaTestAdapter extends CustomJavaAdapterName {
    public JavaTestAdapter(Context context, ArrayList<GencyclerDataType> elements) {
        super(context, elements);
    }

    @Override
    public void onBindJavaProfileTypeViewHolder(@NotNull JavaProfileTypeViewHolder holder,
                                                int position,
                                                @NotNull JavaProfileType element) {

    }
}
