# 練習使用 MVVM 架構

### API：Episodate TV Shows API

Base URL：https://www.episodate.com

### 使用方式：

來源：https://www.episodate.com/api

- 最受歡迎節目列表

Example：https://www.episodate.com/api/most-popular?page=1

```
URL：/api/most-popular?page=:page
parameter：page
```

- 節目資訊

Example：https://www.episodate.com/api/show-details?q=29560

Example: https://www.episodate.com/api/show-details?q=arrow

```
URL：/api/show-details?q=:show
parameter：q (TV-Id or Name)
```

- 搜尋節目

Example：https://www.episodate.com/api/search?q=arrow&page=1

```
URL：/api/search?q=:search&page=:page
parameter：q (Keyword) & page
```
