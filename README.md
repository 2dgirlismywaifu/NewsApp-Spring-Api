<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure">
    <img src="images/logo.png" alt="Logo" width="200" height="200">
  </a>

<h3 align="center", style="font-size:40px">NewsApp Reader</h3>
<p1 align="center", style="font-size:20px">Powered by NewsAPI and Microsft Azure Services</p1><br />
  <p2 align="center", style="font-size:20px">
    A News Reader App from multiple sources
  </p2>
<hr>

 ![Contributors][contributors-shield]
  [![Forks][forks-shield]][forks-url]
  [![Stargazers][stars-shield]][stars-url]
  ![Reposize][size-shield]
  ![Lastcommit][commit-shield]
  [![Issues][issues-shield]][issues-url]
  ![Linecount][linecount-shield]
  [![APACHE License][license-shield]][license-url]

</div>
  <div align="center">
    <p align="center">
    <a href="https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure", style="font-size:20px"><strong>Explore the docs »</strong></a>
    <br />
    <a href="#demo">View Demo</a>
    ·
    <a href="https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/issues">Report Bug</a>
    ·
    <a href="https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/issues">Request Feature</a>
    </p>
  </div>
<!-- TABLE OF CONTENTS -->
<details>
  <summary style="font-size:20px" ><b>Table of Contents</b></summary>
  <ol >
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#include-with-project">Include with project</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#demo">Demo</a></li>
    <li><a href="#known-issues">Known Issues</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>

  </ol>
</details>
<br />


<!-- ABOUT THE PROJECT -->
# **About The Project**

News application or newspaper reading application is a popular application with the role of providing quick news to users. In this project, the application uses: NewsAPI with 53 countries supported, RSS2JSON converts RSS to JSON and Microsoft Azure services: SQL Server, App Services and Blob Storages.

[![Product Name Screen Shot][product-screenshot]](https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## **Include with project**
* SQL file without user information
* Figma design file
* Image use in Azure Blob Storage
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
# **Getting Started**

- This project only use for research and purpose. It is NOT available for retail.
- Follow all instruction to run project in your local devices.
- At this time, the application is only developed on the Android platform. Once I have a Mac device, I will develop this project run on Apple mobile device


## **Prerequisites**

Before use this project, you need have:
* Android Studio
* Android Native Development Kit (version in build.gradle)
* Sign up Free Account Microsoft Azure Portal if you do not have any account
* Create free Azure SQL Database S0: [Free Azure SQL Database](https://learn.microsoft.com/en-us/azure/azure-sql/database/free-sql-db-free-account-how-to-deploy?view=azuresql)
* Create App Services B1 Plan (Recomended: 13$/month): [NewsApp Android RESTServices](https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices)
* Java Development Kit 17\
Notes: You can use Azure App Services with F1 Plan (Free Forever), but performance is very slow.
## **Installation**
1. Clone the repo
   ```sh
   git clone https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.git
   ```
2. Follow instruction from my App Services: [NewsApp Android RESTServices](https://github.com/2dgirlismywaifu/NewsApp_Android_RESTServices)
3. Get a free NewsAPI Key at [https://newsapi.org/](https://newsapi.org/)
4. Get a free RSS2JSON API Key at [https://rss2json.com/](https://rss2json.com/)
5. Get a secret access token to Azure Blob Storage follow this link [Create SAS Token Azure Blob Containers](https://learn.microsoft.com/en-us/azure/cognitive-services/translator/document-translation/how-to-guides/create-sas-tokens?tabs=Containers)
6. Open this project in Android Studio
7. Now connect this project to Firebase Authentication\
  The easy way: In Menu at top your screen, choose Tools -> Firebase -> Authentication. Follow this video bellow to see details\
  System Demo Information:
    - 💻 Device: MSI GF63 Thin10SC
    - 🐧 Operating System: Archcraft (Based on Arch Linux)
    - 💪 CPU: Intel Core i5-10500H
    - 🖼️ GPU: Intel UHD Graphics / NVIDIA GTX 1650 With Max-Q Design
    - 🗃️ RAM: 16GB

  https://user-images.githubusercontent.com/59259855/219111700-1917bc4a-f300-4074-bb46-ce74434ecfed.mp4

\
8. Add SHA-1 and SHA-256 in Firebase Project
 - In root project folder, open Terminal and run this command

    ```sh
    ./gradlew signingReport
    ```
  - Wait for this command run and you will get SHA-1 and SHA-256 code return like bellow

    ```
    Variant: debug
    Config: debug
    Store: C:\Users\pc\.android\debug.keystore
    Alias: AndroidDebugKey
    MD5: <code return>
    SHA1: <code return>
    SHA-256: <code return>
    Valid until: Sunday, December 1, 2052
    ----------
    Variant: release
    Config: release
    Store: D:\GitHub\newsapp_AzureDatbase\app\src\main\keyStore\signedKey.jks
    Alias: NewsAppReader-2dgirlismywaifu
    MD5: <code return>
    SHA1: <code return>
    SHA-256: <code return>
    Valid until: Wednesday, February 16, 2033
    ```
  - Open your project firebase connect with this project, scroll down to your app part. In SHA certificate fingerprints, add SHA-1 and SHA-256 code project have taken above.
9. Now open Firebase Authentication in your Firebase Project
    - In Sign-in method part, add two providers: Email/Password and Google
    - In Settings part, choose `User account linking` , select `Create multiple accounts for each identity provider`. Then select `Save` to complete configuration Firebase Authentication
10. Encode all your API key to Base64 (Azure Blob SAS need encode 2 times)
11. Create `keys.c` file in this path bellow

    ```
    app\src\main\jni\keys.c
    ```
  - Page this content to 'keys.c' like bellow

    ```objectivec
    #include <jni.h>

    JNIEXPORT jstring JNICALL
    Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPIKey_getNewsAPIKey(JNIEnv *env, jobject thiz) {
        // TODO: implement getNewsAPIKey()
        //your NewsAPI key
        return (*env)->NewStringUTF(env, "<input your encode key here>");
    }

    JNIEXPORT jstring JNICALL
    Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPPAPI_getNewsAPPKey(JNIEnv *env, jobject thiz) {
        // TODO: implement geNewsAPPHeader()
        //your API key Spring Boot Services
        return (*env)->NewStringUTF(env, "<input your encode key here>");
    }

    JNIEXPORT jstring JNICALL
    Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPPAPI_geNewsAPPHeader(JNIEnv *env, jclass clazz) {
        // TODO: implement geNewsAPPHeader()
        //your API Header Spring Boot Services
        return (*env)->NewStringUTF(env, "<input your encode key here>");
    }

    JNIEXPORT jstring JNICALL
    Java_com_notmiyouji_newsapp_java_RSS2JSON_FeedMultiRSS_getRSS2JSONAPIKey(JNIEnv *env, jobject thiz) {
        //Your RSS2JSON Key
        return (*env)->NewStringUTF(env, "<input your encode key here>");
    }

    JNIEXPORT jstring JNICALL
    Java_com_notmiyouji_newsapp_java_RecycleViewAdapter_ListRSSAdapter_getSecretKey(JNIEnv *env,jobject thiz) {
        //Your secret key to access image save from Azure Blob Storage
        //You need encode this key two times
        return (*env)->NewStringUTF(env, "<input your encode key here>");
    }
    ```
12. Now project is ready to build and install in your physical device and Google Android emulator 💕💕💕💕💕💕💕 .
<p align="right">(<a href="#readme-top">back to top</a>)</p>


# **Demo**

This is a video demo project\
System Demo Information:
  - 📱 Device: Xiaomi Redmi Note 7 Global
  - 🐧 Operating System: Android 13 Tiramisu (Project Elixir)
  - 💪 CPU: Qualcomm Snapdragon 660
  - 🖼️ GPU: Adreno 512
  - 🗃️ RAM: 4GB

https://user-images.githubusercontent.com/59259855/218577883-4b81b73f-4e08-4efa-bbd5-a07b749f4f35.mp4

<p align="right">(<a href="#readme-top">back to top</a>)</p>

# **Known Issues**

1. RecycleView Search only work with RecycleView Horizontal (expect: NewsSourceList, Favourite News).
2. Set favourite an un favourite in recycleview adapter not update view. Need Swipe To Refresh action from user to update view.
3. ~~Duplicate result login~~
4. Tell me 💕\
See the [open issues](https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
# **Contributing**

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork this Project
2. Create your Feature Branch
    ```sh
    git checkout -b feature/AmazingFeature
    ```
3. Commit your Changes
    ```sh
    git commit -m 'Add some AmazingFeature'
    ```
4. Push to the Branch
    ```sh
    git push origin feature/AmazingFeature
    ```
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
# **License**
- **Do NOT delete my header copyright if you fork or clone this project for personal use**
```
            Copyright By @2dgirlismywaifu (2023)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
# **Contact**
[![twitter-shield]][twitter-url] <br >
My Gmail Workspace: longntworkspace2911@gmail.com <br>
Project Link: [https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure](https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure)

<div align="center">
    <p1 align="center", style="font-size:40px">Follow my account</p1>
    <br />
    <a href='https://github.com/2dgirlismywaifu' border='0' style='cursor:pointer;display:block'><img src='https://cdn.me-qr.com/qr/49033888.png?v=1676917850' alt='2dgirlismywaifu'></a><a href='https://github.com/2dgirlismywaifu' border='0' style='cursor:default;display:none'>2dgirlismywaifu/a>
</div>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[contributors-url]: https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[forks-url]: https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/network/members
[stars-shield]: https://img.shields.io/github/stars/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[size-shield]: https://img.shields.io/github/repo-size/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[linecount-shield]: https://img.shields.io/tokei/lines/github/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure?color=C9CBFF&labelColor=302D41&style=for-the-badge
[commit-shield]: https://img.shields.io/github/last-commit/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[stars-url]: https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/stargazers
[issues-shield]: https://img.shields.io/github/issues/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[issues-url]: https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/issues
[license-shield]: https://img.shields.io/github/license/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure.svg?style=for-the-badge&color=C9CBFF&logoColor=D9E0EE&labelColor=302D41
[license-url]: https://github.com/2dgirlismywaifu/NewsAPP_RSS_NewsAPI_Azure/blob/main/LICENSE
[twitter-shield]: https://img.shields.io/twitter/follow/MyWaifuis2DGirl?color=C9CBFF&label=%40MyWaifuis2DGirl&logo=TWITTER&logoColor=C9CBFF&style=for-the-badge
[twitter-url]: https://twitter.com/MyWaifuis2DGirl
[product-screenshot]: images/screenshot.png
