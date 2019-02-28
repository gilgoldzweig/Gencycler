package goldzweigapps.com.compiler.generators

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.Actions
import goldzweigapps.com.compiler.consts.KDocs
import goldzweigapps.com.compiler.consts.Parameters
import goldzweigapps.com.compiler.models.ViewType
import goldzweigapps.com.compiler.utils.simpleParameterName

class ListenerGenerator {

    fun generate(viewTypes: List<ViewType>) : TypeSpec {

        val listenerInterfaceBuilder = TypeSpec.interfaceBuilder("ActionsListener")
                .addKdoc(KDocs.ON_ACTION_LISTENER_CLASS)



        for (viewType in viewTypes) {

            viewType.actions.forEach {

                val viewHolderName = viewType.viewHolderType.simpleName

                val actionParamaterSpec = ParameterSpec
                        .builder(viewType.dataContainerType.simpleParameterName, viewType.dataContainerType)
                        .build()

                when (it) {
                    Actions.CLICK ->
                        listenerInterfaceBuilder
                                .addFunction(FunSpec.builder("on${viewHolderName}Clicked")
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(Parameters.POSITION_PARAMETER_SPEC)
                                        .addParameter(actionParamaterSpec)
                                        .build())

                    Actions.LONG_CLICK ->
                        listenerInterfaceBuilder
                                .addFunction(FunSpec.builder("on${viewHolderName}LongClicked")
                                        .addModifiers(KModifier.ABSTRACT)
                                        .returns(BOOLEAN)
                                        .addParameter(Parameters.POSITION_PARAMETER_SPEC)
                                        .addParameter(actionParamaterSpec)
                                        .build())

                    Actions.TOUCH ->
                        listenerInterfaceBuilder
                                .addFunction(FunSpec.builder("on${viewHolderName}Touched")
                                        .addModifiers(KModifier.ABSTRACT)
                                        .returns(BOOLEAN)
                                        .addParameter(Parameters.VIEW_PARAMETER_SPEC)
                                        .addParameter(Parameters.MOTION_EVENT_PARAMETER_SPEC)
                                        .build())
                }
            }
        }

        return listenerInterfaceBuilder.build()
    }
}