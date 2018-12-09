package goldzweigapps.com.gencycler.java;

import goldzweigapps.com.annotations.annotations.GencyclerModel;
import goldzweigapps.com.annotations.annotations.GencyclerViewHolder;
import goldzweigapps.com.annotations.annotations.NamingCase;
import goldzweigapps.com.gencycler.R;

/**
 * Created by gilgoldzweig on 15/03/2018.
 */
@GencyclerViewHolder(value = R.layout.java_profile_type)
public class SimpleJavaModel implements GencyclerModel {
    private String name;
    private String description;

    public SimpleJavaModel(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
