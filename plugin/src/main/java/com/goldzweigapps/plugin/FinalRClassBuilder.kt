import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.BodyDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.type.PrimitiveType
import com.github.javaparser.ast.type.Type
import com.squareup.kotlinpoet.*
import java.beans.Expression

import java.io.File
import java.util.Arrays

import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC

/**
 * Generates a class that contains all supported field names in an R file as final values.
 * Also enables adding support annotations to indicate the type of resource for every field.
 */
object FinalRClassBuilder {
    private val SUPPORT_ANNOTATION_PACKAGE = "android.support.annotation"
    private val SUPPORTED_TYPES = arrayOf("anim", "array", "attr", "bool", "color", "dimen", "drawable", "id", "integer", "layout", "menu", "plurals", "string", "style", "styleable")

   private var idMapPropertyBuilder = PropertySpec.builder("idMap",
            ParameterizedTypeName.get(Map::class, String::class, Int::class),
            KModifier.FINAL, KModifier.PUBLIC)
    private var idMap = HashMap<String, Int>()

    private var nameMapPropertyBuilder = PropertySpec.builder("nameMap",
            ParameterizedTypeName.get(Map::class, Int::class, String::class),
            KModifier.FINAL, KModifier.PUBLIC)

    private var nameMap = HashMap<Int, String>()

    @Throws(Exception::class)
    fun brewJava(rFile: File, outputDir: File, packageName: String, className: String) {
        val compilationUnit = JavaParser.parse(rFile)
        val resourceClass = compilationUnit.types[0]

        val result = TypeSpec.objectBuilder(className)
                .addModifiers(KModifier.PUBLIC)

        for (node in resourceClass.childNodes) {
            if (node is ClassOrInterfaceDeclaration) {
                addResourceType(Arrays.asList(*SUPPORTED_TYPES), result, node)
            }
        }
        val finalR = FileSpec.builder(packageName, className)
                .addComment("Generated code from Gencycler gradle plugin. Do not modify!")
                .addType(result.build())
                .build()

        finalR.writeTo(outputDir)
    }

    private fun addResourceType(supportedTypes: List<String>, result: TypeSpec.Builder,
                                node: ClassOrInterfaceDeclaration) {
        if (!supportedTypes.contains(node.nameAsString)) {
            return
        }

        val type = node.nameAsString
        val resourceType = TypeSpec.objectBuilder(type).addModifiers(KModifier.PUBLIC, KModifier.FINAL)

        for (field in node.members) {
            if (field is FieldDeclaration) {
// Check that the field is an Int because styleable also contains Int arrays which can't be
                // used in annotations.
                if (isInt(field)) {
                    addResourceField(resourceType, field.variables[0],
                            getSupportAnnotationClass(type))
                }
            }
        }
        result.addProperty(idMapPropertyBuilder.initializer("mapOf(%E)", idMap.map { "${it.key} to ${it.value}" }.joinToString()).build())
        result.addProperty(nameMapPropertyBuilder.initializer("mapOf(%E)", nameMap.map { "${it.key} to ${it.value}" }.joinToString()).build())
        result.addType(resourceType.build())
    }

    private fun isInt(field: FieldDeclaration): Boolean {
        val type = field.commonType
        return type is PrimitiveType && type.type == PrimitiveType.Primitive.INT
    }

    private fun addResourceField(resourceType: TypeSpec.Builder, variable: VariableDeclarator,
                                 annotation: ClassName?) {
        val fieldName = variable.nameAsString
        val fieldValue = variable.initializer.map { it.toString() }.orElse(null)
        idMap[fieldName] = fieldValue.toInt()
        nameMap[fieldValue.toInt()] = fieldName

        val fieldSpecBuilder = PropertySpec.builder(fieldName,
                Int::class,
                KModifier.PUBLIC)
                .initializer(fieldValue)

        if (annotation != null) {
            fieldSpecBuilder.addAnnotation(annotation)
        }

        resourceType.addProperty(fieldSpecBuilder.build())
    }

    private fun getSupportAnnotationClass(type: String): ClassName {
        return ClassName(SUPPORT_ANNOTATION_PACKAGE, capitalize(type) + "Res")
    }

    private fun capitalize(word: String): String {
        return Character.toUpperCase(word[0]) + word.substring(1)
    }
}
