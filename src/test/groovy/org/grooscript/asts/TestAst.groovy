package org.grooscript.asts

import org.grooscript.test.ConversionMixin
import org.grooscript.test.JavascriptEngine
import spock.lang.Specification
import org.grooscript.util.Util

/**
 * JFL 27/08/12
 */
@Mixin([ConversionMixin])
class TestAst extends Specification {

    def readAndConvert(nameOfFile,consoleOutput) {

        def file = JavascriptEngine.getGroovyTestScript(nameOfFile)
        def result = Util.fullProcessScript(file.text)

        if (consoleOutput) {
            println 'jsScript->\n' + result.jsScript
        }
        if (result.exception) {
            assert false, 'Error: ' + result.exception
        }

        return result
    }

    def 'test GsNotConvert' () {
        when:
        def result = readAndConvert('asts/NotConvert', false)

        then:
        !result.assertFails
        result.jsScript.indexOf('NotConvert') < 0
    }

    def 'test simpleGsNative' () {
        expect:
        !readAndConvert('asts/simpleNative', false).assertFails
    }

    def 'test GsNative' () {
        when:
        def result = readAndConvert('asts/native', false)

        then:
        !result.assertFails
        result.jsScript.indexOf('return true;') > 0
    }

    def 'test advanced GsNative' () {
        expect:
        convertAndEvaluate('asts/advancedNative')
    }
}
