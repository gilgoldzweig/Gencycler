package goldzweigapps.com.gencycler.java;

import goldzweigapps.com.annotations.annotations.GencyclerDataContainer;
import goldzweigapps.com.annotations.annotations.GencyclerViewHolder;
import goldzweigapps.com.gencycler.R;

/**
 * Created by gilgoldzweig on 15/03/2018.
 */
@GencyclerViewHolder(R.layout.java_profile_type)
public class JavaProfileContainer implements GencyclerDataContainer {
    private String name;
    private int age;
    private String profilePicture;

    public JavaProfileContainer(String name, int age, String profilePicture) {
        this.name = name;
        this.age = age;
        this.profilePicture = profilePicture;
    }
}
