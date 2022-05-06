package com.example.ypackfood.sealedClasses

interface TabRowSwitchable {
    val title: String
    val index: Int
    fun getByIndex(index: Int): TabRowSwitchable {
        return DeliveryOptions.DELIVERY()
    }
}

sealed class DeliveryOptions(title: String, index: Int) : TabRowSwitchable {
    class DELIVERY(override val title: String = "Доставка", override val index: Int = 0) : SignOptions(title, index)
    class PICKUP(override val title: String = "Самовывоз", override val index: Int = 1) : SignOptions(title, index)

    companion object {
        fun getOptions(): List<SignOptions> {
            return listOf(DELIVERY(), PICKUP())
        }
    }

    override fun getByIndex(index: Int): TabRowSwitchable {
        val options = getOptions()
        return options.find { it.index == index }!!
    }
}