package goldzweigapps.com.compiler.utils

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import goldzweigapps.com.annotations.interfaces.GencyclerDataType


/**
 * Created by gilgoldzweig on 14/10/2017.
 */
internal const val FILE_NAME_ADDON = "Gencycler"
internal const val PACKAGE_NAME = "goldzweigapps.com.gencycler.adapters"

internal val METHOD_GET_VIEW_TYPE_NAME = "getItemViewType"
internal val METHOD_CREATE_VIEW_HOLDER = "onCreateViewHolder"
internal val METHOD_BIND_VIEW_HOLDER = "onBindViewHolder"
internal val METHOD_BIND_CUSTOM = "onBind"
internal val METHOD_VIEW_HOLDER_CUSTOM = "ViewHolder"
internal val GET_ITEM_COUNT = "getItemCount"
internal val GET_ITEM_VIEW_TYPE = "getItemViewType"

internal val RecyclerViewViewHolder = ClassName("android.support.v7.widget.RecyclerView", "ViewHolder")
internal val RecyclerViewAdapter = ClassName("android.support.v7.widget.RecyclerView", "Adapter")
internal val RecyclerViewAdapterType = ParameterizedTypeName.get(RecyclerViewAdapter, RecyclerViewViewHolder)
internal val RecyclerView = ClassName("android.support.v7.widget", "RecyclerView")
internal val View = ClassName("android.view", "View")
internal val ViewGroup = ClassName("android.view", "ViewGroup")
internal val context = ClassName("android.content", "Context")
internal val LayoutInflater = ClassName("android.view", "LayoutInflater")
internal val LayoutInflaterProperty = PropertySpec.builder("inflater", LayoutInflater)
        .initializer("%T.from(context)", LayoutInflater)
        .mutable(false)
        .addModifiers(KModifier.PRIVATE)
        .build()
internal val elements = ParameterizedTypeName.get(ArrayList::class, GencyclerDataType::class)
internal val elementsList = ParameterizedTypeName.get(List::class, GencyclerDataType::class)
internal val looper = ClassName("android.os", "Looper")
internal val build = ClassName("android.os", "Build")
internal val recyclerAdapterExtension = "GencyclerAdapterExtensions"
internal val recyclerAdapterExtensionImpl = ClassName(PACKAGE_NAME, recyclerAdapterExtension)
internal val elementsProperty = PropertySpec.builder("elements", elements).mutable(false).build()
