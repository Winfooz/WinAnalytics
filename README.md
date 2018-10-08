# WinAnalytics(Beta) library
A light-weight android library that can be quickly integrated into any app to use analytics tools.
- Full Kotlin support.
- Support multiple analytical tools e.g(Firebase, Fabric, Mixpanel).
- Annotations based.
- 100% reflection free.

**Application class**
```

@AnalyticsConfiguration(
        WinAnalyticsClient(type = AnalyticsTypes.FIREBASE, enabled=false),
        WinAnalyticsClient(type = AnalyticsTypes.FABRIC),
        WinAnalyticsClient(key = "mixpanelToken", type = AnalyticsTypes.MIXPANEL)
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
                WinEvent("Login"),
                WinEvent("Logout")
        )
        val name: String,

        @Analytics(WinEvent("Logout"))
        val email: String,

        @Analytics(WinEvent("Login"))
        val password: String,

        @Analytics(WinEvent("Login"))
        val phone: String,

        @Analytics(
                WinEvent("Login"),
                WinEvent("Logout")
        )
        val age: Int,

        @AnalyticsEmbedded
        val address: Address?
)

data class Address(
        @Analytics(WinEvent("Login"))
        val address: String,

        @Analytics(WinEvent("Login"))
        val latitude: String,

        @Analytics(WinEvent("Login"))
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

Project-level build.gradle \(\<project>/build.gradle):
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

App-level build.gradle \(\<project>/\<app-module>/build.gradle):

```
implementation 'com.github.WinfoozLtd.WinAnalytics:annotations:v1.0.2-beta'
implementation 'com.github.WinfoozLtd.WinAnalytics:winanalytics:v1.0.2-beta'
kapt 'com.github.WinfoozLtd.WinAnalytics:compiler:v1.0.2-beta'
```

# Support annotations
```
@Analytics()
@AnalyticsConfiguration()
@AnalyticsEmbedded()
@AnalyticsTypes()
@WinAnalyticsClient()
```
## License

WinAnalytics is released under the MIT license. [See LICENSE](https://github.com/Winfooz/WinAnalytics/blob/master/LICENSE) for details.
