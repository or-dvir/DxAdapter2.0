package com.hotmail.or_dvir.dxadapter

/**
 * an interface containing some shared behaviour for all items to be used by [DxAdapter].
 * note that other "IDxItem..." interfaces already extend this one so (usually) there is no need
 * to implement this interface directly.
 */
interface IDxBaseItem
{
    /**
     * returns an identifier for this adapter item. this identifier should be unique.
     *
     * to prevent bugs, this value should be a constant, and it is recommended to use @IdRes
     */
    fun getViewType(): Int
}