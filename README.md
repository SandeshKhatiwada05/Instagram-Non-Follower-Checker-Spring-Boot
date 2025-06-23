# 📱 Instagram Non-Follower Checker

A simple Spring Boot web app that helps you find out **who doesn’t follow you back** on Instagram.  
Upload your downloaded `followers.json` and `following.json` from Instagram, and the app shows non-followers with links to their profiles.

## 🚀 Features
- ✅ Upload `followers.json` and `following.json` files
- ✅ Find users who don’t follow you back
- ✅ Click usernames to open their profile in a new tab
- ✅ Clean UI using Bootstrap 5

## 🖼️ Preview
![UI Preview](https://via.placeholder.com/800x400?text=Add+Screenshot+Here)

## 🛠️ How to Use
1. Go to your [Instagram Data Download](https://www.instagram.com/download/request/) page 
2. Request your data in **JSON** format
3. Download the ZIP and extract `followers.json` and `following.json` (inside `followers_and_following/`)
4. Run the app locally:
    ```bash
    ./mvnw spring-boot:run
    ```
5. Open [http://localhost:8080](http://localhost:8080) in your browser
6. Upload both files and view who doesn’t follow you back

## 📂 Project Structure
