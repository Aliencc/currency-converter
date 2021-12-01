# currency-converter

RESTful API to convert currency value based on the exchange rate at https://exchangeratesapi.io/.

### Swagger link

http://localhost:8080/swagger-ui/#/

### Supported symbols

We are using a list of currency symbols to verify the validity of incoming request. <br/>
The allowed currency symbols are coming called from the external API. <br/>
To reduce the frequency of calls we pre-load the symbols through a scheduled task with a configurable period. <br/>
In case the supported symbols are not available, the initial static list of symbols from the resource will be used.

### Getting current exchange rate

Calling current exchange rate between two currency is cached to reduce external API calls frequency. The expiration time
of the cache is configurable.

### Latency

The execution time of API endpoints is measured and logged through
the [TrackDuration](com.hn.currency.tracking.TrackDuration) annotation.

### Open tasks

- Store URL and API key to some secure storate e.g. Vault
- Use Redis instead of Caffeine when switching to cluster deployment (Kubenetes)
- Introduce Kubenetes HPA
- Introduce Grafana/Prometheus metrics and monitoring 