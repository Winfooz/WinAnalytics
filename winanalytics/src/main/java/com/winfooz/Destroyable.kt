package com.winfooz

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
interface Destroyable {

    fun destroy()

    companion object {

        val EMPTY_DESTROYABLE: Destroyable = object : Destroyable {

            override fun destroy() {
            }
        }
    }
}
