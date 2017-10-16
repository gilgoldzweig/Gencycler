package goldzweigapps.com.compiler.generators

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.GencyclerHolder
import goldzweigapps.com.compiler.utils.*

/**
 * Created by gilgoldzweig on 12/10/2017.
 */

interface Generator {
    fun generate(): FileSpec
}

object Generators {


    fun ItemViewTypeGenerator(holders: List<GencyclerHolder>): FunSpec {
        val getItemViewType = FunSpec.builder(GET_ITEM_VIEW_TYPE)
                .addParameter(position)
                .returns(Int::class)
                .addModifiers(KModifier.OVERRIDE)
        val layoutSwitch = CodeBlock.builder()
        layoutSwitch.add("return when(elementList[position]) {")
        for (holder in holders) {
            layoutSwitch.add("\nis %T -> %L", ClassName.bestGuess(holder.classType), holder.layoutResId)
        }
        layoutSwitch.add("\nelse -> -1")
        layoutSwitch.add("\n } \n")
        getItemViewType.addCode(layoutSwitch.build())
        return getItemViewType.build()
    }

    fun ViewHoldersGenerator(holders: List<GencyclerHolder>): List<TypeSpec> {
        val viewHolders = ArrayList<TypeSpec>()
        for (holder in holders) {
            val dataType = ClassName.bestGuess(holder.classType)
            val viewHolder = TypeSpec.classBuilder("${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM")
                    .primaryConstructor(FunSpec.constructorBuilder()
                            .addParameter(view)
                            .build())
                    .superclass(RecyclerViewViewHolder)
                    .addSuperclassConstructorParameter("view")
                    .addProperties(holder.viewTypes.map {
                        PropertySpec
                                .builder(it.name, ClassName.bestGuess(it.viewType))
                                .initializer("view.findViewById(${it.resId})")
                                .build()
                    })
            viewHolders.add(viewHolder.build())
        }
        return viewHolders
    }

    fun ItemCountGenerator() = FunSpec.builder(GET_ITEM_COUNT)
            .addModifiers(KModifier.OVERRIDE)
            .returns(Int::class)
            .addStatement("return elements.size")
            .build()

    fun OnCreateViewHolderGenerator(holders: List<GencyclerHolder>): FunSpec {

        val getItemViewType = FunSpec.builder(METHOD_CREATE_VIEW_HOLDER)
                .addParameter("parent", ViewGroup)
                .addParameter("viewType", Int::class)
                .returns(RecyclerViewViewHolder)
                .addModifiers(KModifier.OVERRIDE)
        val layoutSwitch = CodeBlock.builder()
        layoutSwitch.add("return when(viewType) {")
        for (holder in holders) {
            val dataType = ClassName.bestGuess(holder.classType)
            layoutSwitch.add("\n${holder.layoutResId} -> ${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM(inflater.inflate(viewType, parent, false))")
        }
        layoutSwitch.add("\nelse -> throw %T(\"unsupportedType\")", ClassCastException::class.asClassName())
        layoutSwitch.add("\n } \n")
        getItemViewType.addCode(layoutSwitch.build())
        return getItemViewType.build()
    }
    fun OnBindAbstractViewHolderGenerator(holders: List<GencyclerHolder>): List<FunSpec> {
        val abstractOnBindViewHolders = ArrayList<FunSpec>()
        for (holder in holders) {
            val dataType = ClassName.bestGuess(holder.classType)
            val onBindViewHolder = FunSpec.builder("$METHOD_BIND_CUSTOM${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameters(holder.viewTypes.map {
                        ParameterSpec.builder(it.name, ClassName.bestGuess(it.viewType))
                                .build()
                    })
                    .addParameter("rootView", View)
                    .addParameter(ParameterSpec.builder("holder",
                            ClassName.bestGuess("${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM"))
                            .build())
                    .addParameter(position)
                    .addParameter("element", dataType)
            abstractOnBindViewHolders.add(onBindViewHolder.build())
        }
        return abstractOnBindViewHolders
    }

    fun OnBindViewHolderGenerator(holders: List<GencyclerHolder>): FunSpec {

        val getItemViewType = FunSpec.builder(METHOD_BIND_VIEW_HOLDER)
                .addParameter("holder", RecyclerViewViewHolder)
                .addParameter(position)
                .addModifiers(KModifier.OVERRIDE)
        val layoutSwitch = CodeBlock.builder()
        layoutSwitch.add("\n when (holder) {")
        for (holder in holders) {
            val dataType = ClassName.bestGuess(holder.classType)
            val views = CodeBlock.builder()
            holder.viewTypes
                    .forEach { views.add("holder.${it.name}, \n") }
            views.add("holder.itemView, \n")
            layoutSwitch.add("""

                 is ${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM -> $METHOD_BIND_CUSTOM${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM(${views.build()}holder, position, elementList[position] as ${dataType.simpleName()})

            """.trimIndent())
        }
        layoutSwitch.add("\n } \n")
        getItemViewType.addCode(layoutSwitch.build())
        return getItemViewType.build()
    }

}