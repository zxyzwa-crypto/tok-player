package com.example.tiktokplayer
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {

            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
        }
    }

    LaunchedEffect(play) {
        exoPlayer.playWhenReady = play
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

fun getAllVideos(context: android.content.Context): List<Uri> {

    val videoList = mutableListOf<Uri>()

    val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Video.Media._ID
    )

    context.contentResolver.query(
        collection,
        projection,
        null,
        null,
        null
    )?.use { cursor ->

        val idColumn = cursor.getColumnIndexOrThrow(
            MediaStore.Video.Media._ID
        )

        while (cursor.moveToNext()) {

            val id = cursor.getLong(idColumn)

            val contentUri = Uri.withAppendedPath(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id.toString()
            )

            videoList.add(contentUri)
        }
    }

    return videoList
}
