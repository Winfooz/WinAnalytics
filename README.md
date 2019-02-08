# WinAnalytics(Beta) library
[![Build Status](https://travis-ci.org/Winfooz/WinAnalytics.svg?branch=master)](https://travis-ci.org/Winfooz/WinAnalytics)
[![Deploy Status](https://app.buddy.works/mohamednayef95/winanalytics-1/pipelines/pipeline/158574/badge.svg?token=071324226326a701b263c3a2755acc1f179227f6bb2f1d11c84cbbfd3e77c732 "Deploy Status")](https://app.buddy.works/mohamednayef95/winanalytics-1/pipelines/pipeline/158574)
[![Download](https://api.bintray.com/packages/mnayef95/WinAnalytics/com.winfooz:winanalytics/images/download.svg) ](https://bintray.com/mnayef95/WinAnalytics/com.winfooz.winanalytics%3Awinanalytics/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WinAnalytics-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7197)
[![Gitter](https://badges.gitter.im/WinAnalyticsChat/WinAnalytics.svg)](https://gitter.im/WinAnalyticsChat/WinAnalytics?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![GitHub](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/Winfooz/WinAnalytics/blob/master/LICENSE)
![GitHub](https://img.shields.io/badge/Support-Kotlin%20%7C%20Java-lightgrey.svg)

A light-weight android library that can be quickly integrated into any app to use analytics tools.
- Custom adapters for support all analytical tools.
- Annotations based.
- Support Retrofit calls for log events automatically.
- Support log events when user clicks on views.
- Support screens events.
- Null safety.

# Contributing:
If you'd like to contribute, please take a look at the [`Contributing`](https://github.com/Winfooz/WinAnalytics/wiki/Contributing) page on the Wiki.
# Example WinAnalytics:

**Application class**
```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        WinConfiguration configuration = WinConfiguration.builder()
                .registerAdapter(new MixpanelAdapter(this, "token"))
                .registerAdapter(new FirebaseAdapter(this))
                .registerAdapter(new FabricAdapter(this))
                ...
                .debugMode(BuildConfig.DEBUG)
                .build();
        WinAnalytics.init(configuration);
    }
}
```

**Analytics interfaces**
```java
@Analytics(
        events = {
                @Data(value = @Value("post.title"), key = @Key("title")),
                @Data(value = @Value("post.body"), key = @Key("body"))
        },
        timestamp = true
)
public interface MainActivityAnalytics {

    // This event will override class @Analytics events
    @Event(
            value = "Success get posts",
            events = {
                    @Data(value = @Value("post.title"), key = @Key("title")),
                    @Data(value = @Value("otherParam"), key = @Key("otherParam"))
            }
    )
    void successGetPosts(Post post, String otherParam);

    // This event will inherit values from class @Analytics annotation
    @Event(value = "Failed get posts")
    void failedGetPosts(Post post);

    // This event will inherit values from class @Analytics annotation
    @Event(value = "Failed get posts1")
    void failed1GetPosts(Post post);
}
```

**Analytics wrapper**
```java
@AnalyticsWrapper
public interface MyAnalyticsWrapper {

    // This method will be return MainActivityAnalytics implementation
    MainActivityAnalytics mainActivityAnalytics();
}
```

**Access MyAnalyticsWrapper**
```java
public class MainActivity extends AppCompatActivity {

    private JavaMyAnalyticsWrapper wrapper;
    private JavaMainActivityAnalytics mainActivityAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrapper = WinAnalytics.create(JavaMyAnalyticsWrapper.class);
        mainActivityAnalytics = wrapper.mainActivityAnalytics();

        Post post = new Post();
        post.setTitle("title");
        post.setBody("body");

        mainActivityAnalytics.failedGetPosts(post);
    }
}
```

# Example analytics when user clicks on button:

**Analytics interface**
```java
@Analytics
public interface JavaMainActivityAnalytics {

    @Event(
            value = "Failed get posts",
            events = {
                    @Data(value = @Value("post.title"), key = @Key("title"))
            },
            timestamp = true
    )
    void failedGetPosts(/* This name for WinAnalytics know whitch object will bind here */ @Name("post") Post post);
}
```
**MainActivity**
```java
public class MainActivity extends AppCompatActivity {

    private Destroyable destroyable;

    @Name("post")
    @Bind(R.id.btn_login)
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Don't forget add this line to bind clicks listeners to views
        destroyable = WinAnalytics.bind(this);
    }

    @EventOnClick(value = R.id.btn_login, event = "Failed get posts")
    void onLoginClicked() {
        // Do what you want here after user clicks on button and WinAnalytics will log event automatically for you
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyable.destroy();
    }
}
```

# Network analytics

What if you want log events based on retrofit call success or failure, WinAnalytics already supports this type of analytics.

**Http client**
```java
public interface HttpClient {

    // This for tell WinAnalytisc this call supports analytics
    @AnalyticsCall
    @GET("posts")
    Call<List<Post>> getPosts();
}
```
then after add `@Analytics` for your call you need to specify what event you want to call when this call response success or failure

**Analytics interface**
```java
@Analytics
public interface JavaMainActivityAnalytics {

    // That means this analyics will fire after "posts" api response success and "name" means you should use call arguments which named with "getPostsSuccess"
    @CallSuccess(value = "posts", name = "getPostsSuccess")
    @Event(
            value = "Failed get posts",
            events = {
                    @Data(value = @Value("post.title"), key = @Key("title"))
            },
            timestamp = true
    )
    void failedGetPosts(@Name("post") Post post);
}
```

Now in your activity or whatever you want to log events you need to add this code.

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // value means your api, and names means name whitch you specifyed in `@CallSuccess`
    @CallArgument(value = {"posts"}, names = "getPostsSuccess")
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // You need register and unregister your call arguments and binds
        WinAnalytics.getInstance().register(this);

        findViewById(R.id.btn_login2).setOnClickListener(this);
    }

    // this method will return api response for log initialize your variable before log event
    @BindCallArguments(value = {"posts"})
    void init(Response<List<Post>> response) {
        post = response.body().get(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WinAnalytics.getInstance().unregister(this);
    }

    @Override
    public void onClick(View v) {
        HttpHelper.getHttpClient().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                // Do what you want after response and let WinAnalytics log network events for you
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
            }
        });
    }
}
```

The last thing you want to add is indexing interface like this

```java
@AnalyticsIndex
public interface MyAnalyticsIndex { }
```

Now WinAnalytics will log events automatically for you in a background thread, But you need to register WinAnalytics call adapter factory when you initialize retrofit like this
```java
public static HttpClient getHttpClient() {
    return new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(new AnalyticsFactory(BASE_URL))
        .baseUrl(BASE_URL)
        .build()
        .create(HttpClient.class);
}
```

# Log screens opens events

For log screen events you just want to annotate your class with `@Screen` like this
```java
@Screen(value = "Main activity", timestamp = true)
public class JavaMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Don't forget to add this line for log event and bind other cliks if you have.
        WinAnalytics.bind(this);
    }
}
```

# Download

```groovy
dependencies {
    // if you want log retroift calls events you need to add this dependency
    implementation 'com.winfooz:winanalytics-retroft:1.0.0'

    // but if you want just log clicks events or manually events you need to add this dependency
    implementation 'com.winfooz:winanalytics:1.0.0'

    // Always you need to add this dependency.
    kapt 'com.winfooz:winanalytics-compiler:1.0.0'
    
    // If you want use firebase analytics
    implementation 'com.winfooz:adapter-firebase:1.0.0'
    
    // If you want use mixpanel analytics
    implementation 'com.winfooz:adapter-mixpanel:1.0.0'
}
```

# Support annotations
```java
@Analytics
@AnalyticsCall
@AnalyticsIndex
@AnalyticsWrapper
@Bind
@BindCallArguments
@CallArgument
@CallFailure
@CallSuccess
@Event
@EventOnClick
@Name
@Screen
```
## License

WinAnalytics is released under the MIT license. [See LICENSE](https://github.com/Winfooz/WinAnalytics/blob/master/LICENSE) for details.