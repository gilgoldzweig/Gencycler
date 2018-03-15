package goldzweigapps.com.gencycler.java;

import goldzweigapps.com.annotations.annotations.GencyclerDataType;
import goldzweigapps.com.annotations.annotations.Holder;
import goldzweigapps.com.gencycler.R;

/**
 * Created by gilgoldzweig on 15/03/2018.
 */
@Holder(layoutRes = R.layout.java_profile_type,
        recyclerAdapters = {JavaTestAdapter.class})
public class JavaProfileType implements GencyclerDataType {
    private String name;
    private int age;
    private String profilePicture;

    public JavaProfileType(String name, int age, String profilePicture) {
        this.name = name;
        this.age = age;
        this.profilePicture = profilePicture;
    }

}
