package com.example.culinarycompass.data

import com.squareup.moshi.Json


data class Links(
    @field:Json(name = "self") val self: Link, @field:Json(name = "next") val next: Link
)


data class Link(
    @field:Json(name = "href") val href: String, @field:Json(name = "title") val title: String
)


data class Images(
    @field:Json(name = "THUMBNAIL") val thumbnail: Image,
    @field:Json(name = "SMALL") val small: Image,
    @field:Json(name = "REGULAR") val regular: Image,
    @field:Json(name = "LARGE") val large: Image
)


data class Image(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "width") val width: Int,
    @field:Json(name = "height") val height: Int
)


data class Ingredient(
    @field:Json(name = "text") val text: String,
    @field:Json(name = "quantity") val quantity: Double,
    @field:Json(name = "measure") val measure: String,
    @field:Json(name = "food") val food: String,
    @field:Json(name = "weight") val weight: Double,
    @field:Json(name = "foodId") val foodId: String
)


data class TotalNutrients(
    @field:Json(name = "additionalProp1") val additionalProp1: Nutrient,
    @field:Json(name = "additionalProp2") val additionalProp2: Nutrient,
    @field:Json(name = "additionalProp3") val additionalProp3: Nutrient
)


data class Nutrient(
    @field:Json(name = "label") val label: String,
    @field:Json(name = "quantity") val quantity: Double,
    @field:Json(name = "unit") val unit: String
)


data class TotalDaily(
    @field:Json(name = "additionalProp1") val additionalProp1: Nutrient,
    @field:Json(name = "additionalProp2") val additionalProp2: Nutrient,
    @field:Json(name = "additionalProp3") val additionalProp3: Nutrient
)


data class Digest(
    @field:Json(name = "label") val label: String,
    @field:Json(name = "tag") val tag: String,
    @field:Json(name = "schemaOrgTag") val schemaOrgTag: String,
    @field:Json(name = "total") val total: Double,
    @field:Json(name = "hasRDI") val hasRDI: Boolean,
    @field:Json(name = "daily") val daily: Double,
    @field:Json(name = "unit") val unit: String,

    )


data class Recipe(
    @field:Json(name = "uri") val uri: String,
    @field:Json(name = "label") val label: String,
    @field:Json(name = "image") val image: String,
    @field:Json(name = "images") val images: Images,
    @field:Json(name = "source") val source: String,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "shareAs") val shareAs: String,
    @field:Json(name = "yield") val yield: Int,
    @field:Json(name = "dietLabels") val dietLabels: List<String>,
    @field:Json(name = "healthLabels") val healthLabels: List<String>,
    @field:Json(name = "cautions") val cautions: List<String>,
    @field:Json(name = "ingredientLines") val ingredientLines: List<String>,
    @field:Json(name = "ingredients") val ingredients: List<Ingredient>,
    @field:Json(name = "calories") val calories: Double,
    @field:Json(name = "glycemicIndex") val glycemicIndex: Double,
    @field:Json(name = "inflammatoryIndex") val inflammatoryIndex: Double,
   // @field:Json(name = "totalCO2Emissions") val totalCO2Emissions: Double,
    @field:Json(name = "co2EmissionsClass") val co2EmissionsClass: String,
    @field:Json(name = "totalWeight") val totalWeight: Double,
    @field:Json(name = "cuisineType") val cuisineType: List<String>,
    @field:Json(name = "mealType") val mealType: List<String>,
    @field:Json(name = "dishType") val dishType: List<String>,
    @field:Json(name = "instructions") val instructions: List<String>,
    @field:Json(name = "tags") val tags: List<String>,
    @field:Json(name = "externalId") val externalId: String,
    @field:Json(name = "totalNutrients") val totalNutrients: TotalNutrients,
    @field:Json(name = "totalDaily") val totalDaily: TotalDaily,
    @field:Json(name = "digest") val digest: List<Digest>
)


data class SearchResult(
    @field:Json(name = "from") val from: Int,
    @field:Json(name = "to") val to: Int,
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "_links") val links: Links,
    @field:Json(name = "hits") val hits: List<Hit>,
    @field:Json(name = "errors") val errors: List<Error>
)


data class Hit(
    @field:Json(name = "recipe") val recipe: Recipe,
    @field:Json(name = "_links") val links: Links
)


data class Error(
    @field:Json(name = "error") val error: String,
    @field:Json(name = "property") val property: String
)

data class QueryParams(
    val search: String,
    var diet: Array<String> = arrayOf(),
    var health: Array<String>,
    var cusine: Array<String>?,
    var mealtype: Array<String>?,
    var dishtype: Array<String>?,
    val nextpage: String
)