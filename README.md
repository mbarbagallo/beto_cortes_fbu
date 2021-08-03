# Photos to music

## Table of Contents for this app
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)
4. [Schema](#Schema)
5. [Used documentation](#Used-Documentation)

## Overview
### Description
This is an app where users take pictures of everything around them, the pictured is processed in a way that the 3 most present colors are extracted and transformed into emotions fundamented on color theory, then said emotions are related with music genres that also inspire those emotions to finally fetch a *song/album* from the Spotify API, showing that to the user.

## App evaluation
* **Category:** Photo / Entertainment
* **Mobile:** How uniquely mobile is the product experience?
    * The ability to use it anytime with access to the resources of a smartphone such as the camera and location
* **Story:** How compelling is the story around this app once completed?
    * The value is clear as it provides a funny way to discover music by your own based on something that catches your attention
    * Personally I think my friends would use this app for they also like to discover new music constantly, regarding my peers and our initial discussions, they seemed to like the idea and thought about using it.
* **Market:** How large or unique is the market for this app?
    * I think the market for this app is quite large as a lot of people listen to music on daily basis on a variety of music platforms, spotify being one of the largest has proofed that there is a market to listening to music everywhere and that the way a user listens to music matters.
    * This app provides value as it constructs on the idea of connecting the things you like to further build them up, if you see something you like and want to connect with something similar in an audio manner.
    * I do have a well defined audience, people between 15 and 30 years that are open to new kinds of music and their relation with the visuals we are getting all the time.
* **Habit:** How habit-forming or addictive is this app?
    * I think the frequency of usage is good but not that great, if the user is stationary it is quite possible that the amount of interesting things to get a photo of will lower quite quickly.
    * The user consumes as much as they create, that can be a double edged sword, as much as everyone likes to create and share, most of the times most of the users just consume content.
* **Scope:** How well-formed is the scope for this app?
    * The app presents some interesting technicall challenges, mainly extracting the main colors of an image, currently I have found 2 ways of doing so, first is using OpenCV, I have not checked the documentation deeply enough to find the method I but I know there is one. The other idea is running Python inside Java with Chaquopy, there using a the KMeans implementation of SKLearn to pass each pixel's RGB and clustering, then getting the k centroids and those are the main colors.
    * Fetching the random songs from Spotify also may seem like a problem but I think I have found already the references and guides necessary to interact with their API so that may not be that great of a challenge but it is still one nevertheless.
    * Lastly the local implementaion of the features will be the least of the challenges, access to camera and login seems feasible with android tools and Parse.
    * Of course, getting the layouts and the skeleton done will be an interesting activity, as this app sounds like something truly interesting and useful for myself I will surely be putting up my best effort.
    * I think it is quite clearly defined, I want a way to transform pictures into sound and conserve this sounds related to their respective picture.


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User is able to login into the app (Req. 3: Login/Logout, Req. 4: Signup, Req. 1: Multiple Views)
* User is able to take pictures (Req. 1: Multiple Views)
* Pictures can have their colors extracted (Req. 9: Difficult technical problems)
* Colors can be translated to emotions and emotions to genres (Req. 9: Difficult technical problems)
* App is able to fetch random songs from spotify from a certain genre (Req. 5: SDK/API interactionm, Req. 7: Transition animation from photo to loading to showing song)
* App shows the name of the song, artist and genre, user can swipe to accept or reject recommendation (Req. 6: Gesture usage, Req. 8: Polish visuals of song presentations)
* User can see captured songs history with the picture and data (Req. 2: Parse DB, Req. 1: Multiple Views, Req. 8: Polish visuals of song presentations)

**Optional Nice-to-have Stories**

* User can save songs to their sptofiy library
* User can share the song with other apps from the app
* General aesthetics are settled in a clean way

### 2. Screen Archetypes

* Login
   * User is able to login into the app
* Camera
   * User is able to take pictures
* Analysis loading
    * Pictures can have their colors extracted
    * Colors can be translated to emotions and emotions to genres
    * * App is able to fetch random songs from spotify from a certain genre
* Song result
    * App shows the name of the song, artist and genre
* Song history
    * User can see captured songs history with the picture and data

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* User settings
* Camera activity
* History of songs

**Flow Navigation** (Screen to Screen)

* Settings
   * Color palette detail
   * Color palette form reset
* Camera
   * Loading analysis
   * Result presentation
* History
    * Song detail

## Wireframes
![](https://i.imgur.com/wvYCh5U.jpg)

## Schema 
### Models

Songs:
| Property | Type | Description |
| -------- | -------- | -------- |
| Camera picture     | File     | User captured photo     |
| Author     | Pointer to user     | User that photo/songs belongs to    |
| createdAt     | DateTime     | Date when post is created (default field)   |
| updatedAt     | DateTime     | Date when post is last updated (default field)   |
| Song code    | String     | Unique identifier for the song |
| Genre     | Array<String>     | List of the genres the song belongs to |
| Colors     | Array<String>     | List of the main colors of the user captured photo |

User

| Property | Type | Description |
| -------- | -------- | -------- |
| User name | String | Name of the user |
| Color Emotion Hash | JSON Object | Hashmap like structure to save users relation of colors with emotions |
| User avatar | File | Small image for the user to have on their profile |
|Song list| Array<String>| Array of song Parse IDs|




### Networking
* Login
    * (Read/Get) Check if user has a valid login
    * (Create/Post) Signup new user
* Reset color palette
    * (Create/Post) Make new color/emotion matches
* User profile and settings
    * (Read/Get) Show current color palette
* Loading analysis screen
    * (Create/Post) Fetch random song from spotify
* Song history
    * (Read/Get) Get the list of songs for the user
* Song detail
    * (Delete) Remove song from history

## Used documentation
  
* Spotify Android SDK: https://developer.spotify.com/documentation/android/
* Android Developers: https://developer.android.com/docs
* Back4App: https://www.back4app.com/docs/get-started/welcome
* CardStack https://androidrepo.com/repo/wenchaojiang-AndroidSwipeableCardStack-android-android-ui-library
