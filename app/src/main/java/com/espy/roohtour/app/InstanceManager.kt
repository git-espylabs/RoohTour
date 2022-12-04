package com.espy.roohtour.app

import com.espy.roohtour.api.models.common.ExpenseType
import com.espy.roohtour.api.models.login.Profile
import com.espy.roohtour.api.models.products.Category
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.api.models.shops.Shop

object InstanceManager {

    var productList: List<Product> = listOf()
    var categoriesList: List<Category> = listOf()
    var shopsList: List<Shop> = listOf()
    var profile: Profile? = null
    var capExpenseTypes: List<ExpenseType> = listOf()

    fun clear(){
        productList = listOf()
        categoriesList = listOf()
        shopsList = listOf()
        capExpenseTypes = listOf()
        profile = null
    }
}