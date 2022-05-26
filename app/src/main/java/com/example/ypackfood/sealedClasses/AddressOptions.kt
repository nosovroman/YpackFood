package com.example.ypackfood.sealedClasses

sealed class AddressOptions(title: String, index: Int) : TabRowSwitchable {
    class NEW_ADDRESS(override val title: String = "Новый", override val index: Int = 0) : AddressOptions(title, index)
    class OLD_ADDRESS(override val title: String = "Из списка", override val index: Int = 1) : AddressOptions(title, index)

    companion object {
        fun getOptions(): List<AddressOptions> {
            return listOf(NEW_ADDRESS(), OLD_ADDRESS())
        }
    }

    override fun getByIndex(index: Int): TabRowSwitchable {
        val options = getOptions()
        return options.find { it.index == index }!!
    }
}