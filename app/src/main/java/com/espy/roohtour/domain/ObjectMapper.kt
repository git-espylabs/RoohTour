package com.espy.roohtour.domain

import com.espy.roohtour.api.models.order.OrderRequestTrans
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.api.models.products.ProductImage
import com.espy.roohtour.api.models.shops.Shop
import com.espy.roohtour.db.entities.OrderEntity
import com.espy.roohtour.db.entities.ProductsEntity
import com.espy.roohtour.db.entities.ShopsEntity
import com.espy.roohtour.ui.order.models.OrderProduct
import com.espy.roohtour.ui.products.models.ImageSlide
import java.io.File

internal fun Product.toOrderProduct(quantity: Int) = OrderProduct(
    id,
    category_id,
    product_name,
    description,
    product_code,
    brand_id,
    unit_id,
    qty = quantity.toString(),
    price.toString(),
    batchcode,
    thumbnail,
    stock,
    batchid
)

internal fun OrderProduct.toOrderRequestTrans() = OrderRequestTrans(
    batchid?:"0",
    qty,
    totqtyamount = (qty.toInt() * product_price.toFloat()).toString(),
    unitprice = product_price
)

internal fun ProductImage.toImageSlide() = ImageSlide(
    image?:"0"
)

internal fun Product.toEntity() = ProductsEntity(
    id?:"0",
    category_id?:"0",
    product_name?:"0",
    description?:"0",
    product_code?:"0",
    brand_id?:"0",
    unit_id?:"0",
    price?:"0",
    batchcode?:"0",
    thumbnail?:"0",
    stock?:"0",
    batchid?:"0",
    mrp?:"0"
)

internal fun ProductsEntity.toDomain() = Product(
    id,
    category_id,
    product_name,
    description,
    product_code,
    brand_id,
    unit_id,
    created_at = null,
    updated_at = null,
    price,
    batchcode,
    thumbnail,
    stock,
    batchid,
    mrp
)

internal fun Shop.toEntity() = ShopsEntity(
    id,
    shop_name,
    lattitude,
    longitude,
    shop_address,
    shop_primary_number,
    shop_secondary_number,
    shop_image,
    shop_oustanding_amount,
    route_id,
    added_by,
    login_id,
    route_name
)

internal fun ShopsEntity.toDomain() = Shop(
    id,
    shop_name,
    lattitude,
    longitude,
    shop_address,
    shop_primary_number,
    shop_secondary_number,
    shop_image,
    shop_oustanding_amount,
    created_at = null,
    updated_at = null,
    route_id,
    added_by,
    login_id,
    route_name
)

internal fun Product.toOrderEntity(quantity: Int) = OrderEntity(
    id?:"0",
    product_name?:"CapProduct",
    batchid?:"0",
    price?:"0.0",
    productQty = quantity.toString(),
    productSum = (quantity * (price?:"0.0").toDouble()).toString()
)

internal fun OrderEntity.toOrderProduct() = OrderProduct(
    productId,
    category_id = "",
    productName,
    description = "",
    product_code = "",
    brand_id = "",
    unit_id = "",
    qty = productQty,
    product_price = productPrice,
    batchcode = "",
    thumbnail = "",
    stock = "",
    batchid = productBatchId
)

internal fun File.toImageSlide() = ImageSlide(
    name
)