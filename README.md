# WinAnalytics(Beta) library
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
# Download
```groovy
dependencies {
   implementation 'com.winfooz.winanalytics:annotations:1.0.0-beta'
   implementation 'com.winfooz.winanalytics:winanalytics:1.0.0-beta'
   kapt 'com.winfooz.winanalytics:compiler:1.0.0-beta'
}
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
