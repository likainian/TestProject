package com.example.testproject.util

object CheckUtils {

    fun <T> isNotEmpty(obj: T): Boolean {
        return !isEmpty(obj)
    }

    fun <T> isEmpty(obj: T?): Boolean {
        if (null == obj) return true
        return if (obj is CharSequence) {
            isEmptyString(obj as CharSequence?)
        } else if (obj is Collection<*>) {
            isEmptyCollection(obj as Collection<*>?)
        } else if (obj is Map<*, *>) {
            isEmptyMap(obj as Map<*, *>?)
        } else {
            false
        }
    }

    private fun isEmptyString(obj: CharSequence?): Boolean {
        return null == obj || obj.isEmpty()
    }

    private fun isEmptyCollection(obj: Collection<*>?): Boolean {
        return null == obj || obj.isEmpty()
    }

    private fun isEmptyMap(obj: Map<*, *>?): Boolean {
        return null == obj || obj.isEmpty()
    }

    private fun isEmptyArray(obj: Array<Any>): Boolean {
        return obj.isEmpty()
    }

}
