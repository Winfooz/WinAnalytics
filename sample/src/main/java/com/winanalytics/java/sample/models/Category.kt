package com.winanalytics.java.sample.models

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
data class Category(

    var name: String,

    var image: String,

    var subCategory: Category
)