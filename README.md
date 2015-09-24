# spotify-chart
A simple app for display spotify chart

# API
http://charts.spotify.com/docs

# Third Party Libraries
- Android Asynchronous Http Client (http://loopj.com/android-async-http/)
-- for API request to spotify chart
- Glide (https://github.com/bumptech/glide)
-- for loading images provided by the spotify chart

# Feature
Load spotify track list using Spotify Chart API

- The application loads the rank, country, window type and date from the API and create drop down item for each item on each category
- Each time user changes an option, the application begins a new API request and updates the list
- The app only displays the name of the track, artist who sang it and its artwork
- 
# TODO
- Can improve design of the application
- Error handling (Display to user when error occurs)
