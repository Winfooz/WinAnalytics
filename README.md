# WinAnalytics(Beta) library
[![buddy pipeline](https://app.buddy.works/mohamednayef95/winanalytics-1/pipelines/pipeline/158574/badge.svg?token=071324226326a701b263c3a2755acc1f179227f6bb2f1d11c84cbbfd3e77c732 "buddy pipeline")](https://app.buddy.works/mohamednayef95/winanalytics-1/pipelines/pipeline/158574) [ ![Download](https://api.bintray.com/packages/mnayef95/WinAnalytics/com.winfooz.winanalytics%3Awinanalytics/images/download.svg) ](https://bintray.com/mnayef95/WinAnalytics/com.winfooz.winanalytics%3Awinanalytics/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WinAnalytics-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7197) ![GitHub](https://img.shields.io/github/license/mashape/apistatus.svg) ![GitHub](https://img.shields.io/badge/Support-Kotlin%20%7C%20Java-lightgrey.svg)

A light-weight android library that can be quickly integrated into any app to use analytics tools.
- Full Kotlin support.
- Support multiple analytical tools e.g(Firebase, Fabric, Mixpanel).
- Annotations based.
- 100% reflection free.

**Application class**
```

@AnalyticsConfiguration(
        AnalyticsClient(type = AnalyticsTypes.FIREBASE, enabled=false),
        AnalyticsClient(type = AnalyticsTypes.FABRIC),
        AnalyticsClient(key = "mixpanelToken", type = AnalyticsTypes.MIXPANEL)
)
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}
```

# Example WinAnalytics:

**Model class**
```Java
data class User(
        @Analytics(
                Event("Login"),
                Event("Logout")
        )
        val name: String,

        @Analytics(Event("Logout"))
        val email: String,

        @Analytics(Event("Login"))
        val phone: String,

        @Analytics(
                Event("Login"),
                Event("Logout")
        )
        val age: Int,

        @AnalyticsEmbedded
        val address: Address?
)

data class Address(
        @Analytics(Event("Login"))
        val address: String,

        @Analytics(Event("Login"))
        val latitude: String,

        @Analytics(Event("Login"))
        val longitude: String
)
```

**MainActivity**
```
private fun onHelloWorldClicked(view: View) {
    Analytics.getInstance(applicationContext).userAnalytics.loginEvent(user)
}
```

# Example analytics from more than a place:

**MainActivity**
```
@AnalyticsEmbedded
var user: User? = null

@AnalyticsEmbedded
var address: Address? = null

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Analytics.getInstance(applicationContext).mainActivityAnalytics.loginEvent(this)
}
```

# How do I get set up?
App-level build.gradle \(\<project>/\<app-module>/build.gradle):

```
implementation 'com.winfooz.winanalytics:annotations:1.0.0-beta'
implementation 'com.winfooz.winanalytics:winanalytics:1.0.0-beta'
kapt 'com.winfooz.winanalytics:compiler:1.0.0-beta'
```

# Support annotations
```
@Analytics()
@AnalyticsConfiguration()
@AnalyticsEmbedded()
@AnalyticsTypes()
@AnalyticsClient()
```
## License

WinAnalytics is released under the MIT license. [See LICENSE](https://github.com/Winfooz/WinAnalytics/blob/master/LICENSE) for details.
