package com.example.ypackfood.sealedClasses

sealed class TimeOptions(title: String, index: Int) : TabRowSwitchable {
    class Faster(override val title: String = "Поскорее", override val index: Int = 0) : TimeOptions(title, index)
    class ForTime(override val title: String = "Ко времени", override val index: Int = 1) : TimeOptions(title, index)

    companion object {
        fun getOptions(): List<TimeOptions> {
            return listOf(Faster(), ForTime())
        }
    }

    override fun getByIndex(index: Int): TabRowSwitchable {
        val options = getOptions()
        return options.find { it.index == index }!!
    }
}