package org.grooscript.convert.handlers

import org.codehaus.groovy.ast.InnerClassNode
import org.codehaus.groovy.ast.stmt.BlockStatement

import static org.grooscript.JsNames.*

/**
 * User: jorgefrancoleza
 * Date: 05/04/14
 */
class InnerClassNodeHandler extends BaseHandler {

    void handle(InnerClassNode innerClassNode) {

        def className = innerClassNode.outerClass.nameWithoutPackage
        createConstructorWithApplyTryFunction(innerClassNode, className)

        innerClassNode.methods.findAll { !it.isAbstract() }.each {
            if (it.code instanceof BlockStatement) {
                if (!it.code.isEmpty()) {
                    functions.processBasicFunction("${className}.${it.name}", it, false)
                }
            } else {
                if (it.name.startsWith('get')) {
                    out.addScript("${className}.${it.name} = function(\$self) {" +
                            " return \$self.${getNameOfTraitField(innerClassNode.outerClass, it.name)}; }", true)
                }
                if (it.name.startsWith('set')) {
                    out.addScript("${className}.${it.name} = function(\$self, value) {" +
                            " \$self.${getNameOfTraitField(innerClassNode.outerClass, it.name)} = value; }", true)
                }
            }
        }
    }

    private getNameOfTraitField(classNode, String methodName) {
        String propertyName = methodName.substring(3)
        propertyName = propertyName[0].toLowerCase() + propertyName.substring(1)
        org.codehaus.groovy.transform.trait.Traits.remappedFieldName(classNode, propertyName)
    }

    private createConstructorWithApplyTryFunction(InnerClassNode innerClassNode, className) {

        final TARGET = 'target'

        out.addScript("${className} = function() {};", true)
        out.addScript("${className}.${GS_APPLY_TRAIT} = function(${TARGET}) {", true)
        innerClassNode.methods.findAll { !it.isAbstract() }.each { methodNode ->
            if (methodNode.name == '$init$') {
                if (methodNode.code instanceof BlockStatement && !methodNode.code.isEmpty()) {
                    out.addScript("  ${className}.\$init\$(${TARGET});", true)
                }
            } else if (methodNode.name == '$static$init$') {

            } else {
                def listParams = []
                methodNode.parameters.eachWithIndex{ param, int i -> listParams << "x${i}" }
                def targetParams = listParams.join(', ')
                listParams = [TARGET] + listParams
                def traitParams = listParams.join(', ')
                out.addScript("  ${TARGET}.${methodNode.name} = function(${targetParams}) { " +
                        "return ${className}.${methodNode.name}(${traitParams}); };", true)
            }
        }
        out.addScript("};", true)
    }
}
