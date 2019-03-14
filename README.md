
# Gencycler
[![latest version](https://jitpack.io/v/gilgoldzweig/Gencycler.svg)](https://jitpack.io/#gilgoldzweig/Gencycler)


A Boilerplate free RecyclerView adapter.
Gencycler uses annotation processing to analyze your code and generates the Recyclerview adapter for you,
which means you only need to worry about your logic and not about the adapter

- Written in Kotlin <3, for Kotlin and Java
- Generates human readable code(plus comments, minus the human errors)
- Clean and optimized code - Generates only what is required
- Click / Long-Click listeners
- Multi view types
- Expandable items
- Swipe to delete
- Headers
- Footers
- Simple Drag and drop
- Filter
- Easily extensible
- Custom variable naming
- Parses your layout file so no more `findViewById` 
- Comes with useful Helpers
- No runtime cost - everything happens at compile-time
- Multi flavor support
- Butterknife R2 support
 
### Gencycler works in compile time so no runtime performance impact,
Gencycler will generated a readable multi view type RecyclerView Adapter with a thread-safe accessing mechanism.
Gencycler eliminates The need to write all of the boilerplate code needed for writing an adapter and
leaves you with only the bare minimum needed to write your business logic.


# Implement in your project

## Using maven

#### Kotlin
     apply plugin: 'kotlin-kapt'

      repositories {
         maven { url "https://jitpack.io" }
      }
      dependencies {
            implementation "com.github.gilgoldzweig.Gencycler:gencycler:latest_version"
            kapt "com.github.gilgoldzweig.Gencycler:compiler:latest_version"
      }
#### Java
    apply plugin: 'kotlin-android'

    repositories {
      maven { url "https://jitpack.io" }
    }

    dependencies {
         implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$latest_kotlin_version"
         implementation "com.github.gilgoldzweig.Gencycler:gencycler:latest_version"
         annotationProcessor "com.github.gilgoldzweig.Gencycler:compiler:latest_version"
    }
## How to use 

### 1.  Implement your model 
Just create a class which implements the `GencyclerModel`  and annotate it with `@GencyclerViewHolder`

Each class represents a ViewHolder & Data model. In order to associate the class with a layout we place the layout id of what the item should look like inside the annotation as shown below.
  
#### Kotlin

    @GencyclerViewHolder(R.layout.item_simple)  
    data class SimpleModel(val name: String, val description: String) : GencyclerModel

#### Java

    @GencyclerViewHolder(R.layout.item_simple)  
    public class SimpleModel implements GencyclerModel {  
	    private String name;  
	    private String description;  
      
	    public SimpleModel(String name, String description) {  
	           this.name = name;
	           this.description = description;
	    }  
    }

### 2.  Implement your adapter
Just create a class and annotate it with `@GencyclerAdapter`
place every model you want the adapter to use inside the annotation as shown below.

#### Kotlin - Single view type
    
    @GencyclerAdapter(SimpleModel::class)
    class SimpleAdapter
    
#### Kotlin - Mutli view type

    @GencyclerAdapter(SimpleModel::class, AnotherModel::class) //for multiple view types just seperated each model by a comma 
    class SimpleAdapter
    
#### Java - Single view type

    @GencyclerAdapter(SimpleModel.class)
    public class SimpleAdapter {}
    
#### Java - Multi view type
    @GencyclerAdapter({SimpleModel.class, AnotherModel.class})  //for multiple view types just provide an array
    public class SimpleAdapter {}

### 3. Compile
Compile your project and when the compilation finishes your adapters and view holders will be generated.

The generated adapter will be named Generated + Your adapter class name and will be an abstract class with a bind function for every model you provided
The ViewHolder we be Generated and implemented automatically 

### 4. Implementing the generated adapter
Now that we have the generated code all we need to do is implement it

The generated adapter will require 3 parameters

* context: `Context` |  Used to inflate the layout of the ViewHolder  

* elements: `MutableList` with the type of the provided model or `GencyclerModel` when multiple models wer'e provided. Default: `ArrayList()` | Each element represents a Generated ViewHolder
*  updateUi: `Boolean` Default: `true`
	* The generated adapter comes with many helper methods (add, remove, etc...) each action will update the elements in the adapter but if updateUi is True the adapter will also run a UI Thread check and call the appropriate notify method of the adapter (notifyItemInserted, notifyItemRemoved, etc...) whenever one of those function is called

And a `onBind` abstract function 

The complete adapter will look like this 

#### Kotlin

    @GencyclerAdapter(SimpleModel::class)
    class SimpleAdapter(context: Context, elements: ArrayList<SimpleModel>) : GeneratedSimpleAdapter(context, elements) {  
     
        	override fun onBindSimpleModelViewHolder(
            	simpleModelViewHolder: SimpleModelViewHolder, //Generated ViewHolder, all the views from your layout are here
            	simpleModel: SimpleModel, 
            	position: Int) { 
            	
                	//Your logic here
        }  
    }

#### Java
    
    @GencyclerAdapter(SimpleModel.class)  
    public class SimpleAdapter extends GeneratedSimpleAdapter {  
      
        public SimpleAdapter(Context context, ArrayList<SimpleModel> elements) { 
            super(context, elements);  
        }  
      
	    @Override  
	    public void onBindSimpleModelViewHolder(
                    @NotNull SimpleModelViewHolder simpleModelViewHolder,
                    @NotNull SimpleModel simpleModel,
                    int position) {  

                        //Your logic here  
	    }  
    }

# Advance/Extras

## Custom naming

Gencycler provides renaming options to the adapter and the variables inside the ViewHolder

### Adapter
By default the generated adapter will be named `Generated` + `Your adapter name` but you can select a custom name for your adapter by specifying it inside the `@GencyclerAdapter` as shown below

#### Kotlin
    
    @GencyclerAdapter(SimpleModel::class, customName = "CustomNameAdapter")
    class SimpleAdapter
    
#### Java

    @GencyclerAdapter(value = SimpleModel.class, customName = "CustomNameAdapter")
    public class SimpleAdapter {}

### Variables
By default the processor turns the id of every view in the xml layout to variable name and converts it to [lower camel-case](http://wiki.c2.com/?LowerCamelCase)

You can change the names of the variables by specifying a naming adapter inside the `@GencyclerViewHolder` as shown below

#### Kotlin

    @GencyclerViewHolder(R.layout.item_simple, NamingCase.NAMING_CASE_SNAKE)  
    data class SimpleModel(val name: String, val description: String) : GencyclerModel

#### Java
    
    @GencyclerViewHolder(value = R.layout.java_profile_type, namingCase = NamingCase.NAMING_CASE_SNAKE)  
    public class SimpleModel implements GencyclerModel {  
        private String name;  
        private String description;  
      
        public SimpleModel(String name, String description) {  
            this.name = name;
            this.description = description;
        }  
    }

You can choose between the following

    enum class NamingCase {  
        NAMING_CASE_NONE,  
        NAMING_CASE_CAMEL,  //Default
        NAMING_CASE_SNAKE  
    }

## Event listeners
For now Genecycler supports two listeners but if you want to create one for yourself just create and send pull request

### Click /  Long Click

#### 1. Annotate your adapter with `@Clickable` or `@LongClickable`
To add listeners for item click all you need to do is annotate your adapter with `@Clickable` or `@LongClickable` (You can both at the same time) as shown below 

#### Kotlin

    @GencyclerAdapter(SimpleModel::class)
    @Clickable
    @LongClickable
    class SimpleAdapter
    
#### Java

    @GencyclerAdapter(value = SimpleModel.class)
    @Clickable
    @LongClickable
    public class SimpleAdapter {}
	
#### 2. Compile 
Once the compilation completes an interface will be added to the generated adapter constructor

- `OnItemClickedListener<T>` for `@Clickable`
- `OnItemLongClickedListener<T>` for `@LongClickable`

> T a generic type 
> Your model for single type
> GencyclerModel for multi view types

## Simple gestures(Drag and Drop, Swipe to delete)

Create a new `SimpleGesturesHelper` and provide it your generated adapter

#### Kotlin

	val gesturesHelper = SimpleGesturesHelper(adapter)
	
	gesturesHelper.setSwipeEnabled(enabled: Boolean, vararg directions: Int) //ItemTouchHelper.START or ItemTouchHelper.END
	gesturesHelper.setDragAndDropEnabled(enabled: Boolean, longPressOnly: Boolean = false)

    gesturesHelper.attachToRecyclerView(recyclerView)
    
#### Java

	SimpleGesturesHelper gesturesHelper = new SimpleGesturesHelper(adapter);
	
    gesturesHelper.setSwipeEnabled(Boolean enabled, Integer... directions); //ItemTouchHelper.START or ItemTouchHelper.END
    
    gesturesHelper.setDragAndDropEnabled(Boolean enabled); //longPressOnly default: false
    gesturesHelper.setDragAndDropEnabled(Boolean enabled, Boolean longPressOnly);
    
    gesturesHelper.attachToRecyclerView(recyclerView);


## Filter
Gencycler provides you with a way to filter your items in order to that you need to do the following 

#### 1. Annotate your adapter with `@Filterable`

#### Kotlin

    @GencyclerAdapter(SimpleModel::class)
    @Filterable
    class SimpleAdapter
    
#### Java

    @GencyclerAdapter(value = SimpleModel.class)
    @Filterable
    public class SimpleAdapter {}
	
#### 2. Compile 

#### 3. Implement the `performFilter` method

#### Kotlin

    @GencyclerAdapter(SimpleModel::class)  
    @Filterable  
    class SimpleAdapter(context: Context, elements: ArrayList<SimpleModel>) : GeneratedSimpleAdapter(context, elements) {  
          
		//other methods
         
		override fun performFilter(constraint: CharSequence, simpleModel: SimpleModel): Boolean {  
		    //return true if the item should be retained and false if the item should be removed.
		}  
	}
    
#### Java

    @GencyclerAdapter(SimpleModel::class)  
    @Filterable  
    class SimpleAdapter : GeneratedSimpleAdapter {  
          
		//other methods
         
		@Overrides 
      	public Boolean performFilter(CharSequence constraint, SimpleModel simpleModel) {  
      		//return true if the item should be retained and false if the item should be removed.
        }  
	}
    
To filter the items, inside your Activity/Fragment call
    
    Call this in onQueryTextSubmit() & onQueryTextChange() when using SearchView

#### Kotlin

    adapter.filter("yourSearchTerm")
	
#### Java

    adapter.filter("yourSearchTerm");


## Custom view recycling 
Gencycler generates functions for binding and recycling of ViewHolders

The amount of functions will be the same for binding and recycling functions

Binding function are abstract because the system does not provide a default option
Recycling on the other hand do have a default option which you should edit in order to improve performance

The generated functions look as shown below
    
#### Kotlin

    override fun onRecycledSimpleModelViewHolder(simpleModelViewHolder: SimpleModelViewHolder,
                                                 position: Int) {
                                                 
		super.onRecycledSimpleModelViewHolder(simpleModelViewHolder, position) //remove this line
		//apply your custom recycleing logic
    }
    
#### Java

	@Override 
    public void onRecycledSimpleModelViewHolder(SimpleModelViewHolder simpleModelViewHolder,
                                                Integer position) {
                                                 
		super.onRecycledSimpleModelViewHolder(simpleModelViewHolder, position); //remove this line
		//apply your custom recycleing logic
    }

## Miscellaneous(Optional)

Gencycler supports number of custom options regarding customized build/flavors
All of the option are inputed as shown below

    android {
        //your project configuration
        defaultConfig {
            //your project default config
            
            //Add this section
            javaCompileOptions {
                annotationProcessorOptions {
                    arguments = [
                        "androidManifestFile": Providing specific file path to the manifest
                        "rClassPackage": Providing R class package - Just like when you do import but with the .R at the end (example: com.example.app.R = com.example.app)
                        "androidUseR2": true / false if you want to use Butterknife's R2 instead of R 
                        "resourcesFolder": In case you use multi flavor layouts you need to use getCurrentFlavor()
                    ]
                }
            }
        }
    }

This only applies if you have duplicate layouts for multi flavors here is a function to get the current flavor

#### Add the following function to your gradle file

    def getCurrentFlavor() {
    
		Gradle gradle = getGradle()
		Pattern pattern;
        
		String taskRequest = gradle.getStartParameter().getTaskRequests().toString()

		if (taskRequest.contains("assemble")) {
			pattern = Pattern.compile("assemble(\\w+)(Release|Debug)")
		} else {
			pattern = Pattern.compile("generate(\\w+)(Release|Debug)")
		}

		Matcher matcher = pattern.matcher(taskRequest)

		if (matcher.find()) {
			return matcher.group(1).toLowerCase()
		} else {
			println "NO MATCH FOUND"
			return "";
		}
	}


## How it works 
I'm working on the blog post

## Contributing
If you want to contribute to this project check if there are any open issues 
or just send a pull request and I'll do my best to look at it

## License

    MIT License
    
    Copyright (c) 2018 Gil Goldzweig Goldbaum
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
