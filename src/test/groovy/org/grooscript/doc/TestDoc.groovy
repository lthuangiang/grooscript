package org.grooscript.doc

import org.grooscript.test.ConversionMixin
import spock.lang.Specification

/**
 * JFL 13/12/14
 */
@Mixin([ConversionMixin])
class TestDoc extends Specification {

    def 'test inheritance'() {
        expect:
        convertAndEvaluate 'doc/Inheritance'
    }

    def 'test object'() {
        expect:
        convertAndEvaluate 'doc/Object'
    }

    def 'little features'() {
        expect:
        convertAndEvaluate 'doc/LittleFeatures'
    }

    def 'operators'() {
        expect:
        convertAndEvaluate 'doc/Operators'
    }

    def 'groovy truth'() {
        expect:
        convertAndEvaluate 'doc/Truth'
    }
}
