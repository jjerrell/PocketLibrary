package app.jjerrell.lib.pocket

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}