Droidutils
==========

This library contains utils to work with HTTP, JSON and multithreading.
Habrahabr article  https://habrahabr.ru/post/241415/

<h3>JSON</h3>

For example we have JSON like this:
```java
{
    "example":{
         "test":"Hello World"
    },
    "company_name":"Google",
    "staff":[
            {
             "Name":"David"
         },
            {
             "Name":"Mike"
         }
     ],
}
```
Now we need a class in which we write data.

```java
public class Company {

    @JsonKey("test")
    private String mTest;

    @JsonKey("company_name")
    private String mCompanyName;

    @JsonKey("staff")
    private LinkedList<Employee> mStaff;

    public class Employee {
       @JsonKey("Name")
       private String mName;

    }
}
```
Everything is ready, now we can parse JSON.

```java
    JsonConverter converter = new JsonConverter();
    Company company = converter.readJson(exampleJson, Company.class);
```

To build JSON string use this method
```java
   String json = converter.convertToJsonString(new Company());
```

<h3>Http</h3>

To create Url use this simple builder:

```java
 String url = new Url.Builder("http://base_url?")
                            .addParameter("key1", "value1")
                            .addParameter("key2", "value2")
                            .build();
// The output is http://base_url?key1=value1&key2=value2
```
The body of the request, you can create very simple:

```java
Company сompany = new Company();
HttpBody<Company> body = new HttpBody<Company>(сompany);
```
With headers is also easy:

```java
HttpHeaders headers = new HttpHeaders();
headers.add("header1", "value1");

HttpHeader header = new HttpHeader("header2", "value2");
headers.add(header);
```
To create request use this builder:

```
HttpRequest updateNewsRequest= new HttpRequest.Builder()
                            .setRequestKey("update_news_request") // set reauest key
                            .setHttpMethod(HttpMethod.GET)
                            .setUrl(url)
                            .setHttpBody(body)
                            .setHttpHeaders(header)
                            .setReadTimeout(10000)                                       
                            .setConnectTimeout(10000)
                            .build();
```
For execute request we need use HttpExecutor:

```java
HttpURLConnectionClient httpURLConnectionClient = new HttpURLConnectionClient();
httpURLConnectionClient.setRequestLimit("update_news_request", 30000);
httpExecutor = new HttpExecutor(httpURLConnectionClient);
```

```java
RequestResponse response  = httpExecutor.execute(request, RequestResponse.class, new Cache<RequestResponse>() {
                        @Override
                        public RequestResponse syncCache(RequestResponse data, String requestKey) {
                            // sync data with the cache so we avoid problems synchronizing data on the server and cache
                            return data;
                        }

                        @Override
                        public RequestResponse readFromCache(String requestKey) {
                            RequestResponse response = new RequestResponse();
                            response.hello = "hello from cache";
                            return response;
                        }
                    });
```

<h3>Work with multithreading</h3>

How to make sure that two threads do not perform the same request to the server. Use CustomSemaphore for this.

```java
public class CustomSemaphore {

    private Map<String, Semaphore> mRunningTask;

    public CustomSemaphore(){
        mRunningTask = new ConcurrentHashMap<String, Semaphore>();
    }

    public void acquire(String taskTag) throws InterruptedException {

        Semaphore semaphore = null;
        if (!mRunningTask.containsKey(taskTag)) {
            semaphore = new Semaphore(1);
        } else {
            semaphore = mRunningTask.get(taskTag);
        }
        semaphore.acquire();
        mRunningTask.put(taskTag, semaphore);
    }

    public void release(String taskTag) throws InterruptedException {

        if (mRunningTask.containsKey(taskTag)) {
            mRunningTask.remove(taskTag).release();
        }
    }
}
```

There is a easy solution to do something on the timer. For example, go to the server for updates every 30 seconds:

```java
 ScheduledFuture<?> scheduledFuture = ThreadExecutor.doTaskWithInterval(new Runnable() {
            @Override
            public void run() {
                // ходим на сервер
            }
        }, 0, 30, TimeUnit.SECONDS);
```

In ThreadExecutor there are two convenient methods:

```java
doNetworkTaskAsync(final Callable<V> task, final ExecutorListener<V> listener)
doBackgroundTaskAsync(final Callable<V> task, final ExecutorListener<V> listener)
```
