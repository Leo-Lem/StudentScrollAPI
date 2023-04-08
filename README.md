# StudentScroll API

This is the REST API behind the StudentScroll app ([OpenAPI specs](./openapi.yml)).

[![CI](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/maven.yml/badge.svg)](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/maven.yml)

## Setting up Maven

_Necessary for compiling, running, and everything in between_

1. (only macOS) Install using homebrew (`brew install maven`)
2. Now the maven commands should work (e.g., `mvn compile`, `mvn package`, [Docs](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html))

## Setting up a local PostgreSQL database

_Necessary for trying everything out_

1. Install [PostgreSQL](https://www.postgresql.org/download/).
2. Create a database named '**studentscroll**' (macOS: `createdb studentscroll`).
3. Add environment variables for (e.g., in ~/.zshrc)

- PSQL_URL: The url (including port) your psql is running on.
- PSQL_USERNAME: The username to access the database.
- PSQL_PASSWORD: The password to access the database.

## API endpoints

- /signin Post credentials to receive JWT.

### students

- /students Get all students. Post new student.
- /students/{studentID} Get student. Put updated student. Delete student.
- /students/{studentID}/settings Get settings. Put updated settings.
- /students/{studentID}/profile Get profile. Put updated profile.
- /students/{studentID}/profile/followers Get followers.
- /students/{studentID}/profile/followers/{followerID} Post new follower. Delete follower.
- /students/{studentID}/profile/follows/{followID} Get follows. Post new follow. Delete follow.

### chats

- /chats Get all chats. Post new chat.
- /chats/{chatID} Get chat. Put updated chat. Delete chat.
- /chats/{chatID}/students Get students in chat.
- /chats/{chatID}/students/{studentID} Get if student is in chat. Post new student to chat. Delete student from chat.
- /chats/{chatID}/messages Get all messages in chat. Post new message in chat.
- /chats/{chatID}/messages/{messageID} Put updated message to chat. Delete message from chat.

### posts

- /posts Get all posts. Post new post.
- /posts/{postID} Get post. Put updated post. Delete post.
- /posts/{postID}/comments Get all comments of post. Post new comment to post.
- /posts/{postID}/comments/{commentID} Get comment of post. Put updated comment to post. Delete comment from post.
