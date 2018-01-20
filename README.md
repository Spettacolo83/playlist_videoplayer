# Playlist Video Player for Android
This is an Android Assignment. It is a video player that loads list of Video On Demand URLs and play them one after another. The information will be retrieved from a webservice and displayed on screen.

## Sample videos manifests:

```url
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_0ywhyahw/format/applehttp/protocol/http/a.m3u8
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_bse7sb83/format/applehttp/protocol/http/a.m3u8 
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_xmrhw3lb/format/applehttp/protocol/http/a.m3u8 
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_hcmo3xj4/format/applehttp/protocol/http/a.m3u8 
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_f7g7buxm/format/applehttp/protocol/http/a.m3u8 
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_ku0s0cqw/format/applehttp/protocol/http/a.m3u8 
http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_1uga2std/format/applehttp/protocol/http/a.m3u8
```

## JSON sample:

```json
{
    "videoplayer": [
        {
            "id": "1",
            "title": "Very Important People",
            "time": "81000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_0ywhyahw/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_1.jpg"
        },
        {
            "id": "2",
            "title": "Wired",
            "time": "64000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_bse7sb83/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_2.jpg"
        },
        {
            "id": "3",
            "title": "Kitten",
            "time": "63000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_xmrhw3lb/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_3.jpg"
        },
        {
            "id": "4",
            "title": "Bicycle",
            "time": "62000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_hcmo3xj4/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_4.jpg"
        },
        {
            "id": "5",
            "title": "Bye Paint",
            "time": "65000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_f7g7buxm/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_5.jpg"
        },
        {
            "id": "6",
            "title": "Brooch",
            "time": "60000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_ku0s0cqw/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_6.jpg"
        },
        {
            "id": "7",
            "title": "Recipes",
            "time": "57000",
            "url": "http://open.http.staging-k9.streamuk.com/p/3000021/sp/300002100/playManifest/entryId/0_1uga2std/format/applehttp/protocol/http/a.m3u8",
            "thumbnail": "http://www.followmemobile.com/videothumbs/thumb_7.jpg"
        }
    ]
}
```

## Preview
![ScreenShot](https://github.com/Spettacolo83/playlist_videoplayer/raw/master/screenshot_1.png)
![ScreenShot](https://github.com/Spettacolo83/playlist_videoplayer/raw/master/screenshot_2.png)
![ScreenShot](https://github.com/Spettacolo83/playlist_videoplayer/raw/master/screenshot_3.png)
