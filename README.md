# Instagram Non-Follower Detector

A Spring Boot web app that tells you who doesn't follow you back on Instagram.

Upload your `followers.json` and `following.json` from Instagram's data export, and the app shows a list of non-followers with direct links to their profiles. Files are processed in memory — nothing is stored.

## Stack

- Java 17, Spring Boot 3.2
- Thymeleaf for templating
- Vanilla HTML, CSS, and JS on the frontend

## Getting started

**1. Get your Instagram data**

Go to Instagram > Settings > Your Activity > Download your information. Request JSON format. Once downloaded, unzip and find `followers_1.json` and `following.json`.

**2. Run the app**

```bash
mvn spring-boot:run
```

Open `http://localhost:8080`, upload both files, and hit Analyse.

<img width="912" height="972" alt="image" src="https://github.com/user-attachments/assets/3c23de9e-79a7-4e89-be53-ed4034f80b9b" />


## Notes

Instagram's export format has changed over time. The parser handles both the top-level array format (followers) and the keyed object format (following). If your export looks different, open an issue with a redacted sample.
