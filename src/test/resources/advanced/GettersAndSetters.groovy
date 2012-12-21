package advanced

/**
 * JFL 21/12/12
 */

class Mix {

    def a
    def b = []
    def getters = 0
    def setters = 0

    def setA(value) {
        setters++
        a = value
    }

    def getA() {
        getters++
        a
    }

    def getB() {
        getters++
        b
    }

    def setB(value) {
        setters++
        b = value
    }
}

def mix = new Mix()

mix.a = 0

assert mix.getters == 0
assert mix.setters == 1

mix.a += 1

assert mix.a == 1
assert mix.getters == 2
assert mix.setters == 2

mix.a ++

assert mix.a == 2
assert mix.getters == 4
assert mix.setters == 3

mix.b << 1
assert mix.getters == 5
assert mix.setters == 3
