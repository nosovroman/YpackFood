package com.example.ypackfood.sealedClasses


sealed class SignOptions(title: String, index: Int) : TabRowSwitchable {
    class SignIn(override val title: String = "Вход", override val index: Int = 0) : SignOptions(title, index)
    class SignUp(override val title: String = "Регистрация", override val index: Int = 1) : SignOptions(title, index)

    companion object {
        fun getOptions(): List<SignOptions> {
            return listOf(SignIn(), SignUp())
        }
    }

    override fun getByIndex(index: Int): TabRowSwitchable {
        val options = getOptions()
        return options.find { it.index == index }!!
    }
}