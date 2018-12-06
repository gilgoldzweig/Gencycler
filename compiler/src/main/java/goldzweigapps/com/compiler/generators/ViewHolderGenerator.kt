package goldzweigapps.com.compiler.generators

import com.squareup.kotlinpoet.*
import goldzweigapps.com.compiler.adapter.NamingAdapter
import goldzweigapps.com.compiler.consts.Names
import goldzweigapps.com.compiler.consts.Packages
import goldzweigapps.com.compiler.consts.Parameters
import goldzweigapps.com.compiler.models.ViewField
import goldzweigapps.com.compiler.models.ViewHolder
import goldzweigapps.com.compiler.models.asClassName
import goldzweigapps.com.compiler.utils.Logger
import goldzweigapps.com.compiler.utils.simpleEnumName

class ViewHolderGenerator(private val rClass: ClassName) {


    private val fileBuilder = FileSpec.builder(Packages.GENCYCLER,
            FILE_NAME)

    private val viewHolderKDoc = """
        Generated ViewHolder created based on [%T.layout.%L]

    """.trimIndent()

    /**
     * Generate all ViewHolders requested using the 'GencyclerViewHolder' annotation
     * Each ViewHolder will be generated as part of 'GencyclerViewHolders' file
     *
     * @see GencyclerViewHolder
     * @param viewHolders set of ViewHolders
     * @return FileSpec
     */
    fun generate(viewHolders: List<ViewHolder>): FileSpec {

        fileBuilder.addImport(rClass.packageName, rClass.simpleName)
        fileBuilder.addImport(Parameters.VIEW_HOLDER_SUPER_CLASS.packageName,
                Parameters.VIEW_HOLDER_SUPER_CLASS.simpleName)

        val identificationEnum = generateIdentificationEnum()
        val identificationEnumCodeBlockBuilder = CodeBlock.Builder()

        for ((index, holder) in viewHolders.withIndex()) {

            val simpleEnumName = holder.asClassName().simpleEnumName

            val (enumConstantName, enumConstantValue) =
                    generateEnumConstant(simpleEnumName, holder.layoutName, index)

            fileBuilder.addType(generateViewHolder(holder))


            identificationEnum
                    .addEnumConstant(enumConstantName, enumConstantValue)

            identificationEnumCodeBlockBuilder
                    .insertViewHolderEnumCase(index, simpleEnumName)

        }

        return fileBuilder
                .addType(identificationEnum
                        .buildIdentificationEnum(identificationEnumCodeBlockBuilder))
                .build()
    }


    private fun generateIdentificationEnum(): TypeSpec.Builder {
        return TypeSpec.enumBuilder(Parameters.VIEW_HOLDER_TYPE_ENUM_CLASS)
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter("id", INT)
                        .addParameter("layout", INT)
                        .build())
                .addProperty(PropertySpec.builder("id", INT)
                        .initializer("id")
                        .build())
                .addProperty(PropertySpec.builder("layout", INT)
                        .initializer("layout")
                        .build())
    }

    private fun TypeSpec.Builder.buildIdentificationEnum(enumIdentificationBuilder: CodeBlock.Builder): TypeSpec {
        return addType(TypeSpec.companionObjectBuilder()
                .addFunction(FunSpec.builder("valueOf")
                        .addParameter("id", INT)
                        .beginControlFlow("return when (id)")
                        .addCode(
                                enumIdentificationBuilder.insertExceptionThrowCase()
                                        .build())
                        .endControlFlow()
                        .build())
                .build())
                .build()
    }

    private fun generateViewHolder(holder: ViewHolder): TypeSpec {
        return TypeSpec.classBuilder(holder.asClassName())
                .addKdoc(viewHolderKDoc, rClass, holder.layoutName)
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter(Parameters.VIEW_PARAMETER_SPEC)
                        .build())
                .superclass(Parameters.VIEW_HOLDER_SUPER_CLASS)
                .addSuperclassConstructorParameter(Names.VIEW)
                .addProperties(holder.viewFields.map {
                    generateViewProperty(it, holder.namingAdapter)
                })
                .build()
    }

    private fun generateViewProperty(viewField: ViewField,
                                     namingAdapter: NamingAdapter): PropertySpec {
        return PropertySpec
                .builder(namingAdapter.buildNameForId(viewField.resId), ClassName.bestGuess(viewField.viewClassName))
                .initializer("findView(R.id.${viewField.resId})")
                .build()
    }


    private fun generateEnumConstant(enumName: String,
                                     layoutName: String,
                                     id: Int): Pair<String, TypeSpec> {
        return enumName to TypeSpec.anonymousClassBuilder()
                .addSuperclassConstructorParameter("$id")
                .addSuperclassConstructorParameter("R.layout.%L", layoutName)
                .build()
    }

    private fun CodeBlock.Builder.insertViewHolderEnumCase(id: Int,
                                                           viewHolderEnumName: String): CodeBlock.Builder {

        return addStatement("%L ->", id)
                .indent()
                .addStatement("%L", viewHolderEnumName)
                .unindent()
                .addStatement("")
    }

    private fun CodeBlock.Builder.insertExceptionThrowCase(): CodeBlock.Builder {
        return addStatement("else ->")
                .indent()
                .addStatement("throw %T(\"No layout was found for requested id\")",
                        Parameters.ILLEGAL_ARGUMENT_EXCEPTION_CLASS)
                .unindent()
                .addStatement("")
    }


    companion object {
        const val FILE_NAME = "GencyclerViewHolders"
    }
}
