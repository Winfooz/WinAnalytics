package com.winanalytics.kotlin.sample.test

import com.winanalytics.kotlin.sample.models.User
import com.winfooz.Analytics
import com.winfooz.Data
import com.winfooz.Event
import com.winfooz.Key
import com.winfooz.Name
import com.winfooz.Value

/**
 * Project: WinAnalytics2
 * Created: November 22, 2018
 *
 * @author Mohamed Hamdan
 */
@Analytics
interface KotlinMainActivityAnalytics {

    @Event(
        value = "User login",
        timestamp = true,
        events = [
            Data(value = Value("str"), key = Key("Mohamed")),
            Data(value = Value("user.firstName"), key = Key("first_name")),
            Data(value = Value("user.lastName"), key = Key("last_name")),
            Data(value = Value("user.email"), key = Key("email")),
            Data(value = Value("user.user1.email"), key = Key("sub_email")),
            Data(value = Value("user.user1.user2.email"), key = Key("sub_email1")),
            Data(value = Value("user.user1.user2.user3.email"), key = Key("sub_email2"))
        ]
    )
    fun userLoginSuccessfullyEvent(@Name("user") user: User?, @Name("str") str: String?)

    @Event(
        value = "User login",
        timestamp = true,
        events = [
            Data(value = Value("user.firstName"), key = Key("first_name")),
            Data(value = Value("user.lastName"), key = Key("last_name")),
            Data(value = Value("user.email"), key = Key("email")),
            Data(value = Value("user.user1.email"), key = Key("sub_email")),
            Data(value = Value("user.user1.user2.email"), key = Key("sub_email1")),
            Data(value = Value("user.user1.user2.user3.email"), key = Key("sub_email2"))
        ]
    )
    fun userLogin1SuccessfullyEvent(@Name("user") user: User?)
}
